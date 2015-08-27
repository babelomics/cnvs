package org.babelomics.lib.io;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.babelomics.lib.models.CNV;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;
import org.opencb.biodata.models.feature.Region;
import org.apache.commons.lang3.mutable.MutableLong;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class CNVSQueryManager {
	final Datastore datastore;
    static final int DECIMAL_POSITIONS = 3;


    public CNVSQueryManager(String host, String dbName) {
        Morphia morphia = new Morphia();
        morphia.mapPackage("org.babelomics.pvs.lib.models");
        this.datastore = morphia.createDatastore(new MongoClient(host), dbName);
        this.datastore.ensureIndexes();
    }

    public CNVSQueryManager(String dbName) {
        this("localhost", dbName);
    }

    public CNVSQueryManager(Datastore datastore) {
        this.datastore = datastore;
    }

    public List<List<CNV>> getVariantsByRegionList(List<Region> regions) {

        List<List<CNV>> res = new ArrayList<>();

        for (Region r : regions) {

            List<String> chunkIds = getChunkIds(r);
            Query<CNV> auxQuery = (Query) this.datastore.createQuery(CNV.class);

            auxQuery.filter("_at.chIds in", chunkIds).
                    filter("chromosome =", r.getChromosome()).
                    filter("position >=", r.getStart()).
                    filter("position <=", r.getEnd());

            List<CNV> variants = auxQuery.asList();

            for (CNV v : variants) {
                v.setStats(null);
                v.setAnnots(null);
            }
            res.add(variants);
        }

        return res;
    }

    public List<List<IntervalFrequency>> getAllIntervalFrequencies(List<Region> regions, boolean histogramLogarithm, int histogramMax, int interval) {

        List<List<IntervalFrequency>> res = new ArrayList<>();
        for (Region r : regions) {
            res.add(getIntervalFrequencies(r, histogramLogarithm, histogramMax, interval));
        }

        return res;


    }


    public List<IntervalFrequency> getIntervalFrequencies(Region region, boolean histogramLogarithm, int histogramMax, int interval) {

        List<IntervalFrequency> res = new ArrayList<>();

        BasicDBObject start = new BasicDBObject("$gt", region.getStart());
        start.append("$lt", region.getEnd());

        BasicDBList andArr = new BasicDBList();
        andArr.add(new BasicDBObject("c", region.getChromosome()));
        andArr.add(new BasicDBObject("p", start));

        BasicDBObject match = new BasicDBObject("$match", new BasicDBObject("$and", andArr));


        BasicDBList divide1 = new BasicDBList();
        divide1.add("$p");
        divide1.add(interval);

        BasicDBList divide2 = new BasicDBList();
        divide2.add(new BasicDBObject("$mod", divide1));
        divide2.add(interval);

        BasicDBList subtractList = new BasicDBList();
        subtractList.add(new BasicDBObject("$divide", divide1));
        subtractList.add(new BasicDBObject("$divide", divide2));


        BasicDBObject substract = new BasicDBObject("$subtract", subtractList);

        DBObject totalCount = new BasicDBObject("$sum", 1);

        BasicDBObject g = new BasicDBObject("_id", substract);
        g.append("features_count", totalCount);
        BasicDBObject group = new BasicDBObject("$group", g);

        BasicDBObject sort = new BasicDBObject("$sort", new BasicDBObject("_id", 1));


        DBCollection collection = datastore.getCollection(CNV.class);

        List<BasicDBObject> aggList = new ArrayList<>();
        aggList.add(match);
        aggList.add(group);
        aggList.add(sort);

        AggregationOutput aggregation = collection.aggregate(aggList);

        Map<Long, IntervalFrequency> ids = new HashMap<>();

        for (DBObject intervalObj : aggregation.results()) {

            Long _id = Math.round((Double) intervalObj.get("_id"));//is double

            IntervalFrequency intervalVisited = ids.get(_id);

            if (intervalVisited == null) {
                intervalVisited = new IntervalFrequency();

                intervalVisited.setId(_id);
                intervalVisited.setStart(getChunkStart(_id.intValue(), interval));
                intervalVisited.setEnd(getChunkEnd(_id.intValue(), interval));
                intervalVisited.setChromosome(region.getChromosome());
                intervalVisited.setFeaturesCount(Math.log((int) intervalObj.get("features_count")));
                ids.put(_id, intervalVisited);
            } else {
                double sum = intervalVisited.getFeaturesCount() + Math.log((int) intervalObj.get("features_count"));
                intervalVisited.setFeaturesCount(sum);
            }
        }

        int firstChunkId = getChunkId(region.getStart(), interval);
        int lastChunkId = getChunkId(region.getEnd(), interval);

        IntervalFrequency intervalObj;
        for (int chunkId = firstChunkId; chunkId <= lastChunkId; chunkId++) {
            intervalObj = ids.get((long) chunkId);

            if (intervalObj == null) {
                intervalObj = new IntervalFrequency(chunkId, getChunkStart(chunkId, interval), getChunkEnd(chunkId, interval), region.getChromosome(), 0);
            }
            res.add(intervalObj);
        }

        return res;
    }

    public Iterable<CNV> getVariantsByRegionList(List<Region> regions, Integer skip, Integer limit, MutableLong count) {

        Criteria[] or = new Criteria[regions.size()];

        int i = 0;
        for (Region r : regions) {
            List<String> chunkIds = getChunkIds(r);
            Query<CNV> auxQuery = this.datastore.createQuery(CNV.class);

            List<Criteria> and = new ArrayList<>();
            and.add(auxQuery.criteria("_at.chIds").in(chunkIds));
            and.add(auxQuery.criteria("chromosome").equal(r.getChromosome()));
            and.add(auxQuery.criteria("position").greaterThanOrEq(r.getStart()));
            and.add(auxQuery.criteria("position").lessThanOrEq(r.getEnd()));

          /*  if (diseaseIds != null && diseaseIds.size() > 0) {
                and.add(auxQuery.criteria("diseases.diseaseGroupId").in(diseaseIds));
//                and.add(auxQuery.criteria("diseases.diseaseGroupId").hasAllOf(diseaseId));
            }*/
            or[i++] = auxQuery.and(and.toArray(new Criteria[and.size()]));
        }

        Query<CNV> query = this.datastore.createQuery(CNV.class);

        query.or(or);

        if (skip != null && limit != null) {
            query.offset(skip).limit(limit);
        }

        Iterable<CNV> aux = query.fetch();
        count.setValue(query.countAll());

        List<CNV> res = new ArrayList<>();

        //int sampleCount = calculateSampleCount(diseaseIds);

        for (CNV v : aux) {
            //v.setStats(calculateStats(v, diseaseIds, sampleCount));
            res.add(v);
        }

        return res;
    }
/*
    private int calculateSampleCount(List<Integer> diseaseId) {
        int res = 0;


        Iterable<DiseaseGroup> groupId = this.datastore.createQuery(DiseaseGroup.class).field("groupId").in(diseaseId).fetch();

        for (DiseaseGroup dg : groupId) {
            res += dg.getSamples();
        }

        return res;
    }

    private DiseaseCount calculateStats(CNV v, List<Integer> diseaseId, int sampleCount) {
        DiseaseCount dc;

        int gt00 = 0;
        int gt01 = 0;
        int gt11 = 0;
        int gtmissing = 0;

        for (DiseaseCount auxDc : v.getDiseases()) {
            if (diseaseId.contains(auxDc.getDiseaseGroup().getGroupId())) {
                gt00 += auxDc.getGt00();
                gt01 += auxDc.getGt01();
                gt11 += auxDc.getGt11();
                gtmissing += auxDc.getGtmissing();
            }
        }

        gt00 = sampleCount - gt01 - gt11 - gtmissing;


        int refCount = gt00 * 2 + gt01;
        int altCount = gt11 * 2 + gt01;

        float refFreq = (float) refCount / (refCount + altCount);
        float altFreq = (float) altCount / (refCount + altCount);

        float maf = Math.min(refFreq, altFreq);

        dc = new DiseaseCount(null, gt00, gt01, gt11, gtmissing);

        dc.setRefFreq(round(refFreq, DECIMAL_POSITIONS));
        dc.setAltFreq(round(altFreq, DECIMAL_POSITIONS));
        dc.setMaf(round(maf, DECIMAL_POSITIONS));

        return dc;
    }
*/

    private List<String> getChunkIds(Region region) {
        List<String> chunkIds = new LinkedList<>();

        int chunkSize = (region.getEnd() - region.getStart() > CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_BIG) ?
        		CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_BIG : CNVSCopyNumberVariationMongoDataWriter.CHUNK_SIZE_SMALL;
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
        if (places < 0) throw new IllegalArgumentException();

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
}
