package com.company;
import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;
import java.util.LinkedList;

public class Trie
{
    public Node root;      // root of trie
    private int n;          // number of keys in trie

    public Trie(){}

    public boolean contains(String key)
    {
        if (key == null)
            throw new IllegalArgumentException("argument to contains() is null");
        Node x = get(root, key, 0);
        if (x == null)
            return false;
        return x.isString;
    }

    private Node get(Node x, String key, int d)
    {
        if (x == null)
            return null;
        if (d == key.length())
            return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d+1);
    }
    public void add(String key)
    {
        if (key == null)
            throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d)
    {
        if (x == null)
            x = new Node();
        if (d == key.length())
        {
            if (!x.isString)
                n++;
            x.isString = true;
        }
        else
            {
                char c = key.charAt(d);
                x.next[c] = add(x.next[c], key, d+1);
            }
        return x;
    }

}