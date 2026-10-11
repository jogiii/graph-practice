# graph-practice

Java 21 + Gradle project for practicing graph problems.

## Structure

```
src/main/java/com/practice/graph/
  common/      # shared data structures (Graph, Edge, ...)
  traversal/   # BFS, DFS, ...
src/test/java/com/practice/graph/   # JUnit 5 tests mirroring main packages
```

Suggested packages for new problems: `shortestpath`, `mst`, `topologicalsort`, `unionfind`, `cycle`, etc.

## Build & test

```bash
./gradlew build      # compile + run tests
./gradlew test       # run tests only
```
