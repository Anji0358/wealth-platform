# Database Integration and Testcontainers

## TS-DB-001 — PostgreSQL Is the Integration-Test Database

**Status:** Confirmed

Database integration tests use PostgreSQL.

---

## TS-DB-002 — Testcontainers Is the Default Integration-Test Mechanism

**Status:** Confirmed

Use Testcontainers PostgreSQL once persistence work begins.

This provides database behavior close to the project's actual runtime environment.

---

## TS-DB-003 — H2 Is Not Used as a PostgreSQL Substitute

**Status:** Confirmed

H2 is not used to prove PostgreSQL behavior because the project intentionally learns:

- PostgreSQL UUID;
- `TIMESTAMPTZ`;
- `NUMERIC`;
- constraints;
- locking;
- transaction semantics;
- PostgreSQL query planning.

---

## TS-DB-004 — Repository Adapters Require Real DB Tests

Repository adapter tests verify:

- JPA mappings;
- native SQL / JPQL;
- read projections;
- persistence behavior;
- constraint behavior.

Repository mocks do not prove SQL correctness.

---

## TS-DB-005 — Migrations Are Tested from an Empty Database

**Status:** Confirmed

A fresh PostgreSQL container should be able to apply all migrations successfully.

---

## TS-DB-006 — Application Mapping and Migration Schema Must Agree

Tests should detect mismatches between:

- migration-created schema;
- persistence mappings;
- query assumptions.

Do not rely solely on Hibernate automatic schema creation.

---

## TS-DB-007 — Important Constraints Are Tested

Examples:

- duplicate Security Code;
- duplicate `(securityId, businessDate)` Market Price;
- duplicate Position key;
- negative balance constraint;
- invalid `reservedAmount`;
- zero Ledger Entry.

---

## TS-DB-008 — Domain Rules Are Not Replaced by Constraint Tests

If a rule matters in the Domain Model, keep domain-level tests even when a DB constraint also protects it.

---

## TS-DB-009 — Integration Tests Are Isolated

Tests must not depend on execution order or leftover shared database state.

---

## TS-DB-010 — Migration Path Is Consistent Across Tests

Database setup should use the same migration path used by the application.

---

## TS-DB-011 — Testcontainers Reuse Is an Optimization, Not an Initial Requirement

Container reuse may later improve speed.

Correctness and isolation take priority during initial setup.
