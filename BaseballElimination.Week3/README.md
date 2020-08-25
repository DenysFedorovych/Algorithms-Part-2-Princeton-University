# :baseball: The baseball elimination problem :baseball:
## Goal:  
*    Detect the trivial elimination (team cann`t be the winner, because (all her previous win) + (all her games, that are in future) < previous win of another team)
*    Detect the nontrivial elimination using maxFlow ( [wiki](https://en.wikipedia.org/wiki/Maximum_flow_problem#Baseball_elimination))
*    If team is eliminated explain by what teams
## What was in use:
*    Binary-Search ST
*    Flow Network
*    Ford Fulkerson algorithm
![FF](https://coursera.cs.princeton.edu/algs4/assignments/baseball/baseball.png)
## How to use:
*    Import algs4.jar
*    Get test-client file from baseball.zip
*    Start main
*    Give a name of file
*    Enjoy
## File format:
 
 i | team | wins | loss | left | NY | Bal | Bos | Tor | Det
 --|------|------|------|------|----|-----|-----|-----|----
 0 | New York | 75 | 59 | 28 | - | 3 | 8 | 7 | 3
 1 | Baltimore | 71 | 63 | 28 | 3 | - | 2 | 7 | 7
 2 | Boston | 69 | 66 | 27 | 8 | 2 | - | 0 | 3
 3 | Toronto | 63 | 72 | 27 | 7 | 7 | 0 | - | 3
 4 | Detroit | 49 | 86 | 27 | 3 | 7 | 3 | 3 | -
 
## Example of work:
file:

    5
    New_York    75 59 28   0 3 8 7 3
    Baltimore   71 63 28   3 0 2 7 7
    Boston      69 66 27   8 2 0 0 3
    Toronto     63 72 27   7 7 0 0 3
    Detroit     49 86 27   3 7 3 3 0
=>

    Please, give a name of test-client file (Example: "teams5.txt" without quotes ): 
    teams5.txt
    New_York is not eliminated
    Baltimore is not eliminated
    Boston is not eliminated
    Toronto is not eliminated
    Detroit is eliminated by the subset R = { New_York Baltimore Boston Toronto }
