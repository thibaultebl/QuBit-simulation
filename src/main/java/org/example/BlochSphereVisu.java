package org.example;

import org.example.Server.BlochServer;

/**
 * Entry point for the Bloch-sphere desktop visualization.
 * Boots the local HTTP bridge that the TypeScript / Electron front-end polls.
 *
 * Run with:   mvn exec:java -Dexec.mainClass=org.example.BlochSphereVisu
 */
public class BlochSphereVisu {
    public static void main(String[] args) throws Exception {
        new BlochServer().start();
        Thread.currentThread().join();
    }
}
