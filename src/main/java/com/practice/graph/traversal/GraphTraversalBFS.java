package com.practice.graph.traversal;

import com.practice.graph.common.AdjacencyListGraph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class GraphTraversalBFS {


    public void BFSIterative(AdjacencyListGraph graph, int startVertex){
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        queue.add(startVertex);
        visited.add(startVertex);
        while(!queue.isEmpty()){
            int currentVertex = queue.poll();
            IO.println("Visiting vertex: " + currentVertex);

            //Add all the neighbours of the current vertex to the queue if they haven't been visited yet
            for(int neighbour : graph.getNeighbors(currentVertex)){
                if(!visited.contains(neighbour)){
                    visited.add(neighbour);
                    queue.add(neighbour);
                }
            }
        }
    }

    // Graph banao aur BFS chalao (wahi graph jo DFS ke main mein hai)
    //   1 - 2 - 3 - 4
    //           |
    //           5
    public static void main(String[] args) {
        AdjacencyListGraph graph = new AdjacencyListGraph();

        // Pehle vertices add karo (warna addEdge pe NullPointerException)
        for (int v = 1; v <= 5; v++) {
            graph.addVertex(v);
        }

        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(3, 5);

        GraphTraversalBFS traversal = new GraphTraversalBFS();
        IO.println("---- BFS from 1 ----");
        traversal.BFSIterative(graph, 1);   // expected: 1 2 3 4 5

        IO.println("---- BFS from 3 ----");
        traversal.BFSIterative(graph, 3);   // expected: 3 2 4 5 1
    }
}
