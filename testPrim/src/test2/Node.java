package test2;

import java.util.ArrayList;
import java.util.List;

public class Node {
	int[] rgb_values = {0,0,0};
	int id;
	ArrayList<Node> neighbors = new ArrayList<Node>();
	List<Edge> outEdges = new ArrayList<Edge>();
	List<Node> children = new ArrayList<Node>();
	int counter = 0;
	Node parent;
	int sum = 0;
	int segment = -1;
	
	public void setParent(Node p){
		this.parent = p;
	}
	
	public void removeChild(Node child){
		this.children.remove(child);
	}
	
	public Node getParent(){
		return this.parent;
	}
	
	public void addSum(int i) {
		this.sum += i;
	}
	
	public int getSum() {
		return this.sum;
	}

	public Node(int i, int j, int cols) {
		this.id = (i*cols)+(j+1);
	}
	public void addEdge(Edge edge){
		this.outEdges.add(edge);
	}
	public List<Edge> getEdges(){
		return this.outEdges;
	}
	
	public void addChild(Node node){
		this.children.add(node);
	}
	public List<Node> getChildren(){
		return this.children;
	}
	
	public void add_neighbor(Node node) {
		neighbors.add(node);
	}
	
	public void set_values(int[] rgb) {
		rgb_values = rgb;
	}
	
	public Node get_neighbor(int i) {
		return neighbors.get(i);
	}
	
	public void segment(int i) {
		this.segment = i;
	}
	
	public int getSegment() {
		return this.segment;
	}
	
	public Node getNearestNeigh() {
		Node bestNode = neighbors.get(0);
		for (Node node : this.neighbors) {
			if (Main.distance(this, node) < Main.distance(this, bestNode)) {
				bestNode = node;
			}
		}
		return bestNode;
	}
}
