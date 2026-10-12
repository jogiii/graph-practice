package com.practice.graph.traversal;

import com.practice.graph.common.AdjacencyListGraph;

import java.util.*;

// DFS (Depth First Search) bina recursion ke, stack use karke.
// Idea: ek raaste pe jitna gehra (deep) ja sakte ho jao, phir peeche aake doosra raasta lo.
public class GraphTraversalDFS {

    public void DFSIterative(AdjacencyListGraph graph, int startVertex){
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();

        stack.push(startVertex);
        //keep traversing until stack is empty
        while(!stack.isEmpty()){

            //Get an element from the stack and mark it as visited
            int currentVertex = stack.pop();

            // Ek vertex stack mein do baar aa sakta hai, agar pehle visit ho chuka hai to skip
            if(visited.contains(currentVertex)){
                continue;
            }

            System.out.println("Visiting vertex: " + currentVertex);
            visited.add(currentVertex);

            //add all the unvisited neighbours to stack
            for(int neighbour : graph.getNeighbors(currentVertex)){
                if(!visited.contains(neighbour)){
                    stack.push(neighbour);
                }
            }
        }



    }


    // DFS recursion se: stack khud nahi banana, Java ka call stack yahi kaam karta hai
    public void DFSRecursive(AdjacencyListGraph graph, int startVertex){
        Set<Integer> visited = new HashSet<>();
        dfsHelper(graph, startVertex, visited);
    }

    private void dfsHelper(AdjacencyListGraph graph, int currentVertex, Set<Integer> visited){
        //mark current vertex as visited
        visited.add(currentVertex);
        System.out.println("Visiting vertex: " + currentVertex);

        //har unvisited neighbour ke liye wahi kaam dobara (gehrai mein jao)
        for(int neighbour : graph.getNeighbors(currentVertex)){
            if(!visited.contains(neighbour)){
                dfsHelper(graph, neighbour, visited);
            }
        }
        //loop khatam = is vertex se aage kuch nahi, wapas caller pe (backtrack)
    }


    public List<Integer> dfs(AdjacencyListGraph graph, int start) {
        List<Integer> result = new ArrayList<>();   // visit order
        Set<Integer> visited = new HashSet<>();     // kaun-kaun dekh liya
        Deque<Integer> stack = new ArrayDeque<>();  // stack: last in, first out

        stack.push(start);

        while (!stack.isEmpty()) {
            int current = stack.pop();

            // Ek vertex stack mein do baar aa sakta hai (do alag neighbours se),
            // isliye agar pehle hi visit ho chuka hai to skip karo
            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);
            result.add(current);

            // Neighbours ulte order mein push karo, taaki pehla neighbour stack ke top pe aaye
            // aur sabse pehle visit ho (recursive DFS jaisa order milega)
            List<Integer> neighbors = graph.getNeighbors(current);
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                int neighbor = neighbors.get(i);
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }

        return result;
    }

    // Graph banao aur DFS chalao
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

        GraphTraversalDFS traversal = new GraphTraversalDFS();
        traversal.DFSIterative(graph, 1);
        System.out.println("---- recursive ----");
        traversal.DFSRecursive(graph, 1);
        System.out.println("dfs() order: " + traversal.dfs(graph, 1));
    }
}
