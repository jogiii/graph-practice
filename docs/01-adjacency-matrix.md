# 01 · Adjacency Matrix (Graph Representation)

> Code: [`AdjacencyMatrixGraph.java`](../src/main/java/com/practice/graph/common/AdjacencyMatrixGraph.java)
> Tests: [`AdjacencyMatrixGraphTest.java`](../src/test/java/com/practice/graph/common/AdjacencyMatrixGraphTest.java)

---

## 1. Ek line mein

Graph ko ek **`V x V` ke 2D array** mein store karna, jahan `matrix[i][j]` batata hai ki **vertex `i` se vertex `j` tak edge hai ya nahi** (aur weighted ho to uska weight kitna hai).

`V` = vertices (nodes) ki ginti. Vertices ko `0` se `V-1` tak number karte hain, taaki woh seedha array index ban sakein.

---

## 2. Picture se samjho

### Undirected graph

```
    0 ─── 1
    │   ╱
    │  ╱
    2 ─── 3

Edges: 0-1, 0-2, 1-2, 2-3
```

```
        0  1  2  3
   0  [ 0, 1, 1, 0 ]
   1  [ 1, 0, 1, 0 ]
   2  [ 1, 1, 0, 1 ]
   3  [ 0, 0, 1, 0 ]
```

- `0-1` edge hai, isliye `matrix[0][1] = 1` **aur** `matrix[1][0] = 1`.
- Undirected graph mein matrix hamesha **symmetric** hota hai (diagonal ke across mirror image): `matrix[i][j] == matrix[j][i]`.
- Diagonal (`matrix[i][i]`) `0` hai, kyunki koi self-loop nahi hai.

### Directed graph

```
    0 ──► 1
    ▲     │
    │     ▼
    3 ◄── 2

Edges: 0→1, 1→2, 2→3, 3→0
```

```
        0  1  2  3
   0  [ 0, 1, 0, 0 ]
   1  [ 0, 0, 1, 0 ]
   2  [ 0, 0, 0, 1 ]
   3  [ 1, 0, 0, 0 ]
```

- Sirf `matrix[from][to]` set hota hai, ulta nahi, isliye matrix symmetric **nahi** hota.
- **Row** `i` = `i` se bahar jaane wali edges (**out-degree** = row mein count).
- **Column** `j` = `j` pe aane wali edges (**in-degree** = column mein count).

### Weighted graph

```
    0 ──5── 1
    │
    2
    │
    2 ──(-3)── 3
```

```
        0     1     2     3
   0  [ ∅,    5,    2,    ∅ ]
   1  [ 5,    ∅,    ∅,    ∅ ]
   2  [ 2,    ∅,    ∅,   -3 ]
   3  [ ∅,    ∅,   -3,    ∅ ]
```

`∅` = edge nahi hai. Ise kaise represent karein, uske options:

| Choice | Problem |
|---|---|
| `0` | Weight 0 wali edge aur "edge nahi hai" mein farak nahi pata chalega |
| `-1` | Negative weight wale graphs mein galat ho jaayega |
| `Integer.MAX_VALUE` (INF) | Shortest-path problems mein common hai, lekin add karte hi overflow ho sakta hai |
| **`Integer.MIN_VALUE`** (hamara `NO_EDGE`) | Practically koi real weight nahi hoga, isliye safe sentinel hai |
| `Integer[][]` with `null` | Clean hai, lekin boxing ka overhead aata hai |

> Interview mein simple unweighted problem ho to `boolean[][]` ya `int[][]` (0/1) hi kaafi hai.

---

## 3. Operations aur unka logic

| Operation | Kaise | Time |
|---|---|---|
| Graph banana | `new int[V][V]`, sab cells `NO_EDGE` se bharo | O(V²) |
| `addEdge(u, v, w)` | `m[u][v] = w`; undirected hai to `m[v][u] = w` bhi | **O(1)** |
| `removeEdge(u, v)` | `m[u][v] = NO_EDGE` (undirected mein dono taraf) | **O(1)** |
| `hasEdge(u, v)` | `m[u][v] != NO_EDGE` | **O(1)** ⭐ |
| `neighbors(u)` | Row `u` ke saare `V` cells scan karo | O(V) |
| `outDegree(u)` | Row `u` mein edges gino | O(V) |
| `inDegree(u)` | Column `u` mein edges gino | O(V) |
| Saari edges pe loop | Poora matrix scan | O(V²) |

**Space:** hamesha **O(V²)**, chahe graph mein 1 edge ho ya V² edges.

---

## 4. Core code (yaad rakhne layak)

```java
int[][] m = new int[V][V];
for (int[] row : m) Arrays.fill(row, NO_EDGE);

// add edge
m[u][v] = w;
if (!directed) m[v][u] = w;

// neighbours of u
for (int j = 0; j < V; j++)
    if (m[u][j] != NO_EDGE) { /* j is neighbour */ }
```

Matrix pe BFS/DFS ka pattern bhi same hai: bas `for (Edge e : adj.get(u))` ki jagah `for (int j = 0; j < V; j++) if (m[u][j] ...)` likhna hai. Isliye **matrix pe BFS/DFS O(V²)** hota hai, jabki adjacency list pe O(V + E).

---

## 5. Adjacency Matrix vs Adjacency List

| | Matrix | List |
|---|---|---|
| Space | O(V²) | O(V + E) |
| Edge check `(u, v)` | **O(1)** | O(degree(u)) |
| Neighbours of `u` | O(V) | **O(degree(u))** |
| Add edge | O(1) | O(1) |
| Remove edge | **O(1)** | O(degree(u)) |
| BFS / DFS | O(V²) | **O(V + E)** |
| Best for | **Dense** graph (E ≈ V²) | **Sparse** graph (E ≪ V²) |

**Rule of thumb:**
- `V` chhota ho (≈ ≤ 1000) ya graph dense ho, ya baar-baar "u aur v connected hain?" poochna ho, to **Matrix** lo.
- `V` bada ho (10⁵ jaisa), to matrix memory mein fit hi nahi hoga: 10⁵ × 10⁵ ints ≈ 40 GB. Isliye **List** lo.

---

## 6. Kahan use hota hai

- **Floyd-Warshall** (all-pairs shortest path): `dist[i][j]` khud ek matrix hai.
- **Transitive closure / reachability** (Warshall's algorithm).
- **Dense graph pe Prim's MST**, O(V²) wala version.
- **Grid ya chhote graph** wale problems jahan V ≤ ~500.
- Math wala trick: `A^k[i][j]` = `i` se `j` tak exactly `k` length ke kitne walks hain.

---

## 7. Common galtiyan ⚠️

1. **Undirected mein dono cells set karna bhool jaana**: `m[v][u]` set nahi kiya to graph aadha directed ban jaata hai.
2. **`0` ko "no edge" maan lena**, jabki problem mein weight 0 wali edges bhi hain.
3. **1-indexed input** (vertices `1..N`): ya to `new int[N+1][N+1]` lo, ya input aate hi `u-1, v-1` kar do.
4. **Bade V pe matrix banana**: memory limit exceed (MLE).
5. **INF + weight overflow**: `Integer.MAX_VALUE + w` negative ho jaata hai. INF ke liye `1e9` ya `Long` use karo.
6. **Self-loop / multiple edges**: matrix ek cell mein sirf ek value rakhta hai, isliye parallel edges ka sirf last (ya min) weight bachta hai.

---

## 8. Quick revision ✅

- [ ] Matrix ka size `V x V`, cell `(i, j)` = edge `i → j`
- [ ] Undirected → symmetric matrix
- [ ] Row count = out-degree, column count = in-degree
- [ ] Edge check O(1), neighbours O(V), space O(V²)
- [ ] Dense graph → matrix, sparse graph → list
- [ ] Floyd-Warshall ka natural representation

