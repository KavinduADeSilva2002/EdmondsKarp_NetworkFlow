# 🔗 Network Flow Algorithm Project

This project implements a **maximum flow algorithm** in Java to solve network flow problems using benchmark input files. It models flow networks with directed edges and calculates the maximum possible flow from a source node to a sink node.

## 📂 Project Structure
```plaintext
NetworkFlowProject/
├── NetworkFlowProject.iml
├── benchmarks/
│   ├── bridge_1.txt
│   ├── bridge_2.txt
│   ├── bridge_3.txt
│   ├── bridge_4.txt
│   ├── bridge_5.txt
│   ├── bridge_10.txt
│   ├── bridge_11.txt
│   ├── bridge_12.txt
│   ├── bridge_13.txt
│   ├── bridge_14.txt
│   ├── bridge_15.txt
│   ├── bridge_16.txt
│   ├── bridge_17.txt
│   ├── bridge_18.txt
│   ├── bridge_19.txt
├── .idea/
│   ├── .gitignore
│   ├── misc.xml
│   ├── modules.xml
│   ├── workspace.xml
├── src/
│   └── networkflow/
│       ├── Main.java              # Program entry point
│       ├── Edge.java              # Defines the structure of an edge
│       ├── FlowNetwork.java       # Graph structure and logic
│       ├── InputParser.java       # Parses benchmark input files
│       ├── MaxFlowSolver.java     # Core maximum flow algorithm


