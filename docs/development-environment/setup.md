# Base Setup

## DEV-SET-001 — Java 21

The project uses Java 21.

All local development, CI, tests, and production-like local execution should target Java 21 unless a future migration is explicitly approved.

Check:

```bash
java -version
```

---

## DEV-SET-002 — Spring Boot 4.1.1

The project baseline is Spring Boot 4.1.1.

Do not use Snapshot or Milestone releases for the main branch.

---

## DEV-SET-003 — Maven Is the Build Tool

The project uses Maven.

Gradle is not used in parallel.

---

## DEV-SET-004 — Maven Wrapper Is the Standard Entry Point

Use:

```bash
./mvnw
```

rather than depending on a developer's globally installed Maven version.

The repository should include:

```text
mvnw
mvnw.cmd
.mvn/
```

---

## DEV-SET-005 — Maven Wrapper Baseline

The preferred wrapper baseline is Maven 3.9.16.

If the wrapper is later upgraded, the version change should be intentional and verified.

---

## DEV-SET-006 — Eclipse / Pleiades Is Supported but Not Authoritative

Eclipse/Pleiades may be used for:

- editing;
- debugging;
- test execution;
- code navigation.

However, successful IDE execution alone does not prove the repository is reproducible.

CLI commands remain the standard verification path.

---

## DEV-SET-007 — Linux / WSL2 Is the Primary Development Shell

The preferred development shell is Linux, including WSL2 Ubuntu.

Repository location should preferably be on the Linux filesystem, for example:

```text
~/dev/wealth-platform
```

rather than relying on Windows-mounted paths for normal backend development.

---

## DEV-SET-008 — Command-Line Reproducibility

A clean environment should be able to perform at least:

```bash
./mvnw test
./mvnw verify
docker compose up -d
docker compose down
```

without hidden IDE-only setup.

---

## DEV-SET-009 — Initial Spring Dependencies

Initial dependencies include:

```text
spring-boot-starter-web
spring-boot-starter-data-jpa
spring-boot-starter-validation
postgresql
Flyway support
spring-boot-starter-test
spring-boot-testcontainers
Testcontainers PostgreSQL
Testcontainers JUnit Jupiter
```

Exact dependency coordinates should follow Spring Boot 4.1.1 dependency management where available.

---

## DEV-SET-010 — H2 Is Not Added

H2 is intentionally excluded because the project must learn real PostgreSQL behavior.

---

## DEV-SET-011 — Spring-Managed Versions Are Preferred

Do not manually override Spring ecosystem dependency versions without a concrete reason.

Version overrides require:

- compatibility reason;
- test evidence;
- preferably an ADR if architecturally significant.
