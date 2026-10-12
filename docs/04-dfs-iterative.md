# 04 · DFS Iterative: Stack se Depth First Search (meri implementation)

> Code: [`GraphTraversalDFS.java`](../src/main/java/com/practice/graph/traversal/GraphTraversalDFS.java)
> Related: [03-adjacency-list.md](03-adjacency-list.md) (jis graph pe DFS chal raha hai)

---

## 1. Ek line mein

**DFS** = ek raaste pe jitna **gehra (deep)** ja sakte ho jao. Aage koi naya vertex na bache to **peeche aao (backtrack)** aur doosra raasta lo.

Recursion ki jagah hum khud ek **Stack** rakhte hain. Stack **LIFO** (Last In, First Out) hai, isliye jo neighbour sabse last mein daala, wahi pehle explore hota hai. Isi se "gehra jaana" apne-aap ho jaata hai.

---

## 2. Picture se samjho

### Graph (wahi jo `main` mein banaya hai)

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

### DFS ka raasta (start = 1)

```
   ( 1 ) ──① ──► ( 2 ) ──② ──► ( 3 ) ──③ ──► ( 5 )
                                  │
                                  └──④ (5 se aage kuch nahi, wapas 3 pe) ──► ( 4 )

   Output: 1 → 2 → 3 → 5 → 4
```

Dhyan do:
- `1 → 2 → 3` seedha gehrai mein gaya. Ye DFS ki pehchaan hai.
- `3` ke do raaste the (`4` aur `5`). Pehle `5` gaya, phir **backtrack** karke `4`. Ye kyun hua, wo section 4.3 mein hai.

---

## 3. Data structures (method ke andar)

```java
Set<Integer> visited = new HashSet<>();
Stack<Integer> stack = new Stack<>();
```

| Part | Kya hai | Kyun |
|---|---|---|
| `visited` (`HashSet`) | Jo vertices dekh liye | Cycle mein **infinite loop** na ho, aur koi vertex do baar print na ho. `contains` O(1) hai |
| `stack` (`Stack`) | Jo vertices **abhi dekhne baaki** hain | LIFO: last daala, pehle nikla. Isi se gehrai mein jaate hain |
| `graph.getNeighbors(v)` | `v` ke neighbours ki list | Graph ki andar wali list ko bina chhede padhne ke liye |

---

## 4. Method step by step

### 4.1 `DFSIterative(graph, startVertex)`: O(V + E)

```java
public void DFSIterative(AdjacencyListGraph graph, int startVertex){
    Set<Integer> visited = new HashSet<>();
    Stack<Integer> stack = new Stack<>();

    stack.push(startVertex);
    while(!stack.isEmpty()){

        int currentVertex = stack.pop();

        if(visited.contains(currentVertex)){
            continue;
        }

        System.out.println("Visiting vertex: " + currentVertex);
        visited.add(currentVertex);

        for(int neighbour : graph.getNeighbors(currentVertex)){
            if(!visited.contains(neighbour)){
                stack.push(neighbour);
            }
        }
    }
}
```

**Steps:**
1. Start vertex ko stack mein daalo.
2. Jab tak stack khaali nahi hai:
   1. Top nikalo (`pop`).
   2. Pehle visit ho chuka hai to **skip** (`continue`).
   3. Nahi hua hai to print karo aur `visited` mein daalo.
   4. Uske jo neighbours abhi visit nahi hue, unhe stack mein daalo.

### 4.2 Dry run (start = 1)

Stack mein **top pehle** likha hai.

| Step | `pop` | Visited? | Print | Push kiye | Stack (top → bottom) | `visited` |
|---|---|---|---|---|---|---|
| 0 | – | – | – | 1 | [1] | {} |
| 1 | 1 | nahi | 1 | 2 | [2] | {1} |
| 2 | 2 | nahi | 2 | 3 (1 already visited) | [3] | {1, 2} |
| 3 | 3 | nahi | 3 | 4, 5 (2 already visited) | [**5**, 4] | {1, 2, 3} |
| 4 | 5 | nahi | 5 | – (3 visited) | [4] | {1, 2, 3, 5} |
| 5 | 4 | nahi | 4 | – (3 visited) | [] | {1, 2, 3, 4, 5} |

Output:
```
Visiting vertex: 1
Visiting vertex: 2
Visiting vertex: 3
Visiting vertex: 5
Visiting vertex: 4
```

Step 3 ke baad stack aisa dikhta hai:

```
            ┌───┐
  top  ──►  │ 5 │   ← last mein push hua, isliye pehle niklega
            ├───┤
            │ 4 │
            └───┘
```

### 4.3 ⭐ `4` se pehle `5` kyun aaya?

`3` ke neighbours list mein `[2, 4, 5]` order mein hain. Loop unhe **isi order** mein push karta hai, yaani `4` pehle, phir `5`. Stack mein jo **last** gaya (`5`), wahi **pehle** nikalta hai.

| Push order | Stack | Pop order |
|---|---|---|
| 4, phir 5 | [5, 4] | **5**, phir 4 |

Ye **galat nahi** hai. `1 2 3 5 4` aur `1 2 3 4 5` dono valid DFS hain. DFS ka order neighbours ke order pe depend karta hai.

Recursive DFS jaisa order (`4` pehle) chahiye, to neighbours **ulte order** mein push karo. Same file ke `dfs()` method mein yahi kiya hai:

```java
List<Integer> neighbors = graph.getNeighbors(current);
for (int i = neighbors.size() - 1; i >= 0; i--) {   // ulta loop
    ...
}
```

### 4.4 ⭐ `if(visited.contains(currentVertex)) continue;` kyun zaroori hai?

Ek vertex stack mein **do baar** aa sakta hai, jab do alag vertices use push kar dein (dono ke liye wo us waqt "unvisited" tha).

**Example: triangle** `1-2, 2-3, 3-1`

```
      ( 1 )
      ╱   ╲
   ( 2 )───( 3 )

   Lists:  1 → [2, 3]    2 → [1, 3]    3 → [2, 1]
```

| Step | `pop` | Kya hua | Stack (top → bottom) |
|---|---|---|---|
| 0 | – | push 1 | [1] |
| 1 | 1 | visit 1, push 2, 3 | [3, 2] |
| 2 | 3 | visit 3, push **2** (abhi unvisited hai) | [**2**, 2] |
| 3 | 2 | visit 2 | [2] |
| 4 | 2 | ✅ **already visited, skip** | [] |

- ❌ **Check ke bina:** step 4 pe `"Visiting vertex: 2"` **do baar** print hota.
- ✅ **Check ke saath:** har vertex sirf ek baar visit hota hai.

**Push karte waqt bhi to check hai, phir pop pe kyun?** Push wala check sirf itna dekhta hai ki *us waqt* vertex visit hua tha ya nahi. Step 1 mein `2` push hua, phir step 2 mein dobara push hua, kyunki tab tak `2` visit nahi hua tha. Dono copies stack mein baith gayi. Isliye pop pe dobara check karna padta hai.

### 4.5 Ek hi file mein do version

| | `DFSIterative(graph, start)` | `dfs(graph, start)` |
|---|---|---|
| Output | Console pe print karta hai | `List<Integer>` return karta hai (test karna aasaan) |
| Stack | `Stack<Integer>` | `ArrayDeque<Integer>` (modern, fast) |
| Neighbour push order | Seedha, isliye `1 2 3 5 4` | Ulta, isliye `1 2 3 4 5` (recursive jaisa) |
| Logic | Same: pop → skip if visited → visit → unvisited neighbours push | Same |

### 4.6 Graph banake kaise chalayein (`main`)

```java
AdjacencyListGraph graph = new AdjacencyListGraph();
for (int v = 1; v <= 5; v++) {
    graph.addVertex(v);          // pehle vertices, warna addEdge pe NullPointerException
}
graph.addEdge(1, 2);
graph.addEdge(2, 3);
graph.addEdge(3, 4);
graph.addEdge(3, 5);

GraphTraversalDFS traversal = new GraphTraversalDFS();
traversal.DFSIterative(graph, 1);
```

---

## 5. Complexity summary

| | Complexity | Kyun |
|---|---|---|
| Time | **O(V + E)** | Har vertex ek baar visit hota hai (V). Har vertex ki neighbour list ek baar scan hoti hai, sab mila ke 2E (E) |
| Space: `visited` | O(V) | Har vertex max ek baar |
| Space: `stack` | O(E) worst case | Ek vertex kai baar push ho sakta hai (section 4.4), isliye stack V se bada ho sakta hai |

### Iterative vs Recursive DFS

| | Iterative (ye code) | Recursive |
|---|---|---|
| Stack | Khud ka `Stack` (heap memory mein) | Java ka call stack |
| Bahut bada / lamba graph | ✅ Safe | ❌ `StackOverflowError` aa sakta hai |
| Code | Thoda lamba | Chhota, padhne mein aasaan |
| Order | Neighbour push order pe depend karta hai | Neighbour list ka order |

---

## 6. ⚠️ Dhyan rakhne wali baatein (is code mein)

1. **Disconnected graph:** sirf wahi vertices visit honge jo `startVertex` se **jude** hain. Jaise koi vertex `6` kisi se connected nahi hai, to wo print hi nahi hoga. Poora graph cover karna ho to har unvisited vertex se DFS chalana padega. Uske liye graph se saare vertices ki list chahiye, jo abhi `AdjacencyListGraph` mein nahi hai.
2. **Galat start vertex:** `startVertex` graph mein nahi hai, tab bhi `"Visiting vertex: 99"` print ho jaayega. `getNeighbors` khaali list deta hai, isliye crash nahi hota, lekin output misleading hai.
3. **`Stack` class purani hai.** Java docs `ArrayDeque` recommend karte hain: wo fast hai aur bina synchronization ke chalti hai. `push` / `pop` same naam se kaam karte hain.
4. **Print karta hai, return nahi.** Test likhna mushkil hai. Isliye `dfs()` version `List` return karta hai.
5. **Method ka naam** `DFSIterative` capital se shuru hota hai. Java convention mein method names `dfsIterative` jaise lowercase se shuru hote hain. Ye chalega, bas style ki baat hai.
6. **Order alag aaye to ghabrao mat:** DFS ke ek se zyada valid answers hote hain (section 4.3).

---

## 7. Quick revision ✅

- [ ] DFS = gehrai mein jao, phir backtrack. Iterative version mein **Stack (LIFO)** use hota hai
- [ ] Loop: `pop` → visited hai to `continue` → print + `visited.add` → unvisited neighbours `push`
- [ ] Ek vertex stack mein do baar aa sakta hai, isliye **pop ke baad visited check** zaroori hai
- [ ] Neighbours seedhe order mein push karo to last neighbour pehle visit hota hai. Ulta push karo to recursive jaisa order milta hai
- [ ] Time O(V + E), `visited` O(V), stack worst case O(E)
- [ ] Sirf start se jude vertices hi visit hote hain
- [ ] Bade graph pe iterative safe hai, recursive mein `StackOverflowError` ka risk hai

