package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * MSTRunner (Final)
 * Automatically processes all input JSON files:
 *   input_small.json → output_small.json
 *   input_medium.json → output_medium.json
 *   input_large.json → output_large.json
 *   input_extralarge.json → output_extralarge.json
 * Runs Prim and Kruskal algorithms on each graph.
 */
public class MSTRunner {

    private static final String[] CATEGORIES = {
            "small", "medium", "large", "extralarge"
    };

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        for (String category : CATEGORIES) {
            String inputFile = "input_" + category + ".json";
            String outputFile = "output_" + category + ".json";

            System.out.println("\n🚀 Processing category: " + category.toUpperCase());
            try (FileReader reader = new FileReader(inputFile)) {
                InputData input = gson.fromJson(reader, InputData.class);
                if (input == null || input.graphs == null) {
                    System.out.println("⚠️ No graphs found in " + inputFile);
                    continue;
                }

                List<OutputData> results = new ArrayList<>();

                for (Graph g : input.graphs) {
                    MSTResult prim = PrimAlgorithm.run(g);
                    MSTResult kruskal = KruskalAlgorithm.run(g);

                    OutputData out = new OutputData();
                    out.graph_id = g.id;
                    out.input_stats = new Stats(g.nodes.size(), g.edges.size());
                    out.prim = prim;
                    out.kruskal = kruskal;
                    results.add(out);

                    System.out.printf("✅ Graph %d done (V=%d, E=%d)%n",
                            g.id, g.nodes.size(), g.edges.size());
                }

                Map<String, Object> finalOut = new HashMap<>();
                finalOut.put("results", results);

                try (FileWriter writer = new FileWriter(outputFile)) {
                    gson.toJson(finalOut, writer);
                }

                System.out.println("💾 Saved results → " + outputFile);
            } catch (IOException e) {
                System.out.println("❌ Error reading " + inputFile + ": " + e.getMessage());
            }
        }

        System.out.println("\n🎉 All categories processed successfully!");
    }
}

/** Supporting JSON data classes */

class InputData {
    List<Graph> graphs;
}

class OutputData {
    int graph_id;
    Stats input_stats;
    MSTResult prim;
    MSTResult kruskal;
}

class Stats {
    int vertices;
    int edges;
    Stats(int v, int e) {
        vertices = v;
        edges = e;
    }
}
