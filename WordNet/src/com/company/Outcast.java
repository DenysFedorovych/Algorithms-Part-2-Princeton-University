//package com.company;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast
{
    private WordNet wordNet;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet)
    {
        checkNull(wordnet);
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns)
    {
        int maxD = 0;
        int maxIndex = 0;
        for(int i = 0; i < nouns.length; i++)
        {
            int d = 0;
            for(int j = 0; j < nouns.length; j++)
                if(i != j)
                {
                    d += wordNet.distance(nouns[i], nouns[j]);
                }
            if ( d > maxD )
            {
                maxD = d;
                maxIndex = i;
            }
        }
        return nouns[maxIndex];
    }

    private void checkNull(Object object)
    {
        if(object == null)
            throw new IllegalArgumentException("the argument is null");
    }

    public static void main(String[] args)
    {

        WordNet wordNet = new WordNet("D:\\Programming\\WordNet\\synsets.txt","D:\\Programming\\WordNet\\hypernyms.txt" );
        Outcast outcast = new Outcast(wordNet);
        String[] str = {"probability", "statistics", "mathematics", "physics"};
        System.out.println(outcast.outcast(str));
    }
}