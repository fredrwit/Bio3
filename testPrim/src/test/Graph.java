package test;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	
	Node[][] nodes;
	int rows;
	int cols;
	List<List<Edge>> edgesFromNode = new ArrayList<List<Edge>>();
		
	public Graph(int rows, int cols) {
		System.out.println("Creating graph...");
		this.rows = rows;
		this.cols = cols;
		nodes = new Node[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				this.nodes[i][j] = new Node(i,j,cols);
			}
		}
	}
	
	public Node getNode(int id) {
		int i;
		int j;
		double d = (double) id / (double) (this.cols);
		if (d % 1 == 0) {
			i = (int) d -1;
		}
		else {
			i = (int) d;
		}
		j = id % this.cols;
		if (j == 0) {
			j = this.cols-1;
		}
		else {
			j = j-1;
		}
		return this.nodes[i][j];
	}
	
	public int getSize() {
		return this.cols*this.rows;
	}
}
