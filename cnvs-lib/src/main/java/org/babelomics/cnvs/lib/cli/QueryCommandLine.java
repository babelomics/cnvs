package org.babelomics.cnvs.lib.cli;

import java.util.List;

import com.beust.jcommander.Parameter;

public class QueryCommandLine {
    @Parameter(names = {"--all"}, description = "List all variants", arity = 0) //REVISAR PERO HECHO
    private boolean all = false;

	@Parameter(names = {"--code"}, description = "Code INGEMM-ING, NIM GENETIICS-NIM, QGENOMICS-QGE, SANTIAGO-USC")
    private String code = null;

    @Parameter(names = {"--decipher-id"}, description = "Decipher ID")
    private List<Long>  decipId = null;

	/*@Parameter(names = {"--centerId1"}, description = "Local Id1")
	private List<String> centerId1 = null;

	@Parameter(names = {"--centerId2"}, description = "Local Id2")
	private List<String> centerId2 = null;

	@Parameter(names = {"--centerId3"}, description = "Local Id3")
	private List<String> centerId3 = null;*/

    @Parameter(names = {"--regions"}, description = "Comma-separated list of regions")//HECHO
    private String regionList = null;

	@Parameter(names = {"--sizeOp"}, description = "Size: sign for use together 'sigNum'. Default: '=' ")
	private String sizeOp = "=";

	@Parameter(names = {"--sizeNum"}, description = "Size: single value for search")
	private double sizeNum = -1;

    @Parameter(names = {"--assembly"}, description = "Assembly hg18, hg19")
    private List<String> assembly = null;

    @Parameter(names = {"--band"}, description = "Band")
    private List<String> band = null;

    @Parameter(names = {"--type"}, description = "Type of variant:0 gain,1 loss or 2 LOH neutral")
    private List<Integer> type = null;

    @Parameter(names = {"--doses"}, description = "Doses: 0 homozygous deletion, 1 deletion,2 duplication,3 triplication, 4 amplification")
    private List<Integer>  doses = null;

	@Parameter(names = {"--dosesOp"}, description = "Doses: sign for use together 'dosesNum'")
	private String dosesOp = "=";

	@Parameter(names = {"--dosesNum"}, description = "Doses: single value for search")
	private double dosesNum = -1;

    @Parameter(names = {"--clis"}, description = "Clinical Significance: 0 Definitely pathogenic, 1 Probably pathogenic, 2 Pathogenic, 3 Vous, 4 Likely benign, 5 Benign")
    private List<Integer>  cli = null;

    @Parameter(names = {"--inheritance"}, description = " Inheritance: 0 de novo constitutive, 1 maternally inherited, 2 paternally inherited, 3unknown")
    private List<Integer>  inhe = null;

	@Parameter(names = {"--nv"}, description = "Number of variants")
	private int nv = -1;

    @Parameter(names = {"--cl"}, description = "Cell Line: 0 germline, 1 somatic")
    private int cl = -1;

    @Parameter(names = {"--gender"}, description = "Chromosomal gender: 0 XX, 1 XY , 2 X, 3 XXX, 4 XXY, 5 XYY, 6 Unknown")
    private int gender = -1;

 	@Parameter(names = {"--status"}, description = "Status: 0 Proband, 1 Father, 2 Mother, 3 Control")
    private List<Integer> status;

    @Parameter(names = {"--type-sample"}, description = "Type of sample: 0 blood, 1 amniotic fluid, 2 chorionic villi, 3 tumour, 4 etc")
    private List<Integer> typeSample;

	@Parameter(names = {"--ref-diagnosis"}, description = "Referal Diagnosis. Ex.:Control")
	private List<String> referalDiag;

    @Parameter(names = {"--hpo"}, description = "Phenotype (HPO)")
    private List<String> hpo = null;

    @Parameter(names = {"--year"}, description = "Year of Birth")
    private List<Integer> year;

	@Parameter(names = {"--year-test"}, description = "Year test performed")
	private List<Integer> yearTest;

	@Parameter(names = {"--age"}, description = "Age")
	private List<String> age;

	@Parameter(names = {"--age-prenatal"}, description = "Age Prenatal")
	private List<Integer> agePrenatal;

    @Parameter(names = {"--ethic"}, description = "Ethic Group")
    private List<String> ethic = null;

	@Parameter(names = {"--geo"}, description = "Geographic origin")
	private List<String> origin = null;

	@Parameter(names = {"--array-platform"}, description = "Array Platform")
    private List<String> arrayPlatform = null;

	@Parameter(names = {"--array-id"}, description = "Array id")
	private List<String> arrayId = null;

	@Parameter(names = {"--syndrome"}, description = "Aneuploid-Deletion-Duplication Syndrome")
	private List<Integer> syndrome = null;

    @Parameter(names = {"--skip"}, description = "Skip")
    private int skip = -1;

    @Parameter(names = {"--limit"}, description = "Limit number of results ")
    private int limit = -1;

    @Parameter(names = {"--host"}, description = "DB host", arity = 1)
    private String host = "localhost";

    @Parameter(names = {"--user"}, description = "DB User", arity = 1)
    private String user = "";

    @Parameter(names = {"--pass"}, description = "DB Pass", arity = 1)
    private String pass = "";

//	public QueryCommandLine(boolean all, String code, List<Long> decipId, List<String> centerId1, List<String> centerId2, List<String> centerId3,
//							String regionList, String assembly, List<String> band, List<Integer> type, List<Integer> doses,
//							List<Integer> cli, List<Integer> inhe, int nv, int cl, int gender, List<Integer> status,
//							List<Integer> typeSample, List<String> referalDiag, List<String> hpo, List<Integer> year,
//							List<Integer> yearTest, List<String> age, List<Integer> agePrenatal, List<String> ethic, List<String> origin,
//							List<String> arrayPlatform, List<String> arrayId,  List<Integer> syndrome, int skip,
//							int limit, String host, String user, String pass) {

	public String getDosesOp() {
		return dosesOp;
	}

	public void setDosesOp(String dosesOp) {
		this.dosesOp = dosesOp;
	}

	public double getDosesNum() {
		return dosesNum;
	}

	public void setDosesNum(double dosesNum) {
		this.dosesNum = dosesNum;
	}

	public QueryCommandLine(boolean all, String code, List<Long> decipId, String regionList, String sizeOp, int sizeNum, List<String> assembly, List<String> band, List<Integer> type,
							List<Integer> doses, String dosesOp, double dosesNum, List<Integer> cli, List<Integer> inhe, int nv, int cl, int gender, List<Integer> status,
							List<Integer> typeSample, List<String> referalDiag, List<String> hpo, List<Integer> year,
							List<Integer> yearTest, List<String> age, List<Integer> agePrenatal, List<String> ethic, List<String> origin,
							List<String> arrayPlatform, List<String> arrayId,  List<Integer> syndrome, int skip,
							int limit, String host, String user, String pass) {
		this.all = all;
		this.code = code;
		this.decipId = decipId;
//		this.centerId1 = centerId1;
//		this.centerId2 = centerId2;
//		this.centerId3 = centerId3;
		this.regionList = regionList;
		this.sizeOp = sizeOp;
		this.sizeNum = sizeNum;
		this.assembly = assembly;
		this.band = band;
		this.type = type;
		this.doses = doses;
		this.dosesOp = dosesOp;
		this.dosesNum = dosesNum;
		this.cli = cli;
		this.inhe = inhe;
		this.nv = nv;
		this.cl = cl;
		this.gender = gender;
		this.status = status;
		this.typeSample = typeSample;
		this.referalDiag = referalDiag;
		this.hpo = hpo;
		this.year = year;
		this.yearTest = yearTest;
		this.age = age;
		this.agePrenatal = agePrenatal;
		this.ethic = ethic;
		this.arrayPlatform = arrayPlatform;
		this.arrayId = arrayId;
		this.origin = origin;
		this.syndrome = syndrome;
		this.skip = skip;
		this.limit = limit;
		this.host = host;
		this.user = user;
		this.pass = pass;
	}

	public QueryCommandLine(int skip, int limit, String host, String user, String pass) {
		this.skip = skip;
		this.limit = limit;
		this.host = host;
		this.user = user;
		this.pass = pass;
	}

	public QueryCommandLine() {
	}
	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Long> getDecipId() {
		return decipId;
	}

	public void setDecipId(List<Long> decipId) {
		this.decipId = decipId;
	}

	/*public List<String> getcenterId1() {
		return centerId1;
	}

	public void setcenterId1(List<String> centerId1) {
		this.centerId1 = centerId1;
	}

	public List<String> getcenterId2() {
		return centerId2;
	}

	public void setcenterId2(List<String> centerId2) {
		this.centerId2 = centerId2;
	}

	public List<String> getcenterId3() {
		return centerId3;
	}

	public void setcenterId3(List<String> centerId3) {
		this.centerId3 = centerId3;
	}*/

	public String getRegionList() {
		return regionList;
	}

	public void setRegionList(String regionList) {
		this.regionList = regionList;
	}

	public List<String> getAssembly() {
		return assembly;
	}

	public void setAssembly(List<String> assembly) {
		this.assembly = assembly;
	}

	public List<String> getBand() {
		return band;
	}

	public void setBand(List<String> band) {
		this.band = band;
	}

	public List<Integer> getType() {
		return type;
	}

	public void setType(List<Integer> type) {
		this.type = type;
	}

	public List<Integer> getDoses() {
		return doses;
	}

	public void setDoses(List<Integer> doses) {
		this.doses = doses;
	}

	public List<Integer> getCli() {
		return cli;
	}

	public void setCli(List<Integer> cli) {
		this.cli = cli;
	}

	public List<Integer> getInhe() {
		return inhe;
	}

	public void setInhe(List<Integer> inhe) {
		this.inhe = inhe;
	}

	public int getNv() {
		return nv;
	}

	public void setNv(int nv) {
		this.nv = nv;
	}

	public int getCl() {
		return cl;
	}

	public void setCl(int cl) {
		this.cl = cl;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public List<Integer> getStatus() {
		return status;
	}

	public void setStatus(List<Integer> status) {
		this.status = status;
	}

	public List<Integer> getTypeSample() {
		return typeSample;
	}

	public void setTypeSample(List<Integer> typeSample) {
		this.typeSample = typeSample;
	}

	public List<String> getHpo() {
		return hpo;
	}

	public void setHpo(List<String> hpo) {
		this.hpo = hpo;
	}

	public List<String> getReferalDiag() {
		return referalDiag;
	}

	public void setReferalDiag(List<String> referalDiag) {
		this.referalDiag = referalDiag;
	}

	public List<Integer> getYear() {
		return year;
	}

	public void setYear(List<Integer> year) {
		this.year = year;
	}

	public List<Integer> getYearTest() {
		return yearTest;
	}

	public void setYearTest(List<Integer> yearTest) {
		this.yearTest = yearTest;
	}

	public List<String> getAge() {
		return age;
	}

	public void setAge(List<String> age) {
		this.age = age;
	}

	public List<Integer> getAgePrenatal() {
		return agePrenatal;
	}

	public void setAgePrenatal(List<Integer> agePrenatal) {
		this.agePrenatal = agePrenatal;
	}
	public List<String> getEthic() {
		return ethic;
	}

	public void setEthic(List<String> ethic) {
		this.ethic = ethic;
	}


	public List<String> getOrigin() {
		return origin;
	}

	public void setOrigin(List<String> origin) {
		this.origin = origin;
	}
	public List<String> getArrayPlatform() {
		return arrayPlatform;
	}

	public void setArrayPlatform(List<String> arrayPlatform) {
		this.arrayPlatform = arrayPlatform;
	}

	public List<String> getArrayId() {
		return arrayId;
	}

	public void setArrayId(List<String> arrayId) {
		this.arrayId = arrayId;
	}

	public List<Integer> getSyndrome() {
		return syndrome;
	}

	public void setSyndrome(List<Integer> syndrome) {
		this.syndrome = syndrome;
	}

	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

}
