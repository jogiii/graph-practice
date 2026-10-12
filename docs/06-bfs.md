# 06 · BFS: Queue se Breadth First Search (meri implementation)

> Code: [`GraphTraversalBFS.java`](../src/main/java/com/practice/graph/traversal/GraphTraversalBFS.java)
> Related: [03-adjacency-list.md](03-adjacency-list.md) (jis graph pe BFS chal raha hai) · [04-dfs-iterative.md](04-dfs-iterative.md) (same structure, Stack ke saath)

---

## 1. Ek line mein

**BFS** = pehle start ke **saare paas wale** (level 1) dekho, phir unke paas wale (level 2), aise **level by level** failte jao. Paani mein patthar phenkne pe jaise lehrein failti hain.

Iske liye **Queue** use hoti hai. Queue **FIFO** (First In, First Out) hai, isliye jo pehle aaya (paas wala), wahi pehle explore hota hai. DFS se farak bas itna hai: wahan Stack tha, yahan Queue hai.

---

## 2. Picture se samjho

### Graph (wahi jo `main` mein banaya hai, DFS notes wala hi)

```
              ( 2 )───────────( 3 )
             ╱                ╱   ╲
          ( 1 )              ╱     ╲
                          ( 4 )   ( 5 )

   Edges: 1-2, 2-3, 3-4, 3-5
```

### Iski adjacency list (`getNeighbors` yahi lists deta hai)

```
┌───┐    ┌───┐
│ 1 │ ─► │ 2 │ ─► ✗
└───┘    └───┘
┌───┐    ┌───┐   ┌───┐
│ 2 │ ─► │ 1 │ ─►│ 3 │ ─► ✗
└───┘    └───┘   └───┘
┌───┐    ┌───┐   ┌───┐   ┌───┐
│ 3 │ ─► │ 2 │ ─►│ 4 │ ─►│ 5 │ ─► ✗
└───┘    └───┘   └───┘   └───┘
┌───┐    ┌───┐
│ 4 │ ─► │ 3 │ ─► ✗
└───┘    └───┘
┌───┐    ┌───┐
│ 5 │ ─► │ 3 │ ─► ✗
└───┘    └───┘
```

### Levels (start = 3): BFS yahan sabse saaf dikhta hai

```
   Level 0:                 ( 3 )
                          ╱   │   ╲
   Level 1:          ( 2 )  ( 4 )  ( 5 )      ← 3 ke direct neighbours
                     ╱
   Level 2:     ( 1 )                         ← 2 ka neighbour (3 se 2 edge door)

   Output: 3 → 2 → 4 → 5 → 1
```

Dhyan do:
- Pehle **poora level 1** (`2, 4, 5`) print hua, tabhi level 2 (`1`) pe gaye. DFS hota to `3 → 2 → 1` seedha gehrai mein chala jaata.
- Start = 1 se output `1 2 3 4 5` aata hai. Graph lamba (chain jaisa) hai, isliye wahan levels ek-ek vertex ke hain.

---

## 3. Data structures (method ke andar)

```java
Set<Integer> visited = new HashSet<>();
Queue<Integer> queue = new LinkedList<>();
```

| Part | Kya hai | Kyun |
|---|---|---|
| `visited` (`HashSet`) | Jo vertices **queue mein daal diye** (ya visit ho gaye) | Cycle mein infinite loop na ho, aur koi vertex queue mein do baar na jaaye. `contains` O(1) |
| `queue` (`LinkedList` as `Queue`) | Jo vertices **abhi dekhne baaki** hain | FIFO: pehle aaya, pehle gaya. Isi se level by level chalta hai |
| `graph.getNeighbors(v)` | `v` ke neighbours ki list | Graph ki andar wali list ko bina chhede padhne ke liye |

Queue ke do main kaam:

| Method | Kya karta hai |
|---|---|
| `queue.add(x)` | Peeche (back) daalo |
| `queue.poll()` | Aage (front) se nikalo. Khaali ho to `null` |

---

## 4. Method step by step

### 4.1 `BFSIterative(graph, startVertex)`: O(V + E)

```java
public void BFSIterative(AdjacencyListGraph graph, int startVertex){
    Set<Integer> visited = new HashSet<>();
    Queue<Integer> queue = new LinkedList<>();

    queue.add(startVertex);
    visited.add(startVertex);
    while(!queue.isEmpty()){
        int currentVertex = queue.poll();
        IO.println("Visiting vertex: " + currentVertex);

        //Add all the neighbours of the current vertex to the queue if they haven't been visited yet
        for(int neighbour : graph.getNeighbors(currentVertex)){
            if(!visited.contains(neighbour)){
                visited.add(neighbour);
                queue.add(neighbour);
            }
        }
    }
}
```

**Steps:**
1. Start vertex ko queue mein daalo **aur turant `visited` mark karo**.
2. Jab tak queue khaali nahi hai:
   1. Front se nikalo (`poll`) aur print karo.
   2. Har neighbour jo `visited` mein nahi hai: pehle `visited` mark karo, phir queue mein daalo.

### 4.2 Dry run (start = 3)

Queue **front → back** likhi hai.

| Step | `poll` | Print | Add kiye | Queue (front → back) | `visited` |
|---|---|---|---|---|---|
| 0 | – | – | 3 | [3] | {3} |
| 1 | 3 | 3 | 2, 4, 5 | [2, 4, 5] | {3, 2, 4, 5} |
| 2 | 2 | 2 | 1 (3 already visited) | [4, 5, **1**] | {3, 2, 4, 5, 1} |
| 3 | 4 | 4 | – (3 visited) | [5, 1] | same |
| 4 | 5 | 5 | – (3 visited) | [1] | same |
| 5 | 1 | 1 | – (2 visited) | [] | same |

Step 2 ke baad queue aisi dikhti hai:

```
Queue:  front → [ 4 | 5 | 1 ] ← back
                  ▲           ▲
          level 1 wale    level 2 wala peeche lagta hai
```

`1` peeche juda, isliye wo tabhi niklega jab level 1 ke `4, 5` khatam ho jaayein. **Yahi BFS ka level-by-level jaadu hai.**

Output (`main` se):
```
---- BFS from 3 ----
Visiting vertex: 3
Visiting vertex: 2
Visiting vertex: 4
Visiting vertex: 5
Visiting vertex: 1
```

### 4.3 ⭐ `visited.add` queue mein **daalte waqt** kyun, nikaalte waqt kyun nahi?

DFS iterative mein humne pop ke baad check kiya tha (`if visited → continue`). Yahan **add karte hi** mark kar rahe ho. Ye better hai, aur kyun:

**Example: triangle** `1-2, 2-3, 3-1`

```
      ( 1 )
      ╱   ╲
   ( 2 )───( 3 )

   Lists:  1 → [2, 3]    2 → [1, 3]    3 → [2, 1]
```

❌ **Agar `visited` sirf `poll` ke baad mark karte:**

| Step | `poll` | Kya hua | Queue |
|---|---|---|---|
| 0 | – | add 1 | [1] |
| 1 | 1 | visit 1, add 2, 3 | [2, 3] |
| 2 | 2 | visit 2, add **3** (abhi visited nahi hai!) | [3, **3**] |
| 3 | 3 | visit 3 | [3] |
| 4 | 3 | 😬 **3 dobara print** | [] |

✅ **Tumhara code (add pe mark):**

| Step | `poll` | Kya hua | Queue | `visited` |
|---|---|---|---|---|
| 0 | – | add 1 | [1] | {1} |
| 1 | 1 | add 2, 3 | [2, 3] | {1, 2, 3} |
| 2 | 2 | 3 already visited, skip | [3] | same |
| 3 | 3 | sab visited | [] | same |

Fayde:
- Har vertex queue mein **sirf ek baar** jaata hai, isliye queue max V size ki hoti hai (DFS iterative mein stack O(E) tak ja sakta tha).
- Pop ke baad koi extra `continue` check nahi chahiye.

> Yaad rakhne ka tareeka: **"Queue mein daala = visited."** Ek baar line mein laga diya, dobara line mein mat lagao.

### 4.4 ⭐ `startVertex` ko loop se pehle `visited` kyun?

Same reason. Agar start ko mark na karo, to uska koi neighbour start ko dobara queue mein daal dega. Jaise start = 3: `2` ke neighbours `[1, 3]` hain, to `3` phir se add ho jaata aur do baar print hota.

### 4.5 Graph banake kaise chalayein (`main`)

```java
AdjacencyListGraph graph = new AdjacencyListGraph();
for (int v = 1; v <= 5; v++) {
    graph.addVertex(v);          // pehle vertices, warna addEdge pe NullPointerException
}
graph.addEdge(1, 2);
graph.addEdge(2, 3);
graph.addEdge(3, 4);
graph.addEdge(3, 5);

GraphTraversalBFS traversal = new GraphTraversalBFS();
traversal.BFSIterative(graph, 1);   // 1 2 3 4 5
traversal.BFSIterative(graph, 3);   // 3 2 4 5 1
```

---

## 5. Complexity summary

| | Complexity | Kyun |
|---|---|---|
| Time | **O(V + E)** | Har vertex ek baar queue se nikalta hai (V). Har vertex ki neighbour list ek baar scan hoti hai, sab mila ke 2E (E) |
| Space: `visited` | O(V) | Har vertex max ek baar |
| Space: `queue` | O(V) | Add pe hi mark karte ho, isliye koi vertex do baar queue mein nahi jaata |

### BFS vs DFS (same graph, start = 1 aur 3)

| | BFS (ye code) | DFS iterative ([04](04-dfs-iterative.md)) |
|---|---|---|
| Data structure | **Queue** (FIFO) | **Stack** (LIFO) |
| Chalne ka tareeka | Level by level, chaudai mein | Ek raaste pe gehrai mein, phir backtrack |
| `visited` kab mark | Queue mein **daalte waqt** | Stack se **nikaalte waqt** |
| Duplicate entries | Nahi aati | Aa sakti hain, isliye `continue` check |
| Extra space | O(V) | O(E) worst case |
| Output (start = 1) | `1 2 3 4 5` | `1 2 3 5 4` |
| Output (start = 3) | `3 2 4 5 1` | `3 5 4 2 1` |
| Kab lo | **Unweighted graph mein shortest path**, level-wise kaam, "kitne steps door" | Cycle detection, path exist karta hai ya nahi, topological sort, backtracking |

---

## 6. ⚠️ Dhyan rakhne wali baatein (is code mein)

1. **Disconnected graph:** sirf `startVertex` se jude vertices hi print honge. Poora graph cover karna ho to har unvisited vertex se BFS chalana padega, jiske liye graph se saare vertices ki list chahiye (abhi `AdjacencyListGraph` mein nahi hai).
2. **Galat start vertex:** `startVertex` graph mein nahi hai, tab bhi `"Visiting vertex: 99"` print hoga. `getNeighbors` khaali list deta hai, isliye crash nahi hota, bas output misleading hai.
3. **`IO.println` sirf Java 25+ mein hai.** Project toolchain 25 hai, to chal raha hai. DFS file `System.out.println` use karti hai; dono same rakhoge to purane Java pe bhi copy-paste chal jaayega.
4. **`LinkedList` as Queue chalta hai,** lekin `ArrayDeque` zyada fast hai (har node ke liye alag object nahi banta). `Queue<Integer> queue = new ArrayDeque<>();` likhoge to baaki code same rahega.
5. **Print karta hai, return nahi.** Test likhna mushkil hai. DFS wale `dfs()` jaisa ek `List<Integer>` return karne wala version banana useful rahega.
6. **Distance / level track nahi ho raha.** BFS ka asli power shortest path hai. Uske liye `Map<Integer, Integer> distance` rakho: `distance.put(neighbour, distance.get(currentVertex) + 1)` add karte waqt.
7. **Method ka naam** `BFSIterative` capital se shuru hai. Java convention `bfsIterative` hai. Chalega, bas style.

---

## 7. Quick revision ✅

- [ ] BFS = level by level failo. **Queue (FIFO)** use hoti hai
- [ ] Loop: `poll` → print → har unvisited neighbour ko **mark + add**
- [ ] `visited` queue mein **daalte waqt** mark karo (start vertex bhi), isliye koi duplicate nahi
- [ ] Time O(V + E), `visited` O(V), queue O(V)
- [ ] Sirf start se jude vertices hi visit hote hain
- [ ] DFS se farak: Stack → Queue, aur mark karne ka time (pop pe vs add pe)
- [ ] Unweighted graph mein shortest path = BFS

