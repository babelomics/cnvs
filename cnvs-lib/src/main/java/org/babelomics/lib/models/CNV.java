package org.babelomics.lib.models;

/**
 * @author Alejandro Alemán Ramos <alejandro.aleman.ramos@gmail.com>
 */
public class CNV {

	private String ref;
	private String chromosome;
	private long start;
	private long end;
	private String assembly;
	private String genes;
	private String brazoCromo;
	private String size;
	private int type;
	private int doses;
	private int clinicalSig;
	private int inheritance;
	private int nv; // Numero de variant
	private int cellLine; // 0 germline, 1 somatic
	private int chromGender; //0 XX, 1 XY
	private int status; //0 proband, 1 Father, 2 Mother, 3 Control, 4 Other relatives
	private int typeSample;
	private String phenotype;
	private int yearOfBirth;
	private String referalDiag;
	private String ethnicGroup;
	private String origin;
	private String decipherId;
	private String arrayPlatform;
	private String arrayId;
	private String centerId;
	
	
	
	
	public CNV(String ref, String chromosome, long start, long end,
			String assembly, String genes, String brazo_Cromo, String size,
			int type, int doses, int clinical_Sig, int inheritance, int nv,
			String cell_Line, String chromo_Gender, int status,
			String type_Sample, String phenotype, int year_of_Birth,
			String referal_diag, String ethnic_Group, String origin,
			String decipher_id, String array_platform, String array_id,
			String center_id) {
		this.setRef(ref);
//		this.ref = ref;
//		this.chromosome = chromosome;
//		this.start = start;
//		this.end = end;
//		this.assembly = assembly;
//		this.genes = genes;
//		this.brazo_Cromo = brazo_Cromo;
//		this.size = size;
//		this.type = type;
//		this.doses = doses;
//		this.clinical_Sig = clinical_Sig;
//		this.inheritance = inheritance;
//		this.nv = nv;
//		this.cell_Line = cell_Line;
//		this.chromo_Gender = chromo_Gender;
//		this.status = status;
//		this.type_Sample = type_Sample;
//		this.phenotype = phenotype;
//		this.year_of_Birth = year_of_Birth;
//		this.referal_diag = referal_diag;
//		this.ethnic_Group = ethnic_Group;
//		this.origin = origin;
//		this.decipher_id = decipher_id;
//		this.array_platform = array_platform;
//		this.array_id = array_id;
//		this.center_id = center_id;
	}

	public CNV() {
		super();
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

	public String getBrazoCromo() {
		return brazoCromo;
	}

	public void setBrazoCromo(String brazoCromo) {
		this.brazoCromo = brazoCromo;
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
				+ ", genes=" + genes + ", brazo_Cromo=" + brazoCromo
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
	
	
	

}
