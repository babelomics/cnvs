package org.babelomics.cnvs.lib.cli;

import java.util.List;

import com.beust.jcommander.Parameter;

public class QueryCommandLine {
	 @Parameter(names = {"--all"}, description = "List all variants", arity = 0) //REVISAR PERO HECHO
     private boolean all = false;

     @Parameter(names = {"--code"}, description = "Code INGEMM-ING, NIM GENETIICS-NIM, QGENOMICS-QGE, SANTIAGO-USC")
     private String code = null;

     @Parameter(names = {"--decipherId"}, description = "Decipher ID")
     private List<Long>  decipId = null;

     @Parameter(names = {"--regions"}, description = "Comma-separated list of regions")//HECHO
     private String regionList = null;

     @Parameter(names = {"--assembly"}, description = "Assembly hg18, hg19")
     private String assembly = null;

     @Parameter(names = {"--band"}, description = "Band")
     private List<String> band = null;

     @Parameter(names = {"--type"}, description = "Type of variant:0 gain,1 loss or 2 LOH neutral")
     private List<Integer> type = null;

     @Parameter(names = {"--doses"}, description = "Doses: 0 x0,1 x1,2 x2(XY),3 x3")
     private List<Integer>  doses = null;

     @Parameter(names = {"--clis"}, description = "Clinical Significance: 0 Benign, 1 Pathogenic, 2 Likely benign 3 Likely pathogenic 4 VOUS")
     private List<Integer>  cli = null;

     @Parameter(names = {"--inheritance"}, description = " Inheritance: 0 de novo constitutive, 1 maternally inherited, 2 paternally inherited, 3unknown")
     private List<Integer>  inhe = null;

     @Parameter(names = {"--cl"}, description = "Cell Line: 0 germline, 1 somatic")
     private int cl = -1;

     @Parameter(names = {"--gender"}, description = "Chromosomal gender: 0 XX, 1 XY")
     private int gender = -1;


 	@Parameter(names = {"--status"}, description = "Status: 0 Proband, 1 Father, 2 Mother, 3 Control")
    private List<Integer> status;


    @Parameter(names = {"--typeS"}, description = "Type of sample: 0 blood, 1 amniotic fluid, 2 chorionic villi, 3 tumour, 4 etc")
    private List<Integer> typeSample;

    @Parameter(names = {"--hpo"}, description = "Phenotype (HPO)")
    private List<String> hpo = null;

    @Parameter(names = {"--year"}, description = "Year of Birth")
    private List<Integer> year;

    @Parameter(names = {"--ethic"}, description = "Ethic Group")
    private List<String> ethic = null;

    @Parameter(names = {"--geo"}, description = "Geographic origin")
    private List<String> origin = null;

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

	public QueryCommandLine(boolean all, String code, List<Long> decipId, String regionList, String assembly, List<String> band, List<Integer> type, List<Integer> doses, List<Integer> cli, List<Integer> inhe, int cl, int gender, List<Integer> status, List<Integer> typeSample, List<String> hpo, List<Integer> year, List<String> ethic, List<String> origin, int skip, int limit, String host, String user, String pass) {
		this.all = all;
		this.code = code;
		this.decipId = decipId;
		this.regionList = regionList;
		this.assembly = assembly;
		this.band = band;
		this.type = type;
		this.doses = doses;
		this.cli = cli;
		this.inhe = inhe;
		this.cl = cl;
		this.gender = gender;
		this.status = status;
		this.typeSample = typeSample;
		this.hpo = hpo;
		this.year = year;
		this.ethic = ethic;
		this.origin = origin;
		this.skip = skip;
		this.limit = limit;
		this.host = host;
		this.user = user;
		this.pass = pass;
	}

	public QueryCommandLine() {
	}

	public List<Integer> getInhe() {
		return inhe;
	}

	public void setInhe(List<Integer> inhe) {
		this.inhe = inhe;
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


	public String getRegionList() {
		return regionList;
	}

	public void setRegionList(String regionList) {
		this.regionList = regionList;
	}

	public String getAssembly() {
		return assembly;
	}

	public void setAssembly(String assembly) {
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




	public List<Long> getDecipId() {
		return decipId;
	}

	public void setDecipId(List<Long> decipId) {
		this.decipId = decipId;
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

	public List<Integer> getYear() {
		return year;
	}

	public void setYear(List<Integer> year) {
		this.year = year;
	}




	public List<String> getHpo() {
		return hpo;
	}

	public void setHpo(List<String> hpo) {
		this.hpo = hpo;
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
