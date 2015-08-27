package org.babelomics.cnvs.app.cli;

import org.apache.commons.lang3.mutable.MutableLong;
import org.babelomics.lib.io.CNVSCopyNumberVariationMongoDataWriter;
import org.babelomics.lib.io.CNVSCopyNumberVariationXLSDataReader;
import org.babelomics.lib.io.CNVSQueryManager;
import org.babelomics.lib.io.CNVSRunner;
import org.babelomics.lib.models.CNV;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.opencb.biodata.models.feature.Region;
import org.opencb.commons.containers.list.SortedList;
import org.opencb.commons.io.DataReader;
import org.opencb.commons.io.DataWriter;
import org.opencb.commons.run.Runner;
import org.opencb.commons.run.Task;

import com.beust.jcommander.ParameterException;
import com.mongodb.MongoClient;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alejandro Alemán Ramos <alejandro.aleman.ramos@gmail.com>
 */
public class CNVSMain {

	private static Datastore getDatastore() {

		final Morphia morphia = new Morphia();
		morphia.mapPackage("org.babelomics.cnvs.lib.models");

		MongoClient mongoClient = new MongoClient();

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
			case "setup":
				command = parser.getSetupCommand();
				break;
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


		if (command instanceof OptionsParser.CommandSetup) {
			OptionsParser.CommandSetup c = (OptionsParser.CommandSetup) command;

			Datastore datastore = getDatastore();
			//Algun tipo de configuración ? 


		}else if (command instanceof OptionsParser.CommandLoad) {
			OptionsParser.CommandLoad c = (OptionsParser.CommandLoad) command;


			Datastore datastore = getDatastore();

			String fileName = "/home/sgallego/appl-clinic11/cnvsCarga/Proba4.xls";

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

			Datastore datastore = getDatastore();

			CNVSQueryManager qm = new CNVSQueryManager(datastore);


			if (c.all) {

				System.out.println("\n\n All variants");

				List<CNV> list = datastore.createQuery(CNV.class).asList();

				for(CNV a: list){
					System.out.println(a);
				}

			} else if (c.regionLIst.size() > 0) {


				Pattern p = Pattern.compile("(\\w+):(\\d+)-(\\d+)");
				List<Region> regionList = new ArrayList<>();

				for (String region : c.regionLIst) {
					Matcher m = p.matcher(region);

					if (m.matches()) {
						String chr = m.group(1);
						int start = Integer.parseInt(m.group(2));
						int end = Integer.parseInt(m.group(3));

						Region r = new Region(chr, start, end);
						regionList.add(r);

					} else {
						System.out.println("no: " + region);
					}

				}

				long start = System.currentTimeMillis();
				MutableLong count = new MutableLong(-1);

				Iterable<CNV> query = qm.getVariantsByRegionList(regionList,  c.skip, c.limit, count);
				for (CNV v : query) {
					System.out.println("v = " + v);
				}

				long end = System.currentTimeMillis();
				System.out.println(end - start);
				System.out.println("count: " + count);

			}

		}
	}
}
