package src.bio3;

import java.util.ArrayList;

public class Chromosome {
	
	int[] representation;
	ArrayList<Individual> individuals = new ArrayList<Individual>();
	
	public int fitness(){
		return 0;
	}
	
	public void addIndividual(Individual ind) {
		this.individuals.add(ind);
	}
}
