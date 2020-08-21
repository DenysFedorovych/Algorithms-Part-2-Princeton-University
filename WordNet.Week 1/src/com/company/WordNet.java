package com.company;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Topological;
import java.util.LinkedList;
import java.util.List;


public class WordNet
{
    private Digraph G; // DiGraph, that implement all words as vertices and connections between them
    private static int wordNumber;// the number of the words
    private RedBlackBST<String, List<Synonyms>> nouns = new RedBlackBST<>();// all words with their indexes and synonyms
                                                                           // (use BinarySearchTree because it is the best structure for fast search)
    private SAP sap;//object that has implementation of the shortest length and common ancestors
    private Synonyms[] syn;

    private In HypernymsFile;//file with connections between nouns(line format: "id0(that connect with every next id number 1-N), id1, id2, ...., idN")

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {
        checkNull(synsets);
        checkNull(hypernyms);

        int id = readSynsets(synsets);
        wordNumber = id;
        G = new Digraph(wordNumber + 1);
        HypernymsFile = new In(hypernyms);
        readHypernyms();
        checkCycle();
        checkMultipleRoots();

        sap = new SAP(G);
    }

    private int readSynsets(String synsets)
    {
        In SynsetsFile = new In(synsets);
        int id = -1;
        while(!SynsetsFile.isEmpty())
        {
             String line = SynsetsFile.readLine();

            String[] val = line.split(",");
            String[] synonymsInLine = val[1].split(" ");
            id = Integer.parseInt(val[0]);


            Synonyms synonyms = new Synonyms(id, synonymsInLine);
            for(String word:synonymsInLine)
            {
                List<Synonyms> lookingSynonyms = nouns.get(word);
                if(lookingSynonyms == null)
                {
                    lookingSynonyms = new LinkedList<>();
                }
                lookingSynonyms.add(synonyms);
                nouns.put(word, lookingSynonyms);
            }
        }
        arrayWithWord(id);
        return id;
    }

    private class Synonyms
    {
        int id;
        String[] synonyms;

        private Synonyms(int id, String[] synonyms)
        {
            this.synonyms = synonyms;
            this.id = id;
        }
    }

    private void arrayWithWord(int id)//make for fast search of the word using its id
    {
        syn = new Synonyms[id + 1];
        for (String key : nouns.keys())
        {
            List<Synonyms> symbols = nouns.get(key);
            for (Synonyms s : symbols)
            {
                syn[s.id] = s;
            }
        }
    }

    private void readHypernyms()
    {
        String line;
        int number;
        while(HypernymsFile.hasNextLine())
        {
            line = HypernymsFile.readLine();
            String[] split = line.split(",");

            number = Integer.parseInt(split[0]);
            for(int i = 1; i < split.length; i++)
            {
                G.addEdge(number, Integer.parseInt(split[i]));
            }
        }
    }

    private void checkCycle()
    {
        Topological traversal = new Topological(G);
        if (!traversal.hasOrder())
        {
            throw new IllegalArgumentException("Graph has a cycle");
        }
    }

    private void checkMultipleRoots()
    {
        int root = -1;
        for (int v = 0; v < G.V(); v++) {
            if (G.outdegree(v) == 0)
            {
                if (root >= 0)
                {
                    throw new IllegalArgumentException("Graph has multiple roots");
                }
                root = v;
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        return nouns.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        return nouns.contains(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB)
    {
        validateArguments(nounA, nounB);
        return sap.length(getAllWord(nounA), getAllWord(nounB));
    }
    private Iterable<Integer> getAllWord(String noun)
    {
        LinkedList <Integer> words = new LinkedList<Integer>();
        for(Synonyms w : nouns.get(noun))
            words.push(w.id);

        return words;
    }

    private void validateArguments(String nounA, String nounB)
    {
        if(!isNoun(nounA)||!isNoun(nounB))
            throw new IllegalArgumentException("Input nouns don`t exist in WordNet ");
        checkNull(nounA);
        checkNull(nounB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB)
    {
        validateArguments(nounA, nounB);
        int ancestor = sap.ancestor(getAllWord(nounA), getAllWord(nounB));
        if (ancestor < 0) {
            return null;
        }

        return String.join(" ", syn[ancestor].synonyms);
    }

    private void checkNull(Object object)
    {
        if(object == null)
            throw new IllegalArgumentException("the argument is null");
    }
}