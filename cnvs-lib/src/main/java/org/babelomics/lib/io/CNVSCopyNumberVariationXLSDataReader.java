package org.babelomics.lib.io;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.babelomics.lib.models.CNV;
import org.opencb.commons.io.DataReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jersey.repackaged.com.google.common.collect.Lists;

/**
 * @author Alejandro Alemán Ramos <alejandro.aleman.ramos@gmail.com>
 */
public class CNVSCopyNumberVariationXLSDataReader implements DataReader<CNV> {

    private String fileName;
    private Workbook workbook;
    private Sheet sheet;
    private FileInputStream fis;
    private Map<Integer, String> header;
    private Iterator<Row> it;

    public CNVSCopyNumberVariationXLSDataReader(String fileName) {
        this();
    	this.fileName = fileName;
        
    }
    public CNVSCopyNumberVariationXLSDataReader() {
	this.header= new HashMap<>();
	}

    @Override
    public boolean open() {
    	
    	try {
			this.fis = new FileInputStream(this.fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
    	
        return true;
    }

    @Override
    public boolean close() {
    	
    	try {
			this.fis.close();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        return true;
    }

    @Override
    public boolean pre() {
    	
    	try {
			
    		this.workbook = WorkbookFactory.create(this.fis);
			this.sheet = this.workbook.getSheetAt(0);
			
			this.it = this.sheet.rowIterator();
			
			
		} catch (InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	
    	
    	//header 
    	
    	Row headerRow= this.it.next();
    	
    	for(int i = 0; i < headerRow.getLastCellNum(); i++){
    		Cell cell = headerRow.getCell(i, Row.CREATE_NULL_AS_BLANK);
    		this.header.put(i,cell.getStringCellValue());    		
    	}
    	
    	System.out.println(this.header);
    	
        return true;
    }

    @Override
    public boolean post() {
        return false;
    }

    @Override
    public List<CNV> read() {
    	CNV cnv = new CNV();
    	if(this.it.hasNext()){

    		List<CNV> list = new ArrayList<>();
    		Row r = this.it.next();
    		
    		String ref = r.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String chr = r.getCell(1, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		long start = (long) r.getCell(2, Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
    		long end = (long) r.getCell(3, Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
    		String type = r.getCell(8, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		
    		
    		cnv.setRef(ref);
    		cnv.setChromosome(chr);
    		cnv.setStart(start);
    		cnv.setEnd(end);
    		cnv.setType(type);
    		
    		list.add(cnv);
    		
    		return list;
    	}else{
    		return null;	
    	}
    	
        
    }

    @Override
    public List<CNV> read(int batchSize) {
    	
    	List<CNV> listRecords = new ArrayList<>(batchSize);

        int i = 0;
        List<CNV> cnvs;
        while ((i < batchSize) && (cnvs = this.read()) != null && cnvs.size() > 0) {
            listRecords.addAll(cnvs);
            i += cnvs.size();
        }

        return listRecords;
    }
}
