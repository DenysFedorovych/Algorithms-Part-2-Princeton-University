# :collision: Word net + founding the outcast in a set of words :collision:
---
![WordNet](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/wordnet-event.png)
## Goal:
*    Make a word net, that have majority English dictionary nouns and semantic connection between them.
*    Create a function that can find semantic distance between two words
*    Create a function that can find a common semantic ancestor of two words
*    Create opportunity for fast search for outcast in a set of words
*    Do it very fast
## What was used:
1.    Digraph
2.    Topological Sort
3.    Red-Black Binary Search Tree
4.    Breadth-First Directed Paths
5.    Depth-First Search
## How to use:
*    Import "algs4.jar" library, "synsets.txt" and "hypernyms.txt" into project
*    Run class Main
*    Give a set of words where you want to find outcast
*    If you want to find distance => 

` WordNet wordNet = new WordNet("D:\\Programming\\WordNet\\synsets.txt","D:\\Programming\\WordNet\\hypernyms.txt" ); ` 

` int distance = distance( "nounA", "nounB") `

(but choose your path for "synsets.txt" and "hypernyms.txt") 
