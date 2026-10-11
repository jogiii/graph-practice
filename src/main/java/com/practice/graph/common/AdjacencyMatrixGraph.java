package com.practice.graph.common;

import java.util.ArrayList;
import java.util.List;

// Adjacency Matrix se Graph: matrix[i][j] = edge i -> j ka weight (ya NO_EDGE).
// Concept notes: docs/01-adjacency-matrix.md
public class AdjacencyMatrixGraph {

    // "Edge nahi hai" ka marker. 0 ya -1 nahi liya, taaki 0/negative weights bhi valid rahein.
    public static final int NO_EDGE = Integer.MIN_VALUE;

    private final int vertices;
    private final boolean directed;
    private final int[][] matrix;

    public AdjacencyMatrixGraph(int vertices, boolean directed) {
        if (vertices < 0) {
            throw new IllegalArgumentException("Vertices negative nahi ho sakte: " + vertices);
        }
        this.vertices = vertices;
        this.directed = directed;
        this.matrix = new int[vertices][vertices];
        // Shuru mein koi edge nahi hai, isliye har cell ko NO_EDGE se bhar do
        for (int[] row : matrix) {
            java.util.Arrays.fill(row, NO_EDGE);
        }
    }

    // Unweighted edge add karo (weight = 1 maana jaata hai).
    public void addEdge(int from, int to) {
        addEdge(from, to, 1);
    }

    // Weighted edge add karo. Undirected hai to ulti direction bhi set hogi.
    public void addEdge(int from, int to, int weight) {
        validate(from);
        validate(to);
        matrix[from][to] = weight;
        if (!directed) {
            matrix[to][from] = weight; // symmetric rakhna zaroori hai
        }
    }

    // Edge hatao. O(1).
    public void removeEdge(int from, int to) {
        validate(from);
        validate(to);
        matrix[from][to] = NO_EDGE;
        if (!directed) {
            matrix[to][from] = NO_EDGE;
        }
    }

    // Check karo ki from -> to edge hai ya nahi. O(1) – matrix ka sabse bada fayda.
    public boolean hasEdge(int from, int to) {
        validate(from);
        validate(to);
        return matrix[from][to] != NO_EDGE;
    }

    // Edge ka weight do. Edge nahi hai to exception.
    public int getWeight(int from, int to) {
        if (!hasEdge(from, to)) {
            throw new IllegalArgumentException("Edge exist nahi karti: " + from + " -> " + to);
        }
        return matrix[from][to];
    }

    // Vertex v ke saare neighbours – poori row scan hoti hai, isliye O(V).
    public List<Integer> neighbors(int v) {
        validate(v);
        List<Integer> result = new ArrayList<>();
        for (int j = 0; j < vertices; j++) {
            if (matrix[v][j] != NO_EDGE) {
                result.add(j);
            }
        }
        return result;
    }

    // Out-degree: v se kitni edges bahar jaa rahi hain (row mein count).
    public int outDegree(int v) {
        return neighbors(v).size();
    }

    // In-degree: kitni edges v pe aa rahi hain (column mein count). O(V).
    public int inDegree(int v) {
        validate(v);
        int count = 0;
        for (int i = 0; i < vertices; i++) {
            if (matrix[i][v] != NO_EDGE) {
                count++;
            }
        }
        return count;
    }

    public int size() {
        return vertices;
    }

    public boolean isDirected() {
        return directed;
    }

    // Matrix ko 0/1 form mein print-friendly string banao (debugging ke liye).
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                sb.append(matrix[i][j] == NO_EDGE ? 0 : 1);
                if (j < vertices - 1) {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private void validate(int v) {
        if (v < 0 || v >= vertices) {
            throw new IndexOutOfBoundsException("Vertex " + v + " range [0, " + (vertices - 1) + "] ke bahar hai");
        }
    }
}

