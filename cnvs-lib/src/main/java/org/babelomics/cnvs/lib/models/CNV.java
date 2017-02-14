package org.babelomics.cnvs.lib.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.babelomics.cnvs.lib.io.CNVSCopyNumberVariationMongoDataWriter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.*;

@Entity(noClassnameStored = true)
@Indexes({
        @Index(name = "index", value = "c,s,e"),
        @Index(name = "chunks", value = "_at.chIds")
})

public class CNV {

    @Id
    private ObjectId id;

    @Property("r")
    private String code;
    @Property("di")
    private long decipherId;
    @JsonIgnore
    @Property("ci1")
    private String centerId1;
    @JsonIgnore
    @Property("ci2")
    private String centerId2;
    @JsonIgnore
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
    private double doses;

    @Property("dn")
    private double dosesNum; // Mean log2 ratio
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
    @Property("yt")
    private int yearTest;
    private int age;
    @Property("agep")
    private int agePrenatal;
    @Property("eg")
    private String ethnicGroup;
    @Property("o")
    private String origin;
    @Property("ap")
    private String arrayPlatform;
    @Property("ai")
    private String arrayId;


    @JsonIgnore
    @Transient
    private String syndromeName;
    //@Embedded("sy")
    @Reference("sy")
    private List<Syndrome> syndrome;
    @Property("co")
    private String comments;
    @Property("_at")
    private Map<String, Object> attr;




    public CNV() {
        this.attr = new HashMap<>();
        this.syndrome = new ArrayList<>();
    }

    public String getAssembly() {
        switch (assembly) {
            case "GRCh36/hg18":
            case "hg18":
                return "GRCh36/hg18";
            case "GRCh37/hg19":
            case "hg19":
                return "GRCh37/hg19";
            case "GRCh38/hg38":
            case "hg38":
                return "GRCh38/hg38";
            default:
                return assembly;
        }
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

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getDoses() {
       /* switch (doses) {
            case 0:
                return "Homozygous deletion";
            case 1:
                return "Deletion";
            case 2:
                return "Duplication";
            case 3:
                return "Triplication";
            case 4:
                return "Amplification";
            default:
                return ""+doses;
        }*/
//        if(doses <= -1)
//            return "Homozygous deletion("+doses+")";
//        else if(doses > -1 && doses < -0.35){
//            return "deletion("+doses+")";
//        }else if(doses == 1){
//            return "(X duplication in males)";
//        }else if(0.35 < doses && doses < 0.6){
//            return "duplication("+doses+")";
//        }else if(0.61 < doses && doses < 1){
//            return "triplication("+doses+")";
//        }else if(doses > 1){
//            return "amplification("+doses+")";
//        }
//        return ""+doses;
        switch (((int) doses)) {
            case 1:
                return "0-Homozygous deletion";
            case 2:
                return "0-Hemyzygous Deletion";
            case 3:
                return "1-Deletion";
            case 4:
                return "3-Duplication";
            case 5:
                return "4-Triplication";
            case 6:
                return "5 or more-Amplification";
            default:
                return "";
        }
    }

    public void setDoses(String doses) {

        //TODO revisar si esto va a cambiar si es con los nuevos valores o que PARECE QUE CORRECTO.
        switch (doses.toLowerCase().replaceAll(" ", "")) {
            case "0-homozygousdeletion":
                this.doses = 1;
                break;
            case "0-hemyzygousdeletion":
                this.doses = 2;
                break;
            case "1-deletion":
                this.doses = 3;
                break;
            case "3-duplication":
                this.doses = 4;
                break;
            case "4-triplication":
                this.doses = 5;
                break;
            case "5ormore-amplification":
                this.doses = 6;
                break;
            default:
                this.doses = -1;
                break;
        }
    }

    public double getDosesNum() {
        return dosesNum;
    }

    public void setDosesNum(double dosesNum) {
        this.dosesNum = dosesNum;
    }

    public String getClinicalSig() {
        switch (clinicalSig) {
            /*case 0:
                return "benign";
            case 1:
                return "pathogenic";

            case 2:
                return "likely benign";
            case 3:
                return "likely pathogenic";
            case 4:
                return "vous";*/
            case 0:
                return "definitely pathogenic";
            case 1:
                return "probably pathogenic";
            case 2:
                return "possibly pathogenic";
            case 3:
                return "uncertain/vous";
            case 4:
                return "likely benign";
            case 5:
                return "benign";
            default:
                return "";
        }
    }

    public void setClinicalSig(String clinicalSig) {

        switch (clinicalSig.toLowerCase()) {
            case "definitely pathogenic":
            case "definitively pathogenic":
                this.clinicalSig = 0;
                break;
            case "probably pathogenic":
                this.clinicalSig = 1;
                break;
            case "possibly pathogenic":
                this.clinicalSig = 2;
                break;
            case "vous":
            case "uncertain":
                this.clinicalSig = 3;
                break;
            case "likely benign":
                this.clinicalSig = 4;
                break;
            case "benign":
                this.clinicalSig = 5;
                break;
            default:
                this.clinicalSig = -1;
                break;
        }
    }

    public String getInheritance() {
        switch (inheritance) {
            case 0:
                return "de novo, mosaic";
            case 1:
                return "de novo, constitutive";
            case 2:
                return "paternally inherited, constitutive in father";
            case 3:
                return "paternally inherited, mosaic in father";
            case 4:
                return "maternally inherited, constitutive in mother";
            case 5:
                return "maternally inherited, mosaic in mother";
            case 6:
                return "imbalance arising from a balanced parental rearrangement";
            case 7:
                return "biparental";
            case 8:
                return "unknown";
            default:
                return "";
        }

    }

    public void setInheritance(String inheritance) {
        switch (inheritance.toLowerCase()) {
            case "de novo, mosaic":
                this.inheritance = 0;
                break;
            case "de novo, constitutive":
                this.inheritance = 1;
                break;
            case "paternally inherited, constitutive in father":
                this.inheritance = 2;
                break;
            case "paternally inherited, mosaic in father":
                this.inheritance = 3;
                break;
            case "maternally inherited, constitutive in mother":
                this.inheritance = 4;
                break;
            case "maternally inherited, mosaic in mother":
                this.inheritance = 5;
                break;
            case "imbalance arising from a balanced parental rearrangement":
                this.inheritance = 6;
                break;
            case "biparental":
                this.inheritance = 7;
                break;
            case "unknown":
                this.inheritance = 8;
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

    public String getChromGender() {
        switch (chromGender) {
            case 0:
                return "XX";
            case 1:
                return "XY";
            case 2:
                return "X";
            case 3:
                return "XXX";
            case 4:
                return "XXY";
            case 5:
                return "XYY";
            case 6:
                return "Unknown";
            default:
                return "";
        }
    }

    public void setChromGender(String chromGender) {
        switch (chromGender.toLowerCase()) {
            case "xx":
                this.chromGender = 0;
                break;
            case "xy":
                this.chromGender = 1;
                break;
            case "x":
                this.chromGender = 2;
                break;
            case "xxx":
                this.chromGender = 3;
                break;
            case "xxy":
                this.chromGender = 4;
                break;
            case "xyy":
                this.chromGender = 5;
                break;
            case "unknown":
                this.chromGender = 6;
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
                return "bone marrow";
            case 5:
                return "others";
            case 6:
                return "unknown";
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
            case "bone marrow":
                this.typeSample = 4;
                break;
            case "others":
                this.typeSample = 5;
                break;
            case "unknown":
                this.typeSample = 6;
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
    public int getYearTest() {
        return yearTest;
    }

    public void setYearTest(int yearTest) {
        this.yearTest = yearTest;
    }

    public String getAge() {
        switch (age) {
            case -2:
                return "Prenatal";
            case -1:
                return "";
            default:
                return "" + age;
        }
    }

    public void setAge(String age) {
        age = age.toLowerCase();
        if (age.equals("prenatal")){
            this.age = -2;
        }else if(age.equals("")){
            this.age = -1;
        }else {
            this.age = new Double(age).intValue();
        }
    }

    public int getAgePrenatal() {
        return agePrenatal;
    }

    public void setAgePrenatal(int agePrenatal) {
        this.agePrenatal = agePrenatal;
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

    public String getSyndromeName() {
        return syndromeName;
    }

    public void setSyndromeName(String syndromeName) {
        this.syndromeName = syndromeName;
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

    @Override
    public String toString() {
        return "CNV{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", decipherId=" + decipherId +
                ", centerId1='" + centerId1 + '\'' +
                ", centerId2='" + centerId2 + '\'' +
                ", centerId3='" + centerId3 + '\'' +
                ", chromosome='" + chromosome + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", assembly='" + assembly + '\'' +
                ", genes='" + genes + '\'' +
                ", band='" + band + '\'' +
                ", type=" + type +
                ", doses=" + doses +
                ", dosesNum=" + dosesNum +
                ", clinicalSig=" + clinicalSig +
                ", inheritance=" + inheritance +
                ", nv=" + nv +
                ", cellLine=" + cellLine +
                ", chromGender=" + chromGender +
                ", status=" + status +
                ", typeSample=" + typeSample +
                ", referalDiag='" + referalDiag + '\'' +
                ", phenotype='" + phenotype + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                ", yearTest=" + yearTest +
                ", age=" + age +
                ", agePrenatal=" + agePrenatal +
                ", ethnicGroup='" + ethnicGroup + '\'' +
                ", origin='" + origin + '\'' +
                ", arrayPlatform='" + arrayPlatform + '\'' +
                ", arrayId='" + arrayId + '\'' +
                ", syndromeName='" + syndromeName + '\'' +
                ", syndrome=" + syndrome +
                ", comments='" + comments + '\'' +
                ", attr=" + attr +
                '}';
    }

    public void setCenterId3(String centerId3) {
        this.centerId3 = centerId3;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        if(chromosome != null && chromosome.length() != 0) {
            if(!chromosome.startsWith("chrom") && !chromosome.startsWith("chrm") && !chromosome.startsWith("chr") && !chromosome.startsWith("ch")) {
                this.chromosome = chromosome;
            } else {
                this.chromosome = chromosome.replaceFirst("chrom|chrm|chr|ch", "");
            }

        } else {
            throw new IllegalArgumentException("Chromosome must not be empty");
        }
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Syndrome> getSyndrome() {
        return syndrome;
    }
    public void setSyndrome(List<Syndrome> syndrome) {
        this.syndrome = syndrome;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    @PrePersist
    private void prePresist() {

        String chunkSmall = this.getChromosome() + "_" + this.getStart() / CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_SMALL + "_" + CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_SMALL / 1000 + "k";
        String chunkBig = this.getChromosome() + "_" + this.getStart() / CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_BIG + "_" + CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_BIG / 1000 + "k";
        List<String> chunks = Arrays.asList(chunkSmall, chunkBig);

        this.attr.put("chIds", chunks);
    }
}
