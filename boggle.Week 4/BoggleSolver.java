package com.company;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class BoggleSolver
{
    private Trie vocabulary;
    private StringBuilder word = new StringBuilder("");
    private boolean[][] boardTable;
    private int rows;
    private int cols;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        vocabulary = new Trie();
        for(String d : dictionary)
        {
            vocabulary.add(d);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        LinkedList<String> words = new LinkedList<String>();
        rows = board.rows();
        cols = board.cols();
        boardTable = new boolean[rows][cols];
        makeBoardEmpty();
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
            {
                boardTable[i][j] = true;
                word.append(board.getLetter(i, j));
                if(board.getLetter(i, j) == 'Q')
                {
                    word.append('U');
                    get(vocabulary.root.next['Q'].next['U'], allIndexes(i, j), board, words);
                }
                else
                    get(vocabulary.root.next[board.getLetter(i, j)], allIndexes(i, j), board, words);
                word.delete(0, word.length());
                boardTable[i][j] = false;
            }
        return words;
    }

    private void makeBoardEmpty()
    {
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                boardTable[i][j] = false;
    }

    private void get(Node x, LinkedList<Index> indexes, BoggleBoard board, LinkedList<String> words)
    {
        if(x == null)
            return;
        if (x.isString)
        {
            if(word.toString().length() >= 3 && !words.contains(word.toString()))
                words.add(word.toString());
        }
        for(Index i : indexes)
        {
            boardTable[i.row][i.col] = true;
            word.append(board.getLetter(i.row, i.col));
            if(board.getLetter(i.row, i.col) == 'Q')
            {
                word.append('U');
                if(x.next['Q'] != null && x.next['Q'].next['U'] != null)
                {
                    get(x.next['Q'].next['U'], allIndexes(i.row, i.col), board, words);
                }
            }
            else
                {
                    if(x.next[board.getLetter(i.row, i.col)] != null)
                    {
                        get(x.next[board.getLetter(i.row, i.col)], allIndexes(i.row, i.col), board, words);
                    }
                }
            boardTable[i.row][i.col] = false;
            if(board.getLetter(i.row, i.col) == 'Q')
                word.delete(word.length()-2, word.length());
            else
                word.delete(word.length()-1, word.length());
        }
    }

    private LinkedList<Index> allIndexes(int row, int col)
    {
        LinkedList<Index> indexes = new LinkedList<Index>();
        if(col+1 < cols && boardTable[row][col+1] == false)
            indexes.add(new Index(row, col+1));
        if(col-1 >= 0 && boardTable[row][col-1] == false)
            indexes.add(new Index(row, col-1));

        if(row + 1 < rows)
        {
            if(boardTable[row+1][col] == false)
                indexes.add(new Index(row + 1, col));
            if(col+1 < cols && boardTable[row+1][col+1] == false)
                indexes.add(new Index(row+1, col+1));
            if(col-1 >= 0 && boardTable[row+1][col-1] == false)
                indexes.add(new Index(row+1, col-1));
        }
        if(row - 1 >= 0)
        {
            if(boardTable[row - 1][col] == false)
                indexes.add(new Index(row - 1, col));
            if(col+1 < cols && boardTable[row-1][col+1] == false)
                indexes.add(new Index(row-1, col+1));
            if(col-1 >= 0 && boardTable[row-1][col-1] == false)
                indexes.add(new Index(row-1, col-1));
        }
        return indexes;
    }

    private class Index
    {
        int row;
        int col;
        Index(int row, int col)
        {
            this.col = col;
            this.row = row;
        }
    }
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
        if(!vocabulary.contains(word))
            return 0;
        switch (word.length())
        {
            case 0:
                return 0;
            case 1:
                return 0;
            case 2:
                return 0;
            case 3 :
                return 1;
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    public static void main(String[] args) {
        In in = new In("dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board-couscous.txt");
        System.out.println(board.toString());
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word + " - it's a word");
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
