package org.babelomics.cnvs.app.cli;

import org.babelomics.lib.io.CNVSCopyNumberVariationMongoDataWriter;
import org.babelomics.lib.io.CNVSCopyNumberVariationXLSDataReader;
import org.babelomics.lib.io.CNVSRunner;
import org.babelomics.lib.models.CNV;
import org.opencb.commons.containers.list.SortedList;
import org.opencb.commons.io.DataReader;
import org.opencb.commons.io.DataWriter;
import org.opencb.commons.run.Runner;
import org.opencb.commons.run.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alejandro Alemán Ramos <alejandro.aleman.ramos@gmail.com>
 */
public class CNVSMain {

    public static void main(String[] args) throws IOException {


        String fileName = "Planilla.xls";

        DataReader<CNV> reader = new CNVSCopyNumberVariationXLSDataReader(fileName);
        DataWriter<CNV> writer = new CNVSCopyNumberVariationMongoDataWriter();

        List<Task<CNV>> taskList = new SortedList<>();
        List<DataWriter<CNV>> writers = new ArrayList<>();
        writers.add(writer);

        Runner<CNV> runner = new CNVSRunner(reader, writers, taskList, 1);

        runner.run();

    }
}
