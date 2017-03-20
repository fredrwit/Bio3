package test2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import javax.imageio.ImageIO;


public class Main {

	public static BufferedImage get_image(String fileName) {
		BufferedImage img = null;

	    try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return img;
	}
	
	public static void initGraph(Graph graph, BufferedImage pixels) {
		System.out.println("Calculating colors and linking neighbors for each node...");
		Node neighbor;
		Node currentNode;
		for (int i = 0; i < pixels.getHeight(); i++) {
			for (int j = 0; j < pixels.getWidth(); j++) {
				currentNode = graph.nodes[i][j];
				int[] rgb = {((pixels.getRGB(j,i) >> 16) & 0xFF),((pixels.getRGB(j,i) >> 8) & 0xFF),(pixels.getRGB(j,i) & 0xFF)};
				currentNode.set_values(rgb);
				
				if (i > 0) {
					neighbor = graph.nodes[i-1][j];
					currentNode.add_neighbor(neighbor);
				}
				
				if (j > 0) {
					neighbor = graph.nodes[i][j-1];
					currentNode.add_neighbor(neighbor);
				}
				
				if (j < (pixels.getWidth() - 1)) {
					neighbor = graph.nodes[i][j+1];
					currentNode.add_neighbor(neighbor);
				}
				
				if (i < (pixels.getHeight()- 1)) {
					neighbor = graph.nodes[i+1][j];
					currentNode.add_neighbor(neighbor);
				}
				
			}
		}
	}
	
	public static double distance(Node one, Node two) {
        double square = Math.pow(one.rgb_values[0] - two.rgb_values[0], 2) 
            + Math.pow(one.rgb_values[1] - two.rgb_values[1], 2) 
            + Math.pow(one.rgb_values[2] - two.rgb_values[2], 2);
        return Math.pow(square, 0.5);
    }
	
	public static int decode(Chromosome chrom, Graph graph) {
		chrom.setCluster();
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
		Map<Integer,Cluster> clusters = new HashMap<Integer, Cluster>();
		for (int i = 1; i < graph.getSize()+1; i++) {
			if (clusters.containsKey(graph.getNode(i).getSegment())) {
				clusters.get(graph.getNode(i).getSegment()).addNode(graph.getNode(i));
			}
			else {
				clusters.put(graph.getNode(i).getSegment(),new Cluster(graph.getNode(i).getSegment()));
				clusters.get(graph.getNode(i).getSegment()).addNode(graph.getNode(i));
			}
		}
		for (Cluster cluster : clusters.values()) {			
			cluster.calculateCentroid();
			chrom.addCluster(cluster);
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
	
	public static void generateChildren(List<Chromosome> parents) {
		List<Chromosome> children = new ArrayList<Chromosome>();
		while (children.size() < parents.size()) {
			
			
		}
		
	}
	
	public static List<Edge> prims(Graph graph) {
        boolean[] added = new boolean[graph.getSize()];
		List<Edge> mst = new ArrayList<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>((Object o1, Object o2) ->{
            Edge first = (Edge) o1;
            Edge second = (Edge) o2;
            if (first.getWeight() - second.getWeight() > 0) {
                return 1;
            } else if (first.getWeight() - second.getWeight() == 0) {
                return 0;
            } else {
                return -1;
            }
        });
        for (Edge edge : graph.edgesFromNode.get(0)) {
            pq.add(edge);

        }
        added[0] = true;
        while (!pq.isEmpty()) {
            Edge e = pq.peek();
            pq.poll();
            if(added[e.startNode-1] && added[e.endNode-1])continue;
            added[e.startNode-1] = true;
            for(Edge edge : graph.edgesFromNode.get(e.endNode-1)){
                if(!added[edge.endNode-1]){
                    pq.add(edge);
                }
            }
            added[e.endNode-1] = true;
            mst.add(e);
        }
        return mst;
    }
	
	public static void initWeights(Graph graph) {
		double weight;
		for (int i = 0; i < graph.getSize(); i++) {
			graph.edgesFromNode.add(new ArrayList<Edge>());
			for (int n = 0; n < graph.getNode(i+1).neighbors.size(); n++) {
				weight = distance(graph.getNode(i+1),graph.getNode(i+1).get_neighbor(n));
				graph.edgesFromNode.get(i).add(new Edge(graph.getNode(i+1).id, graph.getNode(i+1).get_neighbor(n).id,weight));
			}
		}
	}
	
	public static Chromosome generateChromosome(List<Edge> mst, Graph graph){
        Chromosome chrom = new Chromosome(graph.getSize());
        for (Edge edge : mst) {
            if (chrom.getRepr()[edge.startNode-1] != 0){	
            	int endNode = edge.endNode;
            	int prevEnd = edge.endNode;
            	int lastEnd = edge.endNode;
            	while (chrom.getRepr()[endNode-1] != 0) {
            		endNode = chrom.getRepr()[chrom.getRepr()[endNode-1]-1];
            		chrom.getRepr()[chrom.getRepr()[prevEnd-1]-1] = prevEnd;
            		prevEnd = endNode;
            		if (prevEnd == 0) {
            			break;
            		}
            	}
            	chrom.getRepr()[lastEnd-1] = edge.startNode;
            }else{
                chrom.getRepr()[edge.startNode-1] = edge.endNode;
            }
        }
        for (int i = 0; i < graph.getSize(); i++) {
            if (chrom.getRepr()[i] == 0){
                chrom.getRepr()[i] = i+1;
            }
        }
        return chrom;
    }
	
	public static List<Chromosome> initPop (List<Edge> mst, Graph graph, int size) {
		List<Chromosome> pop = new ArrayList<Chromosome>();
		for (int i = 1; i < size+1; i++ ) {
			ArrayList<Edge> tempMST = new ArrayList<Edge>(mst);
			int removed = 0;
			while (removed < (i-1)) {
				tempMST.remove(tempMST.indexOf(tempMST.stream().max(Comparator.comparing(Edge::getWeight)).get()));
				removed++;
			}
			pop.add(generateChromosome(tempMST, graph));
		}
		return pop;
	}
	
	public static void runNSGA2(List<Chromosome> pop, Graph graph, int generation) {
		NSGA.calcObj(graph, pop);
		for (Chromosome chrom: pop) {
			System.out.println("dev: " + chrom.overallDeviation);
			System.out.println("edge: "+ chrom.edge);
			System.out.println("conn: "+chrom.connectivity);
			System.out.println();
		}
		System.exit(0);
		boolean[] objectives = {true,false,false,false};
		for (int g = 0; g < generation; g++) {
			System.out.println("Generation number: " + Integer.toString(g+1));
			Map<Integer, List<Chromosome>> initFronts = NSGA.fnds(pop, objectives);
			NSGA.crowdingDist(initFronts, objectives);
			List<Chromosome> children = NSGA.createOffspring(pop,graph);
			NSGA.calcObj(graph, children);
			List<Chromosome> R = new ArrayList<Chromosome>(pop);
			R.addAll(children);
			Map<Integer, List<Chromosome>> fronts = NSGA.fnds(R, objectives);
			NSGA.crowdingDist(fronts, objectives);
			List<Chromosome> newGen = new ArrayList<Chromosome>();
			for (int i : fronts.keySet()) {
				if (newGen.size()+fronts.get(i).size() <= pop.size()) {
					newGen.addAll(fronts.get(i));
				}
				else {
					fronts.get(i).sort(Comparator.comparing(Chromosome::getCrowd));
					int last = fronts.get(i).size()-1;
					while (newGen.size() < pop.size()) {
						newGen.add(fronts.get(i).get(last));
						last--;
					}
				}
			}
			pop = newGen;
		}
	}
	
	
	public static void main(String[] args) {		
		BufferedImage pixels = get_image("Test image/1/test image.jpg");
		Graph graph = new Graph(pixels.getHeight(),pixels.getWidth());
		initGraph(graph,pixels);
		initWeights(graph);
		List<Edge> mst = prims(graph);
		List<Chromosome> pop2 = initPop(mst, graph, 50);
		
		//initialPop pop2 = new initialPop(graph,pixels);
		
		for (int p = 0; p < pop2.size(); p++) {
			decode(pop2.get(p), graph);
			//System.out.println(pop.get(q).overallDeviation);
			//System.out.println(pop.get(q).edge);
			//System.out.println(pop.get(q).connectivity);
			//System.out.println();

				colorEdges(graph);
				writeImage(graph, pixels, "loly", p);
			
		}
		
		runNSGA2(pop2, graph, 25);
		
		int num_seg = 0;
		for (int q = 0; q < pop2.size(); q++) {
			num_seg = decode(pop2.get(q), graph);
			//System.out.println(pop.get(q).overallDeviation);
			//System.out.println(pop.get(q).edge);
			//System.out.println(pop.get(q).connectivity);
			//System.out.println();

				colorEdges(graph);
				writeImage(graph, pixels, "loly", (q+50));
			
		}
		int[] counter = new int[num_seg];
		for (int i = 0; i < graph.rows; i++) {
			for (int j = 0; j < graph.cols; j++) {
				counter[graph.nodes[i][j].getSegment()-1] += 1;
			}
		}
		System.out.println(Arrays.toString(counter));
		
		
		
		System.out.println("Done");

	}

}
