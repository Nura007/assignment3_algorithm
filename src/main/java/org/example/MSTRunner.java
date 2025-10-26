package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MSTRunner {

    static class InputData {
        List<Graph> graphs;
    }

    static class OutputData {
        int graph_id;
        Stats input_stats;
        MSTResult prim;
        MSTResult kruskal;
    }

    static class Stats {
        int vertices;
        int edges;
        Stats(int v, int e) { vertices = v; edges = e; }
    }

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileReader reader = new FileReader("assign_3_input_generated.json")) {
            InputData input = gson.fromJson(reader, InputData.class);
            List<OutputData> outputs = new ArrayList<>();

            for (Graph g : input.graphs) {
                MSTResult prim = PrimAlgorithm.run(g);
                MSTResult kruskal = KruskalAlgorithm.run(g);

                OutputData data = new OutputData();
                data.graph_id = g.id;
                data.input_stats = new Stats(g.nodes.size(), g.edges.size());
                data.prim = prim;
                data.kruskal = kruskal;

                outputs.add(data);
                System.out.println("✅ Processed graph " + g.id);
            }

            try (FileWriter writer = new FileWriter("output.json")) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("results", outputs);
                gson.toJson(resultMap, writer);
                System.out.println("🎯 Output saved to output.json");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
