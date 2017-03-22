package test2;

import javax.swing.JFrame;

import org.math.plot.*;

public class test {
	
	public static void main(String[] args) {
		// define your data
        double[] x = { 1, 2, 3, 4, 5, 6 };
        double[] y = { 45, 89, 6, 32, 63, 12 };
        double[] z = { 45, 89, 6, 32, 63, 12 };

        // create your PlotPanel (you can use it as a JPanel)
        Plot2DPanel plot = new Plot2DPanel();
        Plot3DPanel plot3d = new Plot3DPanel();

        // define the legend position
        plot.addLegend("SOUTH");

        // add a line plot to the PlotPanel
        plot.addLinePlot("my plot", x, y);
        plot3d.addLinePlot("LOOOL", x, y, z);

        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("a plot panel");
        frame.setSize(600, 600);
        frame.setContentPane(plot3d);
        frame.setVisible(true);
	}

}
