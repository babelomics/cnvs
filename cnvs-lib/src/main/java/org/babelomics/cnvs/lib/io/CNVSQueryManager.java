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
	QueryCommandLine q;

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

	public CNVSQueryManager(Datastore datastore, QueryCommandLine q) {
		this.datastore = datastore;
		this.q = q;
		
	}
	
	
	public Iterable<CNV> getVariantsByFilters(MutableLong count) {
	
		Query<CNV> query = this.datastore.createQuery(CNV.class);
		
		
		if(q.getCode() != null){
			query.filter("ref =", q.getCode());
		}
		if(q.getDecipId() != null){
			this.addTypeLongToQuery(q.getDecipId(), query, "decipId");
		}
		
		if(q.getRegionList() != null){
			this.addRegionsToQuery(q.getRegionList(), query);
		}
		if(q.getAssembly() != null){
			query.filter("assembly =", q.getAssembly());
		}
		if(q.getBand() != null){
			this.addTypeStringToQuery(q.getBand(), query, "band");
		}
		
		
		if(q.getType() != null){
			this.addTypeIntToQuery(q.getType(), query, "type");
		}
		if(q.getDoses() != null){
			this.addTypeIntToQuery(q.getDoses(), query, "doses");
		}
		if(q.getCli() != null){
			this.addTypeIntToQuery(q.getCli(), query, "clinicalSig");
		}
		
		if(q.getInhe() != null){
			this.addTypeIntToQuery(q.getInhe(), query, "inheritance");
		}
		if(q.getCl() != -1){
			query.filter("cellLine =", q.getCl());
		}
		if(q.getGender() != -1){
			query.filter("chromGender =", q.getGender());
		}
		
		if(q.getStatus() != null){
			this.addTypeIntToQuery(q.getStatus(), query, "status");
		}
		
		if(q.getTypeSample() != null){
			this.addTypeIntToQuery(q.getTypeSample(), query, "typeSample");
		}
		
		if(q.getHpo() != null){
			this.addTypeStringToQuery(q.getHpo(), query, "hpo");
		}
		
		if(q.getYear() != null){
			this.addTypeIntToQuery(q.getYear(), query, "yearOfBirth");
		}
		
		if(q.getEthic() != null){
			this.addTypeStringToQuery(q.getEthic(), query, "ethnicGroup");
		}
		if(q.getOrigin() != null){
			this.addTypeStringToQuery(q.getOrigin(), query, "origin");
		}
	
		if((q.getSkip()!= -1) && (q.getLimit() != -1)){
		
			query.offset(q.getSkip()).limit(q.getLimit());
		} 

		System.out.println(query);

		Iterable<CNV> aux = query.fetch();
		count.setValue(query.countAll());

		return aux;
	}
	
	
	private void addTypeIntToQuery(List <Integer> l, Query<CNV> query, String name) {
		query.field(name).in(l);
		
	}
	
	private void addTypeLongToQuery(List <Long> l, Query<CNV> query, String name) {

		//query.filter("type in ", l);
		query.field(name).in(l);
		
	}
	private void addTypeStringToQuery(List <String> l, Query<CNV> query, String name) {

		//query.filter("type in ", l);
		query.field(name).in(l);
		
	}
	
	private void addRegionsToQuery(String listRegions, Query<CNV> query) {
		
		List<String> regions = Splitter.on(",").splitToList(listRegions);
		
		Criteria[] or = new Criteria[regions.size()];

		int i = 0;
		for(String r: regions){
			Region region = new Region(r);
			List<String> chunkIds = this.getChunkIds(region);
            Query<CNV> auxQuery = this.datastore.createQuery(CNV.class);
            
            List<Criteria> and = new ArrayList<>();
            and.add(auxQuery.criteria("_at.chIds").in(chunkIds));
            and.add(auxQuery.criteria("chromosome").equal(region.getChromosome()));
            and.add(auxQuery.criteria("start").greaterThanOrEq(region.getStart()));
            and.add(auxQuery.criteria("end").lessThanOrEq(region.getEnd()));

            or[i++] = auxQuery.and(and.toArray(new Criteria[and.size()]));
		}
		query.or(or);
		System.out.println(query);
		
	}

	public Iterable<CNV> getVariantsByRegionList(List<Region> regions,Integer skip, Integer limit, MutableLong count) {

		Query<CNV> query = this.datastore.createQuery(CNV.class);
		Criteria[] or = new Criteria[regions.size()];

		int i = 0;
		for(Region region : regions){
			List<String> chunkIds = this.getChunkIds(region);
			Query<CNV> auxQuery = this.datastore.createQuery(CNV.class);

			List<Criteria> and = new ArrayList<>();
			and.add(auxQuery.criteria("_at.chIds").in(chunkIds));
			and.add(auxQuery.criteria("chromosome").equal(region.getChromosome()));
			and.add(auxQuery.criteria("start").greaterThanOrEq(region.getStart()));
			and.add(auxQuery.criteria("end").lessThanOrEq(region.getEnd()));

			or[i++] = auxQuery.and(and.toArray(new Criteria[and.size()]));
		}
		query.or(or);
		System.out.println(query);

		if (skip != null && limit != null) {
			query.offset(skip).limit(limit);
		}

		Iterable<CNV> aux = query.fetch();
		count.setValue(query.countAll());

		System.out.println("count = " + count.getValue());

		return aux;
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

}
