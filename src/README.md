# 🧮 Assignment 3

### 📚 Course: Design and Analysis of Algorithms
### 🧑‍💻 Author: Nurtilek
### 🗓️ Project: Design and Implementation of Graph Algorithms (Prim & Kruskal)

---

## 🎯 Project Goal
The purpose of this assignment is to **implement two classical algorithms for finding a Minimum Spanning Tree (MST)** —  
**Prim’s Algorithm** and **Kruskal’s Algorithm** — and to compare their performance on graphs of different sizes.

Additionally, the project includes:
- Automated **graph generation** in JSON format
- **Execution time measurement** and **operation count**
- **Comparison** of Prim and Kruskal results
- **JUnit testing** to verify algorithm correctness

---

## ⚙️ Technologies Used

| Tool | Purpose |
|------|----------|
| **Java 23** | Implementation language |
| **Maven** | Build and dependency management |
| **Google Gson** | JSON parsing and generation |
| **JUnit 5** | Unit testing framework |
| **IntelliJ IDEA** | Development environment |

---

## 🧩 Project Structure
```
assignment3_design/
├── pom.xml
├── README.md
└── src/
├── main/java/org/example/
│ ├── Graph.java
│ ├── Edge.java
│ ├── MSTResult.java
│ ├── PrimAlgorithm.java
│ ├── KruskalAlgorithm.java
│ ├── GraphGenerator.java
│ └── MSTRunner.java
└── test/java/org/example/
└── MSTTest.java
```

---

## 🧠 Description of Main Components

### 🔹 **GraphGenerator.java**
Generates connected, undirected, weighted graphs of four categories:

| Category | Range of Vertices | ID Range | Description |
|-----------|------------------|-----------|--------------|
| Small | 5–30 | 1–5 | Small test graphs |
| Medium | 30–300 | 6–15 | Mid-sized graphs |
| Large | 300–1000 | 16–25 | Large graphs |
| Extra Large | 1000–2000 | 26–28 | Very large graphs |

Each graph contains:
- A list of nodes (vertices)
- A list of weighted edges (`from`, `to`, `weight`)

Output:  
Creates four input files:
```
input_small.json
input_medium.json
input_large.json
input_extralarge.json
```

---

### 🔹 **MSTRunner.java**
Reads all input JSON files and executes:
- **Prim’s Algorithm**
- **Kruskal’s Algorithm**

For each graph:
- Finds MST edges
- Calculates total MST cost
- Counts number of operations
- Measures execution time in milliseconds

Output:  
Creates result files:
```angular2html
output_small.json
output_medium.json
output_large.json
output_extralarge.json
```

Each output file contains detailed MST results.

---

### 🔹 **PrimAlgorithm.java**
Implements **Prim’s algorithm** using:
- Adjacency lists
- Priority queue (min-heap)
- Greedy approach for minimum edge selection

Tracks:
- Total MST cost
- Number of operations
- Execution time (in ms)

---

### 🔹 **KruskalAlgorithm.java**
Implements **Kruskal’s algorithm** using:
- Edge list sorting
- **Disjoint Set (Union-Find)** data structure
- Greedy edge inclusion without cycles

Tracks:
- Total MST cost
- Number of union/find operations
- Execution time (in ms)

---

### 🔹 **MSTTest.java**
JUnit 5 test file that validates:
1. **Prim** and **Kruskal** always produce the same MST cost
2. MST always contains **V−1 edges**
3. Both algorithms work correctly for small graphs

Test command:
```bash
mvn test
{
  "graphs": [
    {
      "id": 1,
      "category": "small",
      "nodes": ["A", "B", "C", "D"],
      "edges": [
        {"from": "A", "to": "B", "weight": 4},
        {"from": "A", "to": "C", "weight": 3},
        {"from": "B", "to": "C", "weight": 2}
      ]
    }
  ]
}
```
Data types:

| Field      | Type           | Description     |
| ---------- | -------------- | --------------- |
| `id`       | `int`          | Graph ID        |
| `category` | `String`       | Graph size type |
| `nodes`    | `List<String>` | Vertex names    |
| `edges`    | `List<Edge>`   | Graph edges     |

```
📊 Output Data Format
{
  "results": [
    {
      "graph_id": 1,
      "input_stats": {
        "vertices": 5,
        "edges": 7
      },
      "prim": {
        "total_cost": 16,
        "operations_count": 42,
        "execution_time_ms": 1.52
      },
      "kruskal": {
        "total_cost": 16,
        "operations_count": 37,
        "execution_time_ms": 1.28
      }
    }
  ]
}
```
🧠 Data Type Summary:

| Class       | Field               | Type           | Description                    |
| ----------- | ------------------- | -------------- | ------------------------------ |
| `Graph`     | `id`                | `int`          | Unique graph ID                |
|             | `category`          | `String`       | Graph category                 |
|             | `nodes`             | `List<String>` | List of vertices               |
|             | `edges`             | `List<Edge>`   | List of weighted edges         |
| `Edge`      | `from`              | `String`       | Source vertex                  |
|             | `to`                | `String`       | Destination vertex             |
|             | `weight`            | `int`          | Edge weight                    |
| `MSTResult` | `total_cost`        | `int`          | MST total weight               |
|             | `operations_count`  | `int`          | Number of performed operations |
|             | `execution_time_ms` | `double`       | Execution time in milliseconds |

```
```

---
## 🚀 **How to Run the Project**
1️⃣ Generate input data
```bash
mvn exec:java -Dexec.mainClass="org.example.GraphGenerator"
```
2️⃣ Run MST algorithms
```bash
mvn exec:java -Dexec.mainClass="org.example.MSTRunner"
```
3️⃣ Run unit tests
```bash
mvn test
```
---
## 📈 **Example Console Output**
```scss
📦 Generating small graphs (IDs 1–5)...
✅ Saved 5 small graphs → input_small.json
📦 Generating medium graphs (IDs 6–15)...
✅ Saved 10 medium graphs → input_medium.json
📦 Generating large graphs (IDs 16–25)...
✅ Saved 10 large graphs → input_large.json
📦 Generating extralarge graphs (IDs 26–28)...
✅ Saved 3 extralarge graphs → input_extralarge.json

🚀 Processing category: SMALL
✅ Graph 1 done (V=12, E=33)
✅ Graph 2 done (V=25, E=70)
💾 Saved results → output_small.json
...
🎉 All categories processed successfully!

```
---
## 🧩 **Testing Results Example**
```yaml
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running org.example.MSTTest
✅ MST test passed: total cost = 6
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```
---
# 📚Conclusion

This project demonstrates the implementation and comparison of two fundamental algorithms for finding the Minimum Spanning Tree (MST) — Prim’s and Kruskal’s — applied to automatically generated graphs of different scales.

   - Both algorithms produce identical MST costs ✅

   - Kruskal generally performs fewer operations on sparse graphs ⚙️

   - Prim performs slightly faster on dense graphs ⚡

# 🧾 References
- [CLRS: Introduction to Algorithms (Cormen et al.)](https://mitpress.mit.edu/9780262046305/introduction-to-algorithms/)
- [GeeksforGeeks: Prim’s Algorithm](https://www.geeksforgeeks.org/prims-algorithm/)
- [GeeksforGeeks: Kruskal’s Algorithm](https://www.geeksforgeeks.org/kruskals-algorithm/)