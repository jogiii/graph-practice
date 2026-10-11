# Diagram patterns

Copy-paste karne layak ASCII patterns. Hamesha code block ke andar rakho.

## Contents
1. Graph (undirected / directed / weighted)
2. Adjacency list (box → box → ✗)
3. Adjacency matrix
4. Before / after (change highlight)
5. Side-by-side (pehle | baad mein)
6. Array / index shift
7. Tree
8. Queue / stack (BFS/DFS state)

---

## 1. Graph

Nodes `( n )`, edges `─ │ ╱ ╲`. Neeche edge list likho.

```
              ( 2 )───────────( 3 )
             ╱                ╱   ╲
          ( 1 )              ╱     ╲
                          ( 4 )   ( 5 )

   Edges: 1-2, 2-3, 3-4, 3-5
```

Directed: arrows `──►  ▲  ▼  ◄──`.

```
    ( 0 ) ──► ( 1 )
      ▲         │
      │         ▼
    ( 3 ) ◄── ( 2 )

   Edges: 0→1, 1→2, 2→3, 3→0
```

Weighted: weight edge ke beech mein, jaise `( 0 ) ──5── ( 1 )`.

## 2. Adjacency list (user ka favourite style)

Pehla box = key (vertex), aage chain = neighbours, end mein `✗` = null.

```
 key        LinkedList (neighbours)
┌───┐     ┌───┬──┐   ┌───┬──┐   ┌───┬──┐
│ 3 │ ──► │ 2 │ ─┼──►│ 4 │ ─┼──►│ 5 │ ─┼──► ✗
└───┘     └───┴──┘   └───┴──┘   └───┴──┘
```

Empty list:

```
┌───┐
│ 5 │ ──► ✗
└───┘
```

Compact version (jab bahut saari lines hon / side-by-side ho):

```
┌───┐    ┌───┐   ┌───┐
│ 2 │ ─► │ 1 │ ─►│ 3 │ ─► ✗
└───┘    └───┘   └───┘
```

## 3. Adjacency matrix

Upar column headers, left mein row headers.

```
        0  1  2  3
   0  [ 0, 1, 1, 0 ]
   1  [ 1, 0, 1, 0 ]
   2  [ 1, 1, 0, 1 ]
   3  [ 0, 0, 1, 0 ]
```

Skip / remove dikhana ho:

```
     0  1  2  3
0  [ a  b  c  d ]
1  [ e  f  g  h ]
2  [ i  j  k  l ]  ← skip
3  [ m  n  o  p ]
           ↑ skip
```

Letters (`a b c`) tab use karo jab dikhana ho ki **kaunsi value kahan gayi**. 0/1 se ye pata nahi chalta.

## 4. Before / after with highlight

Naya element heavy box `┏━━━┱──┐ ┃ ┗━━━┹──┘` ya `← naya` label.

```
Pehle:
┌───┐     ┌───┬──┐   ┌───┬──┐
│ 3 │ ──► │ 2 │ ─┼──►│ 4 │ ─┼──► ✗
└───┘     └───┴──┘   └───┴──┘

Baad mein:
┌───┐     ┌───┬──┐   ┌───┬──┐   ┏━━━┱──┐
│ 3 │ ──► │ 2 │ ─┼──►│ 4 │ ─┼──►┃ 5 ┃ ─┼──► ✗     ← naya node
└───┘     └───┴──┘   └───┴──┘   ┗━━━┹──┘
```

Hata hua element: line ke right mein `← 3 hata` ya `(poori line gayab)`.

## 5. Side-by-side

Jab pura state ek saath compare karna ho (remove vertex, matrix resize):

```
Purana (4x4)                    Naya (3x3)
     0  1  2  3                      0  1  2
0  [ a  b  c  d ]               0  [ a  b  d ]   ← purana 0
1  [ e  f  g  h ]               1  [ e  f  h ]   ← purana 1
2  [ i  j  k  l ]  ← skip       2  [ m  n  p ]   ← purana 3
3  [ m  n  o  p ]
```

Graph level pe bhi:

```
Graph pehle:                     Graph baad mein:

   ( 2 )─────( 3 )                  ( 2 )
   ╱         ╱   ╲                  ╱
( 1 )     ( 4 ) ( 5 )            ( 1 )      ( 4 )   ( 5 )
```

## 6. Array / index shift

```
index:   0   1   2   3
       [ 2 | 4 | 5 | 7 ]
               ↑
          remove(1) → index 1 hata (value 4)

       [ 2 | 5 | 7 ]       ← baaki ek step left khisak gaye
```

## 7. Tree

```
           ( 1 )
          ╱     ╲
      ( 2 )     ( 3 )
      ╱   ╲         ╲
  ( 4 )  ( 5 )     ( 6 )
```

## 8. Queue / stack state (BFS / DFS dry run)

Step-wise table zyada saaf rehta hai:

| Step | Nikala | Queue (front → back) | Visited |
|---|---|---|---|
| 0 | – | [1] | {1} |
| 1 | 1 | [2] | {1, 2} |
| 2 | 2 | [3] | {1, 2, 3} |

Ya inline:

```
Queue:  front → [ 2 | 3 | 4 ] ← back
Stack:  top   → [ 4 ]
                [ 3 ]
                [ 1 ]
```

