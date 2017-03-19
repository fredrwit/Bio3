package test2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class testing {
	
	public static BufferedImage get_image(String fileName) {
		BufferedImage img = null;

	    try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return img;
	}
	
	
	public static void getNode(int id) {
		int i;
		int j;
		double d = (double) id / (double) (321);
		if (d % 1 == 0) {
			i = (int) d -1;
		}
		else {
			i = (int) d;
		}
		j = id % 321;
		if (j == 0) {
			j = 320;
		}
		else {
			j = j-1;
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
	
	
	public static void main(String[] args) {
		
		getNode(965);
		
		Object[] a = crossover(new Chromosome(10), new Chromosome(10));
		System.out.println(a[1]);
		
	}
}
