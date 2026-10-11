---
name: hinglish-dsa-notes
description: Write revision notes in Hinglish (Markdown) for DSA / graph / algorithm code the user has written, with ASCII box-and-arrow diagrams, before/after pictures, dry-run tables, complexity tables, gotchas and a quick-revision checklist, saved under docs/ in the repo. Use this whenever the user asks for "documentation", "notes", "doc", "explanation", or "samjhao/likho taaki agli baar padh ke samajh saku" for a data structure or algorithm (graphs, adjacency list/matrix, BFS/DFS, trees, DP, sorting, etc.), or shares their own implementation and wants it explained for future reference, even if they don't say "Hinglish" or "Markdown". Do NOT use for Javadoc/API docs or for production README writing.
---

# Hinglish DSA Notes

Ye skill user ke apne DSA code ke liye **revision notes** banata hai. Goal: user kuch hafte baad file khole aur 5 minute mein poora concept wapas yaad aa jaaye: kya ho raha hai, kyun ho raha hai, aur kahan galti ho sakti hai.

User learner hai, interview / practice ke liye padh raha hai. Isliye notes **visual, simple aur unke apne code pe based** hone chahiye, generic textbook nahi.

## Workflow

1. **User ka code padho** (jo file attach/active hai). Notes ussi code ke baare mein honge: unke variable names, unke method names. Agar repo mein related pehle ke notes hain (`docs/*.md`), unhe bhi dekho taaki style aur numbering match kare aur cross-link kar sako.
2. **Logic mat badlo.** Notes likhna hai, code refactor nahi karna. Agar code mein bug ya risk dikhe (NPE, out of bound, duplicate, overwrite), use notes ke "Dhyan rakhne wali baatein" section mein likho aur reply mein user ko batao. Fix tabhi karo jab user bole.
3. **File banao:** `docs/NN-topic-name.md`. `NN` = agla number (existing files dekh ke). Naam lowercase-hyphen, jaise `04-bfs.md`.
4. **Index update karo:** `docs/README.md` ki table mein nayi row jodo (`# | Topic | Notes | Code`). Agar `docs/README.md` nahi hai, to bana do (template neeche hai).
5. Notes file editor mein khol do aur reply mein chhota summary do: kaunse sections hain, aur koi gotcha mila to woh.

User ne **Javadoc / HTML comments mana kiye hain**: woh Markdown notes chahte hain. Code ke andar kuch likhna ho to sirf chhote `//` comments, aur woh bhi tabhi jab user maange.

## Language & tone

- **Hinglish** (Roman script Hindi + English technical words): "Har vertex ke saath ek list rakho", "Isliye O(1) hai".
- Technical terms English mein hi rakho: vertex, edge, index, complexity, `NullPointerException`.
- Chhote sentences. Har "kya" ke saath "kyun" bhi batao. Yahi cheez revision mein sabse kaam aati hai.
- Emoji sirf headings pe, kam: ⭐ (tricky part), ⚠️ (gotchas), ✅ (revision).

## Note structure

Ye order follow karo. Jo section topic pe lagu nahi hota, use chhod do. Jaise kisi algorithm ka "Data structure" section na bane.

```markdown
# NN · Topic Name (meri implementation)

> Code: [`File.java`](../src/main/java/.../File.java)
> Related: [NN-other.md](NN-other.md)        ← agar pehle ka related note hai

---

## 1. Ek line mein
Concept ek-do line mein.

## 2. Picture se samjho
Example input ka diagram + uska internal representation (box diagram / matrix).
Neeche 2-3 "Dhyan do" bullets: picture se kya observe karna hai.

## 3. Data structure            ← agar class mein fields hain
Fields ka code + table: Part | Kya hai | Kyun.

## 4. Methods, step by step
Har method ka sub-section:
### 4.x `methodName(args)`: O(?)
- User ka actual code snippet
- Ek line: kya karta hai
- Pehle / Baad mein diagram
- Kyun ye complexity hai
- Tricky line ho to "#### ⭐ ... kyun?" sub-section

Tricky algorithm ho to: "Idea", "Code flow" (commented pseudo/real code), "Dry run" (table), "Out of bound / edge case kyun nahi aata".

## 5. Complexity summary
Table. Agar koi alternative representation/approach pehle notes mein hai, to side-by-side compare karo + "kab kaunsa lo".

## 6. ⚠️ Dhyan rakhne wali baatein (is code mein)
Numbered list: user ke code ke real risks + common galtiyan.

## 7. Quick revision ✅
- [ ] 5-7 one-line checklist points
```

Sections separate karne ke liye `---` use karo.

## Diagrams

Diagram is skill ki sabse important cheez hai. User ne hand-drawn style ke **box → box → ✗** linked-list diagrams pasand kiye hain. Exact ASCII patterns, aur kaunsa diagram kab use karna hai, `references/diagrams.md` mein hain. Notes likhne se pehle use padho.

Short rules:
- Har state-changing method (add/remove/update) ke liye **Pehle** aur **Baad mein** diagram do. Naya ya badla hua element highlight karo (`┏━━━┓` heavy box ya `← naya`).
- Saare diagrams mein **ek hi example** use karo, taaki reader ko baar-baar naya graph samajhna na pade.
- Code block (```` ``` ````) ke andar diagram rakho, taaki alignment bana rahe. Box-drawing chars (`┌ ─ ┐ │ └ ┘ ► ✗`) use karo.
- Diagram ke baad 1-2 line likho ki picture mein kya badla.

## Dry runs & tables

- Loops/index wale logic ke liye dry-run table do: loop variable, kya hua, result. 4-5 rows kaafi hain.
- "Ye galat kyun hota" ke liye galat aur sahi tareeka saath mein dikhao (`❌` / `✅`).
- Complexity hamesha table mein.

## Index file template (`docs/README.md`)

```markdown
# Graph Notes 📒

Concept refresh karne ke liye notes, Hinglish mein. Har topic ka apna file hai, jismein picture, logic, complexity aur common galtiyan hain.

| # | Topic | Notes | Code |
|---|---|---|---|
| 01 | Topic | [01-topic.md](01-topic.md) | [File.java](../src/main/java/.../File.java) |
```

Heading ("Graph Notes") repo ke subject ke hisaab se badal lo.

## Quality check (finish karne se pehle)

- Har code snippet user ki file se match karta hai? Variable/method names same hain?
- Har method ki complexity likhi hai, aur "kyun" bhi?
- Kam se kam ek before/after diagram hai har state-changing method ke liye?
- Gotchas section mein user ke code ke real issues hain, generic filler nahi?
- Index table update hua? Relative links sahi hain (`../src/...`)?

