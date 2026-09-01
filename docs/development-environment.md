# Development Environment

## Purpose

This document is the entry point for the development environment of the wealth-platform project.

The environment is designed to support:

- Java/Spring backend development;
- TDD;
- PostgreSQL-specific learning;
- reproducible database setup;
- versioned schema migration;
- Testcontainers integration tests;
- SQL execution-plan analysis;
- concurrency experiments;
- Eclipse/Pleiades development;
- command-line reproducibility.

## Fixed Environment Baseline

This table defines the approved target baseline. A tool listed here is not necessarily configured in the repository before the phase that first requires it. For example, Testcontainers dependencies and integration-test separation are introduced when persistence integration tests begin.

```text
Java                  21
Spring Boot           4.1.1
Build Tool            Maven
Maven Wrapper         3.9.16
PostgreSQL            17
Database Runtime      Docker Compose
Application Runtime   Local JVM
Schema Migration      Flyway
DB Integration Tests  Testcontainers
Test DB               PostgreSQL 17
H2                    Not used
SQL CLI               psql
Primary IDE           Eclipse / Pleiades
Primary Shell         Linux / WSL2
```

Spring-managed dependency versions should be preferred over manual version overrides unless a concrete incompatibility requires otherwise.

## Documents

- [Base Setup](development-environment/setup.md)
- [Docker and PostgreSQL](development-environment/docker-postgresql.md)
- [Spring and Application Configuration](development-environment/spring-config.md)
- [Flyway and Schema Management](development-environment/flyway.md)
- [Testcontainers and Test Execution](development-environment/testing.md)
- [SQL Tools and Performance Environment](development-environment/sql-tools.md)
- [Standard Commands](development-environment/commands.md)
- [Repository Environment Structure](development-environment/repository-structure.md)

## Environment Philosophy

1. IDE behavior must not be the only way to build or test the project.
2. The repository must be reproducible from command line.
3. PostgreSQL behavior is tested against PostgreSQL, not an in-memory substitute.
4. Database schema is recreated from migrations.
5. Normal TDD remains fast.
6. Large SQL/performance experiments are isolated from ordinary development.
7. Secrets are not committed.
