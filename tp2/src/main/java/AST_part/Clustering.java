package AST_part;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.util.Pair;

import models.Cluster;
import models.ClusterComposition;
import models.ICluster;
import models.WeightedGraph;

// class implemented to created a cluster hierachy of our classes
public class Clustering {

	// Q2.1 creates a cluster hierachy from our graph, where every vertex represents
	// a
	// simple Cluster
	private static ClusterComposition generateCluster(WeightedGraph graph) {
		ClusterComposition clusterComposition = new ClusterComposition();
		for (String vertex : graph.getvertexList()) {
			Cluster cluster = new Cluster(vertex);
			clusterComposition.addCluster(cluster);
		}
		return clusterComposition;
	}

	// Q2.1 updates a simple cluster composition to a cluster hierarchy
	public static ICluster clustering(WeightedGraph graph) {
		ClusterComposition clusterComposition = generateCluster(graph);

		while (clusterComposition.getClusters().size() > 2) {
			Pair<ICluster, ICluster> coupleClusters = getClosestCluster(clusterComposition);
			ClusterComposition newClusterComposition = new ClusterComposition();
			if (coupleClusters != null) {
				newClusterComposition.addCluster(coupleClusters.getFirst());
				newClusterComposition.addCluster(coupleClusters.getSecond());

				clusterComposition.getClusters().remove(coupleClusters.getFirst());
				clusterComposition.getClusters().remove(coupleClusters.getSecond());

				clusterComposition.addCluster(newClusterComposition);
			}
		}
		return clusterComposition;
	}

	// returns a float representing a coupling metric between two clusters clusterA
	// and clusterB (Q2.1)
	private static float couplingMetric(ICluster clusterA, ICluster clusterB) {
		float res = 0;

		for (String idA : clusterA.getClustersIds()) {
			for (String idB : clusterB.getClustersIds()) {
				if (!idA.equals(idB)) {
					res += StaticAnalysis.getCouplingMetric(idB, idA);
				}
			}
		}
		return res;
	}

	// return a table compose of two elements, representing a couple of clusters
	// that are the closest in the graph (Q2.1)
	private static Pair<ICluster, ICluster> getClosestCluster(ClusterComposition clusterComposition) {

		ICluster clusterA = null, clusterB = null;
		float max = -1;
		List<ICluster> clusters = clusterComposition.getClusters();
		for (ICluster cluster1 : clusters) {
			for (ICluster cluster2 : clusters) {
				if (!cluster1.equals(cluster2)) {
					float cm = couplingMetric(cluster1, cluster2);
					if (max < cm) {
						clusterA = cluster1;
						clusterB = cluster2;
					}
				}
			}
		}

		if (clusterA != null && clusterB != null) {
			return new Pair<ICluster, ICluster>(clusterA, clusterB);
		}
		return null;
	}

	// Q2.2 identifies clusters coupled verifying the average coupling metric is
	// superior to cp for every couple of classes
	public static List<ICluster> filterWithCp(ICluster clusterComposition, float cp, WeightedGraph graph) {
		List<ICluster> res = new ArrayList<ICluster>();
		if ((clusterComposition.getClusters().size() > 1)
				&& (cp < couplingMetric(clusterComposition.getClusters().get(0),
						clusterComposition.getClusters().get(1)))) {
			res.add(clusterComposition);
		}
		if (res.size() < graph.getvertexList().size()) {
			res.addAll(filterWithCp(clusterComposition.getClusters().get(0), cp, graph));
			res.addAll(filterWithCp(clusterComposition.getClusters().get(1), cp, graph));
		}
		return res;
	}

}
