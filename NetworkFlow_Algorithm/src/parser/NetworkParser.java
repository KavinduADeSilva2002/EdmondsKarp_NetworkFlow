package parser;

import model.FlowNetwork;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NetworkParser {
    public static FlowNetwork parseFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String firstLine = reader.readLine();
            if (firstLine == null) {
                throw new IOException("The file is empty!");
            }

            // Parse number of nodes
            int size;
            try {
                size = Integer.parseInt(firstLine.trim());
                if (size <= 1) {
                    throw new IOException("Invalid network size: " + size + ". Must be at least 2 nodes.");
                }
            } catch (NumberFormatException e) {
                throw new IOException("Invalid number format in the first line. Expected the number of nodes.");
            }

            // In this format, source is always 0 and sink is always (size-1)
            int source = 0;
            int sink = size - 1;

            FlowNetwork network = new FlowNetwork(size, source, sink);

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue; // skip blank lines

                String[] parts = line.split("\\s+");
                if (parts.length < 3) {
                    throw new IOException("Line " + lineNumber + ": Invalid edge format. Must have from, to, capacity.");
                }

                int from, to, capacity;

                try {
                    from = Integer.parseInt(parts[0]);
                    to = Integer.parseInt(parts[1]);
                    capacity = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    throw new IOException("Line " + lineNumber + ": Invalid number format for edge parameters.");
                }

                // Validate edge parameters
                if (from < 0 || from >= size) {
                    throw new IOException("Line " + lineNumber + ": Invalid source node: " + from +
                            ". Must be between 0 and " + (size - 1) + ".");
                }

                if (to < 0 || to >= size) {
                    throw new IOException("Line " + lineNumber + ": Invalid target node: " + to +
                            ". Must be between 0 and " + (size - 1) + ".");
                }

                if (capacity < 0) {
                    throw new IOException("Line " + lineNumber + ": Invalid capacity: " + capacity +
                            ". Must be non-negative.");
                }

                network.addEdge(from, to, capacity);
            }

            // Validate that the network is properly formed
            if (network.getAdjacencyList(source).isEmpty()) {
                throw new IOException("The source node " + source + " has no outgoing edges!");
            }

            boolean sinkHasIncomingEdges = false;
            for (int i = 0; i < size; i++) {
                for (var edge : network.getAdjacencyList(i)) {
                    if (edge.getTo() == sink && edge.getCapacity() > 0) {
                        sinkHasIncomingEdges = true;
                        break;
                    }
                }
                if (sinkHasIncomingEdges) break;
            }

            if (!sinkHasIncomingEdges) {
                throw new IOException("The sink node " + sink + " has no incoming edges!");
            }

            return network;
        }
    }
}