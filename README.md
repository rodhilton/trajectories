Trajectories
============

Project for Artificial Intelligence.

This program takes as input a chess board with or without obstacles, an element p (a chess piece), and start and destination points on the chess board.  It will generate a shortest path (or all shortest paths) from the start to the destination using methods covered in Professor Boris Stilman's Artificial Intelligence class at University of Colorado Denver as well as in his book, "Linguistic Geometry: From Search to Construction"

Requirements
----
This program requires a JVM to run, with either java in the path or a JAVA_HOME variable set.

Installation
----
The distribution file is trajectories.zip.  Unzip this into a directory of your choice and cd into it from a terminal.  In the examples below, I have extracted into ~/traj

All examples were run in a unix environment, and thus run the script `trajectories`.  For Windows, use `trajectories.bat` instead.

Usage
----

```
usage: trajectories
 -a,--all             Output information about all paths (can be slow)
 -d,--dest <arg>      The space to end (a2, c8, ...)
 -h,--help            Print this help
 -i,--illegal <arg>   Input spaces that are illegal separated by commas
                      (a2,b3,e6)
 -p,--piece <arg>     The piece to use (pawn, king, queen, knight, bishop,
                      rook, weird)
 -s,--start <arg>     The space to start (a1, b6, ...)
 -z,--size <arg>      Size of the board (must be <= 26)
```

Examples
========

Let's start simply, with a standard chess board in which we must move a king at a4 to c6.

```
air0day@babbage:~/traj ±(master ✓) » bin/trajectories -p king -s a4 -d c6
[ ][ ][ ][ ][ ][ ][ ][ ]  8
[ ][ ][ ][ ][ ][ ][ ][ ]  7
[ ][ ][2][ ][ ][ ][ ][ ]  6
[ ][1][ ][ ][ ][ ][ ][ ]  5
[0][ ][ ][ ][ ][ ][ ][ ]  4
[ ][ ][ ][ ][ ][ ][ ][ ]  3
[ ][ ][ ][ ][ ][ ][ ][ ]  2
[ ][ ][ ][ ][ ][ ][ ][ ]  1
 a  b  c  d  e  f  g  h 

a4->b5->c6

Shortest path is 2 moves
```

As you can imagine, there is only one shortest path from a4 to c6, which is printed out in ASCII.  0 marks the starting point, while 2 marks the destination because it takes 2 moves to get there.

Let's make things a bit more complicated and do a trajectory with multiple equal paths.   We'll go from a4 to d4.

```
air0day@babbage:~/traj ±(master ✓) » bin/trajectories -p king -s a4 -d d4
[ ][ ][ ][ ][ ][ ][ ][ ]  8
[ ][ ][ ][ ][ ][ ][ ][ ]  7
[ ][ ][ ][ ][ ][ ][ ][ ]  6
[ ][1][ ][ ][ ][ ][ ][ ]  5
[0][ ][2][3][ ][ ][ ][ ]  4
[ ][ ][ ][ ][ ][ ][ ][ ]  3
[ ][ ][ ][ ][ ][ ][ ][ ]  2
[ ][ ][ ][ ][ ][ ][ ][ ]  1
 a  b  c  d  e  f  g  h 

a4->b5->c4->d4

Shortest path is 3 moves
```

This outputs a single path, one of the many that are equal.  We can add the -a or --all parameter to have the program print all such paths.  It will only graph one (at random), but it will print all of them as a list.

```
air0day@babbage:~/traj ±(master ✓) » bin/trajectories -p king -s a4 -d d4 --all
Number of shortest trajectories from a4 to d4: 7
Here's one of them: 

[ ][ ][ ][ ][ ][ ][ ][ ]  8
[ ][ ][ ][ ][ ][ ][ ][ ]  7
[ ][ ][ ][ ][ ][ ][ ][ ]  6
[ ][ ][ ][ ][ ][ ][ ][ ]  5
[0][1][2][3][ ][ ][ ][ ]  4
[ ][ ][ ][ ][ ][ ][ ][ ]  3
[ ][ ][ ][ ][ ][ ][ ][ ]  2
[ ][ ][ ][ ][ ][ ][ ][ ]  1
 a  b  c  d  e  f  g  h 

a4->b3->c3->d4
a4->b3->c4->d4
a4->b5->c5->d4
a4->b5->c4->d4
a4->b4->c5->d4
a4->b4->c3->d4
a4->b4->c4->d4

Shortest path is 3 moves
```

This program supports multiple types of pieces including king, queen, bishop, knight, rook, pawn.  It also supports a 'weird' piece, but more on that later.

Let's see how paths look for each of the different types.  Let's generate a queen's path from b4 to f6.

```
Number of shortest trajectories from b4 to f6: 9
Here's one of them: 

[ ][ ][ ][ ][ ][1][ ][ ]  8
[ ][ ][ ][ ][ ][ ][ ][ ]  7
[ ][ ][ ][ ][ ][2][ ][ ]  6
[ ][ ][ ][ ][ ][ ][ ][ ]  5
[ ][0][ ][ ][ ][ ][ ][ ]  4
[ ][ ][ ][ ][ ][ ][ ][ ]  3
[ ][ ][ ][ ][ ][ ][ ][ ]  2
[ ][ ][ ][ ][ ][ ][ ][ ]  1
 a  b  c  d  e  f  g  h 

b4->d4->f6
b4->c3->f6
b4->e7->f6
b4->b2->f6
b4->d6->f6
b4->b6->f6
b4->h4->f6
b4->f8->f6
b4->f4->f6

Shortest path is 2 moves
```
