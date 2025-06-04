package main;

import algorithm.EdmondsKarpSolver;
import model.FlowNetwork;
import parser.NetworkParser;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("#,##0.00");

        boolean continueRunning = true;

        while (continueRunning) {
            boolean validInput = false;
            FlowNetwork network = null;
            String filename = "";

            while (!validInput) {
                try {
                    System.out.println("\n-----------------------------------------");
                    System.out.println("Enter the filename to test (e.g., 'bridge_1.txt') or 'exit' to quit: ");
                    String userInput = scanner.nextLine().trim();

                    // Exit condition
                    if (userInput.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting program.");
                        continueRunning = false;
                        break;
                    }

                    // Validate that input is not empty
                    if (userInput.isEmpty()) {
                        System.out.println("Error: Input cannot be empty. Please try again.");
                        continue;
                    }

                    filename = userInput;

                    // Handle simple number inputs by converting to bridge_X.txt format
                    if (userInput.matches("\\d+")) {
                        filename = "bridge_" + userInput + ".txt";
                    }

                    // Validate file extension
                    if (!filename.toLowerCase().endsWith(".txt")) {
                        System.out.println("Error: File must have a .txt extension. Please try again.");
                        continue;
                    }

                    String basePath = System.getProperty("user.dir");
                    String fullPath = basePath + File.separator + "benchmarks" + File.separator + filename;

                    // Check if file exists
                    File file = new File(fullPath);
                    if (!file.exists()) {
                        System.out.println("Error: File '" + filename + "' not found in the benchmarks directory.");
                        System.out.println("Please check that the file exists at: " + fullPath);
                        continue;
                    }

                    System.out.println("\nProcessing file: " + filename + "\n");

                    // Start timing file parsing
                    long parsingStartTime = System.nanoTime();

                    try {
                        network = NetworkParser.parseFromFile(fullPath);
                        validInput = true;  // File successfully parsed

                        // Calculate parsing time
                        long parsingEndTime = System.nanoTime();
                        double parsingTimeMs = (parsingEndTime - parsingStartTime) / 1_000_000.0;

                        System.out.println("File parsing completed in " + df.format(parsingTimeMs) + " ms");

                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid number format in the file. Please check the file format.");
                        System.out.println("Details: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Error reading the file: " + e.getMessage());
                    }

                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // If user chose to exit, break out of the loop
            if (!continueRunning) {
                break;
            }

            // If we have a valid network, process it
            if (network != null) {
                try {
                    System.out.println("--------- Starting Max Flow Computation ---------");
                    System.out.println("Source --> " + network.getSource() + ", Target --> " + network.getSink());
                    System.out.println("Initial Network:");
                    network.printNetwork();
                    System.out.println();

                    // Start timing algorithm execution
                    long algorithmStartTime = System.nanoTime();

                    EdmondsKarpSolver solver = new EdmondsKarpSolver(network);
                    int maxFlow = solver.findMaxFlow();

                    // Calculate algorithm execution time
                    long algorithmEndTime = System.nanoTime();
                    double algorithmTimeMs = (algorithmEndTime - algorithmStartTime) / 1_000_000.0;

                    System.out.println("\nMaximum Flow: " + maxFlow);
                    System.out.println("\nAlgorithm execution completed in " + df.format(algorithmTimeMs) + " ms");

                    // Calculate total execution time
                    double totalTimeMs = (algorithmEndTime - algorithmStartTime) / 1_000_000.0;
                    System.out.println("Total time for processing '" + filename + "': " + df.format(totalTimeMs) + " ms");

                } catch (Exception e) {
                    System.out.println("Error during max flow computation: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        scanner.close();
        System.out.println("Program terminated.");
    }
}