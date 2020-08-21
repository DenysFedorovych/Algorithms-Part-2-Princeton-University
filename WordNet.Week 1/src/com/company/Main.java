package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {
        WordNet wordNet = new WordNet("D:\\Programming\\WordNet\\synsets.txt","D:\\Programming\\WordNet\\hypernyms.txt" );
        Outcast outcast = new Outcast(wordNet);
        Scanner scan = new Scanner(System.in);
        while (true)
        {
            System.out.println("Give words in which you want to find the biggest outcast (Example : \"probability,statistics,mathematics,physics\" without quotes => outcast = probability)");
            String[] str = scan.nextLine().split(",");
            System.out.println(outcast.outcast(str));
        }

    }
}
