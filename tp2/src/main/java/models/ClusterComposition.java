package models;

import java.util.ArrayList;
import java.util.List;

// Class implemented to create cluster composed of other clusters
public class ClusterComposition implements ICluster {
	/* ATTRIBUTES */
    List<ICluster> clusters;

    /* CONSTRUCTOR */
    public ClusterComposition(){
        this.clusters = new ArrayList<>();
    }

    /* adds a new cluster to this cluster */
    public void addCluster(ICluster cluster){
        this.getClusters().add(cluster);
    }

    // gets identifiers from every clusters 
    @Override
    public List<String> getClustersIds() {
        List<String> clustersComponents = new ArrayList<>();
        for (ICluster cluster :
                this.clusters) {
            clustersComponents.addAll(cluster.getClustersIds());
        }
        return clustersComponents;
    }

    
    /* Getter */
    @Override
    public List<ICluster> getClusters() {
        return this.clusters;
    }
    
    @Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if (!(o instanceof ClusterComposition))
			return false;
		ClusterComposition clusterComposition = (ClusterComposition) o;
		return (clusters.equals(clusterComposition.getClusters()));
	}

    @Override
    public String toString() {
        String res = "";
        res += "{";
        int size = this.clusters.size();
        for (int i = 0; i < size; i++) {
            res += (i != 0 ? ", " : "") + this.clusters.get(i).toString();
        }
        res += "}";
        return  res;
    }
}
