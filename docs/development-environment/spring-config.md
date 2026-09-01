# Spring and Application Configuration

## DEV-CFG-001 — application.properties Holds Shared Defaults

Use:

```text
src/main/resources/application.properties
```

for environment-independent defaults.

---

## DEV-CFG-002 — Local Profile Is Allowed

Local developer overrides may use:

```text
application-local.properties
```

The local profile should not contain committed secrets.

---

## DEV-CFG-003 — Performance Profile Is Separate

SQL/performance experiments may use:

```text
application-performance.properties
```

or an equivalent dedicated Spring profile.

Large-data experiment settings must not affect normal development.

---

## DEV-CFG-004 — Real Credentials Use Environment Variables

The repository currently contains a non-secret local-development database credential for the Docker Compose bootstrap environment.

Credentials for any shared or non-local environment must use environment variables.

Recommended variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Additional variables should be added only when actually required.

---

## DEV-CFG-005 — .env Must Be Ignored When Introduced

The repository does not currently use `.env`. If a local `.env` file is introduced:

```text
.env
```

it must be added to `.gitignore` and must not be committed.

---

## DEV-CFG-006 — .env.example Is Added When Environment Configuration Is Introduced

The repository does not currently require `.env.example`. When `.env`-based configuration is introduced, it may include:

```text
.env.example
```

containing variable names and safe placeholder values only.

Never include real credentials.

---

## DEV-CFG-007 — Hibernate Does Not Own the Schema

Do not use:

```text
ddl-auto=create
ddl-auto=update
```

as normal schema-management strategy.

Prefer validation against migration-managed schema.

---

## DEV-CFG-008 — ddl-auto=validate Is the Preferred Direction

Development/test configuration should use:

```text
spring.jpa.hibernate.ddl-auto=validate
```

or equivalent validation behavior once the schema exists.

---

## DEV-CFG-009 — Event Time Uses Instant

Application event timestamps should use `Instant` semantics.

Local machine timezone must not silently change business behavior.

---

## DEV-CFG-010 — Market Business Date Uses Explicit Asia/Tokyo

Market business-date conversion uses an explicit zone:

```java
ZoneId.of("Asia/Tokyo")
```

Do not rely on:

```java
ZoneId.systemDefault()
```

for Domain semantics.

---

## DEV-CFG-011 — SQL Logging Is On-Demand

Hibernate SQL logging may be enabled for:

- N+1 investigation;
- generated SQL inspection;
- query debugging.

It should not be permanently noisy by default.

---

## DEV-CFG-012 — Application Services Remain Stateless

Configuration must not encourage mutable request state in singleton Spring beans.

Financial state belongs in PostgreSQL/domain objects loaded for the current transaction.
