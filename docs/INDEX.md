# Documentation Index

## Purpose

This file is the navigation entry point for humans and AI working on the wealth-platform project.

Read only the documents relevant to the current task.

---

## Core Product Documents

| Area | Document | Purpose |
| --- | --- | --- |
| Requirements | `requirements.md` | Overall system scope and requirements |
| Domain Language | `glossary.md` | Canonical English Ubiquitous Language and Japanese glossary |
| Use Cases | `use-cases.md` | User/system behavior by use case |
| Business Rules | `business-rules.md` | Domain rules and invariants |
| Market Simulation | `market-simulation.md` | Market-price generation rules |
| Non-Functional Requirements | `non-functional-requirements.md` | Correctness, performance, maintainability, operations |

---

## Design Documents

| Area | Document | Purpose |
| --- | --- | --- |
| Data Model | `data-model.md` | Logical data model and persistence decisions |
| API | `api-spec.md` | REST API contract |
| Architecture | `architecture.md` | Current application architecture |
| ADR | `adr/README.md` | Long-lived architecture decision history |

---

## Engineering Strategy

| Area | Document | Purpose |
| --- | --- | --- |
| Testing | `test-strategy.md` | TDD, integration, concurrency, SQL and performance testing |
| SQL | `sql-strategy.md` | PostgreSQL/SQL learning and implementation strategy |
| Environment | `development-environment.md` | Java, Spring Boot, Docker, PostgreSQL, Flyway, Testcontainers |
| Git | `git-workflow.md` | Branch, commit, PR, merge and Git safety rules |
| Learning | `learning-workflow.md` | How AI and developer collaborate for learning |
| Current Learning State | `learning-state.md` | Current task, TDD state, decisions and next action |

---

## Recommended AI Session Startup

```text
1. AGENTS.md
2. docs/INDEX.md
3. docs/learning-workflow.md
4. docs/learning-state.md
5. docs/git-workflow.md when Git work is involved
6. Relevant Use Case
7. Relevant Business Rules
8. Only the additional design documents required by the task
9. Current code / git diff
```

Do not automatically read the complete documentation tree.
