# 05 · DFS Recursive: Recursion se Depth First Search (meri implementation)

> Code: [`GraphTraversalDFS.java`](../src/main/java/com/practice/graph/traversal/GraphTraversalDFS.java), methods `DFSRecursive` + `dfsHelper`
> Related: [04-dfs-iterative.md](04-dfs-iterative.md) (same DFS, stack se) · [03-adjacency-list.md](03-adjacency-list.md) (graph)

---

## 1. Ek line mein

DFS ko recursion se likhna: **"is vertex ko visit karo, phir har unvisited neighbour pe yahi function dobara chalao."**

Iterative version mein hum khud `Stack` banate the. Yahan **Java ka call stack** wahi kaam karta hai. Har function call ek naya "plate" stack pe rakhta hai, aur `return` hone pe plate hat jaati hai. Yahi **backtrack** hai.

---

## 2. Picture se samjho

### Graph (wahi jo `main` mein hai)

```
              ( 2 )───────────( 3 )
             ╱                ╱   ╲
          ( 1 )              ╱     ╲
                          ( 4 )   ( 5 )

   Edges: 1-2, 2-3, 3-4, 3-5

   Lists:  1 → [2]   2 → [1, 3]   3 → [2, 4, 5]   4 → [3]   5 → [3]
```

### Call tree: kaun kisko call karta hai

```
dfsHelper(1)
 └── dfsHelper(2)
      └── dfsHelper(3)
           ├── dfsHelper(4)      ← pehle 4 (list mein pehle hai)
           │     (4 ke neighbours: 3 visited → return)
           └── dfsHelper(5)      ← 4 se wapas aake, phir 5
                 (5 ke neighbours: 3 visited → return)

   Output: 1 → 2 → 3 → 4 → 5
```

Dhyan do:
- Har level pe **ek call andar** jaata hai. Ye gehrai mein jaana hai.
- `dfsHelper(4)` khatam hua to control **wapas `dfsHelper(3)` ke loop mein** aata hai, aur loop agla neighbour `5` leta hai. Ye backtrack hai, aur iske liye koi extra code nahi likhna padta.
- Order neighbour list ke order jaisa hi hai (`4` phir `5`). Iterative version mein ye ulta aata tha (`5` phir `4`).

---

## 3. Data structures

```java
Set<Integer> visited = new HashSet<>();   // ek hi set, saari calls share karti hain
```

| Part | Kya hai | Kyun |
|---|---|---|
| `visited` (`HashSet`) | Jo vertices dekh liye | Cycle mein infinite recursion na ho. Saari calls mein **same object** pass hota hai |
| Call stack (Java ka) | Abhi chal rahi `dfsHelper` calls | Explicit `Stack` ki zarurat nahi. Return = backtrack |
| `graph.getNeighbors(v)` | Neighbours ki read-only list | Loop chalane ke liye |

---

## 4. Methods step by step

### 4.1 `DFSRecursive(graph, startVertex)`: entry point

```java
public void DFSRecursive(AdjacencyListGraph graph, int startVertex){
    Set<Integer> visited = new HashSet<>();
    dfsHelper(graph, startVertex, visited);
}
```

`visited` **ek baar** banao, phir helper ko do.

#### ⭐ Helper method kyun chahiye?

Agar `visited` ko recursive function ke **andar** banate, to har call ka apna naya khaali set hota. Kisi ko pata hi nahi chalta ki pehle kya visit hua, aur cycle mein recursion kabhi khatam nahi hota.

```
❌  void dfs(v) { Set visited = new HashSet<>(); ... dfs(n); }   → har call ka naya set
✅  DFSRecursive → visited ek baar → dfsHelper(v, visited)       → sab ek hi set share karte hain
```

Public method user ke liye simple rehta hai (`graph, start`). Helper `private` hai, kyunki uska `visited` parameter andar ki detail hai.

### 4.2 `dfsHelper(graph, currentVertex, visited)`: asli kaam, O(V + E) total

```java
private void dfsHelper(AdjacencyListGraph graph, int currentVertex, Set<Integer> visited){
    visited.add(currentVertex);
    System.out.println("Visiting vertex: " + currentVertex);

    for(int neighbour : graph.getNeighbors(currentVertex)){
        if(!visited.contains(neighbour)){
            dfsHelper(graph, neighbour, visited);
        }
    }
}
```

**Steps:**
1. Current vertex ko visited mark karo aur print karo.
2. Har neighbour dekho. Abhi tak visit nahi hua hai to **usi pe `dfsHelper` call karo**, yaani gehrai mein jao.
3. Loop khatam hua matlab is vertex se aage kuch nahi bacha. Function return hota hai aur control caller ke loop mein wapas jaata hai (backtrack).

**Base case kahan hai?** Koi alag `if (...) return;` nahi likha. Jab saare neighbours visited hain, to loop mein koi call nahi hoti aur function apne-aap khatam ho jaata hai. **`visited` hi recursion ko rokta hai.**

### 4.3 Dry run: call stack ke saath (start = 1)

Call stack mein **top upar** hai.

| Step | Kya hua | Call stack (top → bottom) | Print |
|---|---|---|---|
| 1 | `dfsHelper(1)`, neighbour 2 unvisited, call | [1] | 1 |
| 2 | `dfsHelper(2)`, neighbour 1 visited (skip), 3 unvisited, call | [2, 1] | 2 |
| 3 | `dfsHelper(3)`, neighbour 2 skip, 4 unvisited, call | [3, 2, 1] | 3 |
| 4 | `dfsHelper(4)`, neighbour 3 skip, **return** | [4, 3, 2, 1] | 4 |
| 5 | Wapas `3` ke loop mein, agla neighbour 5, call | [3, 2, 1] | – |
| 6 | `dfsHelper(5)`, neighbour 3 skip, **return** | [5, 3, 2, 1] | 5 |
| 7 | `3` ka loop khatam, return. `2` khatam, return. `1` khatam, return | [] | – |

Step 4 pe call stack sabse gehra hai:

```
            ┌──────────────┐
  top  ──►  │ dfsHelper(4) │   ← abhi chal raha hai
            ├──────────────┤
            │ dfsHelper(3) │   ← loop beech mein ruka hai (next: 5)
            ├──────────────┤
            │ dfsHelper(2) │
            ├──────────────┤
            │ dfsHelper(1) │
            ├──────────────┤
            │ DFSRecursive │
            └──────────────┘
```

`dfsHelper(3)` ka loop **wahin ruka hua hai** jahan usne `4` ko call kiya tha. `4` return hote hi wahi loop `5` pe aage badhta hai. Ye "yaad rakhna" call stack apne-aap karta hai. Iterative version mein yahi kaam hum stack mein `5` pehle se daal ke karte the.

### 4.4 ⭐ Yahan duplicate visit kyun nahi hota? (iterative se farak)

Iterative mein pop ke baad ek extra `if(visited.contains) continue;` lagana pada tha. Yahan uski zarurat nahi hai.

**Triangle** `1-2, 2-3, 3-1` pe dekho:

```
      ( 1 )
      ╱   ╲
   ( 2 )───( 3 )

   Lists:  1 → [2, 3]    2 → [1, 3]    3 → [2, 1]
```

| Step | Kya hua | Visited |
|---|---|---|
| 1 | `dfsHelper(1)`, neighbour 2 unvisited, call | {1} |
| 2 | `dfsHelper(2)`, 1 visited skip, 3 unvisited, call | {1, 2} |
| 3 | `dfsHelper(3)`, 2 skip, 1 skip, return | {1, 2, 3} |
| 4 | `2` return. Wapas `1` ke loop mein, agla neighbour **3: ab visited hai, skip** ✅ | {1, 2, 3} |

**Kyun?** Check **call karne se theek pehle** hota hai, aur call ke andar pehli line pe hi visited mark ho jaata hai. Beech mein koi gap nahi hai.

Iterative mein check **push ke waqt** hota tha, lekin visit **pop ke waqt**, jo baad mein hota hai. Is gap mein same vertex do baar push ho jaata tha.

| | Check kab | Visit kab | Gap? |
|---|---|---|---|
| Iterative | push ke waqt | pop ke waqt (baad mein) | ✅ hai, isliye extra check chahiye |
| Recursive | call se pehle | call ki pehli line | ❌ nahi |

### 4.5 `main` mein kaise chalayein

```java
GraphTraversalDFS traversal = new GraphTraversalDFS();
traversal.DFSIterative(graph, 1);   // 1 2 3 5 4
traversal.DFSRecursive(graph, 1);   // 1 2 3 4 5
```

---

## 5. Complexity summary

| | Complexity | Kyun |
|---|---|---|
| Time | **O(V + E)** | Har vertex pe `dfsHelper` ek hi baar chalta hai (V). Har neighbour list ek baar scan hoti hai (E) |
| Space: `visited` | O(V) | Har vertex max ek baar |
| Space: call stack | O(V) worst case | Recursion ki gehrai. Line jaisa graph (`1-2-3-…-V`) ho to `V` calls ek ke upar ek |

### Recursive vs Iterative (dono aapke code mein)

| | `DFSRecursive` | `DFSIterative` |
|---|---|---|
| Stack | Java call stack (apne-aap) | Khud ka `Stack<Integer>` |
| Order (is graph pe) | `1 2 3 4 5` (list order) | `1 2 3 5 4` (ulta, LIFO ki wajah se) |
| Duplicate check | Sirf call se pehle | Push se pehle **+ pop ke baad** |
| Stack space | O(V) | O(E) worst case |
| Bahut gehra graph (lakhon vertices ki line) | ❌ `StackOverflowError` | ✅ Safe (heap memory) |
| Code | Chhota, padhne mein aasaan | Thoda lamba |

**Kab kaunsa?** Interview aur chhote graphs mein recursive likhna fast aur clean hai. Graph bahut bada ya gehra ho sakta hai (`10^5+` vertices ki chain), to iterative lo.

---

## 6. ⚠️ Dhyan rakhne wali baatein (is code mein)

1. **`StackOverflowError`:** Java ka default call stack kuch hazaar se das-hazaar ke aas-paas calls tak hi gehra jaa sakta hai. Lambi chain wale graph pe recursive DFS crash ho jaayega. Tab iterative use karo.
2. **`visited` helper ke andar mat banao.** Har call ka naya set banega, aur cycle mein infinite recursion hoga (section 4.1).
3. **Disconnected graph:** sirf `startVertex` se jude vertices hi visit hote hain, iterative jaisa hi.
4. **Galat start vertex:** `startVertex` graph mein nahi hai, tab bhi `"Visiting vertex: 99"` print hota hai. `getNeighbors` khaali list deta hai, isliye crash nahi hota.
5. **Print karta hai, return nahi.** Test ke liye `List` return karna better hai.
6. **Method name** `DFSRecursive` capital se shuru hota hai. Java convention `dfsRecursive` hai. Chalega, bas style ki baat hai.

---

## 7. Quick revision ✅

- [ ] Recursive DFS = visit karo, phir har unvisited neighbour pe **khud ko call** karo
- [ ] `visited` **ek baar** banao (public method mein), helper ko pass karo
- [ ] Alag base case nahi hai. Saare neighbours visited hain to loop mein koi call nahi, aur function khatam
- [ ] Return = backtrack. Caller ka loop wahin se aage chalta hai
- [ ] Order = neighbour list ka order (iterative ka ulta)
- [ ] Duplicate visit nahi hota, kyunki check aur visit ke beech gap nahi hai
- [ ] Time O(V + E), space O(V). Bahut gehre graph pe `StackOverflowError`, tab iterative lo

