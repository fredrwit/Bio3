package test;

import java.util.*;

public final class Prim {

    public static <T> UndirectedGraph<T> mst(UndirectedGraph<T> graph) {
    	System.out.println("Creating the MST...");
        FibonacciHeap<T> pq = new FibonacciHeap<T>();

        Map<T, FibonacciHeap.Entry<T>> entries = new HashMap<T, FibonacciHeap.Entry<T>>();

        UndirectedGraph<T> result = new UndirectedGraph<T>();

        if (graph.isEmpty())
            return result;

        T startNode = graph.iterator().next();

        result.addNode(startNode);

        addOutgoingEdges(startNode, graph, pq, result, entries);

        for (int i = 0; i < graph.size() - 1; ++i) {
            T toAdd = pq.dequeueMin().getValue();

            T endpoint = minCostEndpoint(toAdd, graph, result);

            result.addNode(toAdd);
            result.addEdge(toAdd, endpoint, graph.edgeCost(toAdd, endpoint));

            addOutgoingEdges(toAdd, graph, pq, result, entries);
        }
        return result;
    }

    private static <T> T minCostEndpoint(T node, UndirectedGraph<T> graph, 
                                         UndirectedGraph<T> result) {

        T endpoint = null;
        double leastCost = Double.POSITIVE_INFINITY;

        for (Map.Entry<T, Double> entry : graph.edgesFrom(node).entrySet()) {
            if (!result.containsNode(entry.getKey())) continue;

            if (entry.getValue() >= leastCost) continue;

            endpoint = entry.getKey();
            leastCost = entry.getValue();
        }
        return endpoint;
    }

    private static <T> void addOutgoingEdges(T node, UndirectedGraph<T> graph,
                                             FibonacciHeap<T> pq,
                                             UndirectedGraph<T> result,
                                             Map<T, FibonacciHeap.Entry<T>> entries ) {
        for (Map.Entry<T, Double> arc : graph.edgesFrom(node).entrySet()) {
            if (result.containsNode(arc.getKey())) continue; 

            if (!entries.containsKey(arc.getKey())) { 
                entries.put(arc.getKey(), pq.enqueue(arc.getKey(), arc.getValue()));
            }
            else if (entries.get(arc.getKey()).getPriority() > arc.getValue()) { 
                pq.decreaseKey(entries.get(arc.getKey()), arc.getValue());
            }

        }
    }
};