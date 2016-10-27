package org.babelomics.cnvs.app.cli;

import com.mongodb.DuplicateKeyException;
import org.apache.commons.lang3.mutable.MutableLong;
import org.babelomics.cnvs.lib.annot.CellBaseAnnotator;
import org.babelomics.cnvs.lib.models.CNV;
import org.babelomics.cnvs.lib.cli.QueryCommandLine;
import org.babelomics.cnvs.lib.io.CNVSCopyNumberVariationMongoDataWriter;
import org.babelomics.cnvs.lib.io.CNVSCopyNumberVariationXLSDataReader;
import org.babelomics.cnvs.lib.io.CNVSQueryManager;
import org.babelomics.cnvs.lib.io.CNVSRunner;
import org.babelomics.cnvs.lib.models.Syndrome;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.opencb.commons.containers.list.SortedList;
import org.opencb.commons.io.DataReader;
import org.opencb.commons.io.DataWriter;
import org.opencb.commons.run.Runner;
import org.opencb.commons.run.Task;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class CNVSMain {

    private static Datastore getDatastore(String host, String user, String pass) {

        final Morphia morphia = new Morphia();
        morphia.mapPackage("org.babelomics.cnvs.lib.models");

        MongoClient mongoClient;
        if (user == "" && pass == "") {
            mongoClient = new MongoClient(host);
        } else {
            MongoCredential credential = MongoCredential.createCredential(user, "cnvs", pass.toCharArray());
            mongoClient = new MongoClient(new ServerAddress(host), Arrays.asList(credential));
        }
        Datastore datastore = morphia.createDatastore(mongoClient, "cnvs");

        datastore.ensureIndexes();

        return datastore;

    }

    public static void main(String[] args) throws IOException {

        OptionsParser parser = new OptionsParser();

        // If no arguments are provided, or -h/--help is the first argument, the usage is shown
        if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
            System.out.println(parser.usage());
            return;
        }

        String cmd = args[0];

        String[] newArgs = new String[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            newArgs[i - 1] = new String(args[i]);
        }

        OptionsParser.Command command = null;

        try {

            if (cmd.equalsIgnoreCase("load")) {
                parser.parse(args);
                OptionsParser.CommandLoad c = parser.getLoadCommand();


                System.out.println("c.user = " + c.user);
                System.out.println("c.host = " + c.host);
                System.out.println("c.pass = " + c.pass);

                Datastore datastore = getDatastore(c.host, c.user, c.pass);

                String fileName = c.input;

                DataReader<CNV> reader = new CNVSCopyNumberVariationXLSDataReader(fileName);
                DataWriter<CNV> writer = new CNVSCopyNumberVariationMongoDataWriter(datastore);

                List<Task<CNV>> taskList = new SortedList<>();
                List<DataWriter<CNV>> writers = new ArrayList<>();
                writers.add(writer);

                Runner<CNV> runner = new CNVSRunner(reader, writers, taskList, 1);

                try {
                    runner.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (cmd.equalsIgnoreCase("query")) {

                QueryCommandLine cmdLine = new QueryCommandLine();
                JCommander cmd1 = new JCommander(cmdLine);

                try {
                    cmd1.parse(newArgs);
                } catch (Exception e) {
                    cmd1.usage();

                    System.exit(-1);
                }

                Datastore datastore = getDatastore(cmdLine.getHost(), cmdLine.getUser(), cmdLine.getPass());

                CNVSQueryManager qm = new CNVSQueryManager(datastore);
                MutableLong count = new MutableLong(-1);

                Iterable<CNV> res = qm.getCNVsByFilters(cmdLine, count);

                for (CNV cnv : res) {
                    System.out.println(cnv);
                }

            } else if (cmd.equalsIgnoreCase("annot")) {
                parser.parse(args);
                OptionsParser.CommandAnnot c = parser.getAnnotCommand();
                Datastore datastore = getDatastore(c.host, c.user, c.pass);

                CellBaseAnnotator cba = new CellBaseAnnotator();

                Iterator<CNV> it = datastore.createQuery(CNV.class).batchSize(1000).iterator();

                while (it.hasNext()) {
                    List<CNV> batch = new ArrayList<>();

                    for (int i = 0; i < 10 && it.hasNext(); i++) {
                        batch.add(it.next());
                    }

                    cba.annot(batch);
                    datastore.save(batch);
                    batch.clear();
                }

            } else if (cmd.equalsIgnoreCase("loadsyndrome")){
                parser.parse(args);
                OptionsParser.CommandLoadSyndromes c = parser.getLoadsyndrome();
                Datastore datastore = getDatastore(c.host, c.user, c.pass);
                syndromes(datastore);
            }else{
                System.out.println("Command not implemented!!");
                System.err.println(parser.usage());
                System.exit(1);
            }
        } catch (ParameterException ex) {
            System.err.println(ex.getMessage());
            System.err.println(parser.usage());
            System.exit(1);
        }

    }

    public static void syndromes(Datastore datastore){

        List<Syndrome> syndromes = new ArrayList<>();

        syndromes.add(new Syndrome(1, "1p36 microdeletion syndrome"));
        syndromes.add(new Syndrome(2, "1q21.1 susceptibility locus for Thrombocytopenia-Absent Radius (TAR) syndrome"));
        syndromes.add(new Syndrome(3, "1q21.1 recurrent microdeletion (susceptibility locus for neurodevelopmental disorders)"));
        syndromes.add(new Syndrome(4, "1q21.1 recurrent microduplication (possible susceptibility locus for neurodevelopmental disorders)"));
        syndromes.add(new Syndrome(5, "2p21 Microdeletion Syndrome"));
        syndromes.add(new Syndrome(6, "2p15-16.1 microdeletion syndrome"));
        syndromes.add(new Syndrome(7, "2q33.1 deletion syndrome"));
        syndromes.add(new Syndrome(8, "2q37 monosomy"));
        syndromes.add(new Syndrome(9, "3q29 microdeletion syndrome"));
        syndromes.add(new Syndrome(10, "3q29 microduplication syndrome"));
        syndromes.add(new Syndrome(11, "Wolf-Hirschhorn Syndrome"));
        syndromes.add(new Syndrome(12, "Cri du Chat Syndrome (5p deletion)"));
        syndromes.add(new Syndrome(13, "Familial Adenomatous Polyposis"));
        syndromes.add(new Syndrome(14, "Adult-onset autosomal dominant leukodystrophy (ADLD)"));
        syndromes.add(new Syndrome(15, "Sotos syndrome"));
        syndromes.add(new Syndrome(16, "Williams-Beuren Syndrome (WBS)"));
        syndromes.add(new Syndrome(17, "7q11.23 duplication syndrome"));
        syndromes.add(new Syndrome(18, "Split hand/foot malformation 1 (SHFM1) "));
        syndromes.add(new Syndrome(19, "8p23.1 deletion syndrome"));
        syndromes.add(new Syndrome(20, "8p23.1 duplication syndrome"));
        syndromes.add(new Syndrome(21, "8q21.11 Microdeletion Syndrome"));
        syndromes.add(new Syndrome(22, "9q subtelomeric deletion syndrome"));
        syndromes.add(new Syndrome(23, "WAGR 11p13 deletion syndrome"));
        syndromes.add(new Syndrome(24, "Potocki-Shaffer syndrome"));
        syndromes.add(new Syndrome(25, "12p13.33 Microdeletion Syndrome"));
        syndromes.add(new Syndrome(26, "12q14 microdeletion syndrome"));
        syndromes.add(new Syndrome(27, "Prader-Willi syndrome (Type 1)"));
        syndromes.add(new Syndrome(28, "Angelman syndrome (Type 1)"));
        syndromes.add(new Syndrome(29, "Prader-Willi Syndrome (Type 2)"));
        syndromes.add(new Syndrome(30, "Angelman syndrome (Type 2)"));
        syndromes.add(new Syndrome(31, "15q13.3 microdeletion syndrome"));
        syndromes.add(new Syndrome(32, "15q24 recurrent microdeletion syndrome"));
        syndromes.add(new Syndrome(33, "15q26 overgrowth syndrome"));
        syndromes.add(new Syndrome(34, "ATR-16 syndrome"));
        syndromes.add(new Syndrome(35, "Rubinstein-Taybi Syndrome"));
        syndromes.add(new Syndrome(36, "16p13.11 recurrent microdeletion (neurocognitive disorder susceptibility locus)"));
        syndromes.add(new Syndrome(37, "16p13.11 recurrent microduplication (neurocognitive disorder susceptibility locus)"));
        syndromes.add(new Syndrome(38, "16p11.2-p12.2 microduplication syndrome"));
        syndromes.add(new Syndrome(39, "16p11.2-p12.2 microdeletion syndrome"));
        syndromes.add(new Syndrome(40, "Recurrent 16p12.1 microdeletion (neurodevelopmental susceptibility locus)"));
        syndromes.add(new Syndrome(41, "16p11.2 microduplication syndrome"));
        syndromes.add(new Syndrome(42, "Miller-Dieker syndrome (MDS)"));
        syndromes.add(new Syndrome(43, "Charcot-Marie-Tooth syndrome type 1A (CMT1A)"));
        syndromes.add(new Syndrome(44, "Hereditary Liability to Pressure Palsies (HNPP)"));
        syndromes.add(new Syndrome(45, "Smith-Magenis Syndrome"));
        syndromes.add(new Syndrome(46, "Potocki-Lupski syndrome (17p11.2 duplication syndrome)"));
        syndromes.add(new Syndrome(47, "NF1-microdeletion syndrome"));
        syndromes.add(new Syndrome(48, "RCAD (renal cysts and diabetes)"));
        syndromes.add(new Syndrome(49, "17q21.31 recurrent microdeletion syndrome (Koolen de Vries syndrome)"));
        syndromes.add(new Syndrome(50, "Early-onset Alzheimer disease with cerebral amyloid angiopathy"));
        syndromes.add(new Syndrome(51, "Cat-Eye Syndrome (Type I)"));
        syndromes.add(new Syndrome(52, "22q11 deletion syndrome (Velocardiofacial / DiGeorge syndrome)"));
        syndromes.add(new Syndrome(53, "22q11 duplication syndrome"));
        syndromes.add(new Syndrome(54, "22q11.2 distal deletion syndrome"));
        syndromes.add(new Syndrome(55, "22q13 deletion syndrome (Phelan-Mcdermid syndrome)"));
        syndromes.add(new Syndrome(56, "Leri-Weill dyschondrostosis (LWD) - SHOX deletion"));
        syndromes.add(new Syndrome(57, "Leri-Weill dyschondrostosis (LWD) - SHOX deletion"));
        syndromes.add(new Syndrome(58, "Steroid sulphatase deficiency (STS)"));
        syndromes.add(new Syndrome(59, "Xp11.22-p11.23 Microduplication"));
        syndromes.add(new Syndrome(60, "Xp11.22-linked intellectual disability"));
        syndromes.add(new Syndrome(61, "Pelizaeus-Merzbacher disease"));
        syndromes.add(new Syndrome(62, "Xq28 (MECP2) duplication"));
        syndromes.add(new Syndrome(63, "Xq28 Microduplication"));
        syndromes.add(new Syndrome(64, "AZFa"));
        syndromes.add(new Syndrome(65, "AZFb+AZFc"));

        for (Syndrome s: syndromes){
            try{
                datastore.save(s);
            }catch (DuplicateKeyException e){
                System.err.println("Duplicated Syndrome:" + s);
            }
        }
    }
}
