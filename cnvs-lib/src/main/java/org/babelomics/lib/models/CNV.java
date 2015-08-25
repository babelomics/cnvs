package org.babelomics.lib.models;

/**
 * @author Alejandro Alemán Ramos <alejandro.aleman.ramos@gmail.com>
 */
public class CNV {

	private String ref;
	private String chromosome;
	private long start;
	private long end;
	private int type;
	
	@Override
	public String toString() {
		return "CNV [ref=" + ref + ", chromosome=" + chromosome + ", start="
				+ start + ", end=" + end + ", type=" + type + "]";
	}

	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public String getType() {
		switch (type) {
		case 0:
			return "gain";
		case 1:
			return "loss";

		case 2:
			return "LOH neutral";
		default:
			return "";
		}
	}

	public void setType(String type) {
		switch (type.toLowerCase()) {
		case "gain":
			this.type = 0;
			break;
		case "loss":
			this.type = 1;
			break;

		case "loh neutral":
			this.type = 2;
			break;
		default:
			this.type = -1;
			break;

		}
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
	
	
	

}
