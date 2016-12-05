package org.babelomics.cnvs.lib.io;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.babelomics.cnvs.lib.cli.QueryCommandLine;
import org.babelomics.cnvs.lib.models.CNV;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;
import org.opencb.biodata.models.feature.Region;
import org.apache.commons.lang3.mutable.MutableLong;

import com.google.common.base.Splitter;

import com.mongodb.MongoClient;

public class CNVSQueryManager {
    final Datastore datastore;
    static final int DECIMAL_POSITIONS = 3;

    public CNVSQueryManager(String host, String dbName) {
        Morphia morphia = new Morphia();
        morphia.mapPackage("org.babelomics.cnvs.lib.models");
        this.datastore = morphia.createDatastore(new MongoClient(host), dbName);
        this.datastore.ensureIndexes();
    }

    public CNVSQueryManager(String dbName) {
        this("localhost", dbName);
    }

    public CNVSQueryManager(Datastore datastore) {
        this.datastore = datastore;
    }

    public Iterable<CNV> getCNVsByFilters(QueryCommandLine q, MutableLong count) {

        Query<CNV> query = this.datastore.createQuery(CNV.class);


        if (q.getCode() != null && !q.getCode().isEmpty()) {
            query.filter("ref =", q.getCode());
        }

        if (q.getDecipId() != null && (!q.getDecipId().isEmpty())) {
            this.addTypeLongToQuery(q.getDecipId(), query, "decipherId");
        }

        if (q.getSizeNum() != -1) {
            this.addSizeToQuery(q.getSizeNum(), q.getSizeOp(), query);
        }

        if (q.getRegionList() != null && !q.getRegionList().isEmpty()) {
            this.addRegionsToQuery(q.getRegionList(), query);
        }

        if (q.getAssembly() != null && !q.getAssembly().isEmpty()) {
            this.addTypeStringToQuery(q.getAssembly(), query, "assembly");
        }

        if (q.getBand() != null && !q.getBand().isEmpty()) {
            this.addTypeStringToQuery(q.getBand(), query, "band");
        }

        if (q.getType() != null && (!q.getType().isEmpty())) {
            this.addTypeIntToQuery(q.getType(), query, "type");
        }

        if (q.getDoses() != null && (q.getDoses().size() > 0)) {
            this.addDosesToQuery(q.getDoses(), q.getDosesNum(), q.getDosesOp(), query);
        } else if (q.getDosesNum() != -10) {
            this.addDNumericDosesToQuery(q.getDosesNum(), q.getDosesOp(), query);
        }

        if (q.getCli() != null && (!q.getCli().isEmpty())) {
            this.addTypeIntToQuery(q.getCli(), query, "clinicalSig");
        }

        if (q.getInhe() != null && (!q.getInhe().isEmpty())) {
            this.addTypeIntToQuery(q.getInhe(), query, "inheritance");
        }
        if (q.getNv() != -1) {
            query.filter("nv =", q.getNv());
        }
        if (q.getCl() != -1) {
            query.filter("cellLine =", q.getCl());
        }
        if (q.getGender() != -1) {
            query.filter("chromGender =", q.getGender());
        }

        if (q.getStatus() != null && (!q.getStatus().isEmpty())) {
            this.addTypeIntToQuery(q.getStatus(), query, "status");
        }

        if (q.getTypeSample() != null && (!q.getTypeSample().isEmpty())) {
            this.addTypeIntToQuery(q.getTypeSample(), query, "typeSample");
        }

        if (q.getReferalDiag() != null && (!q.getReferalDiag().isEmpty())) {
            this.addTypeStringToQuery(q.getReferalDiag(), query, "referalDiag");
        }

        if (q.getHpo() != null && (!q.getHpo().isEmpty())) {
            this.addTypeStringToQuery(q.getHpo(), query, "phenotype");
        }

        if (q.getYear() != null && (!q.getYear().isEmpty())) {
            this.addTypeIntToQuery(q.getYear(), query, "yearOfBirth");
        }

        if (q.getYearTest() != null && (!q.getYearTest().isEmpty())) {
            this.addTypeIntToQuery(q.getYearTest(), query, "yearTest");
        }

        if (q.getAge() != null && (!q.getAge().isEmpty())) {
            this.addAgeTransformIntToQuery(q.getAge(), query, "age");
        }

        if (q.getAgePrenatal() != null && (!q.getAgePrenatal().isEmpty())) {
            this.addTypeIntToQuery(q.getAgePrenatal(), query, "agePrenatal");
        }

        if (q.getEthic() != null && (!q.getEthic().isEmpty())) {
            this.addTypeStringToQuery(q.getEthic(), query, "ethnicGroup");
        }

        if (q.getOrigin() != null && (!q.getOrigin().isEmpty())) {
            this.addTypeStringToQuery(q.getOrigin(), query, "origin");
        }

        if (q.getArrayPlatform() != null && (!q.getArrayPlatform().isEmpty())) {
            this.addTypeStringToQuery(q.getArrayPlatform(), query, "arrayPlatform");
        }

        if (q.getArrayId() != null && (!q.getArrayId().isEmpty())) {
            this.addTypeStringToQuery(q.getArrayId(), query, "arrayId");
        }

        if (q.getSyndrome() != null && (!q.getSyndrome().isEmpty())) {
            this.addTypeIntToQuery(q.getSyndrome(), query, "syndrome");
        }

        if ((q.getSkip() != -1) && (q.getLimit() != -1)) {
            query.offset(q.getSkip()).limit(q.getLimit());
        }

        System.out.println(query);

        Iterable<CNV> aux = query.fetch();
        count.setValue(query.countAll());

        return aux;
    }

    public int getStatsCount() {
        List<CNV> res = datastore.createQuery(CNV.class).asList();
        return res.size();
    }

    public boolean addCNV(List<CNV> listCNV, String username, String sid) {
        boolean result = true;
        DBCollection users = datastore.getDB().getCollection("users");

        BasicDBObject query = new BasicDBObject("name", username).append("sessions.id", sid);

        DBObject dbUser = users.findOne(query);

        List<CNV> res = datastore.createQuery(CNV.class).asList();
        if (dbUser != null) {
            System.out.println("Ha encontrado el usuario");
            try {
                datastore.save(listCNV);
                System.out.println("Se ha insertado correctamente");
            }catch (Exception e){
                System.out.println("Error al insertar");
                return false;
            }
//            return res.size();
        }
        System.out.println("   NOOO HA ENCONTRADO EL USUARIO!!");
        return result;

    }

    public List<String> getAllEthnicGroup() {

        List auxQuery = this.datastore.getCollection(CNV.class).distinct("eg");
        List<String> res = getListOfString(auxQuery);
        return res;
    }

    private void addTypeIntToQuery(List<Integer> l, Query<CNV> query, String name) {
        if (l != null && !l.isEmpty()) {
            query.field(name).in(l);
        }

    }

    private void addSizeToQuery(double size, String op, Query<CNV> query) {

        if (op.equals("=")) {
            query.where("this.e - this.s ==" + size);
        } else if (op.equals(">")) {
            query.where("this.e - this.s >" + size);
        } else if (op.equals(">=")) {
            query.where("this.e - this.s >=" + size);
        } else if (op.equals("<=")) {
            query.where("this.e - this.s <=" + size);
        } else if (op.equals("<")) {
            query.where("this.e - this.s <" + size);
        }
    }

    private void addAgeTransformIntToQuery(List<String> l, Query<CNV> query, String name) {
        List<Integer> li = new ArrayList<>();
        for (String age : l) {
            age = age.toLowerCase();
            if (age.equals("prenatal")){
                li.add(-2);
            }else if(age.equals("")){
                li.add(-1);
            }else {
                li.add(new Double(age).intValue());
            }
        }
        if (li != null && !li.isEmpty()) {
            query.field(name).in(li);
        }

    }

    private void addDosesToQuery(List<Integer> doses, double dosesnum, String op, Query<CNV> query) {
        int criteriaSize = doses.size();
        if (dosesnum != -10) {
            criteriaSize++;
        }
        Criteria[] or = new Criteria[criteriaSize];

        int i = 0;
        for (int dos : doses) {
            Query<CNV> auxQuery = this.datastore.createQuery(CNV.class);
            List<Criteria> andList = new ArrayList<>();
            switch (dos) {
                case 0:
                    andList.add(auxQuery.criteria("doses").lessThanOrEq(-1));
                    break;
                case 1:
                    andList.add(auxQuery.criteria("doses").greaterThan(-1));
                    andList.add(auxQuery.criteria("doses").lessThan(-0.35));
                    break;
                case 2:
                    andList.add(auxQuery.criteria("doses").equal(1));
                    break;
                case 3:
                    andList.add(auxQuery.criteria("doses").greaterThan(0.35));
                    andList.add(auxQuery.criteria("doses").lessThanOrEq(0.6));
                    break;
                case 4:
                    andList.add(auxQuery.criteria("doses").greaterThanOrEq(0.61));
                    andList.add(auxQuery.criteria("doses").lessThan(1));
                    break;
                case 5:
                    andList.add(auxQuery.criteria("doses").greaterThanOrEq(1));
                    break;

            }
            or[i++] = auxQuery.and(andList.toArray(new Criteria[andList.size()]));
        }
        if (dosesnum != -10) {
            Query<CNV> auxQuery = this.datastore.createQuery(CNV.class);
            List<Criteria> andList = new ArrayList<>();
            if (op.equals("=")) {
                andList.add(auxQuery.criteria("doses").equal(dosesnum));
            } else if (op.equals(">")) {
                andList.add(auxQuery.criteria("doses").greaterThan(dosesnum));
            } else if (op.equals(">=")) {
                andList.add(auxQuery.criteria("doses").greaterThanOrEq(dosesnum));
            } else if (op.equals("<=")) {
                andList.add(auxQuery.criteria("doses").lessThanOrEq(dosesnum));
            } else if (op.equals("<")) {
                andList.add(auxQuery.criteria("doses").lessThan(dosesnum));
            }
            or[i++] = auxQuery.and(andList.toArray(new Criteria[andList.size()]));
        }

        query.or(or);
        System.out.println("La doses es:" + doses.size());

    }



    private void addDNumericDosesToQuery(double doses, String op, Query<CNV> query) {
        if (op.equals("=")) {
            query.field("doses").equal(doses);
        } else if (op.equals(">")) {
            query.field("doses").greaterThan(doses);
        } else if (op.equals(">=")) {
            query.field("doses").greaterThanOrEq(doses);
        } else if (op.equals("<=")) {
            query.field("doses").lessThanOrEq(doses);
        } else if (op.equals("<")) {
            query.field("doses").lessThan(doses);
        }
    }

    private void addTypeLongToQuery(List<Long> l, Query<CNV> query, String name) {
        if (l != null && !l.isEmpty()) {
            query.field(name).in(l);
        }
    }

    private void addTypeStringToQuery(List<String> l, Query<CNV> query, String name) {
        if (l != null && !l.isEmpty()) {
            query.field(name).in(l);
        }
    }

    private void addRegionsToQuery(String listRegions, Query<CNV> query) {

        List<String> regions = Splitter.on(",").splitToList(listRegions);

        System.out.println("regions = " + regions);

        Criteria[] or = new Criteria[regions.size()];

        int i = 0;
        for (String r : regions) {
            Region region = new Region(r);
            List<String> chunkIds = this.getChunkIds(region);
            Query<CNV> auxQuery = this.datastore.createQuery(CNV.class);

            List<Criteria> andList = new ArrayList<>();
//            andList.add(auxQuery.criteria("_at.chIds").in(chunkIds));
            andList.add(auxQuery.criteria("chromosome").equal(region.getChromosome()));
            andList.add(auxQuery.criteria("end").greaterThanOrEq(region.getStart()));
            andList.add(auxQuery.criteria("start").lessThanOrEq(region.getEnd()));

            or[i++] = auxQuery.and(andList.toArray(new Criteria[andList.size()]));
//
//            Criteria[] auxOr = new Criteria[3];
//
//            // Rs < Fe && Re > Fs
//
//            List<Criteria> and1 = new ArrayList<>();
//            and1.add(auxQuery.criteria("_at.chIds").in(chunkIds));
//            and1.add(auxQuery.criteria("chromosome").equal(region.getChromosome()));
//            and1.add(auxQuery.criteria("start").greaterThanOrEq(region.getStart()));
//            and1.add(auxQuery.criteria("end").lessThanOrEq(region.getEnd()));
//
//            List<Criteria> and2 = new ArrayList<>();
//            and2.add(auxQuery.criteria("_at.chIds").in(chunkIds));
//            and2.add(auxQuery.criteria("chromosome").equal(region.getChromosome()));
//            and2.add(auxQuery.criteria("start").lessThanOrEq(region.getStart()));
//            and2.add(auxQuery.criteria("end").greaterThanOrEq(region.getStart()));
//
//            List<Criteria> and3 = new ArrayList<>();
//            and3.add(auxQuery.criteria("_at.chIds").in(chunkIds));
//            and3.add(auxQuery.criteria("chromosome").equal(region.getChromosome()));
//            and3.add(auxQuery.criteria("start").lessThanOrEq(region.getEnd()));
//            and3.add(auxQuery.criteria("end").greaterThanOrEq(region.getEnd()));
//
//            auxOr[0] = auxQuery.and(and1.toArray(new Criteria[and1.size()]));
//            auxOr[1] = auxQuery.and(and2.toArray(new Criteria[and2.size()]));
//            auxOr[2] = auxQuery.and(and3.toArray(new Criteria[and3.size()]));
//            or[i++] = auxQuery.or(auxOr);
        }
        query.or(or);

    }

    private List<String> getChunkIds(Region region) {
        List<String> chunkIds = new LinkedList<>();

        int chunkSize = (region.getEnd() - region.getStart() > CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_BIG) ? CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_BIG
                : CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_SMALL;
        int ks = chunkSize / 1000;
        int chunkStart = region.getStart() / chunkSize;
        int chunkEnd = region.getEnd() / chunkSize;

        for (int i = chunkStart; i <= chunkEnd; i++) {
            String chunkId = region.getChromosome() + "_" + i + "_" + ks + "k";
            chunkIds.add(chunkId);
        }

        return chunkIds;
    }

    private static float round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    @Entity
    private static class CountResult {

        @Id
        private int count;

        public int getCount() {
            return count;
        }
    }

    protected int getChunkId(int position, int chunksize) {
        return position / chunksize;
    }

    private int getChunkStart(int id, int chunksize) {
        return (id == 0) ? 1 : id * chunksize;
    }

    private int getChunkEnd(int id, int chunksize) {
        return (id * chunksize) + chunksize - 1;
    }

    private List<String> getListOfString(List auxQuery) {
        List<String> res = new ArrayList<>(auxQuery.size());
        for (Object obj : auxQuery) {
            res.add(obj.toString());
        }
        return res;
    }
}
