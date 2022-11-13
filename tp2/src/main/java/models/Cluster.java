package models;

import java.util.*;

// class implemented to create cluster composed of only classes, not Clusters
public class Cluster implements ICluster {
	/* ATTRIBUTES */
	private String id;
	
	/* CONSTRUCTOR */
	public Cluster(String id) {
        this.id = id;
    }

	/* Getters and Setters */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	@Override
	public List<String> getClustersIds() {
		List<String> id = new ArrayList<>();
		id.add(this.id);
		return id;
	}

	@Override
	public List<ICluster> getClusters() {
		List<ICluster> clusters = new ArrayList<>();
		clusters.add(this);
		return clusters;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if (!(o instanceof Cluster))
			return false;
		Cluster cluster = (Cluster) o;
		return (id.equals(cluster.getId()));
	}
}
