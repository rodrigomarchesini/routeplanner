package com.walmart.routeplanner.processor.map.importer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Generator of map files.
 * Useful to generate large maps.
 * 
 * @author Rodrigo Marchesini
 */
public class FileMapGenerator {

    public static void main(String[] args) throws IOException {
        int edges = Integer.valueOf(args[0]);
        int vertexes = Integer.valueOf(args[1]);
        String filename = args[2];

        List<String> points = new ArrayList<String>(edges);
        int i = 0;
        while (i++ < vertexes) {
            points.add(UUID.randomUUID().toString().replace("-", ""));
        }

        File f = new File(filename);
        if (!f.exists())
            f.createNewFile();

        try (
                PrintWriter w = new PrintWriter(f)) {

            Random r = new Random();
            int j = 0;
            while (j++ < vertexes) {
                w.write(points.get(r.nextInt(edges)) + " " +
                        points.get(r.nextInt(edges)) + " " +
                        r.nextInt(500) +
                        "\n");
            }
        }
    }
}
