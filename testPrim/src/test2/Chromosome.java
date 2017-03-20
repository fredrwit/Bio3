package test2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chromosome {
	
	private int[] representation;
	private List<Cluster> clusters = new ArrayList<Cluster>();
	private int numClusters;
	public double overallDeviation;
	public double connectivity;
	public double edge;
	public int rank;
	public double crowdingDist;

	public double getCrowd() {
		return this.crowdingDist;
	}
	
	public double getDev() {
		return this.overallDeviation;
	}
	
	public double getEdge() {
		return this.edge;
	}
	
	public double getConn() {
		return this.connectivity;
	}
	
	public Chromosome(int size) {
		this.representation = new int[size]; 
	}
	
	public void setOverallDeviation(double dev){
		
		this.overallDeviation = dev;
	}
	public void setConnectivity(double con){
		
		this.connectivity = con;
	}
	public void setEdge(double edge){
		
		this.edge = edge;
	}
	
	public void addCluster(Cluster c){
		this.clusters.add(c);
	}
	
	public void setNumClusters(int n){
		this.numClusters = n;
	}
	
	public List<Cluster> getClusters(){
		return this.clusters;
	}
	
	public void update(int i, int j) {
		this.representation[i] = j;
	}
	
	public int[] getRepr() {
		return this.representation;
	}
	
	public void printRepr() {
		System.out.println(Arrays.toString(representation));
	}
	
	public int getSize() {
		return this.representation.length;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "(" + Double.toString(this.edge) + ", " + Double.toString(this.connectivity) + ")";
	}
}
