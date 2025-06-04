package algorithm;

import model.FlowNetwork;
import model.Edge;

import java.util.*;

public class EdmondsKarpSolver {
    private final FlowNetwork network;
    private final int[] parentNode;  // Stores the parent node in BFS path
    private final Edge[] parentEdge; // Stores the actual edge used in BFS path

    public EdmondsKarpSolver(FlowNetwork network) {
        this.network = network;
        this.parentNode = new int[network.getSize()];
        this.parentEdge = new Edge[network.getSize()];
    }

    public int findMaxFlow() {
        int maxFlow = 0;
        int iterationCount = 0;

        while (bfs()) {
            iterationCount++;
            int pathFlow = Integer.MAX_VALUE;
            int v = network.getSink();

            // Find bottleneck (minimum residual capacity) along the path
            while (v != network.getSource()) {
                int u = parentNode[v];
                int residualCapacity = 0;

                for (Edge edge : network.getAdjacencyList(u)) {
                    if (edge.getTo() == v) {
                        residualCapacity = edge.getResidualCapacity();
                        break;
                    }
                }

                pathFlow = Math.min(pathFlow, residualCapacity);
                v = u;
            }

            // Augment flow along the path
            v = network.getSink();

            System.out.println("\n--- Iteration " + iterationCount + " ---");
            System.out.print("Augmenting Path: ");

            // First trace the path for printing
            List<Integer> pathNodes = new ArrayList<>();
            List<Edge> pathEdges = new ArrayList<>();

            int currentNode = network.getSink();
            while (currentNode != network.getSource()) {
                pathNodes.add(currentNode);
                pathEdges.add(parentEdge[currentNode]);
                currentNode = parentNode[currentNode];
            }
            pathNodes.add(network.getSource());

            Collections.reverse(pathNodes);
            Collections.reverse(pathEdges);

            // Print the path
            for (int i = 0; i < pathNodes.size(); i++) {
                System.out.print(pathNodes.get(i));
                if (i < pathNodes.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();

            // Print edges with residual capacities
            System.out.println("Path Edges:");
            for (Edge edge : pathEdges) {
                System.out.println("  " + edge.getFrom() + " -> " + edge.getTo() +
                        " (flow/cap: " + edge.getFlow() + "/" + edge.getCapacity() +
                        ", residual: " + edge.getResidualCapacity() + ")");
            }

            // Now actually augment the flow
            v = network.getSink();
            while (v != network.getSource()) {
                int u = parentNode[v];
                network.addResidualFlow(u, v, pathFlow);
                v = u;
            }

            maxFlow += pathFlow;
            System.out.println("Bottleneck Flow: " + pathFlow);
            System.out.println("Current Max Flow: " + maxFlow);
        }

        return maxFlow;
    }

    private boolean bfs() {
        Arrays.fill(parentNode, -1);
        Arrays.fill(parentEdge, null);

        boolean[] visited = new boolean[network.getSize()];
        Queue<Integer> queue = new LinkedList<>();

        queue.add(network.getSource());
        visited[network.getSource()] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (Edge edge : network.getAdjacencyList(u)) {
                int v = edge.getTo();

                if (!visited[v] && edge.getResidualCapacity() > 0) {
                    parentNode[v] = u;
                    parentEdge[v] = edge;
                    visited[v] = true;

                    if (v == network.getSink()) {
                        return true;  // Found an augmenting path
                    }

                    queue.add(v);
                }
            }
        }

        return false;  // No augmenting path found
    }
}