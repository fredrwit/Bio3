package test;

import java.util.ArrayList;

public class Node {
	int[] rgb_values = {0,0,0};
	String id;
	ArrayList<Node> neighbors = new ArrayList<Node>();

	public Node(String i, String j) {
		this.id = i + j;
	}
	
	public void add_neighbor(Node node) {
		neighbors.add(node);
	}
	
	public void set_values(int[] rgb) {
		rgb_values = rgb;
	}
	
	
}
