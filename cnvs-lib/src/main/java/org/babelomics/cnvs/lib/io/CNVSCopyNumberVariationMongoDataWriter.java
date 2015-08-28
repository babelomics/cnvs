package org.babelomics.cnvs.lib.io;

import org.babelomics.cnvs.lib.models.CNV;
import org.mongodb.morphia.Datastore;
import org.opencb.commons.io.DataWriter;

import java.util.List;

/**
 * @author Alejandro Alemán Ramos <alejandro.aleman.ramos@gmail.com>
 */
public class CNVSCopyNumberVariationMongoDataWriter implements DataWriter<CNV> {
	
	Datastore datastore;
	
	public static final int CHUNK_SIZE_SMALL = 1000;
    public static final int CHUNK_SIZE_BIG = 10000;	
	
    public CNVSCopyNumberVariationMongoDataWriter(Datastore datastore) {
		super();
		this.datastore = datastore;
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
    public boolean write(CNV elem) {
    	
    	datastore.save(elem);
        
    	return true;
    }

    @Override
    public boolean write(List<CNV> batch) {
    	for(CNV c: batch){
    		this.write(c);
    	}
        return true;
    }
}
