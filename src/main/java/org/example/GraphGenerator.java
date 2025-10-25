package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * GraphGenerator (Final v2)
 * Generates connected, undirected, weighted graphs.
 * Saves separate JSON files for each category:
 *  → input_small.json
 *  → input_medium.json
 *  → input_large.json
 *  → input_extralarge.json
 */
public class GraphGenerator {

    private static final Random random = new Random();

    public static void main(String[] args) {
        //  Category configurations
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
            for (String density : new String[]{"sparse", "medium", "dense"}) {
                int targetEdges = chooseTargetEdgeCount(n, density);
                Graph g = generateConnectedGraph(category, density, n, targetEdges);
                g.id = idCounter++;
                graphs.add(g);
            }
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
    private static Graph generateConnectedGraph(String category, String density, int n, int targetEdges) {
        Graph graph = new Graph(category, density);
        for (int i = 0; i < n; i++) {
            graph.nodes.add(String.valueOf(i));
        }

        // Step 1: ensure connectivity with random spanning tree
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

    /** Determine target edge count based on density */
    private static int chooseTargetEdgeCount(int n, String density) {
        int maxE = n * (n - 1) / 2;
        return switch (density) {
            case "sparse" -> Math.max(n - 1, (int) (1.25 * n));
            case "medium" -> Math.min(maxE, (int) (3 * n));
            case "dense" -> {
                double factor;
                if (n < 50) factor = 0.5;
                else if (n < 300) factor = 0.2;
                else if (n < 1000) factor = 0.05;
                else factor = 0.02;
                yield Math.min(maxE, (int) (factor * maxE));
            }
            default -> Math.max(n - 1, n);
        };
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
    String density;
    List<String> nodes = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();

    Graph(String category, String density) {
        this.category = category;
        this.density = density;
    }
}
