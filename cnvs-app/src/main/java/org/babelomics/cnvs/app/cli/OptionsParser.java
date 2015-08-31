package org.babelomics.cnvs.app.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

import java.util.ArrayList;
import java.util.List;


public class OptionsParser {

    private final JCommander jcommander;

    private final CommandLoad load;
    private final CommandQuery query;


    public OptionsParser() {
        jcommander = new JCommander();
        jcommander.addCommand(load = new CommandLoad());
        jcommander.addCommand(query = new CommandQuery());

    }

    interface Command {
    }

    @Parameters(commandNames = {"load"}, commandDescription = "Upload file in DB")
    class CommandLoad implements Command {

        @Parameter(names = {"-i", "--input"}, description = "File path to load", required = true, arity = 1)
        String input;
        
        @Parameter(names = {"--host"}, description = "DB host", arity = 1)
        String host = "localhost";

        @Parameter(names = {"--user"}, description = "DB User", arity = 1)
        String user = "";
        
        @Parameter(names = {"--pass"}, description = "DB Pass", arity = 1)
        String pass = "";
    }


    @Parameters(commandNames = {"query"}, commandDescription = "Query")
    class CommandQuery implements Command {

        @Parameter(names = {"--all"}, description = "List all variants", arity = 0) //REVISAR PERO HECHO
        boolean all = false;
        
        @Parameter(names = {"--code"}, description = "Code INGEMM-ING, NIM GENETIICS-NIM, QGENOMICS-QGE, SANTIAGO-USC")
        String code = "";
        
        @Parameter(names = {"--decipherId"}, description = "Decipher ID")
        String decipId = "";
        
        @Parameter(names = {"--regions"}, description = "Comma-separated list of regions")//HECHO
        String regionList = "";
        
        @Parameter(names = {"--assembly"}, description = "Assembly hg18, hg19")
        String assembly = "";
        
        @Parameter(names = {"--band"}, description = "Band")
        String band = "";
        
        @Parameter(names = {"--type"}, description = "Type of variant: gain, loss or LOH neutral")
        String type = "";
        
        @Parameter(names = {"--doses"}, description = "Doses: 0 x0,1 x1,2 x2(XY),3 x3")
        String doses = "";
        
        @Parameter(names = {"--clis"}, description = "Clinical Significance: 0 Benign, 1 Pathogenic, 2 Likely benign 3 Likely pathogenic 4 VOUS")
        String cli = "";

        @Parameter(names = {"--inheritance"}, description = " Inheritance: 0 de novo constitutive, 1 maternally inherited, 2 paternally inherited, 3unknown")
        String inhe = "";
        
        @Parameter(names = {"--cl"}, description = "Cell Line: 0 germline, 1 somatic")
        String cl = "";
        
        @Parameter(names = {"--gender"}, description = "Chromosomal gender: 0 XX, 1 XY")
        String gender = "";
        
        @Parameter(names = {"--status"}, description = "Status: 0 Proband, 1 Father, 2 Mother, 3 Control")
        String status = "";


        @Parameter(names = {"--typeS"}, description = "Type of sample: 0 blood, 1 amniotic fluid, 2 chorionic villi, 3 tumour, 4 etc")
        String typeSample = "";

        @Parameter(names = {"--hpo"}, description = "Phenotype (HPO)")
        String hpo = "";
        
        @Parameter(names = {"--year"}, description = "Year of Birth")
        String year = "";
        
        @Parameter(names = {"--ethic"}, description = "Ethic Group")
        String ethic = "";

        @Parameter(names = {"--geo"}, description = "Geographic origin")
        String origin = "";
        
        @Parameter(names = {"--skip"}, description = "Skip")
        int skip;
       
        @Parameter(names = {"--limit"}, description = "Limit number of results ")
        int limit;
        
        @Parameter(names = {"--host"}, description = "DB host", arity = 1)
        String host = "localhost";

        @Parameter(names = {"--user"}, description = "DB User", arity = 1)
        String user = "";
        
        @Parameter(names = {"--pass"}, description = "DB Pass", arity = 1)
        String pass = "";

    }

    String parse(String[] args) throws ParameterException {
        jcommander.parse(args);
        return jcommander.getParsedCommand();
    }

    String usage() {
        StringBuilder builder = new StringBuilder();
        jcommander.usage(builder);
        return builder.toString();
    }

    CommandLoad getLoadCommand() {
        return load;
    }

    CommandQuery getQueryCommand() {
        return query;
    }    

}