package org.babelomics.lib.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.babelomics.lib.io.CNVSCopyNumberVariationMongoDataWriter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity(noClassnameStored=true)
@Indexes(@Index(name = "index", value="c,p,e", unique = true))

public class CNV {

	@Id
	private ObjectId id;
	
	@Property("r")
	private String ref;
	@Property("c")
	private String chromosome;
	@Property("s")
	private long start;
	@Property("e")
	private long end;
	@Property("a")
	private String assembly;
	@Property("g")
	private String genes;
	@Property("l")
	private String locus;
	@Property("sz")
	private String size;
	@Property("t")
	private int type;
	@Property("d")
	private int doses;
	@Property("cs")
	private int clinicalSig;
	@Property("i")
	private int inheritance;
	private int nv; // Numero de variant
	@Property("cl")
	private int cellLine; // 0 germline, 1 somatic
	@Property("cg")
	private int chromGender; //0 XX, 1 XY
	@Property("st")
	private int status; //0 proband, 1 Father, 2 Mother, 3 Control, 4 Other relatives
	@Property("ts")
	private int typeSample;
	@Property("p")
	private String phenotype;
	@Property("y")
	private int yearOfBirth;
	@Property("rd")
	private String referalDiag;
	@Property("eg")
	private String ethnicGroup;
	@Property("o")
	private String origin;
	@Property("di")
	private String decipherId;
	@Property("ap")
	private String arrayPlatform;
	@Property("ai")
	private String arrayId;
	@Property("ci")
	private String centerId;
	
	@Property("_at")
    private Map<String, Object> attr;
	


	public CNV() {
        this.attr = new HashMap<>();

	}

	public String getAssembly() {
		return assembly;
	}

	public void setAssembly(String assembly) {
		this.assembly = assembly;
	}

	public String getGenes() {
		return genes;
	}

	public void setGenes(String genes) {
		this.genes = genes;
	}

	public String getLocus() {
		return locus;
	}

	public void setLocus(String locus) {
		this.locus = locus;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getDoses() {
		switch (doses) {
		case 0:
			return "x0";
		case 1:
			return "x1";

		case 2:
			return "x2(XY)";
		case 3:
			return "x3";
		case 4:
			return "x4";
		default:
			return "";
		}
	}

	public void setDoses(String doses) {
		switch (doses.toLowerCase()) {
		case "x0":
			this.doses = 0;
			break;
		case "x1":
			this.doses = 1;
			break;
		case "x2":
			this.doses = 2;
			break;
		case "x3":
			this.doses = 3;
			break;
		case "x4":
			this.doses = 4;
			break;
		default:
			this.doses = -1;
			break;
		}
		
	}

	public String getClinical_Sig() {
		switch (clinicalSig) {
		case 0:
			return "benign";
		case 1:
			return "pathogenic";

		case 2:
			return "likely benign";
		case 3:
			return "likely pathogenic";
		case 4:
			return "uncertain";
		default:
			return "";
		}
		
	}

	public void setClinical_Sig(String clinical_Sig) {
		
		switch (clinical_Sig.toLowerCase()) {
		case "benign":
			this.clinicalSig = 0;
			break;		
		case "pathogenic":
			this.clinicalSig = 1;
			break;
		case "likely benign":
			this.clinicalSig = 2;
			break;
		case "likely pathogenic":
			this.clinicalSig = 3;
			break;
		case "uncertain":
			this.clinicalSig = 4;
			break;
		default:
			this.clinicalSig = -1;
			break;
		}
	}

	public String getInheritance() {
		switch (inheritance) {
		case 0:
			return "de novo constitutive";
		case 1:
			return "maternally inherited";

		case 2:
			return "paternally inherited";
		case 3:
			return "unknown";
		default:
			return "";
		}
		
	}

	public void setInheritance(String inheritance) {
		switch (inheritance.toLowerCase()) {
		case "de novo constitutive":
			this.inheritance = 0;
			break;
		case "maternally inherited":
			this.inheritance = 1;
			break;

		case "paternally inherited":
			this.inheritance = 2;
			break;
		case "unknown":
			this.inheritance = 3;
			break;
		default:
			this.inheritance = -1;
			break;

		}
		
	}

	public int getNv() {
		return nv;
	}

	public void setNv(int nv) {
		this.nv = nv;
	}

	public String getCell_Line() {
		switch (cellLine) {
		case 0:
			return "germline";
		case 1:
			return "somatic";
		default:
			return "";
		}

	}

	public void setCell_Line(String cell_Line) {
		switch (cell_Line.toLowerCase()) {
		case "germline":
			this.cellLine = 0;
			break;
		case "somatic":
			this.cellLine = 1;
			break;
		default:
			this.cellLine = -1;
			break;

		}
		
	}

	public String getChromoGender() {
		switch (chromGender) {
		case 0:
			return "XX";
		case 1:
			return "XY";
		default:
			return "";
		}
		
	}

	public void setChromoGender(String chromGender) {
		switch (chromGender.toLowerCase()) {
		case "xx":
			this.chromGender = 0;
			break;
		case "xy":
			this.chromGender = 1;
			break;
		default:
			this.chromGender = -1;
			break;

		}
	}

	public String getStatus() {
		switch (status) {
		case 0:
			return "Proband";
		case 1:
			return "Father";

		case 2:
			return "Mother";
		case 3:
			return "Control";
		case 4:
			return "Other relatives";
		default:
			return "";
		}
	}

	public void setStatus(String status) {
		switch (status.toLowerCase()) {
		case "proband":
			this.status = 0;
			break;
		case "father":
			this.status = 1;
			break;
		case "mother":
			this.status = 2;
			break;
		case "control":
			this.status = 3;
			break;
		case "other relatives":
			this.status = 4;
			break;
		default:
			this.status = -1;
			break;

		}
	}

	public String getType_Sample() {
		switch (typeSample) {
		case 0:
			return "blood";
		case 1:
			return "amniotic fluid";

		case 2:
			return "chorionic villi";
		case 3:
			return "tumour";
		case 4:
			return "others";
		default:
			return "";
		}
	
	}

	public void setType_Sample(String type_Sample) {
		switch (type_Sample.toLowerCase()) {
		case "blood":
			this.typeSample = 0;
			break;
		case "amniotic fluid":
			this.typeSample = 1;
			break;
		case "chorionic villi":
			this.typeSample = 2;
			break;
		case "tumour":
			this.typeSample = 3;
			break;
		case "others":
			this.typeSample = 4;
			break;
		default:
			this.typeSample = -1;
			break;

		}
		
	}

	public String getPhenotype() {
		return phenotype;
	}

	public void setPhenotype(String phenotype) {
		this.phenotype = phenotype;
	}

	public int getYear_of_Birth() {
		return yearOfBirth;
	}

	public void setYear_of_Birth(int year_of_Birth) {
		this.yearOfBirth = year_of_Birth;
	}

	public String getReferal_diag() {
		return referalDiag;
	}

	public void setReferal_diag(String referal_diag) {
		this.referalDiag = referal_diag;
	}

	public String getEthnic_Group() {
		return ethnicGroup;
	}

	public void setEthnic_Group(String ethnic_Group) {
		this.ethnicGroup = ethnic_Group;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDecipher_id() {
		return decipherId;
	}

	public void setDecipher_id(String decipher_id) {
		this.decipherId = decipher_id;
	}

	public String getArray_platform() {
		return arrayPlatform;
	}

	public void setArray_platform(String array_platform) {
		this.arrayPlatform = array_platform;
	}

	public String getArray_id() {
		return arrayId;
	}

	public void setArray_id(String array_id) {
		this.arrayId = array_id;
	}

	public String getCenter_id() {
		return centerId;
	}

	public void setCenter_id(String center_id) {
		this.centerId = center_id;
	}
/*
	public void setType(int type) {
		this.type = type;
	} */

	@Override
	public String toString() {
		return "CNV [ref=" + ref + ", chromosome=" + chromosome + ", start="
				+ start + ", end=" + end + ", assembly=" + assembly
				+ ", genes=" + genes + ", brazo_Cromo=" + locus
				+ ", size=" + size + ", type=" + type + ", doses=" + doses
				+ ", clinical_Sig=" + clinicalSig + ", inheritance="
				+ inheritance + ", nv=" + nv + ", cell_Line=" + cellLine
				+ ", chromo_Gender=" + chromGender + ", status=" + status
				+ ", type_Sample=" + typeSample + ", phenotype=" + phenotype
				+ ", year_of_Birth=" + yearOfBirth + ", referal_diag="
				+ referalDiag + ", ethnic_Group=" + ethnicGroup + ", origin="
				+ origin + ", decipher_id=" + decipherId + ", array_platform="
				+ arrayPlatform + ", array_id=" + arrayId + ", center_id="
				+ centerId + "]";
	}

	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public String getType() {
		switch (type) {
		case 0:
			return "gain";
		case 1:
			return "loss";

		case 2:
			return "LOH neutral";
		default:
			return "";
		}
	}

	public void setType(String type) {
		switch (type.toLowerCase()) {
		case "gain":
			this.type = 0;
			break;
		case "loss":
			this.type = 1;
			break;

		case "loh neutral":
			this.type = 2;
			break;
		default:
			this.type = -1;
			break;
		}
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
	
	 @PrePersist
	    private void prePresist() {

	        String chunkSmall = this.getChromosome() + "_" + this.getStart() / CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_SMALL + "_" + CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_SMALL / 1000 + "k";
	        String chunkBig = this.getChromosome() + "_" + this.getStart() / CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_BIG + "_" + CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_BIG / 1000 + "k";
	        List<String> chunks = Arrays.asList(chunkSmall, chunkBig);

	        this.attr.put("chIds", chunks);

	    }
	
	

}
