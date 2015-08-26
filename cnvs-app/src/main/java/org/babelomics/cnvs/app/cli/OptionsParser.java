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
    private final CommandSetup setup;


    public OptionsParser() {
        jcommander = new JCommander();
        jcommander.addCommand(load = new CommandLoad());
        jcommander.addCommand(query = new CommandQuery());
        jcommander.addCommand(setup = new CommandSetup());
        

    }

    interface Command {
    }

    @Parameters(commandNames = {"load"}, commandDescription = "Upload file in DB")
    class CommandLoad implements Command {

        @Parameter(names = {"-i", "--input"}, description = "File path to load", required = true, arity = 1)
        String input;

    }


    @Parameters(commandNames = {"query"}, commandDescription = "Query")
    class CommandQuery implements Command {

        @Parameter(names = {"--all"}, description = "List all variants", arity = 0)
        boolean variant;

        @Parameter(names = {"--regions"}, description = "Comma-separated list of regions")
        List<String> regionLIst = new ArrayList<>();

        @Parameter(names = {"--gender"}, description = "Chrom gender")
        String gender;

        @Parameter(names = {"--type"}, description = "Type of variant")
        String type = null;

        @Parameter(names = {"--start"}, description = "Lower limit of the region")
        Integer lowlimit = null;
        
        @Parameter(names = {"--limit"}, description = "Upper limit of the region")
        Integer uplimit = null;

       


    }

    @Parameters(commandNames = {"setup"}, commandDescription = "Setup Database")
    class CommandSetup implements Command {

        @Parameter(names = {"--populate-diseases"}, description = "Populate diseases", arity = 0)
        boolean populateDiseases;

        @Parameter(names = {"--new-disease"}, description = "New Diseases", arity = 1)
        String newDisease;

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


    CommandSetup getSetupCommand() {
        return setup;
    }

    CommandQuery getQueryCommand() {
        return query;
    }

    

}