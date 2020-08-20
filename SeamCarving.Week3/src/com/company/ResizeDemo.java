package com.company;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Scanner;

public class ResizeDemo
{
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please, give a name of picture to remove \n P.S: Example: \"chameleon.png\" (without quotes)");
        String picture = scan.next();
        Picture inputImg = new Picture(picture);
        System.out.println("Please, give a number of the columns to remove: ");
        int removeColumns = scan.nextInt();
        System.out.println("Please, give a number of the rows to remove: ");
        int removeRows = scan.nextInt();

        StdOut.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
        SeamCarver sc = new SeamCarver(inputImg);

        Stopwatch sw = new Stopwatch();
        System.out.print("loading");
        int NumberOfPoints = 0;
        for (int i = 0; i < removeRows; i++)
        {
                System.out.print(".");
            int[] horizontalSeam = sc.findHorizontalSeam();
            sc.removeHorizontalSeam(horizontalSeam);
        }
        System.out.print("\n");

        for (int i = 0; i < removeColumns; i++) {
            int[] verticalSeam = sc.findVerticalSeam();
            sc.removeVerticalSeam(verticalSeam);
        }
        Picture outputImg = sc.picture();

        StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());

        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
        inputImg.show();
        outputImg.show();
    }

}