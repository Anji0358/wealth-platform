# Testcontainers and Test Execution

## DEV-TST-001 — Testcontainers Is the Database Integration Standard

Database integration tests use Testcontainers with PostgreSQL.

---

## DEV-TST-002 — Test DB Uses PostgreSQL 17

Use the same image baseline as development:

```text
postgres:17
```

---

## DEV-TST-003 — Spring Boot Testcontainers Integration Is Preferred

Where appropriate, use Spring Boot Testcontainers integration and `@ServiceConnection` to reduce manual connection-property wiring.

---

## DEV-TST-004 — Integration Tests Apply Real Migrations

The test database should be initialized through Flyway migrations, not a simplified test-only schema.

---

## DEV-TST-005 — Unit and Integration Tests Are Distinguishable

Recommended naming direction:

```text
*Test
→ unit / normal fast tests

*IT
→ database/integration tests
```

---

## DEV-TST-006 — Surefire and Failsafe Separation Is Recommended

Recommended build behavior:

```text
./mvnw test
→ fast tests

./mvnw verify
→ includes integration verification
```

Exact plugin configuration is implemented when the test structure is introduced.

---

## DEV-TST-007 — Performance Tests Are Separate

Million-row SQL experiments are not part of normal:

```bash
./mvnw test
```

execution.

---

## DEV-TST-008 — SQL Experiments Are Explicitly Invoked

Use a dedicated Maven profile, JUnit tag, or separate task for:

- SQL performance;
- concurrency experiments;
- large-data benchmarks.

---

## DEV-TST-009 — Container Reuse Is Optional Later

Testcontainers reuse may later reduce local startup time.

Do not weaken isolation merely for faster tests.

---

## DEV-TST-010 — Test Execution Must Work Outside Eclipse

All important test categories must be runnable through the Maven Wrapper.

---

## DEV-TST-011 — H2-Based Test Profile Is Not Created

There is no separate H2 profile for convenience.

Tests that require database semantics use PostgreSQL.

---

## DEV-TST-012 — Fast TDD Remains Independent from Docker When Possible

Pure Domain Unit Tests should not require a running Docker daemon.
