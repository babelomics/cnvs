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
        String code = null;
        
        @Parameter(names = {"--decipherId"}, description = "Decipher ID")
        String decipId = null;
        
        @Parameter(names = {"--regions"}, description = "Comma-separated list of regions")//HECHO
        String regionList;
        
        @Parameter(names = {"--assembly"}, description = "Assembly hg18, hg19")
        String assembly = null;
        
        @Parameter(names = {"--band"}, description = "Band")
        String band = null;
        
        @Parameter(names = {"--type"}, description = "Type of variant: gain, loss or LOH neutral")
        String type = null;
        
        @Parameter(names = {"--doses"}, description = "Doses: x0,x1,x2(XY),x3")
        String doses = null;

        @Parameter(names = {"--inheritance"}, description = "Inheritance: de novo constitutive, maternally inherited, paternally inherited, unknown")
        String inhe = null;
        
        @Parameter(names = {"--cl"}, description = "Cell Line")
        String cl = null;
        
        @Parameter(names = {"--gender"}, description = "Chromosomal gender")
        String gender = null;
        
        @Parameter(names = {"--status"}, description = "Status")
        String status = null;


        @Parameter(names = {"--typeS"}, description = "Type of sample")
        String typeSample = null;

        @Parameter(names = {"--hpo"}, description = "Phenotype (HPO)")
        String hpo = null;
        
        @Parameter(names = {"--ethic"}, description = "Ethic Group")
        String ethic = null;

        @Parameter(names = {"--geo"}, description = "Geographic origin")
        String origin = null;
        
        @Parameter(names = {"--skip"}, description = "Skip")
        int skip = 0;
        
        @Parameter(names = {"--limit"}, description = "Limit number of results ")
        int limit = 10;
        
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