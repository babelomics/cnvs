package org.babelomics.cnvs.lib.annot;

import com.beust.jcommander.internal.Lists;
import org.apache.avro.generic.GenericData;
import org.babelomics.cnvs.lib.models.CNV;
import org.opencb.cellbase.core.client.CellBaseClient;
import org.opencb.cellbase.core.common.core.Chromosome;
import org.opencb.datastore.core.ObjectMap;
import org.opencb.datastore.core.QueryResponse;
import org.opencb.datastore.core.QueryResult;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by sgallego on 12/10/15.
 */
public class CellBaseAnnotator {
    private CellBaseClient cellBaseClient;
    private boolean override;
    private boolean remove;
    private boolean ct;
    private boolean gene;
    private Map<String, List<Cytoband>> chromosomes;


    private final static String CT_TAG = "ct";
    private final static String GENE_TAG = "g";
    private final static String HOST = "http://bioinfo.hpc.cam.ac.uk/cellbase/webservices/rest";
    private final static String VERSION = "v3";
    private final static String SPECIES = "hsapiens";

    public CellBaseAnnotator() {
        this.cellBaseClient = null;
        this.override = false;
        this.remove = false;
        this.ct = false;
        this.gene = false;
        this.chromosomes = new HashMap<>();


        try {
            URI uri = new URI(HOST);
            cellBaseClient = new CellBaseClient(uri, VERSION, SPECIES);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.err.println("Invalid URL: " + HOST);
        }


    }

    public void annot(List<CNV> cnvList) throws IOException {

        if (this.isRemove()) {
//            for (CNV v : cnvList) {
//                this.remove(v);
//            }
        } else {

            for (CNV cnv : cnvList) {

                List<Cytoband> cytobands = null;
                if (this.chromosomes.containsKey(cnv.getChromosome())) {
                    cytobands = this.chromosomes.get(cnv.getChromosome());
                } else {
                    QueryResponse<QueryResult<ObjectMap>> queryResponse;
                    List<String> ids = Lists.newArrayList(cnv.getChromosome());

                    queryResponse = cellBaseClient.get(
                            CellBaseClient.Category.genomic,
                            CellBaseClient.SubCategory.chromosome,
                            ids,
                            CellBaseClient.Resource.info,
                            null);

                    if (queryResponse.getResponse() != null && queryResponse.getResponse().size() > 0 && queryResponse.getResponse().get(0) != null && queryResponse.getResponse().get(0).getResult().size() > 0) {

                        ObjectMap objectMap = queryResponse.getResponse().get(0).getResult().get(0);
                        cytobands = getCytobands(objectMap);
                        this.chromosomes.put(cnv.getChromosome(), cytobands);
                    }
                }

                Cytoband cytoband = getCytoband(cytobands, cnv);

                if (cytoband != null) {
                    cnv.setLocus(cytoband.name);
                }
            }
        }

    }

    private Cytoband getCytoband(List<Cytoband> cytobands, CNV cnv) {

        for (Cytoband c : cytobands) {
            if (c.start <= cnv.getStart() && c.end >= cnv.getEnd()) {
                return c;
            }
        }
        return null;


    }

    private List<Cytoband> getCytobands(ObjectMap chromosome) {
        List<Cytoband> res = new ArrayList<>();

        List<LinkedHashMap<String, ObjectMap>> cytobands = (List<LinkedHashMap<String, ObjectMap>>) chromosome.get("chromosomes");

        for (LinkedHashMap<String, ObjectMap> entry : cytobands) {
            List<LinkedHashMap<String, ObjectMap>> cytobandList = (List<LinkedHashMap<String, ObjectMap>>) entry.get("cytobands");
            for (LinkedHashMap<String, ObjectMap> cytobandEntry : cytobandList) {

                String name = String.valueOf(cytobandEntry.get("name"));
                String stain = String.valueOf(cytobandEntry.get("stain"));
                long start = Long.parseLong(String.valueOf(cytobandEntry.get("start")));
                long end = Long.parseLong(String.valueOf(cytobandEntry.get("end")));
                Cytoband c = new Cytoband(stain, name, start, end);

                res.add(c);
            }
        }
        return res;
    }


    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public boolean isCt() {
        return ct;
    }

    public void setCt(boolean ct) {
        this.ct = ct;
    }

    public boolean isGene() {
        return gene;
    }

    public void setGene(boolean gene) {
        this.gene = gene;
    }


    private class Cytoband {
        String stain;
        String name;
        long start, end;

        public Cytoband(String stain, String name, long start, long end) {
            this.stain = stain;
            this.name = name;
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "Cytoband{" +
                    "stain='" + stain + '\'' +
                    ", name='" + name + '\'' +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

}
