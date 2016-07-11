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
    private final CommandAnnot annot;



    public OptionsParser() {
        jcommander = new JCommander();
        jcommander.addCommand(load = new CommandLoad());
        jcommander.addCommand(annot = new CommandAnnot());

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

    @Parameters(commandNames = {"annot"}, commandDescription = "Annot Variants")
    class CommandAnnot implements Command {

        @Parameter(names = {"--ct"}, description = "Annot consequence Type", arity = 0)
        boolean ct;

        @Parameter(names = {"--gene"}, description = "Annot Gene", arity = 0)
        boolean gene;

        @Parameter(names = {"--remove"}, description = "Remove selected annotations", arity = 0)
        boolean remove;

        @Parameter(names = {"--override"}, description = "Override selected annotations", arity = 0)
        boolean override;

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
    CommandAnnot getAnnotCommand() {return annot; }
   

}