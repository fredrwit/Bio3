package test2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NSGA {
	
	public static void calcObj(Graph graph, List<Chromosome> chromList){
		
		for (Chromosome chrom : chromList){
			
			double overallDeviation = 0;
			double edgeValue = 0.0;
			double connValue = 0.0;
			
			Main.decode(chrom, graph);
			
			for (int i = 0; i < graph.rows; i++){
				for (int j = 0; j < graph.cols; j++){
					double counter = 0;
					overallDeviation += distance(graph.nodes[i][j], chrom.getClusters().get(graph.nodes[i][j].segment-1).centroid); 
					for (Node neighbor : graph.nodes[i][j].neighbors){
						if (neighbor.getSegment() != graph.nodes[i][j].getSegment()) {
							edgeValue += Main.distance(graph.nodes[i][j], neighbor);
							counter += 1.0;
							connValue += 1.0/counter;
						}
					}
				}
			}
			edgeValue = -edgeValue;
			chrom.setEdge(edgeValue);	
			chrom.setConnectivity(connValue);
			chrom.setOverallDeviation(overallDeviation);
		}
	}
	
	private static double distance(Node one, double[] centroid ) {
        double square = Math.pow(one.rgb_values[0] - centroid[0], 2) 
            + Math.pow(one.rgb_values[1] - centroid[1], 2) 
            + Math.pow(one.rgb_values[2] - centroid[2], 2);
        return Math.pow(square, 0.5);
	}
	
	public static Map<Integer, List<Chromosome>> fnds(List<Chromosome> population, boolean[] objectives) {
		Map<Chromosome, List<Chromosome>> S = new HashMap<Chromosome,List<Chromosome>>();
		Map<Integer,List<Chromosome>> front = new HashMap<Integer,List<Chromosome>>();
		front.put(1, new ArrayList<Chromosome>());
		Map<Chromosome,Integer> n = new HashMap<Chromosome, Integer>();
		for (int p = 0; p < population.size(); p++) {
			List<Chromosome> s = new ArrayList<Chromosome>();
			n.put(population.get(p), 0);
			for (int q = 0; q < population.size(); q++ ) {
				if (p == q) {
					continue;
				}
				if (objectives[0]) {
					if (population.get(p).edge < population.get(q).edge && population.get(p).connectivity < population.get(q).connectivity) {
						s.add(population.get(q));
					}
					else if (population.get(q).edge < population.get(p).edge && population.get(q).connectivity < population.get(p).connectivity) {
						n.put(population.get(p), n.get(population.get(p))+1);
					}
				}
				else if (objectives[1]) {
					if (population.get(p).edge < population.get(q).edge && population.get(p).overallDeviation < population.get(q).overallDeviation) {
						s.add(population.get(q));
					}
					else if (population.get(q).edge < population.get(p).edge && population.get(q).overallDeviation < population.get(p).overallDeviation) {
						n.put(population.get(p), n.get(population.get(p))+1);
					}
				}
				else if (objectives[2]) {
					if (population.get(p).overallDeviation < population.get(q).overallDeviation && population.get(p).connectivity < population.get(q).connectivity) {
						s.add(population.get(q));
					}
					else if (population.get(q).overallDeviation < population.get(p).overallDeviation && population.get(q).connectivity < population.get(p).connectivity) {
						n.put(population.get(p), n.get(population.get(p))+1);
					}
				}
				else if (objectives[3]) {
					if (population.get(p).edge < population.get(q).edge && population.get(p).connectivity < population.get(q).connectivity && population.get(p).overallDeviation < population.get(q).overallDeviation) {
						s.add(population.get(q));
					}
					else if (population.get(q).edge < population.get(p).edge && population.get(q).connectivity < population.get(p).connectivity && population.get(q).overallDeviation < population.get(p).overallDeviation) {
						n.put(population.get(p), n.get(population.get(p))+1);
					}
				}	
			}
			S.put(population.get(p), s);
			if (n.get(population.get(p)) == 0) {
				population.get(p).rank = 1;
				front.get(1).add(population.get(p));
			}
		}
		int i = 1;
		while (!front.get(i).isEmpty()) {
			List<Chromosome> Q = new ArrayList<Chromosome>();
			for (Chromosome pp : front.get(i)) {
				for (Chromosome qq : S.get(pp)) {
					n.put(qq, n.get(qq)-1);
					if (n.get(qq) == 0) {
						qq.rank = i+1;
						Q.add(qq);
					}
				}
			}
			i++;
			front.put(i, Q);
		}
		return front;
	}
	
	public static void crowdingDist(Map<Integer, List<Chromosome>> fronts, boolean[] objectives) {
		for (int i : fronts.keySet()) {
			int L = fronts.get(i).size()-1;
			if (L+1 == 0) {
				continue;
			}
			for (Chromosome chrom : fronts.get(i)) {
				chrom.crowdingDist = 0;
			}
			if (objectives[0]) {
				fronts.get(i).sort(Comparator.comparing(Chromosome::getEdge));
				fronts.get(i).get(0).crowdingDist = Double.POSITIVE_INFINITY;
				fronts.get(i).get(L).crowdingDist = Double.POSITIVE_INFINITY;
				for (int j = 1; j < L; j++) {
					fronts.get(i).get(j).crowdingDist += (fronts.get(i).get(j+1).edge -fronts.get(i).get(j-1).edge)/(fronts.get(i).get(L).edge-fronts.get(i).get(0).edge);
				}
				fronts.get(i).sort(Comparator.comparing(Chromosome::getConn));
				fronts.get(i).get(0).crowdingDist = Double.POSITIVE_INFINITY;
				fronts.get(i).get(L).crowdingDist = Double.POSITIVE_INFINITY;
				for (int j = 1; j < L; j++) {
					fronts.get(i).get(j).crowdingDist += (fronts.get(i).get(j+1).connectivity -fronts.get(i).get(j-1).connectivity)/(fronts.get(i).get(L).connectivity-fronts.get(i).get(0).connectivity);
				}
			}
			else if (objectives[1]) {
				fronts.get(i).sort(Comparator.comparing(Chromosome::getEdge));
				fronts.get(i).get(0).crowdingDist = Double.POSITIVE_INFINITY;
				fronts.get(i).get(L).crowdingDist = Double.POSITIVE_INFINITY;
				for (int j = 1; j < L; j++) {
					fronts.get(i).get(j).crowdingDist += (fronts.get(i).get(j+1).edge -fronts.get(i).get(j-1).edge)/(fronts.get(i).get(L).edge-fronts.get(i).get(0).edge);
				}
				fronts.get(i).sort(Comparator.comparing(Chromosome::getDev));
				fronts.get(i).get(0).crowdingDist = Double.POSITIVE_INFINITY;
				fronts.get(i).get(L).crowdingDist = Double.POSITIVE_INFINITY;
				for (int j = 1; j < L; j++) {
					fronts.get(i).get(j).crowdingDist += (fronts.get(i).get(j+1).overallDeviation -fronts.get(i).get(j-1).overallDeviation)/(fronts.get(i).get(L).overallDeviation-fronts.get(i).get(0).overallDeviation);
				}
			}
			else if (objectives[2]) {
				fronts.get(i).sort(Comparator.comparing(Chromosome::getDev));
				fronts.get(i).get(0).crowdingDist = Double.POSITIVE_INFINITY;
				fronts.get(i).get(L).crowdingDist = Double.POSITIVE_INFINITY;
				for (int j = 1; j < L; j++) {
					fronts.get(i).get(j).crowdingDist += (fronts.get(i).get(j+1).overallDeviation -fronts.get(i).get(j-1).overallDeviation)/(fronts.get(i).get(L).overallDeviation-fronts.get(i).get(0).overallDeviation);
				}
				fronts.get(i).sort(Comparator.comparing(Chromosome::getConn));
				fronts.get(i).get(0).crowdingDist = Double.POSITIVE_INFINITY;
				fronts.get(i).get(L).crowdingDist = Double.POSITIVE_INFINITY;
				for (int j = 1; j < L; j++) {
					fronts.get(i).get(j).crowdingDist += (fronts.get(i).get(j+1).connectivity -fronts.get(i).get(j-1).connectivity)/(fronts.get(i).get(L).connectivity-fronts.get(i).get(0).connectivity);
				}
			}
			else if (objectives[3]) {
				fronts.get(i).sort(Comparator.comparing(Chromosome::getEdge));
				fronts.get(i).get(0).crowdingDist = Double.POSITIVE_INFINITY;
				fronts.get(i).get(L).crowdingDist = Double.POSITIVE_INFINITY;
				for (int j = 1; j < L; j++) {
					fronts.get(i).get(j).crowdingDist += (fronts.get(i).get(j+1).edge -fronts.get(i).get(j-1).edge)/(fronts.get(i).get(L).edge-fronts.get(i).get(0).edge);
				}
				fronts.get(i).sort(Comparator.comparing(Chromosome::getConn));
				fronts.get(i).get(0).crowdingDist = Double.POSITIVE_INFINITY;
				fronts.get(i).get(L).crowdingDist = Double.POSITIVE_INFINITY;
				for (int j = 1; j < L; j++) {
					fronts.get(i).get(j).crowdingDist += (fronts.get(i).get(j+1).connectivity -fronts.get(i).get(j-1).connectivity)/(fronts.get(i).get(L).connectivity-fronts.get(i).get(0).connectivity);
				}
				fronts.get(i).sort(Comparator.comparing(Chromosome::getDev));
				fronts.get(i).get(0).crowdingDist = Double.POSITIVE_INFINITY;
				fronts.get(i).get(L).crowdingDist = Double.POSITIVE_INFINITY;
				for (int j = 1; j < L; j++) {
					fronts.get(i).get(j).crowdingDist += (fronts.get(i).get(j+1).overallDeviation -fronts.get(i).get(j-1).overallDeviation)/(fronts.get(i).get(L).overallDeviation-fronts.get(i).get(0).overallDeviation);
				}
			}
		}
	}

}
