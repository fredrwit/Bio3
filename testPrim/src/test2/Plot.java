package test2;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;

public class Plot {
	
	public static void plot(Map<Integer, List<Chromosome>> fronts, boolean[] objectives, int size, int dimension) {

		
        Color color = new Color(0,0,0);
        Color[] colors = {color.WHITE,color.blue,color.red,color.green,color.yellow,color.pink,color.cyan,color.gray,color.orange,color.magenta,color};
        
        Plot2DPanel plot = new Plot2DPanel();
        Plot3DPanel plot3d = new Plot3DPanel();
        
        if (objectives[0] == true) {
			for (int key : fronts.keySet()) {
				
				List<Double> x = new ArrayList<Double>();
				List<Double> y = new ArrayList<Double>();
				fronts.get(key).sort(Comparator.comparing(Chromosome::getConn));
				for (Chromosome chrom : fronts.get(key)) {
					if (chrom.getConn() == 0.0) {
						continue;
					}
        			x.add(chrom.getEdge());
        			y.add(chrom.getConn());
        		}
				double[] X = new double[x.size()];
				double[] Y = new double[y.size()];
				for (int i = 0; i < x.size(); i++) {
					X[i] = x.get(i);
					Y[i] = y.get(i);
				}
				if (x.size() > 2) {
	        		plot.addLinePlot("Front number: "+key,colors[key], X, Y);

				}
        	}
        }
        
        if (objectives[1] == true) {
			for (int key : fronts.keySet()) {
				
				List<Double> x = new ArrayList<Double>();
				List<Double> y = new ArrayList<Double>();
				fronts.get(key).sort(Comparator.comparing(Chromosome::getConn));
				for (Chromosome chrom : fronts.get(key)) {
					if (chrom.getEdge() == 0.0) {
						continue;
					}
        			x.add(chrom.getEdge());
        			y.add(chrom.getDev());
        		}
				double[] X = new double[x.size()];
				double[] Y = new double[y.size()];
				for (int i = 0; i < x.size(); i++) {
					X[i] = x.get(i);
					Y[i] = y.get(i);
				}
				if (x.size() > 2) {
	        		plot.addLinePlot("Front number: "+key,colors[key], X, Y);

				}
        	}
        }

		if (objectives[2] == true) {
			for (int key : fronts.keySet()) {
				
				List<Double> x = new ArrayList<Double>();
				List<Double> y = new ArrayList<Double>();
				fronts.get(key).sort(Comparator.comparing(Chromosome::getConn));
				for (Chromosome chrom : fronts.get(key)) {
					if (chrom.getConn() == 0.0) {
						continue;
					}
        			x.add(chrom.getDev());
        			y.add(chrom.getConn());
        		}
				double[] X = new double[x.size()];
				double[] Y = new double[y.size()];
				for (int i = 0; i < x.size(); i++) {
					X[i] = x.get(i);
					Y[i] = y.get(i);
				}
				if (x.size() > 2) {
	        		plot.addLinePlot("Front number: "+key,colors[key], X, Y);

				}
        	}
        }
		
		if (objectives[3] == true) {
			for (int key : fronts.keySet()) {
				
				List<Double> x = new ArrayList<Double>();
				List<Double> y = new ArrayList<Double>();
				List<Double> z = new ArrayList<Double>();
				fronts.get(key).sort(Comparator.comparing(Chromosome::getConn));
				for (Chromosome chrom : fronts.get(key)) {
//					if (chrom.getConn() == 0.0 || chrom.getEdge() == 0) {
//						continue;
//					}
        			x.add(chrom.getDev());
        			y.add(chrom.getConn());
        			z.add(chrom.getEdge());
        		}
				double[] X = new double[x.size()];
				double[] Y = new double[y.size()];
				double[] Z = new double[z.size()];
				for (int i = 0; i < x.size(); i++) {
					X[i] = x.get(i);
					Y[i] = y.get(i);
					Z[i] = z.get(i);
				}
				if (x.size() > 2) {
	        		plot3d.addScatterPlot("Front number: "+key,colors[key], X, Y, Z);

				}
        	}
        }

        JFrame frame = new JFrame("Kim&Fredrik's MOEA - NSGA2 - PRIM's MST - DFS - RECURSION");
        frame.setSize(750, 750);
        if (dimension == 2) {
        	frame.setContentPane(plot);	
        } else {
        	frame.setContentPane(plot3d);	
        }
        frame.setVisible(true);
	}

}
