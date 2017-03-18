package test;

public class Edge {
	int startNode;
	int endNode;
	double weight;
	
	public Edge(int startNode, int endNode, double weight) {
		this.startNode = startNode;
		this.endNode = endNode;
		this.weight = weight;
	}
	
	public double getWeight() {
		return this.weight;
	}
}
