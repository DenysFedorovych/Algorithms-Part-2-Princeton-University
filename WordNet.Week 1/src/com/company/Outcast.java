package com.company;

import java.util.Scanner;

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
    }
}