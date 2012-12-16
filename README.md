# Trajectories

Project for CSCI7582: Artificial Intelligence.

This program takes as input a chess board with or without obstacles, an element p (a chess piece), and start and destination points on the chess board.  It will generate a shortest path (or all shortest paths) from the start to the destination using methods covered in Professor Boris Stilman's Artificial Intelligence class at University of Colorado Denver as well as in his book, "Linguistic Geometry: From Search to Construction"

The program is written using Scala and built using Gradle.  Source code is available on GitHub.

## Requirements

This program requires a JVM to run, with either java in the path or a JAVA_HOME variable set.

## Installation

The distribution file is trajectories.zip.  Unzip this into a directory of your choice and cd into it from a terminal.

All examples were run in a unix environment, and thus run the script `trajectories`.  For Windows, use `trajectories.bat` instead.

## Usage

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

# Examples

Let's start simply, with a standard chess board in which we must move a king at a4 to c6.

```
$ bin/trajectories -p king -s a4 -d c6
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
$ bin/trajectories -p king -s a4 -d d4
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
$ bin/trajectories -p king -s a4 -d d4 --all
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

## Pieces

This program supports multiple types of pieces including king, queen, bishop, knight, rook, pawn.  It also supports a 'weird' piece, but more on that later.

Let's see how paths look for each of the different types.  Let's generate a queen's path from b4 to f6.

```
$ bin/trajectories -p queen -s b4 -d f6 --all
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
$ bin/trajectories -p rook -s e2 -d b6 --all
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
$ bin/trajectories -p bishop -s c1 -d c7 --all
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
$ bin/trajectories -p bishop -s c1 -d c8 --all
Number of shortest trajectories from c1 to c8: 0
It's impossible to reach the destination from the start position.
```

Let's now try a knight's path from d1 to g8.

```
$ bin/trajectories -p knight -s d1 -d g8 --all
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

```
$ bin/trajectories -p pawn -s c3 -d c7 --all
Number of shortest trajectories from c3 to c7: 1
Here it is: 

[ ][ ][ ][ ][ ][ ][ ][ ]  8
[ ][ ][4][ ][ ][ ][ ][ ]  7
[ ][ ][3][ ][ ][ ][ ][ ]  6
[ ][ ][2][ ][ ][ ][ ][ ]  5
[ ][ ][1][ ][ ][ ][ ][ ]  4
[ ][ ][0][ ][ ][ ][ ][ ]  3
[ ][ ][ ][ ][ ][ ][ ][ ]  2
[ ][ ][ ][ ][ ][ ][ ][ ]  1
 a  b  c  d  e  f  g  h 

c3->c4->c5->c6->c7

Shortest path is 4 moves
```

If we try to move in the opposite direction, taking a pawn from c3 to c7, this requires moving "backwards", and is thus disallowed.

```
$ bin/trajectories -p pawn -s c7 -d c3 --all
Number of shortest trajectories from c7 to c3: 0
It's impossible to reach the destination from the start position.
```

### Custom Pieces

The system also supports a 'weird' piece.  This is a piece that doesn't move like any chess piece, in order to show how simple it is to define custom pieces.  Though a piece cannot be defined via commandline, it's easy to add one.

The 'weird' piece moves as follows: it can move one square vertically or horizontally, but 2 squares diagonally.  Here is the tab15 for it:

```
[5][4][5][5][5][4][5][5][5][4][5][5][5][4][5]  15
[4][3][4][4][4][3][4][4][4][3][4][4][4][3][4]  14
[5][4][4][3][4][4][4][3][4][4][4][3][4][4][5]  13
[5][4][3][2][3][3][3][2][3][3][3][2][3][4][5]  12
[5][4][4][3][3][2][3][3][3][2][3][3][4][4][5]  11
[4][3][4][3][2][1][2][2][2][1][2][3][4][3][4]  10
[5][4][4][3][3][2][2][1][2][2][3][3][4][4][5]  9
[5][4][3][2][3][2][1][0][1][2][3][2][3][4][5]  8
[5][4][4][3][3][2][2][1][2][2][3][3][4][4][5]  7
[4][3][4][3][2][1][2][2][2][1][2][3][4][3][4]  6
[5][4][4][3][3][2][3][3][3][2][3][3][4][4][5]  5
[5][4][3][2][3][3][3][2][3][3][3][2][3][4][5]  4
[5][4][4][3][4][4][4][3][4][4][4][3][4][4][5]  3
[4][3][4][4][4][3][4][4][4][3][4][4][4][3][4]  2
[5][4][5][5][5][4][5][5][5][4][5][5][5][4][5]  1
 a  b  c  d  e  f  g  h  i  j  k  l  m  n  o 
```

Here are the paths of the weird piece from d2 to h8.

```
$ bin/trajectories -p weird -s d2 -d h8 --all
Number of shortest trajectories from d2 to h8: 6
Here's one of them: 

[ ][ ][ ][ ][ ][ ][ ][4]  8
[ ][ ][ ][ ][ ][ ][ ][3]  7
[ ][ ][ ][ ][ ][ ][ ][2]  6
[ ][ ][ ][ ][ ][ ][ ][ ]  5
[ ][ ][ ][ ][ ][1][ ][ ]  4
[ ][ ][ ][ ][ ][ ][ ][ ]  3
[ ][ ][ ][0][ ][ ][ ][ ]  2
[ ][ ][ ][ ][ ][ ][ ][ ]  1
 a  b  c  d  e  f  g  h 

d2->d3->f5->f6->h8
d2->d3->f5->h7->h8
d2->d3->d4->f6->h8
d2->f4->f5->f6->h8
d2->f4->f5->h7->h8
d2->f4->h6->h7->h8

Shortest path is 4 moves
```

## Big Move Lists

The program will discover all trajectories, even when there are very many.

We can be confident that the trajectory answers are correct, because the paths are exhaustive.  Starting with a single "path" of only the start point, the program recursively looks at all points that can be reached from the current list of partial paths, adds all valid "next moves" for each path, and calls itself with a new set of partial paths.

This may seem slow, but by employing Linguistic Geometry we can make this fast enough to be feasible.  Each path only considers the intersection of all the spots one move away from the "head" of the path, two moves away from the element before it, three moves away from the element before that, etc, going all the way back to the start point, all of that intersected with SUM, which is a table resulting from adding together a table of moves from the start point with a table of moves from the end point (from the perspective of the opponent's version of the piece), then filtering out any squares which are not the shortest possible path.  These individuals tables, the ST tables, are reused over and over as the computation proceeds, allowing them to be cached and reused in a global cache to make the process extremely fast.  SUM never changes throughout any of this computation.

As a result, we can be confident due to the exhaustive recursive nature of path generation that all possible paths are calculated, but we can also perform the computation in a reasonable amount of time using Linguistic Geometry.  So when zero paths are found, we can be certain it is correct.  This is also true when very many paths are found, we can be certain there are no more.

Let's generate all of the paths of king from a5 to h5.  This is a very long listing, but it's exhaustive.

```
$ bin/trajectories -p king -s a5 -d h5 --all
Number of shortest trajectories from a5 to h5: 393
Here's one of them: 

[ ][ ][ ][ ][ ][ ][ ][ ]  8
[ ][ ][ ][ ][ ][ ][ ][ ]  7
[ ][1][ ][ ][ ][ ][ ][ ]  6
[0][ ][2][ ][ ][5][6][7]  5
[ ][ ][ ][3][4][ ][ ][ ]  4
[ ][ ][ ][ ][ ][ ][ ][ ]  3
[ ][ ][ ][ ][ ][ ][ ][ ]  2
[ ][ ][ ][ ][ ][ ][ ][ ]  1
 a  b  c  d  e  f  g  h 

a5->b5->c6->d7->e6->f7->g6->h5
a5->b5->c6->d7->e6->f6->g5->h5
a5->b5->c6->d7->e6->f6->g6->h5
a5->b5->c6->d7->e6->f5->g4->h5
a5->b5->c6->d7->e6->f5->g5->h5
....(trimmed for space)....
a5->b4->c4->d4->e5->f5->g4->h5
a5->b4->c4->d4->e5->f5->g5->h5
a5->b4->c4->d4->e5->f5->g6->h5
a5->b4->c4->d4->e5->f4->g4->h5
a5->b4->c4->d4->e5->f4->g5->h5

Shortest path is 7 moves
```

## Different Boards

The program also allows you to customize the board space in a handful of different ways to solve other problems.

### Custom Sizes

One way that you can alter the board is by changing the size from the default of 8.  Let's adjust the size of the board to 10, and move from c3 to j8.

```
$ bin/trajectories -p king -z 10 -s c3 -d j8 --all
Number of shortest trajectories from c3 to j8: 28
Here's one of them: 

[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  10
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  9
[ ][ ][ ][ ][ ][ ][ ][ ][ ][7]  8
[ ][ ][ ][ ][ ][ ][ ][ ][6][ ]  7
[ ][ ][ ][ ][ ][ ][ ][5][ ][ ]  6
[ ][ ][ ][ ][ ][ ][4][ ][ ][ ]  5
[ ][ ][ ][1][2][3][ ][ ][ ][ ]  4
[ ][ ][0][ ][ ][ ][ ][ ][ ][ ]  3
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  2
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  1
 a  b  c  d  e  f  g  h  i  j 

c3->d3->e3->f4->g5->h6->i7->j8
c3->d3->e4->f5->g5->h6->i7->j8
c3->d3->e4->f5->g6->h7->i7->j8
c3->d3->e4->f5->g6->h7->i8->j8
c3->d3->e4->f5->g6->h6->i7->j8
....(trimmed for space)....
c3->d4->e5->f5->g5->h6->i7->j8
c3->d4->e5->f5->g6->h7->i7->j8
c3->d4->e5->f5->g6->h7->i8->j8
c3->d4->e5->f5->g6->h6->i7->j8
c3->d4->e5->f4->g5->h6->i7->j8

Shortest path is 7 moves
```

Boards can be requested up to 26 squares in size (after which we'd run out of letters for the columns).  Be warned, however, if there are many trajectories on a very large board, there can be so many possible paths that the program takes a very, very long time to run.

Let's create a board of 26x26, and find paths for the queen from a3 to z20.

```
$ bin/trajectories -p queen -z 26 -s a3 -d z20 --all
Number of shortest trajectories from a3 to z20: 5
Here's one of them: 

[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  26
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  25
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  24
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  23
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  22
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  21
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][2]  20
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  19
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  18
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  17
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  16
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  15
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  14
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  13
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  12
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  11
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  10
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  9
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  8
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  7
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  6
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  5
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  4
[0][ ][ ][ ][ ][ ][ ][ ][1][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  3
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  2
[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]  1
 a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z 

a3->r20->z20
a3->z3->z20
a3->a20->z20
a3->v24->z20
a3->i3->z20

Shortest path is 2 moves
```

## Obstacles

Users can mark certain parts of the board as unavailable or illegal.  To do this, supply a comma delimited (with no spaces) list of spots to the --illegal or -i argument.

Let's take our earlier path from a5 to h5 and drastically reduce the number of possible moves by placing a wall going down the center of the board.

```
$ bin/trajectories -p king -s a5 -d h5 -i e1,e2,e3,e4,e6,e7,e8,d1,d2,d3,d4,d6,d7,d8 --all
Number of shortest trajectories from a5 to h5: 49
Here's one of them: 

[ ][ ][ ][#][#][ ][ ][ ]  8
[ ][ ][ ][#][#][ ][ ][ ]  7
[ ][ ][ ][#][#][ ][ ][ ]  6
[0][ ][2][3][4][5][ ][7]  5
[ ][1][ ][#][#][ ][6][ ]  4
[ ][ ][ ][#][#][ ][ ][ ]  3
[ ][ ][ ][#][#][ ][ ][ ]  2
[ ][ ][ ][#][#][ ][ ][ ]  1
 a  b  c  d  e  f  g  h 

a5->b5->c6->d5->e5->f6->g5->h5
a5->b5->c6->d5->e5->f6->g6->h5
a5->b5->c6->d5->e5->f5->g4->h5
a5->b5->c6->d5->e5->f5->g5->h5
a5->b5->c6->d5->e5->f5->g6->h5
....(trimmed for space)....
a5->b4->c4->d5->e5->f5->g4->h5
a5->b4->c4->d5->e5->f5->g5->h5
a5->b4->c4->d5->e5->f5->g6->h5
a5->b4->c4->d5->e5->f4->g4->h5
a5->b4->c4->d5->e5->f4->g5->h5

Shortest path is 7 moves
```

We can do lots of interesting things by adding obstacles.  Let's add a whole bunch so that the shortest paths are much longer.  Since this increases the total number of possible paths so much, we'll be trimming out a chunk of the actual path output.

```
$ bin/trajectories -p king -s a1 -d h1 -i b1,c1,d1,e1,f1,g1,c2,d2,e2,f2,d3,e3,e4,e5,e6,e7 -all
Number of shortest trajectories from a1 to h1: 1862
Here's one of them: 

[  ][  ][  ][  ][ 7][  ][  ][  ]  8
[  ][  ][  ][ 6][##][ 8][  ][  ]  7
[  ][  ][ 5][  ][##][  ][ 9][  ]  6
[  ][ 4][  ][  ][##][  ][  ][10]  5
[  ][  ][ 3][  ][##][  ][11][  ]  4
[  ][  ][ 2][##][##][  ][12][  ]  3
[  ][ 1][##][##][##][##][  ][13]  2
[ 0][##][##][##][##][##][##][14]  1
  a   b   c   d   e   f   g   h 

a1->a2->b3->a4->b5->c6->d7->e8->f7->f6->f5->g4->h3->h2->h1
a1->a2->b3->a4->b5->c6->d7->e8->f7->f6->f5->g4->h3->g2->h1
a1->a2->b3->a4->b5->c6->d7->e8->f7->f6->f5->g4->f3->g2->h1
a1->a2->b3->a4->b5->c6->d7->e8->f7->f6->f5->g4->g3->h2->h1
a1->a2->b3->a4->b5->c6->d7->e8->f7->f6->f5->g4->g3->g2->h1
....(trimmed for space)....
a1->b2->a3->b4->c5->d6->d7->e8->f7->g6->g5->h4->g3->h2->h1
a1->b2->a3->b4->c5->d6->d7->e8->f7->g6->g5->h4->g3->g2->h1
a1->b2->a3->b4->c5->d6->d7->e8->f7->g6->g5->f4->f3->g2->h1
a1->b2->a3->b4->c5->d6->d7->e8->f7->g6->g5->f4->g3->h2->h1
a1->b2->a3->b4->c5->d6->d7->e8->f7->g6->g5->f4->g3->g2->h1

Shortest path is 14 moves
```

# Remarks

I enjoyed doing this, and it was great Scala practice.  If you look at the code, you'll see that everything is stateless except the memoization of the ST function.  Every modification you make to a Board, be it setting coordinates to specific values or intersecting two boards, results in a new immutable board rather than changing state.  Pieces are created by constructing them with functions, and lots of good functional programming practices are employed.

Every class was designed in a TDD fashion, in a manner similar to the projects in Martin Odersky's Functional Programming Principles in Scala course on Coursera.  That is to say, I would write an interface for a very small unit of functionality, write failing tests for it, then make it work.  I'd build new functionality on old functionality by composing functions together until eventually the class was complete.  It was stunning how often this method led to code working correctly on the first try.  Due to the strong type system I used, a number of would-be bugs were actually caught by the compiler, so getting the code to successfully compile often took longer than in most projects I work on, but once it compiled it almost always worked correctly the first time.  Gluing components together was incredibly smooth, and I felt that using strict TDD with Scala is what allowed me to finish a semester-long term project in 3 days.

# Coming Soon

Though this project is completed and turned in now, there are still a couple of things nagging at my mind about the code.  For one, there is a bit more duplication than I'd like in a few areas, particularly `PathFinder`.  There are also some early functions I wrote in `Board` which I can probably refactor to more elegantly use more general functions that I wrote later, and I'd like to do that knowing the safety of the unit tests will keep things working.

Most importantly, the concept of illegal spaces is implemented in a way I find a bit odd.  It's based on what we discussed in class, in which an illegal space is one that a piece cannot land on.  However, I would imagine that, to evaluate a true chess board, one would have to specify every space in which another piece sits as illegal.  If one were to do this, illegality would not have the effect desired.  For example, a queen could move straight through an illegal spot to her destination, even though in a real game of chess this would be unallowed.  Queens, rooks, and bishops can't move "through" illegal spots to get to destinations, but knights can.  This distinction isn't represented in the project requirements, so it isn't represented in the code.  I'd like to add this.


