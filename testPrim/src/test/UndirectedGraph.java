package test;

import java.util.*; 
import java.util.Map.Entry;

public class UndirectedGraph<T> implements Iterable<T> {

    private Map<T, Map<T, Double>> mGraph = new HashMap<T, Map<T, Double>>();

    public UndirectedGraph(UndirectedGraph<T> another) {
		this.mGraph = another.getCopy();
	}
    
    public UndirectedGraph() {
	}

	public Map<T, Map<T, Double>> getCopy() {
		Map<T, Map<T, Double>> copy = new HashMap<T, Map<T, Double>>();
		for(Entry<T, Map<T, Double>> entry : this.mGraph.entrySet()){
		    copy.put(entry.getKey(), new HashMap<T, Double>(entry.getValue()));
		}
		return copy;
    }
    
    public boolean addNode(T node) {
        if (mGraph.containsKey(node))
            return false;

        mGraph.put(node, new HashMap<T, Double>());
        return true;
    }

    public void addEdge(T one, T two, double length) {
        if (!mGraph.containsKey(one) || !mGraph.containsKey(two))
            throw new NoSuchElementException("Both nodes must be in the graph.");

        mGraph.get(one).put(two, length);
        mGraph.get(two).put(one, length);
    }

    public void removeEdge(T one, T two) {
        if (!mGraph.containsKey(one) || !mGraph.containsKey(two))
            throw new NoSuchElementException("Both nodes must be in the graph.");

        mGraph.get(one).remove(two);
        mGraph.get(two).remove(one);
    }

    public double edgeCost(T one, T two) {
        if (!mGraph.containsKey(one) || !mGraph.containsKey(two))
            throw new NoSuchElementException("Both nodes must be in the graph.");     
        
        Double result = mGraph.get(one).get(two);

        if (result == null)
            throw new NoSuchElementException("Edge does not exist in the graph.");

        return result;
    }

    public Map<T, Double> edgesFrom(T node) {
        /* Check that the node exists. */
        Map<T, Double> arcs = mGraph.get(node);
        if (arcs == null)
            throw new NoSuchElementException("Source node does not exist.");

        return Collections.unmodifiableMap(arcs);
    }

    public boolean containsNode(T node) {
        return mGraph.containsKey(node);
    }

    public Iterator<T> iterator() {
        return mGraph.keySet().iterator();
    }
    
    public Iterator<Map<T,Double>> mapIterator() {
    	return mGraph.values().iterator();
    }
    
    public Iterator<Entry<T, Map<T, Double>>> entryIterator() {
    	return mGraph.entrySet().iterator();
    }

    public int size() {
        return mGraph.size();
    }

    public boolean isEmpty() {
        return mGraph.isEmpty();
    }
}