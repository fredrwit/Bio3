package src.bio3;

import java.util.ArrayList;

public class Population {
	private ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();
	
	public void add(Chromosome chrom) {
		chromosomes.add(chrom);
	}
}
