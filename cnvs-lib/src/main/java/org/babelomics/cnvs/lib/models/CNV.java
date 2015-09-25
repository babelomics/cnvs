package org.babelomics.cnvs.lib.models;

import org.babelomics.cnvs.lib.io.CNVSCopyNumberVariationMongoDataWriter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(noClassnameStored = true)
@Indexes({
        @Index(name = "index", value = "c,s,e"),
        @Index(name = "chunks", value = "_at.chIds")
})

public class CNV {

    @Id
    private ObjectId id;

    @Property("r")
    private String ref;
    @Property("di")
    private long decipherId;
    @Property("ci1")
    private String centerId1;
    @Property("ci2")
    private String centerId2;
    @Property("ci3")
    private String centerId3;
    @Property("c")
    private String chromosome;
    @Property("s")
    private long start;
    @Property("e")
    private long end;
    @Property("a")
    private String assembly;
    @Property("g")
    private String genes; // ARRAY
    @Property("l")
    private String band;
    /*@Property("sz")
    private String size; */ // LO QUITAMOS DE BD
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
    @Property("rd")
    private String referalDiag;
    @Property("p")
    private String phenotype;
    @Property("y")
    private int yearOfBirth;
    @Property("eg")
    private String ethnicGroup;
    @Property("o")
    private String origin;


    @Property("ap")
    private String arrayPlatform;
    @Property("ai")
    private String arrayId;
    @Property("co")
    private String comments;

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
        return band;
    }

    public void setLocus(String locus) {
        this.band = locus;
    }

	/*public String getSize() {
        return size;
	}

	public void setSize(String size) {
		this.size = size;
	}*/

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
        switch (doses.toLowerCase().replaceAll(" ", "")) {
            case "x0":
                this.doses = 0;
                break;
            case "x1":
                this.doses = 1;
                break;
            case "x2(xy)":
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

    public String getClinicalSig() {
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
                return "vous";
            default:
                return "";
        }

    }

    public void setClinicalSig(String clinicalSig) {

        switch (clinicalSig.toLowerCase()) {
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
            case "vous":
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

    public String getCellLine() {
        switch (cellLine) {
            case 0:
                return "germline";
            case 1:
                return "somatic";
            default:
                return "";
        }

    }

    public void setCellLine(String cellLine) {
        switch (cellLine.toLowerCase()) {
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

    public String getTypeSample() {
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

    public void setTypeSample(String typeSample) {
        switch (typeSample.toLowerCase()) {
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

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getReferalDiag() {
        return referalDiag;
    }

    public void setReferalDiag(String referalDiag) {
        this.referalDiag = referalDiag;
    }

    public String getEthnicGroup() {
        return ethnicGroup;
    }

    public void setEthnicGroup(String ethnicGroup) {
        this.ethnicGroup = ethnicGroup;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public long getDecipherId() {
        return decipherId;
    }

    public void setDecipherId(long decipherId) {
        this.decipherId = decipherId;
    }

    public String getArrayPlatform() {
        return arrayPlatform;
    }

    public void setArrayPlatform(String arrayPlatform) {
        this.arrayPlatform = arrayPlatform;
    }

    public String getArrayId() {
        return arrayId;
    }

    public void setArrayId(String arrayId) {
        this.arrayId = arrayId;
    }


	/*
    public void setType(int type) {
		this.type = type;
	} */

    @Override
    public String toString() {
        return "CNV [id=" + id + ", ref=" + ref + ", decipherId=" + decipherId
                + ", centerId1=" + centerId1 + ", centerId2=" + centerId2
                + ", centerId3=" + centerId3 + ", chromosome=" + chromosome
                + ", start=" + start + ", end=" + end + ", assembly="
                + assembly + ", genes=" + genes + ", locus=" + band
                + ", type=" + type + ", doses=" + doses + ", clinicalSig="
                + clinicalSig + ", inheritance=" + inheritance + ", nv=" + nv
                + ", cellLine=" + cellLine + ", chromGender=" + chromGender
                + ", status=" + status + ", typeSample=" + typeSample
                + ", referalDiag=" + referalDiag + ", phenotype=" + phenotype
                + ", yearOfBirth=" + yearOfBirth + ", ethnicGroup="
                + ethnicGroup + ", origin=" + origin + ", arrayPlatform="
                + arrayPlatform + ", arrayId=" + arrayId + ", comments="
                + comments + "]";
    }

    public String getCenterId1() {
        return centerId1;
    }

    public void setCenterId1(String centerId1) {
        this.centerId1 = centerId1;
    }

    public String getCenterId2() {
        return centerId2;
    }

    public void setCenterId2(String centerId2) {
        this.centerId2 = centerId2;
    }

    public String getCenterId3() {
        return centerId3;
    }

    public void setCenterId3(String centerId3) {
        this.centerId3 = centerId3;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
