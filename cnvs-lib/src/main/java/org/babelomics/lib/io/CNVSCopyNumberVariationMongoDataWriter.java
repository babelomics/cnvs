package org.babelomics.lib.io;

import org.babelomics.lib.models.CNV;
import org.opencb.commons.io.DataWriter;

import java.util.List;

/**
 * @author Alejandro Alemán Ramos <alejandro.aleman.ramos@gmail.com>
 */
public class CNVSCopyNumberVariationMongoDataWriter implements DataWriter<CNV> {
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
    public boolean write(CNV elem) {
        System.out.println(elem.toString());
        return false;
    }

    @Override
    public boolean write(List<CNV> batch) {
        return false;
    }
}
