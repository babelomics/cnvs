package org.babelomics.lib.io;

import org.babelomics.lib.models.CNV;
import org.opencb.commons.io.DataReader;

import java.util.List;

/**
 * @author Alejandro Alemán Ramos <alejandro.aleman.ramos@gmail.com>
 */
public class CNVSCopyNumberVariationXLSDataReader implements DataReader<CNV> {

    String fileName;

    public CNVSCopyNumberVariationXLSDataReader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean open() {
        return false;
    }

    @Override
    public boolean close() {
        return false;
    }

    @Override
    public boolean pre() {
        return false;
    }

    @Override
    public boolean post() {
        return false;
    }

    @Override
    public List<CNV> read() {
        return null;
    }

    @Override
    public List<CNV> read(int batchSize) {
        return null;
    }
}
