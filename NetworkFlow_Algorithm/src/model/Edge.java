package model;

public class Edge {
    private final int from;
    private final int to;
    private final int capacity;
    private int flow;

    public Edge(int from, int to, int capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.flow = 0;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFlow() {
        return flow;
    }

    public void addFlow(int additionalFlow) {
        this.flow += additionalFlow;
    }

    public int getResidualCapacity() {
        return capacity - flow;
    }

    @Override
    public String toString() {
        return from + " -> " + to + " (" + flow + "/" + capacity + ")";
    }
}