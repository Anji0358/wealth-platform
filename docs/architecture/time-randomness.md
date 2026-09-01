# Time, Randomness, and Scheduler

## ARCH-043 — Time Is Injectable

**Status:** Confirmed

Do not scatter calls such as:

```java
Instant.now()
LocalDate.now()
```

through Domain logic.

Use an injectable time source, preferably based on `java.time.Clock` or an equivalent abstraction.

This enables deterministic tests.

---

## ARCH-044 — Market Business Date Uses Asia/Tokyo

**Status:** Confirmed

The Market's Business Date is derived using:

```text
Asia/Tokyo
```

The system distinguishes:

```text
Instant
↓ timezone conversion
Market Business Date
```

This must align with `market-simulation.md` and `api-spec.md`.

---

## ARCH-045 — Randomness Is Injectable

**Status:** Confirmed

GBM logic must not construct uncontrolled random generators internally.

Conceptually use a port such as:

```text
GaussianRandomSource
```

with:

- production implementation;
- deterministic test implementation.

The exact interface name is not fixed.

---

## ARCH-046 — Simulation Formula Is Domain/Application Logic, Not Scheduler Logic

**Status:** Confirmed

The scheduler triggers a Market generation Use Case.

It does not contain the GBM formula itself.

---

## ARCH-047 — Scheduler Is an External Entry Adapter

**Status:** Confirmed

Conceptually:

```text
HTTP Controller ──┐
                  ├→ Application Use Cases
Scheduler ────────┘
```

Scheduler and HTTP are both entry points into Application behavior.

---

## ARCH-048 — Scheduler Recovery Uses Existing Market Rules

**Status:** Confirmed

Catch-Up behavior belongs to Market application/domain logic.

Restarting or replacing the scheduler must not redefine the calculation rules.
