package com.company;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;

import java.util.LinkedList;

public class SAP
{
    private final Digraph G;
    private final Iterable<Integer> tSort;

    private class Ancestor
    {
        int distance;
        int ancestor;

        Ancestor(int ancestor, int distance)
        {
            this.ancestor = ancestor;
            this.distance = distance;
        }
    }

    // constructor takes a digraph
    public SAP(Digraph G)
    {
        checkNull(G);
        this.G = G;
        DepthFirstOrder traversal = new DepthFirstOrder(G);
        tSort = traversal.reversePost();
    }

    private Ancestor commonAncestor( Iterable<Integer> v, Iterable<Integer> w)
    {
        validateArgs(v, w);
        BreadthFirstDirectedPaths paths1 = new BreadthFirstDirectedPaths(G, v);//path from v to common ancestor
        BreadthFirstDirectedPaths paths2 = new BreadthFirstDirectedPaths(G, w);//path from w to common ancestor

        int minDistance = Integer.MAX_VALUE;
        Ancestor ancestor = null;

        for (int root : tSort)
        {
            if (paths1.hasPathTo(root) && paths2.hasPathTo(root))
            {
                int distance = paths1.distTo(root) + paths2.distTo(root);
               // System.out.println("distance = " + distance);
                if (distance >= 0 && distance < minDistance)
                {
                    minDistance = distance;
                    ancestor = new Ancestor(root, distance);
                }
            }
            //System.out.println(minDistance);
        }
        return ancestor;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {
        checkNull(v);
        checkNull(w);
        LinkedList<Integer> V = new LinkedList<Integer>();
        LinkedList<Integer> W = new LinkedList<Integer>();
        V.push(v);
        W.push(w);
        return length(V, W);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)
    {
        checkNull(v);
        checkNull(w);
        LinkedList<Integer> V = new LinkedList<Integer>();
        LinkedList<Integer> W = new LinkedList<Integer>();
        V.push(v);
        W.push(w);
        return ancestor(V, W);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        validateArgs(v, w);
        Ancestor common = commonAncestor(v, w);
        if (common != null)
        {
            return common.distance;
        }
        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        validateArgs(v, w);
        Ancestor common = commonAncestor(v, w);
        if (common != null)
        {
            return common.ancestor;
        }
        return -1;
    }

    private boolean isValidVertex(int v)
    {
        return (v >= 0) && (v <= G.V() - 1);
    }

    private void validateArgs(Iterable<Integer> v, Iterable<Integer> w)
    {
        checkNull(v);
        checkNull(w);
        for (int V : v) {
            if (!isValidVertex(V)) {
                throw new IllegalArgumentException("Invalid origin vertex " + V);
            }
        }
        for (int W : w) {
            if (!isValidVertex(W)) {
                throw new IllegalArgumentException("Invalid destination vertex " + W);
            }
        }
    }

    private void checkNull(Object object)
    {
        if(object == null)
            throw new IllegalArgumentException("the argument is null");
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
        /*
        Digraph G = new Digraph(13);
        G.addEdge(7, 3);
        G.addEdge(8, 3);
        G.addEdge(3, 1);
        G.addEdge(4, 1);
        G.addEdge(5, 1);
        G.addEdge(9, 5);
        G.addEdge(10, 5);
        G.addEdge(11, 10);
        G.addEdge(12, 10);
        G.addEdge(1, 0);
        G.addEdge(2, 0);
        SAP sap = new SAP(G);
        System.out.println(sap.length(3, 3));
        /*In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }*/
    }
}
