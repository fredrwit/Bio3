package src.bio3;

import java.io.IOException;
import java.util.Random;

public class Main {
	
	public int POPULATION_SIZE = 50;
	
	Problem problem;
	Population initPop;
	
	public void run(Problem prob) {
		problem = prob;
		initPopulation();
	}
	
	public double distance(Individual ind1, Individual ind2) {
		double deltaR = problem.pixels.getR(ind2.x, ind2.y) - problem.pixels.getR(ind1.x, ind1.y);
		double deltaG = problem.pixels.getG(ind2.x, ind2.y) - problem.pixels.getG(ind1.x, ind1.y);
		double deltaB = problem.pixels.getB(ind2.x, ind2.y) - problem.pixels.getB(ind1.x, ind1.y);
		return Math.sqrt(Math.pow(deltaR, 2) + Math.pow(deltaG, 2) + Math.pow(deltaB, 2));
	}
	
	public void initPopulation() {
		Population initPop = new Population();
		Chromosome chrom = new Chromosome();
		Random rand = new Random();
		int counter = 0;
		for (int i = 0; i < problem.sizeY; i++) {
			for (int j = 0; j < problem.sizeX; j++) {
				chrom.addIndividual(new Individual(counter,j,i));
				counter++;
			}
		}
		int randX = rand.nextInt(problem.sizeX);
		int randY = rand.nextInt(problem.sizeY);
		north, south, east, west 
		distance(individual1 , individual 2)
		add to quueue
		
		
		for (int i = 0; i < problem.sizeY; i++) {
			for (int j = 0 <)
		}
		
		
		
		for (int i = 0; i < POPULATION_SIZE; i++) {
			initPop.add(chrom);
		}
	}
	
	public static void main(String[] args) throws IOException {
		Problem test = new Problem("Test Image/2/Test image.jpg");
		
		
		
		System.out.println(test.pixels.getR(0, 480));
		System.out.println(test.pixels.getG(0, 480));
		System.out.println(test.pixels.getB(0, 480));
	}
}
