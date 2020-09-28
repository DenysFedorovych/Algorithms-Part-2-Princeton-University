package com.company;

// R-way trie node
public class Node
{
    public static final int R = 256;
    public Node[] next = new Node[R];
    public boolean isString;
}
