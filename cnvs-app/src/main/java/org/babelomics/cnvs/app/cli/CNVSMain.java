package org.babelomics.cnvs.app.cli;

import org.apache.commons.lang3.mutable.MutableLong;
import org.babelomics.cnvs.lib.models.CNV;
import org.babelomics.cnvs.lib.cli.QueryCommandLine;
import org.babelomics.cnvs.lib.io.CNVSCopyNumberVariationMongoDataWriter;
import org.babelomics.cnvs.lib.io.CNVSCopyNumberVariationXLSDataReader;
import org.babelomics.cnvs.lib.io.CNVSQueryManager;
import org.babelomics.cnvs.lib.io.CNVSRunner;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.opencb.commons.containers.list.SortedList;
import org.opencb.commons.io.DataReader;
import org.opencb.commons.io.DataWriter;
import org.opencb.commons.run.Runner;
import org.opencb.commons.run.Task;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alejandro Alemán Ramos <alejandro.aleman.ramos@gmail.com>
 */
public class CNVSMain {

	private static Datastore getDatastore(String host, String user, String pass) {

		final Morphia morphia = new Morphia();
		morphia.mapPackage("org.babelomics.cnvs.lib.models");

		MongoClient mongoClient;
        if (user == "" && pass == "") {
            mongoClient = new MongoClient(host);
        } else {
            MongoCredential credential = MongoCredential.createCredential(user, "cnvs", pass.toCharArray());
            mongoClient = new MongoClient(new ServerAddress(host), Arrays.asList(credential));
        }
		Datastore datastore = morphia.createDatastore(mongoClient, "cnvs");
		datastore.ensureIndexes();

		return datastore;
	
	}

	public static void main(String[] args) throws IOException {

			
				
		OptionsParser parser = new OptionsParser();

		// If no arguments are provided, or -h/--help is the first argument, the usage is shown
		if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
			System.out.println(parser.usage());
			return;
		}

		String cmd = args[0];

		String[] newArgs = new String[args.length - 1]; 
		for(int i = 1; i < args.length; i++) {
			newArgs[i-1] = new String(args[i]);
		}
		
		OptionsParser.Command command = null;

		try {
			
			if (cmd.equalsIgnoreCase("load")){
				parser.parse(args);
				OptionsParser.CommandLoad c = parser.getLoadCommand();


				System.out.println("c.user = " + c.user);
				System.out.println("c.host = " + c.host);
				System.out.println("c.pass = " + c.pass);

				Datastore datastore = getDatastore(c.host, c.user, c.pass);

				String fileName = c.input;

				DataReader<CNV> reader = new CNVSCopyNumberVariationXLSDataReader(fileName);
				DataWriter<CNV> writer = new CNVSCopyNumberVariationMongoDataWriter(datastore);

				List<Task<CNV>> taskList = new SortedList<>();
				List<DataWriter<CNV>> writers = new ArrayList<>();
				writers.add(writer);

				Runner<CNV> runner = new CNVSRunner(reader, writers, taskList, 1);

				try {
					runner.run();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if (cmd.equalsIgnoreCase("query")) {
				
				QueryCommandLine cmdLine = new QueryCommandLine();
				JCommander cmd1 = new JCommander(cmdLine);
				
				try {
					
					cmd1.parse(newArgs);
				} catch (Exception e) {
					cmd1.usage();
					
					System.exit(-1);
				}
				
				Datastore datastore = getDatastore(cmdLine.getHost(), cmdLine.getUser(), cmdLine.getPass());

				CNVSQueryManager qm = new CNVSQueryManager(datastore);
				MutableLong count = new MutableLong(-1);

				Iterable<CNV> res= qm.getCNVsByFilters(cmdLine,count);
				
				for(CNV cnv: res){
					System.out.println(cnv);
				}
				
				
			}else{
				System.out.println("Command not implemented!!");
				System.err.println(parser.usage());
				System.exit(1);
			}
		} catch (ParameterException ex) {
			System.err.println(ex.getMessage());
			System.err.println(parser.usage());
			System.exit(1);
		}

		

	}
}
