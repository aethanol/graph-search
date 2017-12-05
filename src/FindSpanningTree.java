import java.util.*;
import java.io.*;

/**
 * Driver program that reads in a graph and prints a minimum spanning tree
 * (Intentionally without comments.  Read through the code to understand what it does.)
 */

public class FindSpanningTree {
    public static void main(String[] args) {
        if(args.length != 2) {
            System.err.println("USAGE: java Paths <vertex_file> <edge_file>");
            System.exit(1);
        }

        MSTGraph g = readGraph(args[0],args[1]);

        Scanner console = new Scanner(System.in);
        Collection<Vertex> v = g.vertices();
        Collection<Edge> e = g.edges();
        System.out.println("Vertices are "+v);
        System.out.println("Edges are "+e);
        Set<Edge> span = g.kruskalMST();
        for(Edge ed : span){
            System.out.print(ed + " ");
        }

    }

    public static MSTGraph readGraph(String f1, String f2) {
        Scanner s = null;
        try {
            s = new Scanner(new File(f1));
        } catch(FileNotFoundException e1) {
            System.err.println("FILE NOT FOUND: "+f1);
            System.exit(2);
        }

        Collection<Vertex> v = new ArrayList<Vertex>();
        while(s.hasNext())
            v.add(new Vertex(s.next()));

        try {
            s = new Scanner(new File(f2));
        } catch(FileNotFoundException e1) {
            System.err.println("FILE NOT FOUND: "+f2);
            System.exit(2);
        }

        Collection<Edge> e = new ArrayList<Edge>();
        while(s.hasNext()) {
            try {
                Vertex a = new Vertex(s.next());
                Vertex b = new Vertex(s.next());
                int w = s.nextInt();
                e.add(new Edge(a,b,w));
            } catch (NoSuchElementException e2) {
                System.err.println("EDGE FILE FORMAT INCORRECT");
                System.exit(3);
            }
        }

        return new MSTGraph(v,e);
    }
}
