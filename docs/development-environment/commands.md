# Standard Commands

This file defines the preferred command-line entry points for daily development.

## Environment Checks

```bash
java -version
./mvnw -version
docker version
docker compose version
psql --version
```

---

## Start Development PostgreSQL

```bash
docker compose up -d
```

---

## Check Running Containers

```bash
docker compose ps
```

---

## View PostgreSQL Logs

```bash
docker compose logs -f postgres
```

The Compose service name may be adjusted if another name is chosen.

---

## Stop Development PostgreSQL

```bash
docker compose down
```

---

## Destructive Local DB Reset

Use only intentionally:

```bash
docker compose down -v
docker compose up -d
```

This removes the named PostgreSQL volume.

---

## Run Fast Tests

```bash
./mvnw test
```

---

## Run Full Verification

```bash
./mvnw verify
```

---

## Run Spring Boot Locally

Typical command:

```bash
./mvnw spring-boot:run
```

With local profile:

```bash
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

---

## Build Artifact

```bash
./mvnw clean package
```

---

## Connect with psql

Current local-development example:

```bash
docker compose exec postgres psql -U wealth -d wealth
```

When environment-based credentials are introduced, a direct host connection may instead use the corresponding `psql` connection parameters.

Do not place passwords directly in committed scripts.

---

## Inspect Database

Inside `psql`:

```text
\l
\dt
\d table_name
```

---

## Explain a Query

```sql
EXPLAIN (ANALYZE, BUFFERS)
SELECT ...
```

---

## Transaction Experiment

Session A:

```sql
BEGIN;
SELECT ... FOR UPDATE;
```

Session B:

```sql
BEGIN;
SELECT ... FOR UPDATE;
```

Observe behavior before committing/rolling back.

---

## Performance/SQL Experiment Command

The exact Maven profile/tag command is intentionally deferred until the corresponding test configuration exists.

When added, it should be explicit and separate from ordinary:

```bash
./mvnw test
```
