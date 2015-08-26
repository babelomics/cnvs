package org.babelomics.cnvs.app.cli;

import org.babelomics.lib.io.CNVSCopyNumberVariationMongoDataWriter;
import org.babelomics.lib.io.CNVSCopyNumberVariationXLSDataReader;
import org.babelomics.lib.io.CNVSRunner;
import org.babelomics.lib.models.CNV;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.opencb.commons.containers.list.SortedList;
import org.opencb.commons.io.DataReader;
import org.opencb.commons.io.DataWriter;
import org.opencb.commons.run.Runner;
import org.opencb.commons.run.Task;

import com.mongodb.MongoClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alejandro Alemán Ramos <alejandro.aleman.ramos@gmail.com>
 */
public class CNVSMain {

    public static void main(String[] args) throws IOException {

    	final Morphia morphia = new Morphia();
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
        }

    }
}
