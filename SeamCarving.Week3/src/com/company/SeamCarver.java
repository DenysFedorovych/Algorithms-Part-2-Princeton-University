package com.company;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import  java.awt.Color;


public class SeamCarver
{
    private Picture picture;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture)
    {
        checkNull(picture);
        this.picture = new Picture(picture);

        energy = new double[height()][width()];

        for(int i = 0; i < width(); i++)
            for(int j = 0; j < height(); j++)
                energy[j][i] = energy(picture, i, j);
    }

   // dual-gradient energy function
    private double energy(Picture picture, int col, int row)
    {
        checkNull(picture);
        validateIndex(row, col);
        if(isBorder(row, col))
            return 1000;
        Color rightX = picture.get(col+1, row);
        Color leftX = picture.get(col-1, row);
        double Rx = rightX.getRed() -leftX.getRed();
        double Gx = rightX.getGreen() - leftX.getGreen();
        double Bx = rightX.getBlue() - leftX.getBlue();
        double deltaXsquare = Rx*Rx + Gx*Gx + Bx*Bx;

        Color upperY = picture.get(col, row+1);
        Color downY = picture.get(col, row-1);
        double Ry = upperY.getRed() - downY.getRed();
        double Gy = upperY.getGreen() - downY.getGreen();
        double By = upperY.getBlue() - downY.getBlue();
        double deltaYsquare = Ry*Ry + Gy*Gy + By*By;

        return Math.sqrt(deltaXsquare + deltaYsquare);
    }

    //check if pixel belong to borders
    private boolean isBorder(int row, int col)
    {
        validateIndex(row, col);
       return col == 0 || row == 0 || col == width() - 1 || row == height() - 1;
    }

    // current picture
    public Picture picture()
    {
        return picture;
    }

    // width of current picture
    public int width()
    {
        return picture().width();
    }

    // height of current picture
    public int height()
    {
        return picture().height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y)
    {
        validateIndex(y, x);
        return energy[y][x];
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam()
    {
        int[] verticalSeam = new int[height()];
        Index[][] edgeTo;
        double[][] distTo;

        edgeTo = new Index[height()][width()];
        distTo = new double[height()][width()];

        Index v = LastVertexInSP(edgeTo, distTo);

        int i = height() - 1;
        while(v.row != 0)
        {
            verticalSeam[i] = v.col;
            v = edgeTo[v.row][v.col];
            i--;
        }
        verticalSeam[0] = v.col;
        return verticalSeam;
    }

    private Index LastVertexInSP(Index[][] edgeTo, double[][] distTo)
    {
        AcyclicSP(edgeTo, distTo);
        
        Index optimalFinishVertex = new Index(height()-1, width()-1);
        double shortestLength = Double.MAX_VALUE;

        for(int j = 0; j < width() ; j++)
        {
            if(shortestLength > distTo[height()-1][j])
            {
                optimalFinishVertex = new Index(height()-1, j);
                shortestLength = distTo[height()-1][j];
               // System.out.println("distance to " + j + "= " + distTo[j][height() - 1]);
            }
        }
        //System.out.println("optimal finish vertex col= " + optimalFinishVertex.col);
        return optimalFinishVertex;
    }

    private void AcyclicSP(Index[][] edgeTo, double[][] distTo)
    {
        for (int i = 0; i < height(); i++)
            for (int j = 0; j < width(); j++)
            {
                if (i == 0)
                    distTo[i][j] = 0;
                else
                    distTo[i][j] = Double.POSITIVE_INFINITY;
            }

        for(int i = 0; i < height()-1; i++)
            for(int j = 0; j < width(); j++)
            {
                Index v = new Index(i, j);
                for (Index w : adj(v))
                {
                    relax(distTo, edgeTo, v, w);
                }
            }

    }
    private Index[] adj(Index v)
    {
        /// при ширине или высоте 1 сосед 1
        if(height() == 1)
            return null;
        else if(width() == 1)
            return new Index[]
                    {new Index(v.row + 1, v.col)};
        else if(v.col == 0)
        {
            return new Index[]
                    {new Index(v.row + 1, v.col), new Index(v.row + 1, v.col + 1)};
        }
        else if(v.col == width() - 1)
        {
            return new Index[]
                    {new Index(v.row + 1, v.col-1), new Index(v.row + 1, v.col)};
        }
        else
        {
            return new Index[]
                    {new Index(v.row + 1, v.col-1), new Index(v.row + 1, v.col), new Index(v.row + 1, v.col + 1)};
        }
    }
    private void relax(double[][] distTo, Index[][] edgeTo, Index v, Index w)//v->w
    {
        if (distTo[w.row][w.col] > distTo[v.row][v.col]  + energy[w.row][w.col])
        {
            distTo[w.row][w.col] = distTo[v.row][v.col]  + energy[w.row][w.col];
            edgeTo[w.row][w.col] = v;
        }
    }
    private class Index
    {
        public int row;
        public int col;
        public Index(int row, int col)
        {
            this.col = col;
            this.row = row;
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam()
    {
        traversePicture();
        int[] seam ;
        seam = findVerticalSeam();
        traversePicture();
        return seam;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam)
    {
        if(width() == 1)
            throw new IllegalArgumentException("width = 1 and we can`t delete");
        checkNull(seam);
        for(int i = 0; i < seam.length - 1; i++)
        {
           if(Math.abs(seam[i] - seam[i+1]) > 1)
               throw new IllegalArgumentException("difference between two neighbors is bigger then one");
        }
        Picture pic = new Picture(width()-1, height());

        for(int i = 0; i < height(); i++)
        {
            for(int j = 0; j < seam[i]; j++)
            {
                pic.setRGB(j, i, picture.getRGB(j, i));
            }

            for(int j = seam[i]; j < width() - 1; j++)
                pic.setRGB(j, i, picture.getRGB(j+1, i));
        }
        picture = pic;
        for(int i = 0; i < width(); i++)
            for(int j = 0; j < height(); j++)
                energy[j][i] = energy(picture, i, j);
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam)
    {
        traversePicture();
        removeVerticalSeam(seam);
        traversePicture();
    }
    private void traversePicture()
    {
        Picture traversePic = new Picture(height(), width());
        for(int i = 0; i < width(); i++)
            for(int j = 0; j < height(); j++)
            {
                traversePic.setRGB(j, i, picture.getRGB(i, j));
            }
        picture = traversePic;
        energy = new double[height()][width()];

        for(int i = 0; i < width(); i++)
            for(int j = 0; j < height(); j++)
            {
                energy[j][i] = energy(picture, i, j);
            }
    }
    private void validateIndex(int row, int col)
    {
        if(row < 0 || row >= height() || col < 0 || col >= width())
            throw new IllegalArgumentException("indexes are out of bonds");
    }
    private void checkNull(Object object)
    {
        if(object == null)
            throw new IllegalArgumentException();
    }
    //  unit testing (optional)
    public static void main(String[] args)
    {
        Picture picture = new Picture("chameleon.png");
        //picture.show();
        SeamCarver sc = new SeamCarver(picture);

        StdOut.printf("Displaying energy calculated for each pixel.\n");
        //SCUtility.showEnergy(sc);
        //picture = sc.traversePicture();
        SeamCarver sc1 = new SeamCarver(picture);
        picture.show();
        SCUtility.showEnergy(sc);
    }

}
