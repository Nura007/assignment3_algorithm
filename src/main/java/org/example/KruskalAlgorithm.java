package org.example;

import java.util.*;

public class KruskalAlgorithm {

    static class DisjointSet {
        private final Map<String, String> parent = new HashMap<>();
        private final Map<String, Integer> rank = new HashMap<>();
        int operations = 0;

        public DisjointSet(Collection<String> nodes) {
            for (String n : nodes) {
                parent.put(n, n);
                rank.put(n, 0);
            }
        }

        public String find(String node) {
            operations++;
            if (!parent.get(node).equals(node))
                parent.put(node, find(parent.get(node)));
            return parent.get(node);
        }

        public void union(String a, String b) {
            operations++;
            String rootA = find(a);
            String rootB = find(b);
            if (!rootA.equals(rootB)) {
                if (rank.get(rootA) < rank.get(rootB)) parent.put(rootA, rootB);
                else if (rank.get(rootA) > rank.get(rootB)) parent.put(rootB, rootA);
                else {
                    parent.put(rootB, rootA);
                    rank.put(rootA, rank.get(rootA) + 1);
                }
            }
        }
    }

    public static MSTResult run(Graph graph) {
        long start = System.nanoTime();

        List<Edge> edges = new ArrayList<>(graph.edges);
        edges.sort(Comparator.comparingInt(e -> e.weight));

        DisjointSet ds = new DisjointSet(graph.nodes);
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        for (Edge e : edges) {
            if (!ds.find(e.from).equals(ds.find(e.to))) {
                ds.union(e.from, e.to);
                mstEdges.add(e);
                totalCost += e.weight;
            }
        }

        long end = System.nanoTime();
        double timeMs = (end - start) / 1_000_000.0;

        return new MSTResult("Kruskal", mstEdges, totalCost, graph.nodes.size(),
                graph.edges.size(), ds.operations, timeMs);
    }
}
