package org.example;

import java.util.List;

public class MSTResult {
    public String algorithm;
    public List<Edge> mst_edges;
    public int total_cost;
    public int vertices;
    public int edges;
    public int operations_count;
    public double execution_time_ms;

    public MSTResult(String algorithm, List<Edge> mstEdges, int totalCost,
                     int vertices, int edges, int operationsCount, double executionTimeMs) {
        this.algorithm = algorithm;
        this.mst_edges = mstEdges;
        this.total_cost = totalCost;
        this.vertices = vertices;
        this.edges = edges;
        this.operations_count = operationsCount;
        this.execution_time_ms = executionTimeMs;
    }
}
