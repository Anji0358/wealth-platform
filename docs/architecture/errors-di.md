# Validation, Errors, and Dependency Injection

## ARCH-049 — Validation Is Two-Level

**Status:** Confirmed

Presentation validation covers boundary concerns such as:

```text
null
malformed UUID
invalid JSON
basic primitive constraints
unknown enum representation
```

Domain validation covers business meaning such as:

```text
balance sufficient?
account FROZEN?
Security DISABLED?
Business Day?
Position sufficient?
```

---

## ARCH-050 — Bean Validation Is Boundary-Oriented

**Status:** Confirmed

Jakarta/Spring validation annotations such as:

```text
@NotNull
@Positive
```

may be used on Request DTOs.

Do not attempt to encode complex cross-domain Business Rules exclusively as Bean Validation annotations.

---

## ARCH-051 — API Exception Mapping Is Centralized

**Status:** Confirmed

Use centralized presentation-layer handling such as:

```text
@RestControllerAdvice
```

to translate application/domain/infrastructure failures into API Problem Details.

---

## ARCH-052 — Domain Errors Are Transport-Agnostic

**Status:** Confirmed

Domain code must not depend on:

```text
HttpStatus
ResponseEntity
ProblemDetail
```

HTTP semantics belong to Presentation.

---

## ARCH-053 — Domain and Infrastructure Failures Are Distinct

**Status:** Confirmed

Examples of domain/application failures:

```text
Insufficient Balance
Invalid Account State
Position Quantity Insufficient
```

Examples of infrastructure failures:

```text
SQLException
Connection failure
Persistence mapping failure
```

These should not be represented as the same internal abstraction without meaningful classification.

---

## ARCH-054 — Constructor Injection Is Standard

**Status:** Confirmed

Spring dependencies use constructor injection.

Typical dependency fields are:

```java
private final ...
```

Field injection is avoided.

---

## ARCH-055 — @Bean Is Used in Configuration, Not on Normal Constructors

**Status:** Confirmed

Bean registration should use appropriate mechanisms:

```text
@Component
@Service
@Repository
@Configuration + @Bean
```

Do not annotate an ordinary class constructor with `@Bean`.

---

## ARCH-056 — Application Services Are Stateless

**Status:** Confirmed

Application Service instances must not store per-request financial state in mutable fields.

Persistent financial state belongs in PostgreSQL/domain entities loaded for the current transaction.

---

## ARCH-057 — Singleton Bean Mutable Request State Is Avoided

**Status:** Confirmed

Because Spring beans are normally singleton-scoped, avoid request-specific mutable fields such as:

```java
private Money currentBalance;
```

inside Services.

---

## ARCH-065 — Acting Identity Is Exposed Through ActorContext

**Status:** Confirmed

Presentation resolves the trusted development-only identity input and passes a transport-agnostic `ActorContext` to the Application use case. Application and Domain code must not depend on HTTP header names, `HttpServletRequest`, Spring Security types, or Controller DTOs.

`ActorContext` expresses the Acting Customer required by the use case; it does not claim production-grade authentication.

---

## ARCH-066 — Application Layer Enforces Ownership

**Status:** Confirmed

The Application use case compares the Acting Customer from `ActorContext` with persisted resource ownership before returning private Customer data or changing Customer-owned state.

Path, query, and body identifiers select target resources only. Presentation validation alone must not authorize the operation.
