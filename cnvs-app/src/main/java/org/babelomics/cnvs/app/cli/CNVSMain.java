package org.babelomics.cnvs.app.cli;

import org.apache.commons.lang3.mutable.MutableLong;
import org.babelomics.cnvs.lib.models.CNV;
import org.babelomics.cnvs.lib.io.CNVSCopyNumberVariationMongoDataWriter;
import org.babelomics.cnvs.lib.io.CNVSCopyNumberVariationXLSDataReader;
import org.babelomics.cnvs.lib.io.CNVSQueryManager;
import org.babelomics.cnvs.lib.io.CNVSRunner;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.opencb.biodata.models.feature.Region;
import org.opencb.commons.containers.list.SortedList;
import org.opencb.commons.io.DataReader;
import org.opencb.commons.io.DataWriter;
import org.opencb.commons.run.Runner;
import org.opencb.commons.run.Task;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		/*

        List<CNV> list = datastore.createQuery(CNV.class).asList();

        for(CNV c: list){
        	System.out.println(c);
        }
		 */
	}

	public static void main(String[] args) throws IOException {

		/*	final Morphia morphia = new Morphia();
    	morphia.mapPackage("org.babelomics.cnvs.lib.models");

    	MongoClient mongoClient = new MongoClient();

    	Datastore datastore = morphia.createDatastore(mongoClient, "cnvs");
    	datastore.ensureIndexes();

        String fileName = "/home/sgallego/appl-clinic11/cnvsCarga/Proba4.xls";

        DataReader<CNV> reader = new CNVSCopyNumberVariationXLSDataReader(fileName);
        DataWriter<CNV> writer = new CNVSCopyNumberVariationMongoDataWriter(datastore);

        List<Task<CNV>> taskList = new SortedList<>();
        List<DataWriter<CNV>> writers = new ArrayList<>();
        writers.add(writer);

        Runner<CNV> runner = new CNVSRunner(reader, writers, taskList, 1);

        runner.run();

        List<CNV> list = datastore.createQuery(CNV.class).asList();

        for(CNV c: list){
        	System.out.println(c);
        }*/
		OptionsParser parser = new OptionsParser();

		// If no arguments are provided, or -h/--help is the first argument, the usage is shown
		if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
			System.out.println(parser.usage());
			return;
		}

		OptionsParser.Command command = null;

		try {
			switch (parser.parse(args)) {
			case "load":
				command = parser.getLoadCommand();
				break;
			case "query":
				command = parser.getQueryCommand();
				break;

			default:
				System.out.println("Command not implemented!!");
				System.exit(1);
			}
		} catch (ParameterException ex) {
			System.err.println(ex.getMessage());
			System.err.println(parser.usage());
			System.exit(1);
		}


		if (command instanceof OptionsParser.CommandLoad) {
			OptionsParser.CommandLoad c = (OptionsParser.CommandLoad) command;


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
		}else if (command instanceof OptionsParser.CommandQuery) {
			OptionsParser.CommandQuery c = (OptionsParser.CommandQuery) command;

			Datastore datastore = getDatastore(c.host, c.user, c.pass);

			CNVSQueryManager qm = new CNVSQueryManager(datastore);

			Map<String,String> map = new HashMap<String, String>();
				
		        
			if(!c.code.isEmpty()){
				map.put("code", c.code);
			}
			
			if(!c.decipId.isEmpty()){
				map.put("decipId", c.decipId);
			}
			if(!c.regionList.isEmpty()){
				map.put("region", c.regionList);
			}
			if(!c.assembly.isEmpty()){
				map.put("assembly", c.assembly);
			}
			if(!c.band.isEmpty()){
				map.put("band", c.band);
			}
			if(!c.type.isEmpty()){
				map.put("type", c.type);
			}
			
			if(!c.doses.isEmpty()){
				map.put("doses", c.doses);
			}
			if(!c.inhe.isEmpty()){
				map.put("inheritance", c.inhe);
			}
			if(!c.cl.isEmpty()){
				map.put("cellline", c.cl);
			}
			if(!c.gender.isEmpty()){
				map.put("gender", c.gender);
			}
			if(!c.status.isEmpty()){
				map.put("status", c.status);
			}
			
			
			if(!c.typeSample.isEmpty()){
				map.put("typeSample", c.typeSample);
			}
			if(!c.hpo.isEmpty()){
				map.put("hpo", c.hpo);
			}
			if(!c.ethic.isEmpty()){
				map.put("ethic", c.ethic);
			}
			if(!c.origin.isEmpty()){
				map.put("origin", c.origin);
			}
			
			
			
			
			MutableLong count = new MutableLong(-1);

			Iterable<CNV> res= qm.getVariantsByFilters(map, c.skip, c.limit, count);
			
			for(CNV cnv: res){
				System.out.println(cnv);
			}

		}
	}
}
