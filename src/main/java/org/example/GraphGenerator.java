package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * GraphGenerator (Final)
 * Generates connected, undirected, weighted graphs
 * Uses Google Gson for JSON export.
 *
 * ID ranges:
 *  small:       1–5
 *  medium:      6–15
 *  large:       16–25
 *  extralarge:  26–28
 */
public class GraphGenerator {

    private static final Random random = new Random();

    public static void main(String[] args) {
        List<Graph> allGraphs = new ArrayList<>();

        //  Category configurations
        int smallCount = 5;      // IDs 1–5
        int mediumCount = 10;    // IDs 6–15
        int largeCount = 10;     // IDs 16–25
        int extraLargeCount = 3; // IDs 26–28

        int idCounter = 1; // starting ID

        // Generate by category (each includes 3 density variants)
        idCounter = generateCategory("small", 5, 30, smallCount, idCounter, allGraphs);
        idCounter = generateCategory("medium", 30, 300, mediumCount, idCounter, allGraphs);
        idCounter = generateCategory("large", 300, 1000, largeCount, idCounter, allGraphs);
        idCounter = generateCategory("extralarge", 1000, 2000, extraLargeCount, idCounter, allGraphs);

        // Write all graphs to JSON
        Map<String, Object> output = new HashMap<>();
        output.put("graphs", allGraphs);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter("assign_3_input_generated.json")) {
            ((Gson) gson).toJson(output, writer);
            System.out.println("✅ Graphs generated successfully!");
            System.out.println("Total graphs: " + allGraphs.size());
            System.out.println("Saved to assign_3_input_generated.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Generate a specific number of graphs for a category, returning updated ID counter */
    private static int generateCategory(String category, int minNodes, int maxNodes, int count, int idCounter, List<Graph> allGraphs) {
        System.out.println("📦 Generating " + category + " graphs (" + count + " total)...");
        for (int i = 0; i < count; i++) {
            int n = randBetween(minNodes, maxNodes - 1);

            // generate one graph per density type (sparse, medium, dense)
            for (String density : new String[]{"sparse", "medium", "dense"}) {
                int targetEdges = chooseTargetEdgeCount(n, density);
                Graph g = generateConnectedGraph(category, density, n, targetEdges);
                g.id = idCounter++;
                allGraphs.add(g);
            }
        }
        return idCounter;
    }

    /** Create connected undirected weighted graph */
    private static Graph generateConnectedGraph(String category, String density, int n, int targetEdges) {
        Graph graph = new Graph(category, density);
        for (int i = 0; i < n; i++) {
            graph.nodes.add(String.valueOf(i));
        }

        // Step 1: random spanning tree ensures connectivity
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

        // Step 2: add random edges until target count
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
