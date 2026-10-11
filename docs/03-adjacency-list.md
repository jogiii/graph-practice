# 03 · Adjacency List (meri implementation)

> Code: [`AdjacencyListGraph.java`](../src/main/java/com/practice/graph/common/AdjacencyListGraph.java)
> Matrix wala version: [02-adjacency-matrix-add-remove-vertex.md](02-adjacency-matrix-add-remove-vertex.md)

---

## 1. Ek line mein

Har vertex ke saath ek **list** rakho, jismein uske saare **neighbours** (jinse woh connected hai) likhe hon.

Matrix mein har vertex ke liye `V` cells hote hain, chahe edge ho ya na ho. List mein **sirf wahi edges store hoti hain jo sach mein hain**.

---

## 2. Picture se samjho

### Graph (undirected)

```
              ( 2 )───────────( 3 )
             ╱                ╱   ╲
          ( 1 )              ╱     ╲
                          ( 4 )   ( 5 )

   Edges: 1-2, 2-3, 3-4, 3-5
```

### Iski adjacency list

Har line ka pehla dabba **vertex** hai (HashMap ki key). Uske aage ki chain uske **neighbours** hain (LinkedList). `✗` matlab list khatam (`null`).

```
 key        LinkedList (neighbours)
┌───┐     ┌───┬──┐
│ 1 │ ──► │ 2 │ ─┼──► ✗
└───┘     └───┴──┘

┌───┐     ┌───┬──┐   ┌───┬──┐
│ 2 │ ──► │ 1 │ ─┼──►│ 3 │ ─┼──► ✗
└───┘     └───┴──┘   └───┴──┘

┌───┐     ┌───┬──┐   ┌───┬──┐   ┌───┬──┐
│ 3 │ ──► │ 2 │ ─┼──►│ 4 │ ─┼──►│ 5 │ ─┼──► ✗
└───┘     └───┴──┘   └───┴──┘   └───┴──┘

┌───┐     ┌───┬──┐
│ 4 │ ──► │ 3 │ ─┼──► ✗
└───┘     └───┴──┘

┌───┐     ┌───┬──┐
│ 5 │ ──► │ 3 │ ─┼──► ✗
└───┘     └───┴──┘
```

Dhyan do:
- Edge `3-5` **do jagah** dikhti hai: `3` ki list mein `5`, aur `5` ki list mein `3`. Undirected graph mein har edge dono taraf likhi jaati hai.
- Har vertex ki list ki length = uski **degree**. Vertex `3` ke 3 neighbours hain, isliye uski degree 3 hai.
- Saari lists ki total length `2 × E` hai (yahan `2 × 4 = 8`).

---

## 3. Data structure

```java
private Map<Integer, List<Integer>> adjacencyList;   // vertex → uske neighbours
```

| Part | Kya hai | Kyun |
|---|---|---|
| `Map` (`HashMap`) | key = vertex number | Vertex ki list O(1) mein mil jaati hai. Vertex number kuch bhi ho sakta hai (`1, 5, 100`), `0..V-1` hona zaroori nahi |
| `List` (`LinkedList`) | value = neighbours | Size apne-aap badhta-ghatta hai, matrix ki tarah poora copy nahi karna padta |

---

## 4. Methods, step by step

### 4.1 `addVertex(vertex)`: O(1)

```java
adjacencyList.put(vertex, new LinkedList<>());
```

Map mein nayi key daalo, jiske saath ek **khaali list** ho, yaani abhi koi neighbour nahi hai.

```
addVertex(5)

┌───┐
│ 5 │ ──► ✗        (khaali list)
└───┘
```

Matrix mein yahi kaam O(V²) tha (naya matrix banake copy karna). Yahan sirf ek entry judti hai.

---

### 4.2 `addEdge(source, destination)`: O(1)

```java
adjacencyList.get(source).add(destination);
adjacencyList.get(destination).add(source);   // undirected
```

`source` ki list ke **end** mein `destination` jodo, aur `destination` ki list mein `source`.

```
addEdge(3, 5)

Pehle:
┌───┐     ┌───┬──┐   ┌───┬──┐
│ 3 │ ──► │ 2 │ ─┼──►│ 4 │ ─┼──► ✗
└───┘     └───┴──┘   └───┴──┘
┌───┐
│ 5 │ ──► ✗
└───┘

Baad mein:
┌───┐     ┌───┬──┐   ┌───┬──┐   ┏━━━┱──┐
│ 3 │ ──► │ 2 │ ─┼──►│ 4 │ ─┼──►┃ 5 ┃ ─┼──► ✗     ← naya node
└───┘     └───┴──┘   └───┴──┘   ┗━━━┹──┘
┌───┐     ┏━━━┱──┐
│ 5 │ ──► ┃ 3 ┃ ─┼──► ✗                            ← naya node
└───┘     ┗━━━┹──┘
```

`LinkedList.add()` end mein jodta hai, aur LinkedList ko apna last node pata hota hai. Isliye ye O(1) hai.

---

### 4.3 `removeEdge(source, destination)`: O(degree)

```java
adjacencyList.get(source).remove((Integer) destination);
adjacencyList.get(destination).remove((Integer) source);
```

Dono lists mein jaake ek-doosre ko hatao.

```
removeEdge(3, 4)

Pehle:
┌───┐     ┌───┬──┐   ┌───┬──┐   ┌───┬──┐
│ 3 │ ──► │ 2 │ ─┼──►│ 4 │ ─┼──►│ 5 │ ─┼──► ✗
└───┘     └───┴──┘   └───┴──┘   └───┴──┘
┌───┐     ┌───┬──┐
│ 4 │ ──► │ 3 │ ─┼──► ✗
└───┘     └───┴──┘

Baad mein:
┌───┐     ┌───┬──┐   ┌───┬──┐
│ 3 │ ──► │ 2 │ ─┼──►│ 5 │ ─┼──► ✗      (4 hata, 2 seedha 5 se jud gaya)
└───┘     └───┴──┘   └───┴──┘
┌───┐
│ 4 │ ──► ✗
└───┘
```

Ye O(degree) hai, kyunki value dhoondhne ke liye list ko shuru se scan karna padta hai.

#### ⭐ `(Integer)` cast kyun lagaya?

`List` mein `remove` ke **do version** hain:

| Call | Kya karta hai |
|---|---|
| `list.remove(int index)` | Us **position** pe jo hai, use hatata hai |
| `list.remove(Object o)` | Us **value** ko dhoondh ke hatata hai |

```
list = [2, 4, 5]

list.remove(4)            → index 4 hatane ki koshish → IndexOutOfBoundsException ❌
list.remove((Integer) 4)  → value 4 hatao             → [2, 5] ✅
```

`(Integer)` lagane se Java ko pata chalta hai ki ye **value** hai, index nahi. Ye cast bhool gaye to bug aayega: kabhi galat element hat jaayega, kabhi exception aayega.

---

### 4.4 `removeVertex(vertex)`: O(V + E)

```java
adjacencyList.remove(vertex);                        // 1. vertex ki apni line hatao
for (List<Integer> neighbors : adjacencyList.values()) {
    neighbors.remove((Integer) vertex);              // 2. baaki sab lists se bhi hatao
}
```

Do kaam karne padte hain:
1. Vertex ki **apni entry** map se hatao.
2. **Baaki sab vertices ki lists** mein jahan-jahan ye vertex neighbour ke roop mein likha hai, wahan se bhi hatao.

```
removeVertex(3)

Pehle:                                         Baad mein:
┌───┐    ┌───┐                                 ┌───┐    ┌───┐
│ 1 │ ─► │ 2 │ ─► ✗                            │ 1 │ ─► │ 2 │ ─► ✗
└───┘    └───┘                                 └───┘    └───┘
┌───┐    ┌───┐   ┌───┐                         ┌───┐    ┌───┐
│ 2 │ ─► │ 1 │ ─►│ 3 │ ─► ✗                    │ 2 │ ─► │ 1 │ ─► ✗          ← 3 hata
└───┘    └───┘   └───┘                         └───┘    └───┘
┌───┐    ┌───┐   ┌───┐   ┌───┐
│ 3 │ ─► │ 2 │ ─►│ 4 │ ─►│ 5 │ ─► ✗            (poori line gayab)              ← step 1
└───┘    └───┘   └───┘   └───┘
┌───┐    ┌───┐                                 ┌───┐
│ 4 │ ─► │ 3 │ ─► ✗                            │ 4 │ ─► ✗                     ← 3 hata
└───┘    └───┘                                 └───┘
┌───┐    ┌───┐                                 ┌───┐
│ 5 │ ─► │ 3 │ ─► ✗                            │ 5 │ ─► ✗                     ← 3 hata
└───┘    └───┘                                 └───┘
```

```
Graph pehle:                     Graph baad mein:

   ( 2 )─────( 3 )                  ( 2 )
   ╱         ╱   ╲                  ╱
( 1 )     ( 4 ) ( 5 )            ( 1 )      ( 4 )   ( 5 )      ← 4 aur 5 akele reh gaye
```

**O(V + E) kyun?** Har vertex ki list dekhni padti hai (`V`), aur saari lists mila ke total `2E` elements scan hote hain.

**Matrix se farak:** yahan vertex hatane pe **baaki vertices ke number nahi badalte**. `4` abhi bhi `4` hai, `5` abhi bhi `5`, kyunki map mein key hi vertex ka naam hai, index nahi.

---

## 5. Complexity summary

| Operation | Adjacency List (ye code) | Adjacency Matrix |
|---|---|---|
| `addVertex` | **O(1)** | O(V²) |
| `removeVertex` | O(V + E) | O(V²) |
| `addEdge` | **O(1)** | O(1) |
| `removeEdge` | O(degree) | **O(1)** |
| Edge hai ya nahi? | O(degree) | **O(1)** |
| Saare neighbours | **O(degree)** | O(V) |
| Space | **O(V + E)** | O(V²) |

- **List** tab lo jab graph sparse ho (kam edges), vertices baar-baar add hon, ya neighbours pe loop karna ho (BFS/DFS). Zyaadatar interview problems mein yahi chalta hai.
- **Matrix** tab lo jab graph dense ho, ya baar-baar "kya `u-v` connected hain?" poochna ho.

---

## 6. ⚠️ Dhyan rakhne wali baatein (is code mein)

1. **Pehle `addVertex`, phir `addEdge`.** Agar vertex map mein nahi hai, to `adjacencyList.get(x)` `null` dega aur `.add()` pe `NullPointerException` aayega.
2. **Same vertex do baar `addVertex` kiya** to `put` purani list ko **nayi khaali list se replace** kar dega, aur us vertex ki saari edges gayab ho jaayengi. Bachne ke liye `putIfAbsent` use kar sakte ho.
3. **Same edge do baar `addEdge` kiya** to list mein duplicate aa jaayega (`3 → 5 → 5`). List isko rokti nahi hai.
4. **`remove` mein `(Integer)` cast** zaroori hai (upar section 4.3 dekho).
5. `removeEdge` sirf **pehli matching value** hatata hai. Duplicate edges hon to ek bachi reh jaayegi.

---

## 7. Quick revision ✅

- [ ] `Map<vertex, List<neighbour>>`: har vertex ke saath uske neighbours ki list
- [ ] Undirected hai, isliye edge **dono lists** mein jodo aur dono se hatao
- [ ] `remove((Integer) x)` = value hatao, `remove(x)` = index hatao
- [ ] `removeVertex` = apni line hatao **+** sabki lists se bhi hatao
- [ ] Vertex hatane pe baaki vertices ke number nahi badalte (matrix mein badalte hain)
- [ ] Space O(V + E), isliye sparse graph ke liye best

