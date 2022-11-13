package models;

import java.util.List;

// Interface implemented to use clusters, that can be composed of other clusters
public interface ICluster {
	// gets every class names from the cluster
    List<String> getClustersIds();
    // gets every clusters from the cluster
    List<ICluster> getClusters();
}
