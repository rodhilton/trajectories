Trajectories
============

Project for Artificial Intelligence.

This program takes as input a chess board with or without obstacles, an element p (a chess piece), and start and destination points on the chess board.  It will generate a shortest path (or all shortest paths) from the start to the destination using methods covered in Professor Boris Stilman's Artificial Intelligence class at University of Colorado Denver as well as in his book, "Linguistic Geometry: From Search to Construction"

Requirements
----
This program requires a JVM to run, with either java in the path or a JAVA_HOME variable set.

Installation
----
The distribution file is trajectories.zip.  Unzip this into a directory of your choice and cd into it from a terminal.  In the examples below, I have extracted into `~/traj/`

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
air0day@babbage:~/traj ±(master ✓) » bin/trajectories -p queen -s b4 -d f6 --all
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

Now, a rook's path from e2 to b6

```
air0day@babbage:~/traj ±(master ✓) » bin/trajectories -p rook -s e2 -d b6 --all
Number of shortest trajectories from e2 to b6: 2
Here's one of them: 

[ ][ ][ ][ ][ ][ ][ ][ ]  8
[ ][ ][ ][ ][ ][ ][ ][ ]  7
[ ][2][ ][ ][1][ ][ ][ ]  6
[ ][ ][ ][ ][ ][ ][ ][ ]  5
[ ][ ][ ][ ][ ][ ][ ][ ]  4
[ ][ ][ ][ ][ ][ ][ ][ ]  3
[ ][ ][ ][ ][0][ ][ ][ ]  2
[ ][ ][ ][ ][ ][ ][ ][ ]  1
 a  b  c  d  e  f  g  h 

e2->e6->b6
e2->b2->b6

Shortest path is 2 moves
```

And a bishop's path from c1 to c7.

```
air0day@babbage:~/traj ±(master ✓) » bin/trajectories -p bishop -s c1 -d c7 --all
Number of shortest trajectories from c1 to c7: 1
Here it is: 

[ ][ ][ ][ ][ ][ ][ ][ ]  8
[ ][ ][2][ ][ ][ ][ ][ ]  7
[ ][ ][ ][ ][ ][ ][ ][ ]  6
[ ][ ][ ][ ][ ][ ][ ][ ]  5
[ ][ ][ ][ ][ ][1][ ][ ]  4
[ ][ ][ ][ ][ ][ ][ ][ ]  3
[ ][ ][ ][ ][ ][ ][ ][ ]  2
[ ][ ][0][ ][ ][ ][ ][ ]  1
 a  b  c  d  e  f  g  h 

c1->f4->c7

Shortest path is 2 moves
```

What happens if we try to generate the bishop's path from c1 to c8?  Those spaces have different parity, meaning if you were to color the spaces like a regular chess board, one would be black and the other would be white.  There's no way for a bishop to ever move off a space of its own color, so c1 to c8 is actually impossible.  The program verifies this is the case:

```
air0day@babbage:~/traj ±(master ✓) » bin/trajectories -p bishop -s c1 -d c8 --all
Number of shortest trajectories from c1 to c8: 0
It's impossible to reach the destination from the start position.
```

We can be confident that this answer (and every other) is correct, because the paths are exhaustive.  Starting with a single "path" of only the start point, the program recursively looks at all points that can be reached from the current list of partial paths, adds all valid "next moves" for each path, and calls itself with a new set of partial paths.

This may seem slow, but by employing Linguistic Geometry we can make this fast enough to be feasible.  Each path only considers the intersection of all the spots one move away from the "head" of the path, two moves away from the element before it, three moves away from the element before that, etc, going all the way back to the start point, all of that intersected with SUM, which is a table resulting from adding together a table of moves from the start point with a table of moves from the end point, then filtering out any squares which are not the shortest possible path.  These indivual tables, the ST tables, are reused over and over as the computation proceeds, allowing them to be cached and reused in a global cache to make the process extremely fast.  SUM never changes throughout any of this computation, and intersecting $n$ tables is O(n).

As a result, we can be confident do the exhaustive recursive nature of path generation that all possible paths are calculated, but we can also perform the computation in a reasonable amount of time using Linguistic Geometry.  So when zero paths are found, as above, we can be certain it is correct.

Let's now try a knight's path from d1 to g8.

```
air0day@babbage:~/traj ±(master ✓) » bin/trajectories -p knight -s d1 -d g8 --all
Number of shortest trajectories from d1 to g8: 12
Here's one of them: 

[ ][ ][ ][ ][ ][ ][4][ ]  8
[ ][ ][ ][ ][3][ ][ ][ ]  7
[ ][ ][ ][ ][ ][ ][ ][ ]  6
[ ][ ][ ][ ][ ][2][ ][ ]  5
[ ][ ][ ][ ][ ][ ][ ][ ]  4
[ ][ ][ ][ ][1][ ][ ][ ]  3
[ ][ ][ ][ ][ ][ ][ ][ ]  2
[ ][ ][ ][0][ ][ ][ ][ ]  1
 a  b  c  d  e  f  g  h 

d1->e3->g4->f6->g8
d1->e3->g4->h6->g8
d1->e3->f5->h6->g8
d1->e3->f5->e7->g8
d1->e3->d5->f6->g8
d1->e3->d5->e7->g8
d1->f2->g4->f6->g8
d1->f2->g4->h6->g8
d1->f2->e4->f6->g8
d1->c3->d5->f6->g8
d1->c3->d5->e7->g8
d1->c3->e4->f6->g8

Shortest path is 4 moves
```

Let's look at a pawn.  A pawn can only move straight ahead (we don't take diagonals for taking a piece into account) and cannot move backwards.

So if we generate moves from c3 to c7, we get what we expect.

TODO: this section coming soon.  Pawns are weird.

The system also supports a 'weird' piece.  This is a piece that doesn't move like any chess piece, in order to show how simple it is to define custom pieces.  Though a piece cannot be defined via commandline, it's easy to add one.

The 'weird' piece moves as follows: move like a king, unless you're sitting on a column that's a vowel, in which case move like a queen.  The tab15 for this piece looks like this:

```
[2][2][2][3][3][3][3][3][2][3][3][3][3][3][2]  15
[3][2][2][2][3][3][3][3][2][3][3][3][3][2][2]  14
[3][3][2][2][2][3][3][3][2][3][3][3][2][2][2]  13
[3][3][3][2][2][2][3][3][2][3][3][2][2][2][3]  12
[3][3][3][3][2][2][2][3][2][3][2][2][2][3][3]  11
[3][3][3][3][3][2][2][2][2][2][2][2][3][3][3]  10
[2][2][2][2][2][2][1][1][1][2][2][2][2][2][2]  9
[2][2][2][2][2][2][1][0][1][2][2][2][2][2][2]  8
[2][2][2][2][2][2][1][1][1][2][2][2][2][2][2]  7
[3][3][3][3][3][2][2][2][2][2][2][2][3][3][3]  6
[3][3][3][3][2][2][2][3][2][3][2][2][2][3][3]  5
[3][3][3][2][2][2][3][3][2][3][3][2][2][2][3]  4
[3][3][2][2][2][3][3][3][2][3][3][3][2][2][2]  3
[3][2][2][2][3][3][3][3][2][3][3][3][3][2][2]  2
[2][2][2][3][3][3][3][3][2][3][3][3][3][3][2]  1
 a  b  c  d  e  f  g  h  i  j  k  l  m  n  o 
```

What is interesting here is that the tab15 is of less use than the tab15 for other pieces, since you can't directly overlay the 8x8 grid of a chess board without it changing the letters.  Moves *must* be calculated from scratch on the board when working with the weird piece.

Nonetheless, the program rapidly generates paths.  Here are the paths of the weird piece from d2 to h8.

remaining: 
* weird piece
* obstacles
* the sample king move with 393 moves, all listed
