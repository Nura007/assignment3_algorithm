package org.example;

import java.util.*;

public class PrimAlgorithm {

    public static MSTResult run(Graph graph) {
        long start = System.nanoTime();

        Map<String, List<Edge>> adj = new HashMap<>();
        for (String node : graph.nodes) adj.put(node, new ArrayList<>());
        for (Edge e : graph.edges) {
            adj.get(e.from).add(e);
            adj.get(e.to).add(new Edge(e.to, e.from, e.weight));
        }

        Set<String> visited = new HashSet<>();
        List<Edge> mstEdges = new ArrayList<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));

        String startNode = graph.nodes.get(0);
        visited.add(startNode);
        pq.addAll(adj.get(startNode));

        int totalCost = 0;
        int operations = 0;

        while (!pq.isEmpty() && mstEdges.size() < graph.nodes.size() - 1) {
            Edge edge = pq.poll();
            operations++;
            if (!visited.contains(edge.to)) {
                visited.add(edge.to);
                mstEdges.add(edge);
                totalCost += edge.weight;
                for (Edge next : adj.get(edge.to)) {
                    if (!visited.contains(next.to)) pq.offer(next);
                }
            }
        }

        long end = System.nanoTime();
        double timeMs = (end - start) / 1_000_000.0;

        return new MSTResult("Prim", mstEdges, totalCost, graph.nodes.size(),
                graph.edges.size(), operations, timeMs);
    }
}
