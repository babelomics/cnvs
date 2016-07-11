package org.babelomics.cnvs.lib.io;

import org.babelomics.cnvs.lib.models.CNV;
import org.opencb.commons.io.DataReader;
import org.opencb.commons.io.DataWriter;
import org.opencb.commons.run.Runner;
import org.opencb.commons.run.Task;

import java.util.List;

/**
 * @author Alejandro Alemán Ramos <alejandro.aleman.ramos@gmail.com>
 */
public class CNVSRunner extends Runner<CNV> {
    public CNVSRunner(DataReader<CNV> reader, List<? extends DataWriter<CNV>> dataWriters, List<Task<CNV>> tasks, int batchSize) {
        super(reader, dataWriters, tasks, batchSize);
    }

    public CNVSRunner(DataReader<CNV> reader, List<? extends DataWriter<CNV>> dataWriters, List<Task<CNV>> tasks) {
        super(reader, dataWriters, tasks);
    }
}
