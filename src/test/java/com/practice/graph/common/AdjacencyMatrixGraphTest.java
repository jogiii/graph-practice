package com.practice.graph.common;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyMatrixGraphTest {

    @Test
    void undirectedGraphIsSymmetric() {
        AdjacencyMatrixGraph g = new AdjacencyMatrixGraph(4, false);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(2, 3);

        assertTrue(g.hasEdge(0, 1));
        assertTrue(g.hasEdge(1, 0));
        assertFalse(g.hasEdge(0, 3));
        assertEquals(List.of(0, 1, 3), g.neighbors(2));
        assertEquals("0 1 1 0\n1 0 1 0\n1 1 0 1\n0 0 1 0\n", g.toString());
    }

    @Test
    void directedGraphOnlySetsOneDirection() {
        AdjacencyMatrixGraph g = new AdjacencyMatrixGraph(3, true);
        g.addEdge(0, 1);
        g.addEdge(2, 1);

        assertTrue(g.hasEdge(0, 1));
        assertFalse(g.hasEdge(1, 0));
        assertEquals(2, g.inDegree(1));
        assertEquals(0, g.outDegree(1));
    }

    @Test
    void weightedEdgesAndRemoval() {
        AdjacencyMatrixGraph g = new AdjacencyMatrixGraph(3, false);
        g.addEdge(0, 1, 0);
        g.addEdge(1, 2, -5);

        assertEquals(0, g.getWeight(0, 1));
        assertEquals(-5, g.getWeight(2, 1));

        g.removeEdge(1, 2);
        assertFalse(g.hasEdge(2, 1));
        assertThrows(IllegalArgumentException.class, () -> g.getWeight(1, 2));
    }

    @Test
    void invalidVertexThrows() {
        AdjacencyMatrixGraph g = new AdjacencyMatrixGraph(2, false);
        assertThrows(IndexOutOfBoundsException.class, () -> g.addEdge(0, 5));
    }
}

