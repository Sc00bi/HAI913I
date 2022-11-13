package models;

// class implemented to create weighted edges
public class WeightedEdge {
	/* ATTRIBUTES*/
    String vertex1;
    String vertex2;
    int weight;

    
    /* CONSTRUCTOR */
    public WeightedEdge(String vertex1, String vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = 1;
    }
    
    
    // increments edge's weight
    public void incrementWeight() {
        this.weight++;
    }

    /* Getters and Setters */
    public int getWeight() {
        return weight;
    }

    public String getVertex1() {
        return vertex1;
    }

    public String getVertex2() {
        return vertex2;
    }
    
    @Override
    public String toString()
    {
    	return "{" + vertex1 + "-" + weight + "-" + vertex2 + "}";
    }
}
