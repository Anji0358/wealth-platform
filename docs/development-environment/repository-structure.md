# Repository Environment Structure

Recommended top-level environment-related structure:

```text
wealth-platform/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .mvn/
│
├── compose.yaml
├── .gitignore
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-local.properties  (when needed)
│   │       └── db/
│   │           └── migration/
│   │
│   └── test/
│
└── docs/
```

## Repository Rules

### DEV-REP-001 — compose.yaml Is Version Controlled

The PostgreSQL service definition belongs in the repository.

The current Compose credentials are explicit non-secret local bootstrap values. Real credentials are externalized.

---

### DEV-REP-002 — .env Is Ignored When Introduced

The repository does not currently use `.env`. If introduced, `.gitignore` must include:

```text
.env
```

---

### DEV-REP-003 — .env.example Is Version Controlled When Introduced

When `.env`-based configuration is introduced, provide variable names and safe placeholders in `.env.example` and version-control that example file.

---

### DEV-REP-004 — Maven Wrapper Is Version Controlled

All wrapper files belong in Git.

---

### DEV-REP-005 — Migration Files Are Version Controlled

Every migration belongs in Git and participates in code review.

---

### DEV-REP-006 — Local IDE Metadata Is Minimized

Do not make developer-specific Eclipse workspace state part of the required project setup.

---

### DEV-REP-007 — Generated Performance Data Is Not Committed by Default

Large generated database dumps or million-row CSVs should not be committed unless there is a deliberate reason.

Prefer reproducible generators/seeds.

---

### DEV-REP-008 — SQL Experiment Reports Are Version Controlled

Markdown reports and small representative SQL snippets should be committed under:

```text
docs/sql-experiments/
```

---

## Future Environment Extensions

Possible later additions include:

```text
Dockerfile
CI configuration
ArchUnit configuration
performance-test Maven profile
pg_stat_statements setup
benchmark data generator
```

These are added only when the corresponding phase begins.
