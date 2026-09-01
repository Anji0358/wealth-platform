# Flyway and Schema Management

## DEV-FLY-001 — Flyway Is the Schema Migration Tool

Flyway is the project's authoritative migration mechanism.

---

## DEV-FLY-002 — PostgreSQL Flyway Support Is Included

The project should include the PostgreSQL-specific Flyway database support required by the selected Flyway version.

---

## DEV-FLY-003 — Migration Directory

Use:

```text
src/main/resources/db/migration/
```

---

## DEV-FLY-004 — Versioned Migration Naming

Use Flyway naming such as:

```text
V1__create_customers.sql
V2__create_bank_accounts.sql
V3__create_ledger.sql
```

Exact grouping may evolve with the implementation sequence.

---

## DEV-FLY-005 — Applied Migrations Are Not Edited

Once a migration has been applied/shared:

```text
Do not modify it
→ create a new migration
```

---

## DEV-FLY-006 — Migration Follows Incremental Design

Do not create all Phase 6 schema in Phase 1.

Schema grows as use cases are implemented.

---

## DEV-FLY-007 — Migration Is Tested Against Empty PostgreSQL

Integration tests should verify that all migrations can create a valid schema from an empty database.

---

## DEV-FLY-008 — Migration and JPA Mapping Must Agree

A successful migration is not enough if application mappings are invalid.

Integration tests must validate both together.

---

## DEV-FLY-009 — Manual DB Changes Are Not Normal Workflow

Do not use direct ad hoc DDL changes as the authoritative development process.

If a schema change matters, it belongs in a migration.

---

## DEV-FLY-010 — Migration Version Upgrades Are Intentional

If Flyway itself is upgraded, verify:

- migration compatibility;
- PostgreSQL support;
- Spring Boot compatibility;
- test execution.
