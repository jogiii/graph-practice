package com.practice.graph.traversal;

import com.practice.graph.common.AdjacencyListGraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphTraversalDFSTest {

    //   1 - 2 - 3 - 4
    //           |
    //           5
    private AdjacencyListGraph sampleGraph() {
        AdjacencyListGraph g = new AdjacencyListGraph();
        for (int v = 1; v <= 5; v++) {
            g.addVertex(v);
        }
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        g.addEdge(3, 5);
        return g;
    }

    @Test
    void visitsDeepFirst() {
        assertEquals(List.of(1, 2, 3, 4, 5), new GraphTraversalDFS().dfs(sampleGraph(), 1));
    }

    @Test
    void startFromMiddle() {
        assertEquals(List.of(3, 2, 1, 4, 5), new GraphTraversalDFS().dfs(sampleGraph(), 3));
    }

    @Test
    void handlesCycle() {
        //  1 - 2
        //   \ /
        //    3
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 1);

        assertEquals(List.of(1, 2, 3), new GraphTraversalDFS().dfs(g, 1));
    }

    @Test
    void doesNotReachDisconnectedVertex() {
        AdjacencyListGraph g = sampleGraph();
        g.addVertex(6); // kisi se juda nahi

        assertEquals(List.of(1, 2, 3, 4, 5), new GraphTraversalDFS().dfs(g, 1));
    }
}

