package org.example;

import org.example.Server.BlochServer;

public class BlochSphereVisu {
    public static void main(String[] args) throws Exception {
        new BlochServer().start();
        Thread.currentThread().join();
    }
}
