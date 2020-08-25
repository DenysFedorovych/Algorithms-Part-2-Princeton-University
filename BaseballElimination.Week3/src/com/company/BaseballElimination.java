package com.company;
import edu.princeton.cs.algs4.BinarySearchST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class BaseballElimination
{
    private BinarySearchST<String, int[]> bst = new BinarySearchST<String, int[]>();
    private int numberCol;
    private int numberOfTeams;
    private int maxWin;
    private String teamWithMaxWin;
    private String[] teams;
    private int[][] games;
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename)
    {
        In file = new In(filename);
        int index = 0;
        if(!file.isEmpty())
            numberOfTeams = Integer.parseInt(file.readLine());
        games = new int[numberOfTeams][numberOfTeams];
        teams = new String[numberOfTeams];
        if(!file.isEmpty())
        {
            ArrayList<String> line = new ArrayList<String>();
            for(String str : file.readLine().split(" "))
                if(str.equals(""))
                    continue;
                else
                    line.add(str);
            String commandName = line.get(0);
            numberCol = line.size() - 1;
            int[] dataAndNumber = new int[numberCol+1];

            for(int i = 1; i < numberCol + 1; i++)
                dataAndNumber[i - 1] = Integer.parseInt(line.get(i));
            dataAndNumber[numberCol] = index;
            teams[index] = commandName;
            for(int i = 3; i < numberCol; i++)
                games[index][i-3] = dataAndNumber[i];

            index++;
            maxWin = dataAndNumber[0];
            teamWithMaxWin = commandName;
            bst.put(commandName, dataAndNumber);
        }
        while(file.hasNextLine())
        {
            ArrayList<String> line = new ArrayList<String>();
            for(String str : file.readLine().split(" "))
                if(str.equals(""))
                    continue;
                else
                    line.add(str);
            String commandName = line.get(0);
            int[] dataAndNumber = new int[numberCol+1];

            for(int i = 1; i < numberCol + 1; i++)
                dataAndNumber[i - 1] = Integer.parseInt(line.get(i));
            dataAndNumber[numberCol] = index;
            teams[index] = commandName;
            for(int i = 3; i < numberCol; i++)
                games[index][i-3] = dataAndNumber[i];
            index++;
            if(dataAndNumber[0] > maxWin)
            {
                maxWin = dataAndNumber[0];
                teamWithMaxWin = commandName;
            }
            bst.put(commandName, dataAndNumber);
        }

    }

    // number of teams
    public int numberOfTeams()
    {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams()
    {
        return Arrays.asList(teams);
    }

    // number of wins for given team
    public int wins(String team)
    {
        validateTeam(team);
        return bst.get(team)[0];
    }

    // number of losses for given team
    public int losses(String team)
    {
        validateTeam(team);
        return bst.get(team)[1];
    }

    // number of remaining games for given team
    public int remaining(String team)
    {
        validateTeam(team);
        return bst.get(team)[2];
    }
    private int remain(String team)
    {
        validateTeam(team);
        int index = bst.get(team)[numberCol];
       // System.out.println();
        int remaining = 0 ;
        int k = 0;
        for(int i = 0; i < numberOfTeams(); i++)
        {
            for(int j = k; j < numberOfTeams(); j++)
            {
                if(i == index || j == index)
                    continue;
                if(games[i][j] == 0)
                    continue;
                remaining ++;
            }
            k++;
        }
        return remaining;
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2)
    {
        validateTeam(team1);
        validateTeam(team2);
        int index1 = bst.get(team1)[numberCol];
        int index2 = bst.get(team2)[numberCol];
        return games[index1][index2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team)
    {
        validateTeam(team);
        if(trivialElimination(team) )
            return true;
        if(NontrivialElimination(team))
            return true;
        return false;
    }
    private boolean trivialElimination(String team)
    {
        validateTeam(team);
        if(wins(team)+remaining(team) < maxWin)
            return true;
        else return false;
    }
    private boolean NontrivialElimination(String team)
    {
        validateTeam(team);
        FlowNetwork flowNetwork = new FlowNetwork((numberOfTeams()-1) + remain(team) + 2);
        int s = 0;
        int t = ( numberOfTeams()-1 ) + remain(team) + 1 ;
        int v = 1;
        int index = bst.get(team)[numberCol];

        for(int i = 0; i < numberOfTeams(); i++)
        {
            if(i == index)
                continue;

            int l =remain(team)+1 + i;
            FlowEdge e4 = new FlowEdge(l, t, wins(team) + remaining(team) - wins(teams[i]));
            flowNetwork.addEdge(e4);
            for(int j = i; j < numberOfTeams(); j++)
            {
                if(games[i][j] == 0)
                    continue;
                if(j == index)
                    continue;

                FlowEdge e = new FlowEdge(s, v , games[i][j]);
                flowNetwork.addEdge(e);

                FlowEdge e2 = new FlowEdge(v, remain(team)+1 + i, Double.POSITIVE_INFINITY);

                FlowEdge e3 = new FlowEdge(v, remain(team)+1 + j, Double.POSITIVE_INFINITY);
                v++;
                flowNetwork.addEdge(e2);
                flowNetwork.addEdge(e3);


            }
        }
        //System.out.println(flowNetwork.toString());
        FordFulkerson FF = new FordFulkerson(flowNetwork, s, t);
        for(FlowEdge edge: flowNetwork.adj(s))
            if(edge.flow() != edge.capacity())
                return true;

        return false;
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team)
    {
        validateTeam(team);
        LinkedList<String> list = new LinkedList<>();
        if(!isEliminated(team))
            return null;
        if(trivialElimination(team))
        {
            list.add(teamWithMaxWin);
            return list;
        }
        FlowNetwork flowNetwork = new FlowNetwork((numberOfTeams()-1) + remain(team) + 2);
        int s = 0;
        int t = ( numberOfTeams()-1 ) + remain(team) + 1 ;
        int v = 1;
        int index = bst.get(team)[numberCol];

        for(int i = 0; i < numberOfTeams(); i++)
        {
            if(i == index)
                continue;

            int l =remain(team)+1 + i;
            FlowEdge e4 = new FlowEdge(l, t, wins(team) + remaining(team) - wins(teams[i]));
            flowNetwork.addEdge(e4);
            for(int j = i; j < numberOfTeams(); j++)
            {
                if(games[i][j] == 0)
                    continue;
                if(j == index)
                    continue;

                FlowEdge e = new FlowEdge(s, v , games[i][j]);
                flowNetwork.addEdge(e);

                FlowEdge e2 = new FlowEdge(v, remain(team)+1 + i, Double.POSITIVE_INFINITY);

                FlowEdge e3 = new FlowEdge(v, remain(team)+1 + j, Double.POSITIVE_INFINITY);
                v++;
                flowNetwork.addEdge(e2);
                flowNetwork.addEdge(e3);


            }
        }
        //System.out.println(flowNetwork.toString());
        FordFulkerson FF = new FordFulkerson(flowNetwork, s, t);
        for(int i = 0; i < teams.length; i++)
        {
            if(FF.inCut(remain(team)+i+1))
                list.add(teams[i]);
        }
        return list;
    }
    private void validateTeam(String team)
    {
        if(!bst.contains(team))
            throw new IllegalArgumentException("don`t contains");
        if(team.equals(null))
            throw new IllegalArgumentException("team is null");
    }

    public static void main(String[] args)
    {
        System.out.println("Please, give a name of test-client file (Example: \"teams5.txt\" without quotes ):");
        Scanner scan = new Scanner(System.in);
        String file = scan.next();
        BaseballElimination division = new BaseballElimination(file);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                System.out.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    System.out.print(t + " ");
                }
                System.out.println("}");
            }
            else {
                System.out.println(team + " is not eliminated");
            }
    }
    }
}
