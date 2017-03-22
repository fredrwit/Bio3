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
	
	public static void plot2d(Map<Integer, List<Chromosome>> fronts, boolean[] objectives, int size) {
		// define your data
		
        Color color = new Color(0,0,0);
        Color[] colors = {color.WHITE,color.blue,color.red,color.green,color.yellow,color.pink,color.cyan,color.gray,color.orange,color.magenta,color};
        
        
        Plot2DPanel plot = new Plot2DPanel();
        Plot3DPanel plot3d = new Plot3DPanel();
        

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
	        		plot.addLinePlot("LOOOL"+key,colors[key], X, Y);

				}
        	}
        }
		
        // create your PlotPanel (you can use it as a JPanel)
        

        // define the legend position
        plot.addLegend("SOUTH");

        // add a line plot to the PlotPanel
        //plot.addScatterPlot("my plot", x, y);

        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("a plot panel");
        frame.setSize(600, 600);
        frame.setContentPane(plot);
        frame.setVisible(true);
	}
	
	public static void plot3d() {
		
	}

}
