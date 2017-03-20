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
	
	public void setSegmentSize(){
		Node[] nodepoint = new Node[nodes];
		for (int i = 0; i < mst.size(); i++) {
			int startNode = mst.get(i).startNode;
			int endNode = mst.get(i).endNode;
			if (nodepoint[startNode] == null){
				Node newNode = new Node(startNode);
				Node newEndNode = new Node(endNode);
				newNode.pointers.add(endNode);
				newEndNode.pointers.add(startNode);
				nodepoint[startNode] = newNode;
				nodepoint[endNode] = newEndNode;
			}else{
				nodepoint[startNode].pointers.add(endNode);
				Node newEndNode = new Node(endNode);
				newEndNode.pointers.add(startNode);
				nodepoint[endNode] = newEndNode;
			}
		}
			
		this.nodesList = nodepoint;
		int sum = 0;
		for (int j = 0; j < this.cuts.size(); j++) {
			sum = 0;
			int start = cuts.get(j);
			for (int i = 0; i < nodesList[start].pointers.size(); i++) {
				nodesList[start].numbers.add(Recursive(nodesList[nodesList[start].pointers.get(i)], start));	
				sum += nodesList[start].numbers.get(i);
			}nodesList[start].sum = sum;
		}
		for (int i = 0; i < this.cuts.size(); i++){
			int start2 = cuts.get(i);
			for (int j = 0; j < nodesList[start2].pointers.size(); j++) {
				Recursive2(nodesList[nodesList[start2].pointers.get(j)], start2, nodesList[start2].sum-nodesList[start2].numbers.get(j)+1);
			}
		}
	}
	public int Recursive2(Node node, int previous, int number){
		int test = 0;
		node.numbers.set(0, number);
		node.sum += number;
		if(node.pointers.size() == 1){
			return test;
		}
		for (int i = 1; i < node.pointers.size(); i++) {
			test = Recursive2(this.nodesList[node.pointers.get(i)], node.number, node.sum-node.numbers.get(i)+1);	
		}
		return test;
	}

	public int Recursive(Node node, int previous){
		int number = 0;
		if (node.pointers.size() == 1){
			node.numbers.add(1);
			return number+1;
		}
		int localNumber = 0;
		for (int i = 0; i < node.pointers.size(); i++) {
			if(node.pointers.get(i) == previous){
				node.numbers.add(1);
			}else{
				localNumber = Recursive(this.nodesList[node.pointers.get(i)], node.number);
				node.numbers.add(localNumber);
			}
			number += localNumber;
		}node.sum = number;
		return number+1;
	}
	
	
	
	
	public static void main(String[] args) {
		
		getNode(965);
		
		Object[] a = crossover(new Chromosome(10), new Chromosome(10));
		System.out.println(a[1]);
		
	}
}
