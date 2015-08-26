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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CNVSCopyNumberVariationXLSDataReader implements DataReader<CNV> {

    private String fileName;
    private Workbook workbook;
    private Sheet sheet;
    private FileInputStream fis;
    private Map<Integer, String> header;
    private Iterator<Row> it;
    private int nc;// numero de campos-columnas
    private int vacio = 0;

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
    	/*
    	Row headerRow= this.it.next();
    	nc = headerRow.getLastCellNum();
    	for(int i = 0; i < nc; i++){
    		Cell cell = headerRow.getCell(i, Row.CREATE_NULL_AS_BLANK);
    		this.header.put(i,cell.getStringCellValue());    		
    	}
    	*/
    	System.out.println(this.header);
    	
    	String[] columnas = {"CASE Ref", "Chromosome", "Start", "End", "Assembly", "Genes", " " , "Size (Kb)", "Type of variant" , "Doses", "Clinical significance", "Inheritance", "Number of variants", "Cell line", "Chromosomal gender", "Status", "Type of sample", "Phenotype (HPO)", "Year of Birth", "Referral diagnosis", "Ethnic group", "Geographical origin (if Spain, province)", "Decipher ID", "Array platform", "array ID", "ID"};
    	Row headerRow= this.it.next();
    	nc = headerRow.getLastCellNum();
    	for(int i = 0; i < nc; i++){
    		String col = (headerRow.getCell(i, Row.CREATE_NULL_AS_BLANK)).getStringCellValue();
    		if(!(columnas[i].replaceAll(" ", "")).equals(col.replaceAll(" ", ""))){
    			System.out.println("El programa no reconoce las columnas, las columnas deben ser exactamente estas: ");
    			System.out.print("{ ");
				for (String c: columnas){
					System.out.print(c);
					System.out.print(",");
				}
				System.out.println("}");
				System.out.println( "La columna que se difencia es la numero " + i + "(" + col + ") debería ser "+ columnas[i] + "");
				System.exit(0);
    		}
    	}
    		 		
    	System.out.println("*** Se han reconocido todas las columnas del excel, cargando a la base de datos: ***");
    	
        return true;
    }

    @Override
    public boolean post() {
        return false;
    }

   
    public List<CNV> read() {
    	CNV cnv = new CNV();
    	
    	if(this.it.hasNext() && vacio <3){

    		List<CNV> list = new ArrayList<>();
    		Row r = this.it.next();
    		
    		
			String ref = r.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
			if(ref.equals("")){
				vacio++;
				System.out.println("PASO POR VACIO **************************"+ vacio);
			}
    		String chr = r.getCell(1, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		long start = (long) r.getCell(2, Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
    		long end = (long) r.getCell(3, Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
    		String ass = r.getCell(4, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String gen = r.getCell(5, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String bc = r.getCell(6, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String size = r.getCell(7, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String type = r.getCell(8, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String doses = r.getCell(9, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String clisig = r.getCell(10, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String inhe = r.getCell(11, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		int nv = (int)r.getCell(12, Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
    		String cellLine = r.getCell(13, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String chrgen = r.getCell(14, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String status = r.getCell(15, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String typeSample = r.getCell(16, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String hpo = r.getCell(17, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		int yearB = (int)r.getCell(18, Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
    		String refediag = r.getCell(19, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String ehnic = r.getCell(20, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String geo = r.getCell(21, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String deciId = r.getCell(22, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String arrayPlat = r.getCell(23, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String arrayId = r.getCell(24, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    		String centerId = r.getCell(25, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
	    		
	    		
    		
    		
    		cnv.setRef(ref);
    		cnv.setChromosome(chr);
    		cnv.setStart(start);
    		cnv.setEnd(end);
    		cnv.setAssembly(ass);
    		cnv.setGenes(gen);
    		cnv.setBrazoCromo(bc);
    		cnv.setSize(size);
    		cnv.setType(type);
    		cnv.setDoses(doses);
    		cnv.setClinical_Sig(clisig);
    		cnv.setInheritance(inhe);
    		cnv.setNv(nv);
    		cnv.setCell_Line(cellLine);
    		cnv.setChromoGender(chrgen);
    		cnv.setStatus(status);
    		cnv.setType_Sample(typeSample);
    		cnv.setPhenotype(hpo);
    		cnv.setYear_of_Birth(yearB);
    		cnv.setReferal_diag(refediag);
    		cnv.setEthnic_Group(ehnic);
    		cnv.setOrigin(geo);
    		cnv.setDecipher_id(deciId);
    		cnv.setArray_platform(arrayPlat);
    		cnv.setArray_id(arrayId);
    		cnv.setCenter_id(centerId);
    		
    		
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
