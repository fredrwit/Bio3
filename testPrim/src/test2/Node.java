package test2;

import java.util.ArrayList;

public class Node {
	int[] rgb_values = {0,0,0};
	int id;
	ArrayList<Node> neighbors = new ArrayList<Node>();
	int segment = -1;

	public Node(int i, int j, int cols) {
		this.id = (i*cols)+(j+1);
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
