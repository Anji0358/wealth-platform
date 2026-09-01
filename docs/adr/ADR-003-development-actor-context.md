# ADR-003 — Use a Development Actor Context for Customer Identity

**Status:** Accepted
**Date:** 2026-09-01

## Context

Customer-owned operations must validate ownership, but production-grade Authentication, Authorization, KYC, and identity-provider integration are outside the learning project's current scope.

Trusting a `customerId` from a path or request body would confuse target selection with caller identity. Depending on an HTTP header directly in Application or Domain code would also leak transport concerns inward.

## Decision

The learning environment accepts a trusted development-only Acting Customer input, initially represented at the HTTP boundary as:

```text
X-Acting-Customer-Id: <Customer UUID>
```

Presentation validates and converts this input into a transport-agnostic `ActorContext`. Application use cases depend on `ActorContext`, load persisted ownership, and reject access when the Acting Customer does not own the target resource.

Path, query, and body `customerId` values select targets only. They never prove caller identity.

This mechanism is explicitly not production Authentication or Authorization.

## Alternatives Considered

### Trust Path or Body Customer ID

Not selected because a caller could claim another Customer identity and because target identity and actor identity have different responsibilities.

### Depend Directly on the HTTP Header

Not selected because Application and Domain behavior should remain independent of HTTP and Spring MVC.

### Introduce Spring Security and Production Authentication Now

Not selected because production security integration is outside the current learning scope and would add unrelated complexity.

## Consequences

### Positive

- ownership behavior can be implemented and tested now;
- Application tests remain transport-independent;
- later authentication can replace the Presentation adapter without changing ownership rules;
- caller identity and target resource identity remain distinct.

### Negative / Trade-offs

- the development header must only be trusted in the learning environment;
- this mechanism must not be described as secure production authentication;
- every Customer-private use case must explicitly perform ownership validation.

## Related Documents

- `docs/requirements.md`
- `docs/business-rules/customer.md`
- `docs/api-spec/common.md`
- `docs/architecture/errors-di.md`
- `docs/test-strategy/domain-application.md`
