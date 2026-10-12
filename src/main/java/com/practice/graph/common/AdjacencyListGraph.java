package com.practice.graph.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AdjacencyListGraph {
    private Map<Integer, List<Integer>> adjacencyList;

    public AdjacencyListGraph(){
        this.adjacencyList = new HashMap<>();
    }

    //Add an edge to the graph
    public void addEdge(int source, int destination) {
        adjacencyList.get(source).add(destination);
        adjacencyList.get(destination).add(source); // For undirected graph
    }

    public void removeEdge(int source, int destination) {
        adjacencyList.get(source).remove((Integer) destination);
        adjacencyList.get(destination).remove((Integer) source); // For undirected graph
    }

    public void addVertex(int vertex) {
            adjacencyList.put(vertex, new LinkedList<>());
    }

    public void removeVertex(int vertex) {
        adjacencyList.remove(vertex);
        for (List<Integer> neighbors : adjacencyList.values()) {
            neighbors.remove((Integer) vertex);
        }
    }

    // Vertex ke neighbours do (traversal ke liye). Sirf padhne ke liye, bahar se change nahi ho sakti.
    public List<Integer> getNeighbors(int vertex) {
        return Collections.unmodifiableList(adjacencyList.getOrDefault(vertex, List.of()));
    }
}
