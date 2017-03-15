package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {

	public static BufferedImage get_image(String fileName) {
		BufferedImage img = null;

	    try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return img;

		
	}
	
	public static void initGraph(UndirectedGraph graph, BufferedImage pixels) {
		System.out.println("Calculating colors and linking neighbors for each node...");
		for (int i = 0; i < pixels.getHeight(); i++) {
			for (int j = 0; j < pixels.getWidth(); j++) {
				
			}
		}
	}
	
	
	public static void main(String[] args) {
		BufferedImage test = get_image("lol.jpg");
		int rows = test.getHeight();
		int cols = test.getWidth();
		
		UndirectedGraph graph = new UndirectedGraph(rows,cols);
		

	}

}
