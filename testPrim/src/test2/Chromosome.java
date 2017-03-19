package test2;

import java.util.Arrays;

public class Chromosome {
	
	private int[] representation;
	
	public Chromosome(int size) {
		this.representation = new int[size]; 
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
}
