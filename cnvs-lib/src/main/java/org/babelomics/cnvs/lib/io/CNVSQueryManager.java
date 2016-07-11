package org.babelomics.cnvs.lib.io;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


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

        if (q.getRegionList() != null && !q.getRegionList().isEmpty()) {
            this.addRegionsToQuery(q.getRegionList(), query);
        }
        if (q.getAssembly() != null && !q.getAssembly().isEmpty()) {
            query.filter("assembly =", q.getAssembly());
        }
        if (q.getBand() != null && !q.getBand().isEmpty()) {
            this.addTypeStringToQuery(q.getBand(), query, "band");
        }

        if (q.getType() != null && (!q.getType().isEmpty())) {
            this.addTypeIntToQuery(q.getType(), query, "type");
        }
        if (q.getDoses() != null && (q.getDoses().size() > 0)) {
            this.addTypeIntToQuery(q.getDoses(), query, "doses");
        }

        if (q.getCli() != null && (!q.getCli().isEmpty())) {
            this.addTypeIntToQuery(q.getCli(), query, "clinicalSig");
        }

        if (q.getInhe() != null && (!q.getInhe().isEmpty())) {
            this.addTypeIntToQuery(q.getInhe(), query, "inheritance");
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

        if (q.getHpo() != null && (!q.getHpo().isEmpty())) {
            this.addTypeStringToQuery(q.getHpo(), query, "phenotype");
        }

        if (q.getYear() != null && (!q.getYear().isEmpty())) {
            this.addTypeIntToQuery(q.getYear(), query, "yearOfBirth");
        }

        if (q.getEthic() != null && (!q.getEthic().isEmpty())) {
            this.addTypeStringToQuery(q.getEthic(), query, "ethnicGroup");
        }
        if (q.getOrigin() != null && (!q.getOrigin().isEmpty())) {
            this.addTypeStringToQuery(q.getOrigin(), query, "origin");
        }

        if ((q.getSkip() != -1) && (q.getLimit() != -1)) {

            query.offset(q.getSkip()).limit(q.getLimit());
        }

        System.out.println(query);

        Iterable<CNV> aux = query.fetch();
        count.setValue(query.countAll());

        return aux;
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
        System.out.println(query);

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
