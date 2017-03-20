package test2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;


public class initialPop {
	
	int size;
	int popSize = 50;
	Graph graph;
	List<Chromosome> population;
	List<Integer> genoType;
	Chromosome chrom;
	BufferedImage pixel;
	
	public initialPop(Graph pixelGraph, BufferedImage pixels){
		
		this.graph = pixelGraph;
		this.pixel = pixels;
		this.population = new ArrayList<Chromosome>();
		
		createPopulation();
	}

	
	public void createPopulation(){
		
		while(this.population.size() < popSize){
			List<Integer> chromRep = new ArrayList<Integer>();
			this.chrom = new Chromosome(this.graph.getSize());
			
			for (int i = 1; i <= this.graph.getSize(); i++){
				chromRep.add(i);
			}
			Collections.shuffle(chromRep);
			
			for (int i = 1; i <= this.graph.getSize(); i++){
				chrom.update(i-1, chromRep.get(i-1));
			}
			this.population.add(chrom);	
		}
		for (int i = 1; i <= this.population.size(); i++){
			int chromClusters = decode(this.population.get(i-1), this.graph);
			this.population.get(i-1).setNumClusters(chromClusters);
			System.out.println(chromClusters);
		}
	}
		
	public static int decode(Chromosome chrom, Graph graph) {
		int currentCluster = 1;
		for (int i = 1; i < chrom.getSize()+1; i++) {
			graph.getNode(i).segment(-1);
			}
		for (int i = 1; i < chrom.getSize()+1; i++) {
			int ctr = 1;
			if (graph.getNode(i).getSegment() == -1) {
				graph.getNode(i).segment(currentCluster);
				int neighbor = chrom.getRepr()[i-1];
				List<Integer> previousCtr = new ArrayList<Integer>();
				previousCtr.add(i);
				ctr++;
				while (graph.getNode(neighbor).getSegment() == -1) {
					previousCtr.add(neighbor);
					graph.getNode(neighbor).segment(currentCluster);
					neighbor = chrom.getRepr()[neighbor-1];
					ctr++;
				}
				if (graph.getNode(neighbor).getSegment() != currentCluster) {
					ctr--;
					while (ctr >= 1) {
						graph.getNode(previousCtr.get(ctr-1)).segment(graph.getNode(neighbor).getSegment());
						ctr--;
					}
				}
				else {
					currentCluster++;
				}
			}
		}
		return currentCluster-1;
	}

	public static void colorEdges(Graph graph) {
		int[] rgb = {0,255,0};
		for (int i = 0; i < graph.rows; i++) {
			for (int j = 0; j < graph.cols; j++) {
				for (Node neighbor : graph.nodes[i][j].neighbors) {
					if (neighbor.getSegment() != graph.nodes[i][j].getSegment()) {
						graph.nodes[i][j].set_values(rgb);
					}
				}
			}
		}
	}
	
	public static void writeImage(Graph graph, 
			BufferedImage image, String filename, int num) {
		 
		for (int i = 0; i < graph.rows; i++) {
			for (int j = 0; j < graph.cols; j++) {
				Color color = new Color(graph.nodes[i][j].rgb_values[0],graph.nodes[i][j].rgb_values[1],graph.nodes[i][j].rgb_values[2]);
				image.setRGB(j,i, color.getRGB());
			}
		}

		try {
			String newName = filename.substring(0, filename.length() - 4);
			File f = new File(newName + num + ".jpg");
			ImageIO.write(image, "jpg", f);
		} catch (IOException e) {
			System.out.println("IMAGE FAILED TO BE WRITTEN!");
		}
		        
	}
		
}
