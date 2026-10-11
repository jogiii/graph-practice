# graph-practice

Java 21 + Gradle project for practicing graph problems.

## Structure

```
docs/                               # 📒 concept notes (Hinglish) – start at docs/README.md
src/main/java/com/practice/graph/
  common/      # graph representations (AdjacencyMatrixGraph, ...)
src/test/java/com/practice/graph/   # JUnit 5 tests mirroring main packages
```

Suggested packages for new problems: `traversal`, `shortestpath`, `mst`, `topologicalsort`, `unionfind`, `cycle`, etc.

## Notes

Concept revise karne ke liye [docs/README.md](docs/README.md) dekho.

## Build & test

```bash
./gradlew build      # compile + run tests
./gradlew test       # run tests only
```
