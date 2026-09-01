# Data Access Strategy

## SQL-ACC-001 — PostgreSQL Is the Target Database

The project targets PostgreSQL and may intentionally use PostgreSQL-specific features.

Complete portability to unrelated database engines is not a primary requirement.

---

## SQL-ACC-002 — ORM Is Not the Only Data-Access Method

Spring Data JPA is used where it fits the problem, but the architecture must not hide SQL completely.

The project explicitly learns how generated and handwritten SQL behave.

---

## SQL-ACC-003 — Use the Simplest Appropriate Access Technique

Recommended guidance:

```text
Simple Aggregate persistence
→ Spring Data JPA

Simple object-oriented query
→ JPQL / Spring Data query

Complex Read Model
→ JPQL, native SQL, or JdbcClient

Performance-sensitive / SQL-learning query
→ native SQL or JdbcClient
```

This is guidance rather than a rigid rule.

---

## SQL-ACC-004 — Command Repositories and Query Services Are Separate Responsibilities

Command-side repositories support Aggregate persistence.

Read-side Query Services may directly return purpose-built projections.

Do not force complex reporting SQL into a generic Aggregate Repository abstraction.

---

## SQL-ACC-005 — Avoid Unbounded findAll Processing

Avoid:

```text
SELECT everything
↓
load into Java
↓
Stream.filter()
↓
Stream.sorted()
↓
aggregate
```

for data operations that PostgreSQL can perform efficiently.

Prefer database-side:

- filtering;
- sorting;
- grouping;
- aggregation;
- pagination.

---

## SQL-ACC-006 — N+1 Is an Explicit Learning Target

When a query causes N+1 behavior:

1. observe the executed SQL;
2. measure the number of statements;
3. explain why it occurs;
4. compare an alternative.

Potential alternatives include:

- JOIN;
- fetch join;
- projection;
- dedicated SQL.

---

## SQL-ACC-007 — EAGER Loading Is Not a General N+1 Solution

Do not make relationships globally eager merely to hide one N+1 query.

Fetch shape should follow the specific Use Case or Read Model.

---

## SQL-ACC-008 — Read Models May Bypass Domain Rehydration

Portfolio, transaction history, and other read-heavy APIs may use dedicated SQL and DTO projections without reconstructing every write-side Domain Aggregate.

---

## SQL-ACC-009 — Important SQL Uses Explicit Selected Columns

Avoid `SELECT *` for important application queries.

Selecting required columns makes:

- query intent clearer;
- projection shape explicit;
- index analysis easier;
- schema coupling more visible.

---

## SQL-ACC-010 — Bind Parameters Are Mandatory

User-controlled values must use parameter binding.

Do not build SQL values through string concatenation.

---

## SQL-ACC-011 — Dynamic Structural SQL Uses Whitelists

Values such as dynamic sort-column identifiers cannot always be bind parameters.

If such behavior is introduced, validate it against an explicit whitelist rather than concatenating arbitrary client input.

---

## SQL-ACC-012 — Complex SQL Has a Clear Home

Do not scatter large native SQL strings across unrelated classes.

Place complex queries in:

- dedicated Query Services;
- dedicated repository adapters;
- clearly named query components.

---

## SQL-ACC-013 — Query Names Express Business Intent

Prefer names such as:

```text
findLatestMarketPrices
findLedgerEntriesForAccount
calculatePortfolioValuation
```

over vague names such as:

```text
findData
executeQuery
loadInfo
```

---

## SQL-ACC-014 — Comments Explain Why, Not Basic Syntax

Comments for complex SQL should explain design intent, assumptions, or trade-offs.

Avoid comments that merely translate obvious SQL syntax into English.
