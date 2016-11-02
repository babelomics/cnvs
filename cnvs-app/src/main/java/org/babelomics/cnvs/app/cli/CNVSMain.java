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

        syndromes.add(new Syndrome(1, "1",  "1p36 microdeletion syndrome "));
        syndromes.add(new Syndrome(2, "1",  "1p36 microdeletion  (GABRD) "));
        syndromes.add(new Syndrome(3, "1",  "1p36 microduplication (GABRD) "));
        syndromes.add(new Syndrome(4, "1",  "1p34.1 microduplication "));
        syndromes.add(new Syndrome(5, "1",  "1p32.2 microdeletion "));
        syndromes.add(new Syndrome(6, "1",  "1p21.3 microdeletion "));
        syndromes.add(new Syndrome(7, "1",  "1q21.1 microdeletion "));
        syndromes.add(new Syndrome(8, "1",  "1q21.1 microduplication "));
        syndromes.add(new Syndrome(9, "1",  "Thrombocytopenia-absent radius syndrome/TAR "));
        syndromes.add(new Syndrome(10, "1",  "1q21.1 deletion (GJA5) "));
        syndromes.add(new Syndrome(11, "1",  "1q21.1 duplication (GJA5) "));
        syndromes.add(new Syndrome(12, "1",  "1q24q25 microdeletion "));
        syndromes.add(new Syndrome(13, "1",  "1q24.3 microdeletion "));
        syndromes.add(new Syndrome(14, "1",  "Van der Woude syndrome/VWS1 "));
        syndromes.add(new Syndrome(15, "1",  "1q41–42 microdeletion "));
        syndromes.add(new Syndrome(16, "1",  "Corpus callosum agenesis microdeletion "));
        syndromes.add(new Syndrome(17, "2",  "2p25.3 microduplication "));
        syndromes.add(new Syndrome(18, "2",  "Feingold syndrome/FS "));
        syndromes.add(new Syndrome(19, "2",  "Hypotonia-cystinuria syndrome/HCS "));
        syndromes.add(new Syndrome(20, "2",  "Holoprosencephaly 2/HPE2 (SIX3) "));
        syndromes.add(new Syndrome(21, "2",  "2p21 microduplication "));
        syndromes.add(new Syndrome(22, "2",  "NRXN1 microdeletion "));
        syndromes.add(new Syndrome(23, "2",  "NRXN1 microduplication "));
        syndromes.add(new Syndrome(24, "2",  "2p15–16.1 microdeletion "));
        syndromes.add(new Syndrome(25, "2",  "2p14-p15 microdeletion "));
        syndromes.add(new Syndrome(26, "2",  "2p11.2-p12 microdeletion "));
        syndromes.add(new Syndrome(27, "2",  "2q11.2 microdeletion (LMAN2L, ARID5A) "));
        syndromes.add(new Syndrome(28, "2",  "Mesomelic dysplasia/MMD "));
        syndromes.add(new Syndrome(29, "2",  "2q11.2q13 microdeletion (NCK2, FHL2) "));
        syndromes.add(new Syndrome(30, "2",  "2q11.2q13 microduplication (NCK2, FHL2) "));
        syndromes.add(new Syndrome(31, "2",  "Joubert syndrome "));
        syndromes.add(new Syndrome(32, "2",  "Nephronophthisis 1/NPHP1 "));
        syndromes.add(new Syndrome(33, "2",  "2q11.2q13 microduplication "));
        syndromes.add(new Syndrome(34, "2",  "2q13 microdeletion "));
        syndromes.add(new Syndrome(35, "2",  "2q13 microduplication "));
        syndromes.add(new Syndrome(36, "2",  "Autism-dyslexia microdeletion 2q14.3 "));
        syndromes.add(new Syndrome(37, "2",  "2q14.3 microduplication "));
        syndromes.add(new Syndrome(38, "2",  "Mowat–Wilson syndrome/MWS "));
        syndromes.add(new Syndrome(39, "2",  "2q23.1 microdeletion "));
        syndromes.add(new Syndrome(40, "2",  "2q23.3q24.1 microdeletion "));
        syndromes.add(new Syndrome(41, "2",  "2q24.3 microdeletion "));
        syndromes.add(new Syndrome(42, "2",  "Neonatal epilepsy microduplication "));
        syndromes.add(new Syndrome(43, "2",  "Split hand/foot malformation 5/SHFM5 "));
        syndromes.add(new Syndrome(44, "2",  "Synpolydactyly 1/SPD1 "));
        syndromes.add(new Syndrome(45, "2",  "2q31.1 microduplication "));
        syndromes.add(new Syndrome(46, "2",  "2q31.2-q32.3 microdeletion "));
        syndromes.add(new Syndrome(47, "2",  "2q33.1 microdeletion "));
        syndromes.add(new Syndrome(48, "2",  "Brachydactyly-mental retardation syndrome/BDMR "));
        syndromes.add(new Syndrome(49, "3",  "Distal 3p deletion "));
        syndromes.add(new Syndrome(50, "3",  "Von Hippel Lindau disease/VHL "));
        syndromes.add(new Syndrome(51, "3",  "3p21.31 microdeletion "));
        syndromes.add(new Syndrome(52, "3",  "3p14.1p13 microdeletion "));
        syndromes.add(new Syndrome(53, "3",  "3p11.1p12.1 microdeletion "));
        syndromes.add(new Syndrome(54, "3",  "Proximal 3q microdeletion syndrome  "));
        syndromes.add(new Syndrome(55, "3",  "3q13.31 microdeletion "));
        syndromes.add(new Syndrome(56, "3",  "Blepharophimosis, ptosis, and epicanthus inversus syndrome/BPES "));
        syndromes.add(new Syndrome(57, "3",  "Dandy–Walker syndrome/DWS "));
        syndromes.add(new Syndrome(58, "3",  "3q27.3q29 microdeletion "));
        syndromes.add(new Syndrome(59, "3",  "Split-Hand/Foot Malformation 4/SHFM4 "));
        syndromes.add(new Syndrome(60, "3",  "3q29 microdeletion "));
        syndromes.add(new Syndrome(61, "3",  "3q29 microduplication "));
        syndromes.add(new Syndrome(62, "4",  "Wolf–Hirschhorn syndrome/WHS "));
        syndromes.add(new Syndrome(63, "4",  "4p16.3 microduplication "));
        syndromes.add(new Syndrome(64, "4",  "4p16.1 microduplication "));
        syndromes.add(new Syndrome(65, "4",  "4p15.3 microdeletion "));
        syndromes.add(new Syndrome(66, "4",   " 4q21.21q21.22 microdeletion "));
        syndromes.add(new Syndrome(67, "4",   "4q21 microdeletion "));
        syndromes.add(new Syndrome(68, "4",   "4q21.2q21.3 microdeletion "));
        syndromes.add(new Syndrome(69, "4",   "Parkinson disease/PARK1 "));
        syndromes.add(new Syndrome(70, "4",   "Rieger type 1/RIEG1 "));
        syndromes.add(new Syndrome(71, "4",   "4q32.1-q32.2 Triple/Duplication syndrome "));
        syndromes.add(new Syndrome(72, "5",   "Cri–du-Chat syndrome/CdCS "));
        syndromes.add(new Syndrome(73, "5",   "Cornelia de Lange syndrome/CDLS "));
        syndromes.add(new Syndrome(74, "5",   "NIPBL microduplication "));
        syndromes.add(new Syndrome(75, "5",   "Spinal muscular atrophy/SMA "));
        syndromes.add(new Syndrome(76, "5",   "5q14.3 microdeletion "));
        syndromes.add(new Syndrome(77, "5",   "5q14.3-q15 microdeletion "));
        syndromes.add(new Syndrome(78, "5",   "Familial adenomatous polyposis/FAP "));
        syndromes.add(new Syndrome(79, "5",   "Adult-onset autosomal dominant leukodystrophy/ADLD "));
        syndromes.add(new Syndrome(80, "5",   "PITX1 microdeletion "));
        syndromes.add(new Syndrome(81, "5",   "5q31.3 microdeletion "));
        syndromes.add(new Syndrome(82, "5",   "Pseudo trisomy 13 syndrome "));
        syndromes.add(new Syndrome(83, "5",   "Microdeletion 5q35.1 "));
        syndromes.add(new Syndrome(84, "5",   "Parietal foramina/PFM "));
        syndromes.add(new Syndrome(85, "5",   "Sotos syndrome "));
        syndromes.add(new Syndrome(86, "5",   "5q35 microduplication "));
        syndromes.add(new Syndrome(87, "6",   "6p microdeletion "));
        syndromes.add(new Syndrome(88, "6",   "6p22.3  microdeletion "));
        syndromes.add(new Syndrome(89, "6",   "Adrenal hyperplasia/AH "));
        syndromes.add(new Syndrome(90, "6",   "6p21.31  microdeletion "));
        syndromes.add(new Syndrome(91, "6",   "Cleidocranial dysplasia "));
        syndromes.add(new Syndrome(92, "6",   "6q13–14  microdeletion "));
        syndromes.add(new Syndrome(93, "6",   "Prader–Willi like "));
        syndromes.add(new Syndrome(94, "6",   "Transient neonatal diabetes mellitus 1/TNDM1 "));
        syndromes.add(new Syndrome(95, "6",   "6q25.2-q25.3  microdeletion "));
        syndromes.add(new Syndrome(96, "6",   "PARK2 microdeletion "));
        syndromes.add(new Syndrome(97, "6",   "PARK2 microduplication "));
        syndromes.add(new Syndrome(98, "6",   "6q27  microdeletion (anosmia) "));
        syndromes.add(new Syndrome(99, "6",   "Chondroma/CHDM "));
        syndromes.add(new Syndrome(100,"7",   "Saethre–Chotzen syndrome/SCS "));
        syndromes.add(new Syndrome(101,"7",   "Greig cephalopolysyndactyly/GCPS "));
        syndromes.add(new Syndrome(102,"7",   "Williams–Beuren syndrome/WBS "));
        syndromes.add(new Syndrome(103,"7",   "7q11.23 microduplication (WBS region duplication) "));
        syndromes.add(new Syndrome(104,"7",   "WBS-distal deletion (RHBDD2, HIP1) "));
        syndromes.add(new Syndrome(105,"7",   "Split hand/foot malformation 1/SHFM1 "));
        syndromes.add(new Syndrome(106,"7",   "7q22.1-q22.3  microdeletion "));
        syndromes.add(new Syndrome(107,"7",   "Autism/dyslexia microdeletion 7q31.1 "));
        syndromes.add(new Syndrome(108,"7",   "Speech-language-disorder 1/SPCH1 "));
        syndromes.add(new Syndrome(109,"7",   "Holoprosencephaly 3/HPE3 (SHH) "));
        syndromes.add(new Syndrome(110,"7",   "Triphalangeal thumb polysyndactyly syndrome/TPTS "));
        syndromes.add(new Syndrome(111,"7",   "Currarino syndrome/CS "));
        syndromes.add(new Syndrome(112,"8",   "8p23.1  microdeletion "));
        syndromes.add(new Syndrome(113,"8",   "8p23.1 microduplication "));
        syndromes.add(new Syndrome(114,"8",   "Kabuki syndrome "));
        syndromes.add(new Syndrome(115,"8",   "8p21.2  microdeletion "));
        syndromes.add(new Syndrome(116,"8",   "8p12p21  microdeletion "));
        syndromes.add(new Syndrome(117,"8",   "8q11.23 microduplication "));
        syndromes.add(new Syndrome(118,"8",   "CHARGE syndrome "));
        syndromes.add(new Syndrome(119,"8",   "8q12 microduplication "));
        syndromes.add(new Syndrome(120,"8",   "8q12.3q13.2  microdeletion "));
        syndromes.add(new Syndrome(121,"8",   "Mesomelia-synostoses syndrome/MSS "));
        syndromes.add(new Syndrome(122,"8",   "Branchiootorenal syndrome (EYA1) "));
        syndromes.add(new Syndrome(123,"8",   " 8q21.11  microdeletion "));
        syndromes.add(new Syndrome(124,"8",   "Nablus mask-like facial syndrome/NMLFS "));
        syndromes.add(new Syndrome(125,"8",   "8q22.2q22.3  microdeletion "));
        syndromes.add(new Syndrome(126,"8",   "Langer–Giedion syndrome/LGS; Trichorrhinophalangeal type I "));
        syndromes.add(new Syndrome(127,"9",   "Sex reversal syndrome 4/SRXY4 "));
        syndromes.add(new Syndrome(128,"9",   "Monosomy 9p syndrome "));
        syndromes.add(new Syndrome(129,"9",   "9q21.11 microduplication "));
        syndromes.add(new Syndrome(130,"9",   "9q22.3  microdeletion "));
        syndromes.add(new Syndrome(131,"9",   "PTCH1 microduplication "));
        syndromes.add(new Syndrome(132,"9",   "Holoprosencephaly 7/HPE7 "));
        syndromes.add(new Syndrome(133,"9",   "Basal cell nevus syndrome (PTCH1) "));
        syndromes.add(new Syndrome(134,"9",   "Nail-patella syndrome/NPS "));
        syndromes.add(new Syndrome(135,"9",   "Early infantile epileptic encephalopathy 4/EIEE4 "));
        syndromes.add(new Syndrome(136,"9",   "9q34 (EHMT1)  microdeletion "));
        syndromes.add(new Syndrome(137,"9",   "9q34 (EHMT1) microduplication "));
        syndromes.add(new Syndrome(138,"9",   "Subtelomere deletion 9q "));
        syndromes.add(new Syndrome(139,"10",   "Hypoparathyroidism, sensorineural deafness, and renal disease/HDRS "));
        syndromes.add(new Syndrome(140,"10",   "Di George syndrome 2/DGS2 "));
        syndromes.add(new Syndrome(141,"10",   "10q22-q23 (NRG3, GRID1)  microdeletion "));
        syndromes.add(new Syndrome(142,"10",   "Juvenile polyposis syndrome/JPS "));
        syndromes.add(new Syndrome(143,"10",   "Split-Hand/Foot Malformation 3/SHFM3 "));
        syndromes.add(new Syndrome(144,"10",   "10q25q26  microdeletion "));
        syndromes.add(new Syndrome(145,"11",   "Beckwith–Wiedemann syndrome/BWS—Silver Russell syndrome/SRS microdeletion "));
        syndromes.add(new Syndrome(146,"11",   "Beckwith–Wiedemann syndrome/BWS—Silver Russell syndrome/SRS microduplication "));
        syndromes.add(new Syndrome(147,"11",   "Wilms tumor syndrome (isolated) "));
        syndromes.add(new Syndrome(148,"11",   "WAGR syndrome "));
        syndromes.add(new Syndrome(149,"11",   "11p13 microduplication "));
        syndromes.add(new Syndrome(150,"11",   "Potocki–Shaffer syndrome/PSS "));
        syndromes.add(new Syndrome(151,"11",   "Spinocerebellar ataxia type 20/SCA20 "));
        syndromes.add(new Syndrome(152,"11",   "11q14.1  microdeletion "));
        syndromes.add(new Syndrome(153,"11",   "Jacobsen syndrome/JBS "));
        syndromes.add(new Syndrome(154,"12",   "12p13.31 microduplication "));
        syndromes.add(new Syndrome(155,"12",   "12q14  microdeletion "));
        syndromes.add(new Syndrome(156,"12",   "Buschke-Ollendorff syndrome "));
        syndromes.add(new Syndrome(157,"12",   "Nasal speech-hypothyroidism microdeletion/NSH "));
        syndromes.add(new Syndrome(158,"12",   "Noonan syndrome 1/NS1 "));
        syndromes.add(new Syndrome(159,"13",   "13q12 (CRYL1)  microdeletion "));
        syndromes.add(new Syndrome(160,"13",   "13q12 (CRYL1) microduplication "));
        syndromes.add(new Syndrome(161,"13",   "Spastic ataxia Charlevoix–Saguenay/SACS "));
        syndromes.add(new Syndrome(162,"13",   "13q12.3-q13.1  microdeletion "));
        syndromes.add(new Syndrome(163,"13",   "Retinoblastoma/RB1 "));
        syndromes.add(new Syndrome(164,"13",   "Hirschsprung disease 2/HSCR2 "));
        syndromes.add(new Syndrome(165,"13",   "Holoprosencephaly5/HPE5 (ZIC2) "));
        syndromes.add(new Syndrome(166,"14",   "14q11.2  microdeletion "));
        syndromes.add(new Syndrome(167,"14",   "Congenital Rett variant/CRV "));
        syndromes.add(new Syndrome(168,"14",   "14q12 microduplication "));
        syndromes.add(new Syndrome(169,"14",   "14q22-q23  microdeletion "));
        syndromes.add(new Syndrome(170,"14",   "Autism spherocytosis microdeletion/ASC "));
        syndromes.add(new Syndrome(171,"14",   "14q32.2  microdeletion "));
        syndromes.add(new Syndrome(172,"15",   "15q11.2 (NIPA1)  microdeletion "));
        syndromes.add(new Syndrome(173,"15",   "15q11.2 (NIPA1) microduplication "));
        syndromes.add(new Syndrome(174,"15",   "Angelman syndrome Typ1/AS1 "));
        syndromes.add(new Syndrome(175,"15",   "Chromosome 15 microduplciation "));
        syndromes.add(new Syndrome(176,"15",   "Angelman syndrome Typ2/AS2 "));
        syndromes.add(new Syndrome(177,"15",   "Prader–Willi syndrome Typ 1/ PWS1 "));
        syndromes.add(new Syndrome(178,"15",   "Prader–Willi syndrome Typ 2/ PWS2 "));
        syndromes.add(new Syndrome(179,"15",   "15q13.3 (CHRNA7)  microdeletion "));
        syndromes.add(new Syndrome(180,"15",   "15q13.3 (CHRNA7) microduplication "));
        syndromes.add(new Syndrome(181,"15",   "15q14  microdeletion "));
        syndromes.add(new Syndrome(182,"15",   "Deafness and male infertility syndrome/DMIS "));
        syndromes.add(new Syndrome(183,"15",   "15q21  microdeletion "));
        syndromes.add(new Syndrome(184,"15",   "15q24 (BBS4,NPTN, NE01)  microdeletion "));
        syndromes.add(new Syndrome(185,"15",   "15q24  microdeletion "));
        syndromes.add(new Syndrome(186,"15",   "15q24 microduplciation "));
        syndromes.add(new Syndrome(187,"15",   "Orofacial clefting/OC "));
        syndromes.add(new Syndrome(188,"15",   "15q25 microdeletion "));
        syndromes.add(new Syndrome(189,"15",   "15q26.1  microdeletion "));
        syndromes.add(new Syndrome(190,"15",   "Fryns syndrome/FNS "));
        syndromes.add(new Syndrome(191,"15",   "15q26.2-qter  microdeletion "));
        syndromes.add(new Syndrome(192,"16",   "ATR-16-syndrome "));
        syndromes.add(new Syndrome(193,"16",   "Tuberous sclerosis microdeletion syndrome/PKDTS "));
        syndromes.add(new Syndrome(194,"16",   "Tuberous sclerosis microduplication "));
        syndromes.add(new Syndrome(195,"16",   "Rubinstein–Taybi syndrome 1/RSTS1 "));
        syndromes.add(new Syndrome(196,"16",   "Rubinstein–Taybi-microduplication "));
        syndromes.add(new Syndrome(197,"16",   "16p13.1 (MYH11)  microdeletion "));
        syndromes.add(new Syndrome(198,"16",   "16p13.1 (MYH11) microduplication "));
        syndromes.add(new Syndrome(199,"16",   "16p11.2-p12.2  microdeletion "));
        syndromes.add(new Syndrome(200,"16",   "16p11.2-p12.2 microduplication "));
        syndromes.add(new Syndrome(201,"16",   "16p12.1 (EEF2K,CDR2)  microdeletion "));
        syndromes.add(new Syndrome(202,"16",   "16p12.2 (EEF2K,CDR2) microduplication "));
        syndromes.add(new Syndrome(203,"16",   "16q11.2 distal microdeletion (SH2B1) "));
        syndromes.add(new Syndrome(204,"16",   "16q11.2 distal microduplication (SH2B1) "));
        syndromes.add(new Syndrome(205,"16",   "16p11.2 (TBX6)  microdeletion "));
        syndromes.add(new Syndrome(206,"16",   "16p11.2 (TBX6) microduplication "));
        syndromes.add(new Syndrome(207,"16",   "16q11.2-q12.1  microdeletion "));
        syndromes.add(new Syndrome(208,"16",   "16q21-q22  microdeletion "));
        syndromes.add(new Syndrome(209,"16",   "16q12.1-q12.2  microdeletion "));
        syndromes.add(new Syndrome(210,"16",   "Townes-Brocks syndrome "));
        syndromes.add(new Syndrome(211,"16",   "16q24.1  microdeletion "));
        syndromes.add(new Syndrome(212,"16",   "FANCA deletion "));
        syndromes.add(new Syndrome(213,"17",   "Miller–Dieker syndrome/MDLS "));
        syndromes.add(new Syndrome(214,"17",   "Miller–Dieker microduplication "));
        syndromes.add(new Syndrome(215,"17",   "17p13.3 (YWHAE)  microdeletion "));
        syndromes.add(new Syndrome(216,"17",   "17p13.3 (YWHAE) microduplication "));
        syndromes.add(new Syndrome(217,"17",   "17p13.1  microdeletion "));
        syndromes.add(new Syndrome(218,"17",   "Hereditary liability to pressure palsies/HNPP "));
        syndromes.add(new Syndrome(219,"17",   "Charcot–Marie–Tooth 1A/CMT1A "));
        syndromes.add(new Syndrome(220,"17",   "Smith–Magenis syndrome/SMS "));
        syndromes.add(new Syndrome(221,"17",   "Potocki–Lupski syndrome/PTLS "));
        syndromes.add(new Syndrome(222,"17",   "Neurofibromatosis 1/NF1 "));
        syndromes.add(new Syndrome(223,"17",   "NF1 microduplication "));
        syndromes.add(new Syndrome(224,"17",   "17q11.2-q12  microdeletion "));
        syndromes.add(new Syndrome(225,"17",   "17q12a  microdeletion "));
        syndromes.add(new Syndrome(226,"17",   "Renal cysts and diabetes syndrome/RCAD "));
        syndromes.add(new Syndrome(227,"17",   "17q12b microduplication "));
        syndromes.add(new Syndrome(228,"17",   "Van Buchem disease/VBCH "));
        syndromes.add(new Syndrome(229,"17",   "17q21.31 (MAPT)  microdeletion "));
        syndromes.add(new Syndrome(230,"17",   "17q21.31 (MAPT) microduplication "));
        syndromes.add(new Syndrome(231,"17",   "17q21.31-q21.32  microdeletion "));
        syndromes.add(new Syndrome(232,"17",   "17q22-q23.2  microdeletion "));
        syndromes.add(new Syndrome(233,"17",   "17q23.1–23.2 microduplication "));
        syndromes.add(new Syndrome(234,"17",   "Camptomelic dysplasia "));
        syndromes.add(new Syndrome(235,"17",   "17q24.2-q24.3  microdeletion "));
        syndromes.add(new Syndrome(236,"17",   "Carney complex syndrome 1/CNC1 "));
        syndromes.add(new Syndrome(237,"17",   "17q24.3 microduplication "));
        syndromes.add(new Syndrome(238,"18",   "Holoprosencephaly 4/HPE4 (TGIF1) "));
        syndromes.add(new Syndrome(239,"18",   "Proximal 18q microdeletion "));
        syndromes.add(new Syndrome(240,"18",   "Pitt–Hopkins syndrome/PTHS "));
        syndromes.add(new Syndrome(241,"18",   "18q22.3-q23  microdeletion "));
        syndromes.add(new Syndrome(242,"19",   "Sotos-like microduplication 19p13.2 "));
        syndromes.add(new Syndrome(243,"19",   "19p13.13  microdeletion "));
        syndromes.add(new Syndrome(244,"19",   " 19p13.13 microduplication "));
        syndromes.add(new Syndrome(245,"19",   "19p13.12  microdeletion "));
        syndromes.add(new Syndrome(246,"19",   "19p13.11  microdeletion "));
        syndromes.add(new Syndrome(247,"19",   "19q13.11  microdeletion "));
        syndromes.add(new Syndrome(248,"19",   "Diamond–Blackfan anemia/DBA "));
        syndromes.add(new Syndrome(249,"20",   "20p12.3  microdeletion "));
        syndromes.add(new Syndrome(250,"20",   "Alagille syndrome 1/ALGS1 "));
        syndromes.add(new Syndrome(251,"20",   "20q13.13-q13.2  microdeletion "));
        syndromes.add(new Syndrome(252,"20",   "Albright hereditary osteodystrophy/AHO "));
        syndromes.add(new Syndrome(253,"20",   "20q13.33  microdeletion "));
        syndromes.add(new Syndrome(254,"21",   "21q21.1  microdeletion "));
        syndromes.add(new Syndrome(255,"21",   "21q21.3  microdeletion "));
        syndromes.add(new Syndrome(256,"21",   "Platelet disorder/PD "));
        syndromes.add(new Syndrome(257,"21",   "Down syndrome/DS "));
        syndromes.add(new Syndrome(258,"21",   "Holoprosencephaly (TMEM1) "));
        syndromes.add(new Syndrome(259,"22",   "Cat-Eye syndrome/CES "));
        syndromes.add(new Syndrome(260,"22",   "Di George syndrome/CATCH22/DGS "));
        syndromes.add(new Syndrome(261,"22",   "22q11.2 microduplication "));
        syndromes.add(new Syndrome(262,"22",   "Distal microdeletion 22q11.2 (BCR, MAPK1) "));
        syndromes.add(new Syndrome(263,"22",   "Distal microduplication 22q11.2 (BCR, MAPK1) "));
        syndromes.add(new Syndrome(264,"22",   "Neurofibromatosis 2 microdeletion syndrome "));
        syndromes.add(new Syndrome(265,"22",   "Phelan–McDermid syndrome "));
        syndromes.add(new Syndrome(266,"X",   "22q13 (SHANK3) microduplication "));
        syndromes.add(new Syndrome(267,"X",   "Metachromatic Leukodystrophy "));
        syndromes.add(new Syndrome(268,"X",   "Leri–Weill dyschondrosteosis/LWD "));
        syndromes.add(new Syndrome(269,"X",   "X-Linked autism-2/AUTSX2 "));
        syndromes.add(new Syndrome(270,"X",   "Steroid sulphatase deficiency/STS (Ichthyosis, X-linked) "));
        syndromes.add(new Syndrome(271,"X",   "Kallmann syndrome 1/KAL1 "));
        syndromes.add(new Syndrome(272,"X",   "Microphthalmia syndromic type 7 "));
        syndromes.add(new Syndrome(273,"X",   "MIDAS syndrome "));
        syndromes.add(new Syndrome(274,"X",   "Nance–Horan syndrome/NHS "));
        syndromes.add(new Syndrome(275,"X",   "Xp22.11  microdeletion "));
        syndromes.add(new Syndrome(276,"X",   "X-linked congenital adrenal hypoplasia/AHC "));
        syndromes.add(new Syndrome(277,"X",   "DAX1 microduplication "));
        syndromes.add(new Syndrome(278,"X",   "Complex glycerol kinase/CGK (Hyperglycerolemia) "));
        syndromes.add(new Syndrome(279,"X",   "Duchenne/Becker muscular dystrophy /DMD "));
        syndromes.add(new Syndrome(280,"X",   "Xp11.3 deletion syndrome "));
        syndromes.add(new Syndrome(281,"X",   "Goltz syndrome/GS "));
        syndromes.add(new Syndrome(282,"X",   "17-beta-hydroxysteroid dehydrogenase X/HSD "));
        syndromes.add(new Syndrome(283,"X",   "Xq12q13.1 microduplication "));
        syndromes.add(new Syndrome(284,"X",   "Charcot–Marie–Tooth X-linked/CMTX1 "));
        syndromes.add(new Syndrome(285,"X",   "X inactivation specific transcript/XIST "));
        syndromes.add(new Syndrome(286,"X",   "Dosage-sensitive sex reversal "));
        syndromes.add(new Syndrome(287,"X",   "Bruton agammaglobulinemia/XLA "));
        syndromes.add(new Syndrome(288,"X",   "Xq22.2  microdeletion "));
        syndromes.add(new Syndrome(289,"X",   "Pelizaeus–Merzbacher microduplication/PMD "));
        syndromes.add(new Syndrome(290,"X",   "Lissencephaly, X-linked "));
        syndromes.add(new Syndrome(291,"X",   "Xq22.3q23  microdeletion "));
        syndromes.add(new Syndrome(292,"X",   "Lymphoproliferative syndrome 1/XLP1 "));
        syndromes.add(new Syndrome(293,"X",   "Heterotaxia, visceral, 1, X-linked (HTX1; ZIC3) "));
        syndromes.add(new Syndrome(294,"X",   "X-linked hypopituitarism with intellectual disability/SRXX3 "));
        syndromes.add(new Syndrome(295,"X",   "Fragile site mental retardation 1/FMR1 "));
        syndromes.add(new Syndrome(296,"X",   "Xq28  microdeletion "));
        syndromes.add(new Syndrome(297,"X",   "Rett syndrome/RS "));
        syndromes.add(new Syndrome(298,"X",   "MECP2 microduplication "));
        syndromes.add(new Syndrome(299,"Y",   "Sex-determining region Y/SRY "));
        syndromes.add(new Syndrome(300,"Y",   "AZFa microdeletion "));
        syndromes.add(new Syndrome(301,"Y",   "AZFb microdeletion "));
        syndromes.add(new Syndrome(302,"Y",   "AZFb+c microdeletion "));
        syndromes.add(new Syndrome(303,"Y",   "AZFc microdeletion "));

        for (Syndrome s: syndromes){
            try{
                datastore.save(s);
            }catch (DuplicateKeyException e){
                System.err.println("Duplicated Syndrome:" + s);
            }
        }
    }
}
