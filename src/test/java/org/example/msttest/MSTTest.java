package org.example.msttest;

import org.example.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Prim and Kruskal algorithms.
 * Checks if both produce the same MST total cost and correct number of edges.
 */
public class MSTTest {

    @Test
    public void testPrimAndKruskalGiveSameCost() {
        // Создаём тестовый граф вручную
        Graph g = new Graph("test");
        g.nodes.addAll(List.of("A", "B", "C", "D"));
        g.edges.add(new Edge("A", "B", 1));
        g.edges.add(new Edge("B", "C", 2));
        g.edges.add(new Edge("C", "D", 3));
        g.edges.add(new Edge("A", "D", 10));

        MSTResult prim = PrimAlgorithm.run(g);
        MSTResult kruskal = KruskalAlgorithm.run(g);

        assertEquals(prim.total_cost, kruskal.total_cost,
                "❌ MST costs must be equal");

        assertEquals(g.nodes.size() - 1, prim.mst_edges.size(),
                "❌ Prim MST must have V-1 edges");
        assertEquals(g.nodes.size() - 1, kruskal.mst_edges.size(),
                "❌ Kruskal MST must have V-1 edges");

        System.out.println("✅ MST test passed: total cost = " + prim.total_cost);
    }
}
