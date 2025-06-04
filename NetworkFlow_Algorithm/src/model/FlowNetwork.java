package model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class FlowNetwork {
    private final int size;
    private final int source;
    private final int sink;
    private final List<Edge>[] adjList;
    private final Map<Integer, Map<Integer, Edge>> edgeMap;

    @SuppressWarnings("unchecked")
    public FlowNetwork(int size, int source, int sink) {
        this.size = size;
        this.source = source;
        this.sink = sink;
        this.adjList = new List[size];
        this.edgeMap = new HashMap<>();

        for (int i = 0; i < size; i++) {
            adjList[i] = new ArrayList<>();
            edgeMap.put(i, new HashMap<>());
        }
    }

    public int getSize() {
        return size;
    }

    public int getSource() {
        return source;
    }

    public int getSink() {
        return sink;
    }

    public void addEdge(int from, int to, int capacity) {
        Edge edge = new Edge(from, to, capacity);
        adjList[from].add(edge);

        // Store for quick lookup
        edgeMap.get(from).put(to, edge);

        // Add reverse edge with 0 capacity for residual flow
        if (!edgeMap.get(to).containsKey(from)) {
            Edge reverseEdge = new Edge(to, from, 0);
            adjList[to].add(reverseEdge);
            edgeMap.get(to).put(from, reverseEdge);
        }
    }

    public List<Edge> getAdjacencyList(int node) {
        return adjList[node];
    }

    public int getResidualCapacity(int u, int v) {
        Edge edge = edgeMap.get(u).get(v);
        if (edge != null) {
            return edge.getResidualCapacity();
        }
        return 0;
    }

    public void addResidualFlow(int u, int v, int flow) {
        Edge edge = edgeMap.get(u).get(v);
        Edge reverseEdge = edgeMap.get(v).get(u);

        if (edge != null) {
            edge.addFlow(flow);
        }

        if (reverseEdge != null) {
            reverseEdge.addFlow(-flow);
        }
    }

    public void printNetwork() {
        for (int u = 0; u < size; u++) {
            for (Edge edge : adjList[u]) {
                // Only print original edges (not residual edges with 0 capacity)
                if (edge.getCapacity() > 0) {
                    System.out.println("Edge " + edge.getFrom() + " -> " + edge.getTo() +
                            ", Flow = " + edge.getFlow() +
                            "/" + edge.getCapacity());
                }
            }
        }
    }
}