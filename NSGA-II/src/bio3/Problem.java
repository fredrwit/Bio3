package bio3;

public class Problem {
	public Pixels pixels;
	public int size;
	public int sizeX;
	public int sizeY;
	
	public Problem(String fileName) {
		pixels = new Pixels(fileName);
		sizeX = pixels.img.getWidth();
		sizeY = pixels.img.getHeight();
		size = sizeX*sizeY;
		
	}
}
