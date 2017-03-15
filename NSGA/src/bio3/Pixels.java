package src.bio3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Pixels
{
	
	BufferedImage img;

    Pixels(String fileName)
    {

    	try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    public int getR(int x, int y) {
    	return (img.getRGB(x,y) >> 16) & 0xFF;
    }
    
    public int getG(int x, int y) {
    	return (img.getRGB(x,y) >> 8) & 0xFF;
    }

    public int getB(int x, int y) {
    	return img.getRGB(x,y) & 0xFF;
    }
    
 
    
    
    
    
    
}