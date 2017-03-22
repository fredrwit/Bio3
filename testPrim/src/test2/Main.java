package test2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import javax.imageio.ImageIO;


public class Main {
	
	public static String IMAGE = "Test image/4/test image.jpg";

	public static BufferedImage get_image(String fileName) {
		BufferedImage img = null;

	    try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return img;
	}
	
	public static BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
        BufferedImage scaledImage = null;
        if (imageToScale != null) {
            scaledImage = new BufferedImage(dWidth, dHeight, imageToScale.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
        	graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
            graphics2D.dispose();
        }
        return scaledImage;
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
	
	public static void colorAndWrite(Graph graph, int type, int num) {
		BufferedImage img = get_image(IMAGE);
		img = scale(img, (int) (img.getWidth()*0.5), (int) (img.getHeight()*0.5));
		
		if (type == 1) {
			Color color = new Color(0,0,0);
			Color background = new Color(255,255,255);
			for (int i = 0; i < graph.rows; i++) {
				for (int j = 0; j < graph.cols; j++) {
					img.setRGB(j,i, background.getRGB());
					for (Node neighbor : graph.nodes[i][j].neighbors) {
						if (neighbor.getSegment() != graph.nodes[i][j].getSegment()) {
							img.setRGB(j,i, color.getRGB());					
						}
					}
				}
			}
			
		}
		else if (type == 2) {
			Color color = new Color(0,255,0);
			for (int i = 0; i < graph.rows; i++) {
				for (int j = 0; j < graph.cols; j++) {
					for (Node neighbor : graph.nodes[i][j].neighbors) {
						if (neighbor.getSegment() != graph.nodes[i][j].getSegment()) {
							img.setRGB(j,i, color.getRGB());
			
							
						}
					}
				}
			}
		}
		try {
			img = scale(img, (int) (img.getWidth()/0.5), (int) (img.getHeight()/0.5));
			File f = new File(type + "-" + num + ".jpg");
			ImageIO.write(img, "jpg", f);
		} catch (IOException e) {
			System.out.println("IMAGE FAILED TO BE WRITTEN!");
		}
	}
	
	public static void colorBigAndWrite(Graph graph, int type, int num) {
		BufferedImage img = get_image(IMAGE);
		if (type == 1) {
			Color color = new Color(0,0,0);
			Color background = new Color(255,255,255);
			for (int i = 0; i < img.getHeight(); i++) {
				for (int j = 0; j < img.getWidth(); j++) {
					img.setRGB(j,i, background.getRGB());
				}
			}
			for (int i = 0; i < graph.rows; i++) {
				for (int j = 0; j < graph.cols; j++) {
					for (Node neighbor : graph.nodes[i][j].neighbors) {
						if (neighbor.getSegment() != graph.nodes[i][j].getSegment()) {
							if ((j < graph.nodes[i].length && j > 1) && (i < graph.nodes[i].length && i > 1) ) {
								img.setRGB(j*2-1,i*2-1, color.getRGB());
								img.setRGB(j*2-1,i*2, color.getRGB());
								img.setRGB(j*2,i*2-1, color.getRGB());
								img.setRGB(j*2,i*2, color.getRGB());
							}
						}
					}
				}
			}
		}
		else if (type == 2) {
			Color color = new Color(0,255,0);
			for (int i = 0; i < graph.rows; i++) {
				for (int j = 0; j < graph.cols; j++) {
					for (Node neighbor : graph.nodes[i][j].neighbors) {
						if (neighbor.getSegment() != graph.nodes[i][j].getSegment()) {
							if ((j < graph.nodes[i].length && j > 1) && (i < graph.nodes[i].length && i > 1) ) {
								img.setRGB(j*2-1,i*2-1, color.getRGB());
								img.setRGB(j*2-1,i*2, color.getRGB());
								img.setRGB(j*2,i*2-1, color.getRGB());
								img.setRGB(j*2,i*2, color.getRGB());
							}				
						}
					}
				}
			}
		}
		try {
			File f = new File(type + "-" + num + ".jpg");
			ImageIO.write(img, "jpg", f);
		} catch (IOException e) {
			System.out.println("IMAGE FAILED TO BE WRITTEN!");
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
		double tull = mst.stream().max(Comparator.comparing(Edge::getWeight)).get().weight*0.1;
		List<Chromosome> pop = new ArrayList<Chromosome>();
		for (int i = 1; i < size+1; i++ ) {
			ArrayList<Edge> tempMST = new ArrayList<Edge>(mst);
			ArrayList<Edge> candidates = new ArrayList<Edge>();
			for (Edge e : mst) {
				if (e.weight > tull) {
					candidates.add(e);
				}
			}
			int removed = 0;
			while (removed < (i-1)) {
				tempMST.remove(candidates.get(new Random().nextInt(candidates.size())));
				//tempMST.remove(tempMST.indexOf(tempMST.stream().max(Comparator.comparing(Edge::getWeight)).get()));
				removed++;
			}
			pop.add(generateChromosome(tempMST, graph));
		}
		return pop;
	}
	
	public static List<Chromosome> initPop2 (List<Edge> mst, Graph graph, int size) {
		Random rand = new Random();
		int lol;
		List<Chromosome> pop = new ArrayList<Chromosome>();
		for (int i = 1; i < size+1; i++ ) {
			lol = rand.nextInt(10)+15;
			List<Edge> tempMST = cut(mst, graph, lol);
			pop.add(generateChromosome(tempMST, graph));
		}
		return pop;
	}
	
	public static List<Edge> cut(List<Edge> mst, Graph graph, int cuts) {
		int clusterSize = graph.getSize()/cuts;
		List<Edge> tempMST = new ArrayList<Edge>(mst);
		for (int j = 0; j < cuts; j++) {
			for (int i = tempMST.size()-1; i >= 0; i--) {
				if (graph.getNode(tempMST.get(i).endNode).getSum() > clusterSize*0.6 && graph.getNode(tempMST.get(i).endNode).getSum() < clusterSize*1.4){
					graph.getNode(tempMST.get(i).endNode).setParent(null);
					graph.getNode(tempMST.get(i).startNode).getChildren().remove(graph.getNode(tempMST.get(i).endNode));
					int deduct = -graph.getNode(tempMST.get(i).startNode).getSum();
					updateParent(graph.getNode(tempMST.get(i).startNode), deduct);
					tempMST.remove(i);
				}
			}
		}
		return tempMST;		
	}
	
	public static void updateParent(Node n,int deduct){
		
		if (n.getParent()== null){
			n.addSum(deduct);
			return;
		}
		else{
			updateParent(n.getParent(), deduct);
		}
		n.addSum(deduct);
		return;
		
	}
	
	public static void dfs(List<Edge> mst, Graph graph, boolean b){
		
		if (b){
			for (Edge e: mst){
				graph.getNode(e.startNode).addEdge(e);
			}
		}
		else{
			for(Edge e : mst){
				graph.getNode(e.startNode).counter = 0;
				graph.getNode(e.startNode).sum = 0;
			}	
		}
		
		Deque<Node> s = new ArrayDeque<Node>();
		Node[] visited = new Node[graph.getSize()];
		s.push(graph.getNode(mst.get(0).startNode));
		Node current;
		while(!s.isEmpty()){
			current = s.pop();

			if(visited[current.id-1]!= null){
				continue;
			}
			
			visited[current.id-1] = current;
			current.addSum(1);
			for (int i = 0; i<current.getEdges().size(); i++){

				if(visited[(current.getEdges().get(i).endNode)-1] == null ){
					s.push(graph.getNode(current.getEdges().get(i).endNode));
					current.addChild(graph.getNode(current.getEdges().get(i).endNode));
					graph.getNode(current.getEdges().get(i).endNode).setParent(current);
				}
			}
		}

		calcSum(graph.getNode(mst.get(0).startNode));
		
	}
	
	public static void calcSum(Node n){
		
		if (n.getChildren().size() == n.counter){
			n.getParent().addSum(n.getSum());
			return;
		}
		else{
			for (Node child : n.getChildren()){

				n.counter +=1;
				calcSum(child);		
			}
		}
		if(n.getParent()!= null){
			
			n.getParent().addSum(n.getSum());
		}
		return;	
	}
	
	
	
	public static void runNSGA2(List<Chromosome> pop, Graph graph, int generation, BufferedImage pixels) {
		NSGA.calcObj(graph, pop);
//		for (Chromosome chrom: pop) {
//			System.out.println("dev: " + chrom.overallDeviation);
//			System.out.println("edge: "+ chrom.edge);
//			System.out.println("conn: "+chrom.connectivity);
//			System.out.println();
//		}
//		System.exit(0);
		boolean[] objectives = {false,false,true,false};
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
			Map<Integer, List<Chromosome>> lastFront = NSGA.fnds(pop, objectives);
			NSGA.crowdingDist(lastFront, objectives);
			System.out.println("Segments in best chromosome: " + decode(NSGA.getBestChrom(pop, lastFront), graph));
			//colorAndWrite(graph, 1, g);
			//colorAndWrite(graph, 2, g);
			colorBigAndWrite(graph,1,g);
			colorBigAndWrite(graph,2,g);
		}
		Map<Integer, List<Chromosome>> lastFronts = NSGA.fnds(pop, objectives);
		NSGA.crowdingDist(lastFronts, objectives);
		Plot.plot2d(lastFronts);
		//return NSGA.getBestChrom(pop, lastFront);
		System.exit(0);
	}
	
	
	public static void main(String[] args) {		
		
		BufferedImage pixels = get_image(IMAGE);
		pixels = scale(pixels, (int) (pixels.getWidth()*0.5), (int) (pixels.getHeight()*0.5));
		
		Graph graph = new Graph(pixels.getHeight(),pixels.getWidth());
		initGraph(graph,pixels);		
		initWeights(graph);
		List<Edge> mst = prims(graph);

		dfs(mst, graph,true);
//		List<Edge> a = test(mst,graph,5);
//		
//		Chromosome chrom = generateChromosome(a, graph);
//		
//		
//		decode(chrom,graph);
//		for (Cluster cluster : chrom.getClusters()) {
//			System.out.println(cluster.nodes.size());
//		}
//		colorEdges(graph);
//		writeImage(graph, pixels, "loly", 500);
//		System.exit(0);
		
		List<Chromosome> pop2 = initPop2(mst, graph, 50);
		
		//initialPop pop2 = new initialPop(graph,pixels);
		
//		for (int p = 0; p < pop2.size(); p++) {
//			decode(pop2.get(p), graph);
//			//System.out.println(pop.get(q).overallDeviation);
//			//System.out.println(pop.get(q).edge);
//			//System.out.println(pop.get(q).connectivity);
//			//System.out.println();
//
//				colorEdges(graph);
//				writeImage(graph, pixels, "loly", p);
//				resetImage(graph);
//				
//			
//		}
		runNSGA2(pop2, graph, 100, pixels);
		
		
//		int num_seg = decode(bestChrom, graph);
//		colorEdges(graph);
//		writeImage(graph, pixels, "loly", (0));
//		for (int q = 0; q < pop2.size(); q++) {
//			num_seg = decode(pop2.get(q), graph);
//			System.out.println(pop.get(q).overallDeviation);
//			System.out.println(pop.get(q).edge);
//			System.out.println(pop.get(q).connectivity);
//			System.out.println();
//
//				colorEdges(graph);
//				writeImage(graph, pixels, "loly", (q+50));
//			
//		}
//		int[] counter = new int[num_seg];
//		for (int i = 0; i < graph.rows; i++) {
//			for (int j = 0; j < graph.cols; j++) {
//				counter[graph.nodes[i][j].getSegment()-1] += 1;
//			}
//		}
//		System.out.println(Arrays.toString(counter));
//		
//		
//		
//		System.out.println("Done");

	}

}
