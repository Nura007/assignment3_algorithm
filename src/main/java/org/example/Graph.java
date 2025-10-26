package org.example;

import java.util.ArrayList;
import java.util.List; /** Graph class */
public class Graph {
    int id;
    String category;
    public List<String> nodes = new ArrayList<>();
    public List<Edge> edges = new ArrayList<>();

    public Graph(String category) {
        this.category = category;
    }
}
