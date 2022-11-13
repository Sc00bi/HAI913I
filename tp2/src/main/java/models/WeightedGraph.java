package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// class implemented to create graph having weightedEdges
public class WeightedGraph {
	/* ATTRIBUTES */
	private List<String> vertexList;
	private List<WeightedEdge> weightedEdges;

	/* CONSTRUCTOR */
	public WeightedGraph() {
		vertexList = new ArrayList<>();
		weightedEdges = new ArrayList<>();
	}

	// adds vertex to our graph
	public void addVertex(String vertex) {
		if (!vertexList.contains(vertex)) {
			vertexList.add(vertex);
		}
	}

	// returns the edge found in the graph, or null if it doesn't exists
	public WeightedEdge getEdge(String vertex1, String vertex2) {
		Optional<WeightedEdge> result = weightedEdges.stream()
				.filter(edge -> edge.vertex1.equals(vertex1) && edge.vertex2.equals(vertex2)
						|| edge.vertex1.equals(vertex2) && edge.vertex2.equals(vertex1))
				.findFirst();
		if (result.isPresent()) {
			return result.get();
		}
		return null;
	}

	// adds an edge between vertex1 and vertex2 if it doesn't exist, else increments
	// its weight
	public void updateWeightedEdge(String Vertex1, String Vertex2) {
		WeightedEdge edge = this.getEdge(Vertex1, Vertex2);
		if (edge == null) {
			this.weightedEdges.add(new WeightedEdge(Vertex1, Vertex2));
		} else {
			edge.incrementWeight();
		}
	}

	// adds newVertex if it doesn't exist
	public void updateWeightedEdge(String newVertex) {
		if (!vertexList.contains(newVertex)) {
			this.addVertex(newVertex);
		}
	}

	// returns the coupling metric between two classes, A and B
	public float couplingClasses(String classA, String classB) {
		float couplingMetric = 0;
		WeightedEdge edge = getEdge(classA, classB);
		if (edge != null) {
			int total = 0;
			for (WeightedEdge e : this.getWeightedEdges()) {
				if (e.getVertex1() == classA || e.getVertex1() == classB || e.getVertex2() == classB
						|| e.getVertex2() == classA) {
					total += e.getWeight();
				}
			}
			couplingMetric = (float) edge.getWeight() / (float) total;
		}
		return couplingMetric;
	}

	/* Getters */
	public List<String> getvertexList() {
		return vertexList;
	}

	public List<WeightedEdge> getWeightedEdges() {
		return weightedEdges;
	}

	// prints vertex from the graph
	public String printVertex() {
		String res = "L'ensemble des classes du graphe sont :\n";
		for (String className : vertexList) {
			res += className + "\n";
		}
		return res;
	}

	// print graph
	public String toString() {
		String res = printVertex();
		res += "Et l'ensembles des arrêtes {sommet1-poids-sommet2} sont :\n";
		for (WeightedEdge edge : weightedEdges) {
			res += edge.toString() + "\n";
		}
		return res;

	}

}
