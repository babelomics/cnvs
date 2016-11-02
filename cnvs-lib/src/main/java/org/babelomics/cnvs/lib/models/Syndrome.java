package org.babelomics.cnvs.lib.models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;

/**
 * Created by sgallego on 9/22/16.
 */

@Entity(noClassnameStored = true)
public class Syndrome {

    @Id
    private ObjectId id;

    @Indexed(name = "index_syndrome_id", unique = true)
    @Property("sid")
    private int syndromeId;

    @Property("c")
    private String chromosome;

    @Property("n")
    private String name;


    public Syndrome(int syndromeId, String chromosome, String name) {
        this.name = name;
        this.chromosome = chromosome;
        this.syndromeId = syndromeId;
    }

    public Syndrome() {
        this(-1, "-1","");
    }

    public String getChromosome() {
        return chromosome;
    }


    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSyndromeId() {
        return syndromeId;
    }

    public void setSyndromeId(int syndromeId) {
        this.syndromeId = syndromeId;
    }

    @Override
    public String toString() {
        return "Syndrome{" +
                "id=" + id +
                ", syndromeId=" + syndromeId +
                ", chromosome='" + chromosome + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
