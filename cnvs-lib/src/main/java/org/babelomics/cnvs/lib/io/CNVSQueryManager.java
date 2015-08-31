package org.babelomics.cnvs.lib.io;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

	public Iterable<CNV> getVariantsByFilters(Map<String, String> queries,
			Integer skip, Integer limit, MutableLong count) {

		Query<CNV> query = this.datastore.createQuery(CNV.class);


		System.out.println(queries);

		for (Map.Entry<String, String> elem : queries.entrySet()) {
			String value = elem.getValue();
			String key = elem.getKey();

			switch (key) {
			case "code":
				this.addCodeToQuery(value, query);
				break;
			case "decipId":
				this.addDecipToQuery(value, query);
				break;
			case "region":
				this.addRegionsToQuery(value, query);
				break;
			case "assembly":
				this.addAssemblyToQuery(value, query);
				break;
			case "band":
				this.addBandToQuery(value, query);
				break;
			case "type":
				this.addTypeToQuery(value, query);
				break;
			case "doses":
				this.addDosesToQuery(value, query);
				break;
			case "inheritance":
				this.addInheToQuery(value, query);
				break;
			case "cellline":
				this.addCellLineToQuery(value, query);
				break;
			case "gender":
				this.addGenderToQuery(value, query);
				break;
			case "status":
				this.addStatusToQuery(value, query);
				break;
			case "typeSample":
				this.addTypeSampleToQuery(value, query);
				break;
			case "hpo":
				this.addHpoToQuery(value, query);
				break;
			case "ethic":
				this.addEthicToQuery(value, query);
				break;
			case "origin":
				this.addOriginToQuery(value, query);
				break;

			default:
				break;

			}
		}

		if (skip != null && limit != null) {
			query.offset(skip).limit(limit);
		}

		System.out.println(query);

		Iterable<CNV> aux = query.fetch();
		count.setValue(query.countAll());

		return aux;
	}

	private void addTypeToQuery(String value, Query<CNV> query) {
		query.filter("type =", Integer.parseInt(value));
		
	}

	private void addOriginToQuery(String value, Query<CNV> query) {
		query.filter("origin =", value);

	}

	private void addEthicToQuery(String value, Query<CNV> query) {
		query.filter("ethnicGroup =", value);

	}

	private void addHpoToQuery(String value, Query<CNV> query) {
		query.filter("phenotype =", value);
	}

	private void addTypeSampleToQuery(String value, Query<CNV> query) {
		query.filter("typeSample =", Integer.parseInt(value));

	}

	private void addStatusToQuery(String value, Query<CNV> query) {
		query.filter("status =", value);

	}

	private void addGenderToQuery(String value, Query<CNV> query) {
		query.filter("chromGender =", Integer.parseInt(value));

	}

	private void addCellLineToQuery(String value, Query<CNV> query) {
		query.filter("cellLine =", value);

	}

	private void addInheToQuery(String value, Query<CNV> query) {
		query.filter("inheritance =", Integer.parseInt(value));

	}

	private void addDosesToQuery(String value, Query<CNV> query) {
		query.filter("doses =", Integer.parseInt(value));

	}

	private void addBandToQuery(String value, Query<CNV> query) {
		query.filter("locus =", value);

	}

	private void addAssemblyToQuery(String value, Query<CNV> query) {
		query.filter("assembly =", value);

	}

	private void addDecipToQuery(String value, Query<CNV> query) {
		query.filter("decipherId =", Long.parseLong(value));

	}

	private void addCodeToQuery(String value, Query<CNV> query) {
		query.filter("ref =", value);

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
		
	}

	public Iterable<CNV> getVariantsByRegionList(List<Region> regions,
			Integer skip, Integer limit, MutableLong count) {

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

			or[i++] = auxQuery.and(and.toArray(new Criteria[and.size()]));
		}

		Query<CNV> query = this.datastore.createQuery(CNV.class);

		query.or(or);

		if (skip != null && limit != null) {
			query.offset(skip).limit(limit);
		}

		Iterable<CNV> aux = query.fetch();
		count.setValue(query.countAll());

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
