# 02 · Adjacency Matrix: Edge aur Vertex add/remove (meri implementation)

> Code: [`AdjacencyMatGraph.java`](../src/main/java/com/practice/graph/common/AdjacencyMatGraph.java)
> Basic concept pehle padhna ho to: [01-adjacency-matrix.md](01-adjacency-matrix.md)

Ye graph **undirected** aur **unweighted** hai:
- `adjMatrix[i][j] = 1` matlab `i` aur `j` ke beech edge hai.
- `adjMatrix[i][j] = 0` matlab edge nahi hai.

---

## 1. Fields

```java
private int adjMatrix[][];   // V x V ka matrix
private int vertices;        // abhi kitne vertices hain (V)
```

Vertices ke number `0` se `vertices - 1` tak hain. Yahi matrix ke index bhi hain.

---

## 2. Constructor

```java
adjMatrix = new int[vertices][vertices];
```

Java mein naya `int` array apne-aap `0` se bhara hota hai, isliye shuru mein **koi edge nahi** hoti.

---

## 3. `addEdge(source, destination)`: O(1)

```java
validateVertex(source);
validateVertex(destination);
adjMatrix[source][destination] = 1;
adjMatrix[destination][source] = 1;
```

Undirected graph hai, matlab `A - B` edge ka matlab `A → B` bhi hai aur `B → A` bhi. Isliye **dono cells** mein `1` likhte hain. Ek bhi bhool gaye to graph galat ho jaayega.

---

## 4. `removeEdge(source, destination)`: O(1)

`addEdge` ka ulta: pehle dono vertex validate karo, phir dono cells ko wapas `0` kar do.

---

## 5. `validateVertex(vertex)`: range check

```java
if (vertex < 0 || vertex >= vertices) {
    throw new IllegalArgumentException("Invalid vertex index: " + vertex);
}
```

- Valid vertex `0` se `vertices - 1` tak hi hote hain.
- Check na karein to Java khud `ArrayIndexOutOfBoundsException` dega, jisse samajhna mushkil hai ki galti kahan hui. Apna message wala error saaf batata hai ki kaunsa vertex galat tha.
- `addEdge`, `removeEdge` aur `removeVertex` teeno isi ek method ko use karte hain, isliye check ek hi jagah likha hai.
- Graph mein `0` vertices hon aur `removeVertex` call ho, to `vertex >= 0` wali condition fail ho jaayegi aur error aayega. Ye sahi behaviour hai.

---

## 6. `addVertex()`: O(V²)

**Problem:** Java mein array ka size fix hota hai, usme ek row/column "jod" nahi sakte.

**Solution:** ek size **bada** naya matrix banao aur purana data copy kar do.

```
Purana (2x2)        Naya (3x3)
    0  1                0  1  2
0 [ 0  1 ]          0 [ 0  1  0 ]
1 [ 1  0 ]    →     1 [ 1  0  0 ]
                    2 [ 0  0  0 ]   ← naya vertex 2, abhi kisi se connected nahi
```

Steps:
1. `new int[vertices + 1][vertices + 1]`, yaani naya matrix, sab `0`.
2. Purane matrix ke saare cells **same index** pe copy karo (`new[i][j] = old[i][j]`).
3. Naye vertex ki row aur column `0` hi rehte hain, yaani koi edge nahi.
4. `adjMatrix` ko naye matrix se replace karo, `vertices++`.

Yahan index shift ki zarurat **nahi** hai, kyunki naya vertex hamesha **last** mein judta hai.

---

## 7. `removeVertex(vertex)`: O(V²) ⭐ (tricky wala)

**Problem:** vertex hatane ka matlab hai uski **row** aur **column** dono hatana. Iske baad baaki values ko khiska ke chhote matrix mein fit karna padta hai.

### Idea: do counter, `newRow` aur `newCol`

- `i`, `j` → **purane** matrix mein position
- `newRow`, `newCol` → **naye** matrix mein agli khaali jagah

Counter **sirf tab badhta hai jab kuch copy hota hai**. Hataye gaye vertex ki row/column pe counter ruka rehta hai. Isliye aage ki values apne-aap ek step peeche khisak jaati hain.

### Code flow

```java
// 1. Galat vertex aaya to error
validateVertex(vertex);

// 2. Ek size chhota matrix
int[][] newAdjMatrix = new int[vertices - 1][vertices - 1];

// 3. Copy, hataye gaye vertex ko chhod ke
int newRow = 0;
for (i = 0 .. vertices-1) {
    if (i != vertex) {                 // iski row skip
        int newCol = 0;                // har nayi row mein column 0 se shuru
        for (j = 0 .. vertices-1) {
            if (j != vertex) {         // iska column skip
                newAdjMatrix[newRow][newCol] = adjMatrix[i][j];
                newCol++;
            }
        }
        newRow++;
    }
}

// 4. Replace
adjMatrix = newAdjMatrix;
vertices--;
```

### Dry run: 4 vertices, vertex `2` hatao

```
Purana (4x4)                    Naya (3x3)
     0  1  2  3                      0  1  2
0  [ a  b  c  d ]               0  [ a  b  d ]   ← purana 0
1  [ e  f  g  h ]               1  [ e  f  h ]   ← purana 1
2  [ i  j  k  l ]  ← skip       2  [ m  n  p ]   ← purana 3
3  [ m  n  o  p ]
           ↑ skip
```

| `i` | Kya hua | Kis `newRow` mein gaya |
|---|---|---|
| 0 | copy (`c` skip) | 0 |
| 1 | copy (`g` skip) | 1 |
| 2 | **poori row skip**, `newRow` nahi badha | – |
| 3 | copy (`o` skip) | 2 |

### Out of bound kyun nahi aata?

Naye matrix ke valid index `0` se `V-2` tak hain. Loop `V` baar chalta hai, lekin ek baar skip hota hai, isliye `newRow++` sirf `V-1` baar hota hai. Index kabhi `V-2` se aage nahi jaata. Yahi baat `newCol` pe bhi lagti hai.

**Galat tareeka** (jo pehle likha tha): `newAdjMatrix[i][j] = adjMatrix[i][j]`. Isme `i` ya `j` `V-1` tak jaate hain, jo naye matrix ke bahar hai, isliye `ArrayIndexOutOfBoundsException` aata hai. Ye sirf tab chalta hai jab **aakhri** vertex hatao.

### ⚠️ Side effect: vertex numbers badal jaate hain

Vertex `2` hatane ke baad purana vertex `3` ab vertex `2` kehlata hai. Agar code mein kahin purane numbers save the, to wo ab galat vertex ko point karenge.

---

## 8. Complexity summary

| Method | Time | Kyun |
|---|---|---|
| `addEdge` | O(1) | 2 cells set |
| `removeEdge` | O(1) | 2 cells reset |
| `addVertex` | O(V²) | poora matrix copy |
| `removeVertex` | O(V²) | poora matrix copy |
| Space | O(V²) | `V x V` matrix |

Vertex add/remove matrix mein **mehenga** hai. Adjacency list mein vertex add karna O(1) hota hai. Isliye agar vertices baar-baar badalte hain, to list better hai.

---

## 9. Quick revision ✅

- [ ] Undirected hai, to edge add/remove mein **dono** cells update karo
- [ ] Har method mein pehle `validateVertex`, taaki galat index pe saaf error mile
- [ ] Array resize nahi hota, isliye vertex add/remove = **naya matrix + copy**
- [ ] `addVertex`: same index pe copy, naya vertex last mein
- [ ] `removeVertex`: `newRow` / `newCol` counter, sirf copy hone pe badhte hain
- [ ] Vertex hatane ke baad aage ke vertex numbers ek kam ho jaate hain






