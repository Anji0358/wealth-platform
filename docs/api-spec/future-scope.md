# API Future Scope

## Planned Future Scope

### Phase 5

- `Idempotency-Key` support for asset-moving Commands.
- Stable concurrency-conflict API semantics.
- Retry-oriented behavior where appropriate.

### Phase 6 Trading

Potential endpoint:

```text
POST /orders/{orderId}/cancel
```

Related future capabilities:

- Limit Orders;
- open Order states;
- Partial Execution;
- `1 Order : N Executions`;
- Reserved Amount;
- Reserved Quantity.

Limit Order request fields such as `limitPrice` should be added only when this phase begins.

---

## Future Considerations

### Ledger / Audit API

A future administrative or learning-oriented API may expose:

- Ledger Transactions;
- Ledger Entries;
- reconciliation views;
- reversal/correction relationships.

Ordinary Customer APIs should continue to use domain-oriented Read Models rather than exposing internal Ledger structure unnecessarily.

---

### Authentication and Authorization

Future authentication may introduce:

- authenticated Customer identity;
- Admin identity;
- `401 Unauthorized`;
- `403 Forbidden`;
- endpoint-level authorization.

The initial `/admin` namespace does not imply these features already exist.

---

### API Evolution

Compatible additive changes can remain within `/api/v1`.

Breaking changes may require:

```text
/api/v2
```

Examples of breaking change include:

- changing the meaning of an existing field;
- replacing a resource identity model;
- fundamentally changing endpoint semantics.

---

## Explicitly Not Added Prematurely

The API should not add endpoints or fields solely for possible future needs.

Examples:

```text
limitPrice in Phase 3
cancel endpoint in Phase 3
Admin identity endpoints without authentication scope
generic Ledger edit endpoints
generic status PATCH for all resources
unbounded search endpoints
```

Future behavior should enter the API when its Use Case and Business Rules become implementation scope.
