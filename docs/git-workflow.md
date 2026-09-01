# Git Workflow

## Purpose

This document defines the Git workflow for the wealth-platform project.

The goal is to use a workflow close to normal professional software development while keeping the process understandable for a learning project.

The workflow is based on:

- a protected `main` branch;
- short-lived working branches;
- small logical commits;
- Pull Request review;
- CI checks when available;
- Squash Merge;
- explicit handling of destructive Git operations.

Git history is treated as an engineering record.

---

## 1. Branch Strategy

### GIT-001 — `main` Is the Default Branch

`main` represents the latest accepted project state.

It should normally satisfy:

```text
build succeeds
important tests pass
migrations are valid
documentation is consistent enough to continue development
```

### GIT-002 — Do Not Implement Directly on `main`

Normal feature work must use a working branch.

```text
main
↓
working branch
↓
implementation
↓
tests
↓
review
↓
Pull Request
↓
Squash Merge
↓
main
```

### GIT-003 — Working Branches Are Short-Lived

A branch should normally represent one clear development objective.

Examples:

```text
feature/uc-bnk-002-deposit
feature/uc-bnk-003-withdrawal
fix/bank-account-close-validation
refactor/ledger-entry-creation
perf/ledger-history-index
docs/update-test-strategy
```

### GIT-004 — Branch Prefixes

Preferred prefixes:

```text
feature/
fix/
refactor/
test/
perf/
docs/
build/
chore/
```

### GIT-005 — Create Branches from Updated `main`

```bash
git switch main
git fetch origin
git pull --ff-only
git switch -c feature/uc-bnk-002-deposit
```

---

## 2. Commit Strategy

### GIT-006 — Commit at the Level of One Completed Logical Change

The default commit unit is:

```text
one completed business behavior
or
one small coherent design/refactoring change
```

Examples:

```text
BankAccount can accept a valid deposit
Non-positive deposit is rejected
Deposit creates the expected Ledger movement
Deposit HTTP endpoint is exposed
```

### GIT-007 — Prefer Green Commits

The normal TDD flow is:

```text
Red
↓
Green
↓
Refactor
↓
Commit
```

A commit should normally represent a usable checkpoint where relevant tests pass.

### GIT-008 — Keep Commits Small but Meaningful

Avoid both extremes: huge unrelated commits and meaningless one-line commits.

A reviewer should be able to understand the purpose of each commit.

### GIT-009 — Separate Behavior Changes and Refactoring Where Practical

Prefer:

```text
refactor(ledger): simplify entry creation
```

followed by:

```text
feat(banking): support withdrawals
```

when a structural cleanup is logically separate from a behavior change.

### GIT-010 — Use Conventional-Commits-Style Messages

Preferred types:

```text
feat
fix
refactor
test
perf
docs
build
chore
```

Examples:

```text
feat(banking): support deposits
feat(banking): reject withdrawals from frozen accounts
test(ledger): verify zero-sum transaction invariant
refactor(brokerage): separate order validation
perf(ledger): optimize account history query
docs(sql): record cursor pagination experiment
```

### GIT-011 — Commit Messages Must Describe the Change

Avoid vague messages such as:

```text
fix
update
changes
修正
test
```

### GIT-012 — Use Commit Body When the Reason Is Not Obvious

Feature-level design reasoning normally belongs in the Pull Request.
Long-lived architectural reasoning belongs in an ADR.

---

## 3. Staging and Pre-Commit Review

### GIT-013 — Inspect Changes Before Staging

```bash
git status
git diff
```

### GIT-014 — Inspect Staged Changes

```bash
git diff --staged
```

### GIT-015 — `git add .` Is Allowed but Must Be Intentional

When the working tree contains unrelated changes, prefer:

```bash
git add -p
```

or explicit file/path staging.

### GIT-016 — Run Relevant Tests Before Commit

For ordinary logic changes:

```bash
./mvnw test
```

For persistence, migration, transaction, or integration-sensitive changes:

```bash
./mvnw verify
```

---

## 4. Synchronizing with `main`

### GIT-017 — Fetch Before Integrating Recent `main`

```bash
git fetch origin
```

### GIT-018 — Rebase Is Preferred for Personal Short-Lived Branches

```bash
git rebase origin/main
```

### GIT-019 — Do Not Rewrite Shared Branch History Without Coordination

Do not casually rebase a branch used by multiple developers.

### GIT-020 — Resolve Conflicts Intentionally

Use:

```bash
git status
```

and understand both sides before editing.

Migration conflicts require particular care.

### GIT-021 — Re-run Tests After Conflict Resolution

A textual conflict resolution does not prove semantic correctness.

---

## 5. Pull Request Strategy

### GIT-022 — Every Normal Change Reaches `main` Through a Pull Request

Even in a solo learning repository, Pull Requests are used for review and decision recording.

### GIT-023 — One Pull Request Has One Primary Purpose

A Pull Request should be explainable in one sentence.

### GIT-024 — Pull Request Description Is an Engineering Record

Required sections:

```text
Purpose
Changes
Design Decisions
Alternatives Considered
Tests
Related
Out of Scope
Notes
```

Database-related Pull Requests add:

```text
Database Changes
```

Performance-related Pull Requests add:

```text
Performance Evidence
```

### GIT-025 — Purpose Explains Why

Describe the business, learning, technical, or corrective reason for the change.

### GIT-026 — Changes Explains What

List the important implementation changes, not every edited line.

### GIT-027 — Design Decisions Records Feature-Level Reasoning

Examples:

```text
Why a Business Rule belongs in BankAccount rather than Application Service
Why a Query Service uses dedicated SQL instead of Aggregate loading
Why a specific validation boundary was selected
```

### GIT-028 — Alternatives Considered Records Real Alternatives

Record meaningful alternatives actually considered and why they were not selected.

### GIT-029 — Tests Records Verification

List what was actually run.

Examples:

```text
./mvnw test
./mvnw verify
BankAccountTest
TransferMoneyIT
API contract test
```

### GIT-030 — Related Links the Change to Design Documents

Where relevant, reference:

```text
Use Case ID
Business Rule ID
ADR
SQL Experiment
Issue
```

### GIT-031 — Out of Scope Records Intentional Omissions

Explicitly record deferred concerns such as:

```text
Concurrency control
Idempotency
Daily transfer limit
```

### GIT-032 — Notes Records Follow-Up Information

Use for future refactor candidates, known limitations, follow-up learning topics, and non-blocking observations.

---

## 6. Database Changes

### GIT-033 — Database Changes Are Explicitly Declared in the Pull Request

Include:

```text
Migration file
Schema impact
Compatibility impact
Data migration considerations
```

### GIT-034 — Applied Migrations Are Not Rewritten

Prefer a new migration rather than editing migration history.

### GIT-035 — Migration Changes Require Integration Verification

Schema-changing Pull Requests should normally run:

```bash
./mvnw verify
```

---

## 7. SQL and Performance Changes

### GIT-036 — Performance Pull Requests Require Evidence

Include:

```text
Dataset
Baseline
Change
After
Interpretation
Trade-off
```

### GIT-037 — SQL Optimization Includes Plan Evidence

For important SQL changes, include relevant evidence from:

```sql
EXPLAIN (ANALYZE, BUFFERS)
```

### GIT-038 — Record Why the Query Improved

Explain the plan change, index behavior, work reduction, and trade-off.

---

## 8. Self Review

### GIT-039 — The Author Is the First Reviewer

Before opening or finalizing a Pull Request:

```bash
git status
git diff origin/main...HEAD
```

### GIT-040 — Self-Review Checklist

Check for:

- unrelated files;
- debug code;
- commented-out code;
- accidental formatting churn;
- missing tests;
- weakened tests;
- misleading names;
- TODOs that should not ship;
- accidental secret/configuration changes;
- unnecessary abstractions.

### GIT-041 — Do Not Weaken Tests Merely to Merge

A failing test is not by itself a reason to reduce assertions.

---

## 9. Merge Strategy

### GIT-042 — Squash Merge Is the Default

Working-branch commits may be detailed.
`main` receives one logical commit per Pull Request.

### GIT-043 — Merge Commit Is Not the Normal Strategy

Avoid unnecessary merge commits in `main`.

### GIT-044 — Rebase Merge Is Not the Default

Squash Merge keeps `main` focused on accepted logical changes.

### GIT-045 — Merge Only When the Branch Is Ready

Minimum criteria:

```text
scope is coherent
self-review complete
required tests pass
conflicts resolved
documentation updated where necessary
PR description complete
```

CI status checks become mandatory once configured.

### GIT-046 — Update from `main` Before Final Merge When Needed

```bash
git fetch origin
git rebase origin/main
```

Then rerun relevant verification.

### GIT-047 — Delete the Branch After Merge

A merged branch should not be reused for another feature.

---

## 10. Post-Merge Local Cleanup

```bash
git switch main
git pull --ff-only
git branch -d feature/uc-bnk-002-deposit
```

---

## 11. Force Push and Destructive Operations

### GIT-048 — Never Force Push `main`

Never use:

```bash
git push --force origin main
```

### GIT-049 — Prefer `--force-with-lease` over `--force`

For an intentionally rebased personal branch:

```bash
git push --force-with-lease
```

Plain `--force` is not part of normal workflow.

### GIT-050 — Destructive Commands Require Explicit Intent

Commands such as:

```bash
git reset --hard
git clean -fd
git restore .
git checkout -- .
git push --force
```

must not be used casually.

---

## 12. `git stash`

### GIT-051 — Stash Is Temporary Storage

```bash
git stash push -m "wip deposit"
```

Use stash for short interruptions, not long-term history.

---

## 13. AI / Codex Git Operations

### GIT-052 — AI May Inspect Git State Freely

AI may normally inspect:

```text
git status
git diff
git log
git show
```

and other non-destructive state.

### GIT-053 — AI Does Not Perform Repository-History Changes Without Explicit Instruction

The following require explicit user instruction:

```text
commit
push
merge
rebase
reset
branch deletion
force push
```

### GIT-054 — AI Must Not Revert Unrelated Existing Changes

Existing unrelated work must be preserved unless explicitly requested otherwise.

### GIT-055 — AI Must Not Use Destructive Git Commands Automatically

Destructive commands are never automatic recovery mechanisms.

---

## 14. Recommended Daily Flow

```text
1. Update main

git switch main
git fetch origin
git pull --ff-only

2. Create working branch

git switch -c feature/...

3. Work in small TDD steps

Red
Green
Refactor

4. Commit each completed logical behavior

git status
git diff
./mvnw test
git add ...
git diff --staged
git commit

5. Continue until the Pull Request objective is complete

6. Update from main if necessary

git fetch origin
git rebase origin/main

7. Run final verification

./mvnw verify

8. Self-review

git diff origin/main...HEAD

9. Push branch

git push -u origin <branch>

10. Open Pull Request

11. Complete PR description and review

12. Merge using Squash Merge

13. Delete branch

14. Update local main
```

---

## 15. Responsibility of Git Artifacts

```text
Code
→ What was implemented

Commit
→ One small completed logical change

Pull Request
→ Why the feature/change was implemented this way

ADR
→ Why a long-lived architectural decision was made
```

This distinction should be preserved throughout the project.
