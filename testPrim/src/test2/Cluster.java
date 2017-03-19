package test2;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
	private int Id;
	List<Node> nodes = new ArrayList<Node>();
	double [] centroid;
	public Cluster(int Id){
		this.Id = Id;
	}
	
	public void addNode(Node node){
		this.nodes.add(node);
	}
	
	public List<Node> getNodes(){
		return this.nodes;
	}
	
	public void calculateCentroid(){
		double[] sumDistance = {0,0,0};
		int rSum = 0;
		int gSum = 0;
		int bSum = 0;
		for (Node n : this.nodes){
			int [] rgbValues = n.rgb_values;
			rSum += rgbValues[0];
			gSum += rgbValues[1];
			bSum += rgbValues[2];
		}
		
		sumDistance[0] = rSum/this.nodes.size();
		sumDistance[1] = gSum/this.nodes.size();
		sumDistance[2] = bSum/this.nodes.size();
		
		this.centroid = sumDistance;
	}
	
	public double[] getCentroid(){
		return this.centroid;
	}
	
	public int getId(){
		return this.Id;
	}
}
