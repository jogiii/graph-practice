package com.practice.graph.common;

// Undirected, unweighted graph using adjacency matrix.
// adjMatrix[i][j] = 1 -> i aur j ke beech edge hai, 0 -> nahi hai.
// Notes: docs/02-adjacency-matrix-add-remove-vertex.md
public class AdjacencyMatGraph {

    private int adjMatrix[][];   // V x V matrix
    private int vertices;        // abhi kitne vertices hain (0 se vertices-1 tak)

    public AdjacencyMatGraph(int vertices) {
        this.vertices = vertices;
        adjMatrix = new int[vertices][vertices]; // Java mein default 0, yaani shuru mein koi edge nahi
    }

    // O(1): undirected hai, isliye dono taraf 1
    public void addEdge(int source, int destination) {
        validateVertex(source);
        validateVertex(destination);
        adjMatrix[source][destination] = 1;
        adjMatrix[destination][source] = 1;
    }

    // O(1): dono taraf wapas 0
    public void removeEdge(int source, int destination) {
        validateVertex(source);
        validateVertex(destination);
        adjMatrix[source][destination] = 0;
        adjMatrix[destination][source] = 0;
    }

    // Vertex 0 se vertices-1 ke beech hona chahiye, warna saaf error do
    // (nahi to Java khud ArrayIndexOutOfBoundsException dega, jo samajhna mushkil hai)
    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= vertices) {
            throw new IllegalArgumentException("Invalid vertex index: " + vertex);
        }
    }

    // O(V^2): array ka size badal nahi sakta, isliye bada matrix banake copy karo.
    // Naya vertex last index (vertices) pe aata hai, uski row/column 0 = koi edge nahi.
    public void addVertex() {
        int[][] newAdjMatrix = new int[vertices + 1][vertices + 1];
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                newAdjMatrix[i][j] = adjMatrix[i][j]; // same index pe copy, shift ki zarurat nahi
            }
        }
        adjMatrix = newAdjMatrix;
        vertices++;
    }

    // O(V^2): chhota matrix banao, vertex ki row + column chhod ke baaki copy karo.
    // newRow/newCol = naye matrix mein agli khaali jagah. Ye sirf copy hone pe badhte hain,
    // isliye vertex ke baad wali values apne-aap ek step peeche khisak jaati hain (no out of bound).
    // Dhyan do: iske baad aage wale vertices ka number 1 kam ho jaata hai.
    public void removeVertex(int vertex){
        validateVertex(vertex);
        int[][] newAdjMatrix = new int[vertices-1][vertices - 1];

        int newRow =0;
        for(int i=0;i<vertices;i++){
            if(i != vertex){                 // hataye gaye vertex ki row skip
                int newCol = 0;              // har row mein column 0 se shuru
                for(int j=0;j<vertices;j++){
                    if(j!=vertex){           // hataye gaye vertex ka column skip
                        newAdjMatrix[newRow][newCol] = adjMatrix[i][j];
                        newCol++;
                    }
                }
                newRow++;
            }
        }

        adjMatrix = newAdjMatrix;
        vertices--;

    }


}
