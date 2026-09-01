# Docker and PostgreSQL

## DEV-DB-001 — PostgreSQL 17

Development PostgreSQL uses:

```text
PostgreSQL 17
```

---

## DEV-DB-002 — Docker Image Is Version-Pinned

Use:

```text
postgres:17
```

Do not use:

```text
postgres:latest
```

for the project baseline.

---

## DEV-DB-003 — Application Runs on Local JVM Initially

Initial runtime architecture:

```text
Spring Boot
→ Local JVM

PostgreSQL
→ Docker
```

The application itself is not containerized initially.

This keeps debugging and TDD fast while making the database reproducible.

---

## DEV-DB-004 — Docker Compose Manages Development PostgreSQL

Use modern Compose:

```bash
docker compose
```

not the legacy standalone `docker-compose` command as the project standard.

---

## DEV-DB-005 — Default Development Database

Current local-development defaults:

```text
database = wealth
user     = wealth
port     = 5432
```

The current `wealth` password is a non-secret local-development bootstrap value. Real credentials must come from environment configuration rather than source code.

---

## DEV-DB-006 — Development Data Uses a Persistent Volume

Normal:

```bash
docker compose down
```

should not remove the PostgreSQL data volume.

Destructive reset is a separate deliberate command.

---

## DEV-DB-007 — Destructive Reset Is Explicit

For example:

```bash
docker compose down -v
```

may be used when a full local database reset is intentionally required.

This command should never be treated as an ordinary stop command.

---

## DEV-DB-008 — Development and Test PostgreSQL Versions Match

Development Docker Compose and Testcontainers should use the same major/patch baseline:

```text
postgres:17
```

This reduces version drift.

---

## DEV-DB-009 — Separate Performance Database

Large SQL experiments should use a separate database, for example:

```text
wealth
→ normal development

wealth_perf
→ performance / million-row experiments
```

This prevents large synthetic datasets from polluting normal development workflows.

---

## DEV-DB-010 — compose.yaml Direction

The current `compose.yaml` includes:

- pinned PostgreSQL image;
- named volume;
- database name;
- local-development user;
- an explicit non-secret local bootstrap password;
- exposed local port.

Environment-based credentials and a healthcheck may be introduced when required. Real secrets must not be committed.
