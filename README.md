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
