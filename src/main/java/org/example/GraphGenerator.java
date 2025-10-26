package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * GraphGenerator (Final v3)
 * Generates exactly 28 graphs:
 *  small:       1–5
 *  medium:      6–15
 *  large:       16–25
 *  extralarge:  26–28
 * One graph per ID (no density variants)
 */
public class GraphGenerator {

    private static final Random random = new Random();

    public static void main(String[] args) {
        // generate 4 categories separately
        generateCategoryToFile("small", 5, 30, 1, 5);
        generateCategoryToFile("medium", 30, 300, 6, 15);
        generateCategoryToFile("large", 300, 1000, 16, 25);
        generateCategoryToFile("extralarge", 1000, 2000, 26, 28);
    }

    /** Generates graphs for one category and writes to a separate JSON file */
    private static void generateCategoryToFile(String category, int minNodes, int maxNodes,
                                               int startId, int endId) {
        List<Graph> graphs = new ArrayList<>();
        int idCounter = startId;

        System.out.println("📦 Generating " + category + " graphs (IDs " + startId + "–" + endId + ")...");

        for (int i = startId; i <= endId; i++) {
            int n = randBetween(minNodes, maxNodes - 1);
            int targetEdges = chooseTargetEdgeCount(n);
            Graph g = generateConnectedGraph(category, n, targetEdges);
            g.id = idCounter++;
            graphs.add(g);
        }

        Map<String, Object> output = new HashMap<>();
        output.put("graphs", graphs);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String fileName = "input_" + category + ".json";

        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(output, writer);
            System.out.println("✅ Saved " + graphs.size() + " " + category + " graphs → " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Create connected undirected weighted graph */
    private static Graph generateConnectedGraph(String category, int n, int targetEdges) {
        Graph graph = new Graph(category);
        for (int i = 0; i < n; i++) {
            graph.nodes.add(String.valueOf(i));
        }

        // Step 1: create random spanning tree to ensure connectivity
        List<String> shuffled = new ArrayList<>(graph.nodes);
        Collections.shuffle(shuffled, random);
        Set<Set<String>> edgeSet = new HashSet<>();

        for (int i = 1; i < n; i++) {
            String u = shuffled.get(i);
            String v = shuffled.get(random.nextInt(i));
            int w = randBetween(1, 100);
            graph.edges.add(new Edge(u, v, w));
            edgeSet.add(Set.of(u, v));
        }

        // Step 2: add extra random edges up to targetEdges
        int maxEdges = n * (n - 1) / 2;
        targetEdges = Math.min(targetEdges, maxEdges);
        int remaining = targetEdges - (n - 1);

        while (remaining > 0) {
            String u = String.valueOf(random.nextInt(n));
            String v = String.valueOf(random.nextInt(n));
            if (u.equals(v)) continue;
            Set<String> pair = Set.of(u, v);
            if (edgeSet.contains(pair)) continue;
            int w = randBetween(1, 100);
            graph.edges.add(new Edge(u, v, w));
            edgeSet.add(pair);
            remaining--;
        }

        return graph;
    }

    /** Estimate target edge count (medium density) */
    private static int chooseTargetEdgeCount(int n) {
        int maxE = n * (n - 1) / 2;
        return Math.min(maxE, (int) (2.5 * n)); // moderate density
    }

    private static int randBetween(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}

/** Edge class */
class Edge {
    String from;
    String to;
    int weight;

    Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}

/** Graph class */
class Graph {
    int id;
    String category;
    List<String> nodes = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();

    Graph(String category) {
        this.category = category;
    }
}
