package test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.spec.EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	
	public static void initUndirectedGraph(UndirectedGraph<Node> undirectedGraph, Graph graph) {
		System.out.println("Initializing the undirected graph with edges...");
		for (int i = 0; i < graph.nodes.length; i++) {
			for (int j = 0; j < graph.nodes[i].length; j++) {
				undirectedGraph.addNode(graph.nodes[i][j]);
			}
		}
		for (int i = 0; i < graph.nodes.length; i++) {
			for (int j = 0; j < graph.nodes[i].length; j++) {
				for (int k = 0; k < graph.nodes[i][j].neighbors.size(); k++) {
					Double length = distance(graph.nodes[i][j],graph.nodes[i][j].get_neighbor(k));
					undirectedGraph.addEdge(graph.nodes[i][j], graph.nodes[i][j].get_neighbor(k), length);
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
	
	public static ArrayList<Chromosome> initialPopulation(UndirectedGraph<Node> graph, Graph bgraph, int size) {
		System.out.println("Creating initial population...");
		ArrayList<Chromosome> population = new ArrayList<Chromosome>();
		Map<Double, List<Node>> edges = new HashMap<Double, List<Node>>();
		List<List<Double>> edgeValues = new ArrayList<List<Double>>();
		Iterator<Entry<Node, Map<Node, Double>>> iterator = graph.entryIterator();
		Double id = 0.0;
		while (iterator.hasNext()) {
			Entry<Node, Map<Node, Double>> entry = iterator.next();
			Entry<Node, Double> maxEntry = entry.getValue().entrySet().stream().max(Map.Entry.comparingByValue()).get();
			edgeValues.add(new ArrayList<Double>(Arrays.asList(id,maxEntry.getValue())));
			edges.put(id, Arrays.asList(entry.getKey(),maxEntry.getKey()));
			id++;
		}
		Collections.sort(edgeValues, new Comparator<List<Double>>()
		{
		  public int compare(List<Double> o1, List<Double> o2)
		  {
		    return o2.get(1).compareTo(o1.get(1));
		  }
		});
		
		UndirectedGraph<Node> tempGraph;
		
		for (int i = 1; i < size+1; i++) {
			tempGraph = new UndirectedGraph<Node>(graph);

			int j = 1;
			int removed = 0;
			while (removed < i-1) {	
				tempGraph.removeEdge(edges.get(edgeValues.get(j-1).get(0)).get(0),edges.get(edgeValues.get(j-1).get(0)).get(1));
				if (edges.get(edgeValues.get(j-1).get(0)).get(0) == edges.get(edgeValues.get(j).get(0)).get(1) && edges.get(edgeValues.get(j-1).get(0)).get(1) == edges.get(edgeValues.get(j).get(0)).get(0)) {
					j++;
				}
				removed++;
				j++;
				
			}
			
			Chromosome newChrom = new Chromosome(graph.size());
			
//			for (int k = 1; k < graph.size()+1; k++) {
//				Iterator<Entry<Node, Double>> newIterator = graph.edgesFrom(bgraph.getNode(k)).entrySet().iterator();
//				while (newIterator.hasNext()) {
//					
//				}
//				
//				
//				if (newChrom.getRepr()[k-1] == 0) {
//					newChrom.update(k,bgraph.getNode(k).id);
//						
//					Iterator<Entry<Node, Double>> newIterator = graph.edgesFrom(bgraph.getNode(k)).entrySet().iterator();
//					while (newIterator.hasNext()) {
//						
//					}
//				}
//				
//				
//			}
			
			Iterator<Entry<Node, Map<Node, Double>>> nodeIterator = tempGraph.entryIterator();
			while (nodeIterator.hasNext()) {
				Entry<Node, Map<Node, Double>> node = nodeIterator.next();
				Iterator<Entry<Node, Double>> edgeIterator = node.getValue().entrySet().iterator();
				if (node.getValue().size() > 0) {
					while (edgeIterator.hasNext()) {
						Entry<Node, Double> edge = edgeIterator.next();
						if (newChrom.getRepr()[edge.getKey().id-1] == node.getKey().id) {
							if (edgeIterator.hasNext()) {
								continue;
							}
							else {
								newChrom.update(node.getKey().id-1,node.getKey().id);
							}
						}
						else {
							newChrom.update(node.getKey().id-1,edge.getKey().id);
						}
					}
				}
				else {
					newChrom.update(node.getKey().id-1,node.getKey().id);
					
				}
			}
			population.add(newChrom);
		}
		return population;
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
	
	public static Object[] crossover(Chromosome p1, Chromosome p2) {
		Random rand = new Random();
		
		Chromosome c1 = new Chromosome(p1.getSize());
		Chromosome c2 = new Chromosome(p2.getSize());
		for (int i = 0; i < p1.getSize(); i++) {
			if (rand.nextDouble() < 0.5) {
				c1.update(i, p1.getRepr()[i]);
			}
			else {
				c1.update(i, p2.getRepr()[i]);
			}
			if (rand.nextDouble() < 0.5) {
				c2.update(i, p1.getRepr()[i]);
			}
			else {
				c2.update(i, p2.getRepr()[i]);
			}
			
		}
		return new Object[]{c1, c2};
	}
	
	public static Chromosome mutate(Chromosome chrom, Graph graph) {
		Random rand = new Random();
		int randNode = rand.nextInt(chrom.getSize())+1;
		Node nearestNeigh = graph.getNode(randNode).getNearestNeigh();
		chrom.update(randNode-1, nearestNeigh.id);
		return chrom;
	}
	
	public static void runSPEA2() {
		int generations = 0;
		
	}
	
	public static ArrayList<Edge> prims(Graph graph) {
        boolean[] added = new boolean[graph.getSize()];
		ArrayList<Edge> mst = new ArrayList<>();
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
	
	public static Chromosome encode(List<Edge> mst, Graph graph) {
		Chromosome chrom = new Chromosome(graph.getSize());
		for (Edge edge : mst) {
			chrom.update(edge.startNode-1, edge.endNode);
		}
		return chrom;
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
	
	public static Chromosome generateChromosome(ArrayList<Edge> mst, Graph graph){
        Chromosome chrom = new Chromosome(graph.getSize());
        for (Edge edge : mst) {
            if (chrom.getRepr()[edge.startNode-1] != 0){
                chrom.getRepr()[edge.endNode-1] = edge.startNode;
            }else{
                chrom.getRepr()[edge.startNode-1] = edge.endNode;
            }
        }
        for (int i = 0; i < graph.getSize(); i++) {
            if (chrom.getRepr()[i] == 0){
                chrom.getRepr()[i] = i;
            }
        }
        return chrom;
    }
	
	
	public static void main(String[] args) {		
		BufferedImage pixels = get_image("Test image/1/Test image.jpg");
		Graph graph = new Graph(pixels.getHeight(),pixels.getWidth());
		initGraph(graph,pixels);
		initWeights(graph);
		UndirectedGraph<Node> undirectedGraph = new UndirectedGraph<Node>();
		initUndirectedGraph(undirectedGraph, graph);
		UndirectedGraph<Node> mst2 = Prim.mst(undirectedGraph);
		ArrayList<Edge> mst = prims(graph);
		System.out.println(decode(generateChromosome(mst, graph), graph));
		
//		double sum = 0;
//		for (Edge edge : mst) {
//			System.out.println(edge.startNode + " " + edge.endNode + " " + edge.getWeight());
//			sum += edge.getWeight();
//		}
//		System.out.println(sum);
//		System.out.println(mst.size());
//		
//		System.out.println(Arrays.toString(encode(mst, graph).getRepr()));
		
//		Iterator<Entry<Node, Map<Node, Double>>> lool = mst2.entryIterator();
//		Double sum2 = 0.0;
//		while (lool.hasNext()) {
//			Entry<Node, Map<Node, Double>> entry = lool.next();
//			sum2 += entry.getValue().values().stream().mapToDouble(i -> i).sum();
//		}
//		System.out.println(sum2/2);
		
		ArrayList<Chromosome> initPop = initialPopulation(mst2,graph, 1);
	
//		for (int z = 0; z < initPop.get(0).getRepr().length; z++) {
//			if (initPop.get(2).getRepr()[z] != initPop.get(3).getRepr()[z]) {
//				System.out.println("Success!");
//			}
//		}
		
		for (int k = 0; k < initPop.size(); k++) {
			
//			System.out.println(Arrays.toString(initPop.get(k).getRepr()));
			System.out.println(decode(initPop.get(k), graph));
//			for (int a = 0; a < graph.getSize(); a++) {
//				System.out.println("Node: " + (a+1) + " has segment: " + graph.getNode(a+1).getSegment());
//			}
			colorEdges(graph);
			writeImage(graph, pixels, "loly", k);
		}

		System.out.println("Done");

	}

}
