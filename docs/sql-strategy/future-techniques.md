# Deferred and Future SQL Techniques

These techniques are useful learning candidates, but are not required in the initial data-access architecture.

## Partial Index

Consider when a future long-lived Order status creates a useful selective subset.

---

## INCLUDE / Covering Index

Consider when a measured query benefits from reducing heap access.

---

## Materialized View

Potential future exercise for expensive Portfolio/analytics queries.

Do not adopt before a measured need.

---

## Database View

May be useful for stable Read Models, but not required initially.

---

## JSONB

Do not use for normalized financial Core Data.

Future use is limited to genuinely flexible auxiliary information.

---

## Stored Functions

May be studied as PostgreSQL functionality.

Core business behavior remains in Java unless a later decision explicitly changes that.

---

## PostgreSQL Schema Separation

The initial project uses one schema.

Separate schemas by business domain may later become an ADR candidate if database ownership complexity increases.

---

## Prepared Statement / Plan Cache Analysis

Advanced topic after basic planning/indexing competency.

---

## Partitioning

Not required initially.

May become a future exercise if Ledger or Market Price tables become large enough to justify studying range partitioning.

---

## Advanced Statistics

PostgreSQL extended statistics may be studied if correlated columns cause meaningful estimate errors.

---

## Expression Index

Possible advanced exercise if a real query uses a stable expression predicate.

---

## Full-Text Search

Not relevant to the current financial core and therefore not part of this roadmap.

---

## Foreign Data Wrapper / Distributed Database Features

Out of scope unless the project later integrates external database sources.
