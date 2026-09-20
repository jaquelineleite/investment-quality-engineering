# Quality Traceability Matrix

## Objective

This matrix connects business and technical quality requirements to identified risks,
automated scenarios and CI/CD evidence.

The purpose is to make quality decisions traceable from requirement to execution result.

> Important:
> The external Alpaca integration uses Paper Trading only.
> The controlled UI, CRM, PostgreSQL and RabbitMQ environments are laboratory components
> created for this Quality Engineering portfolio and do not represent the architecture
> of any financial institution.

---

## Traceability Matrix

| ID | Requirement / Quality Concern | Main Risk | Automated Validation | Layer | Evidence / Quality Gate |
|---|---|---|---|---|---|
| TR-001 | Valid API authentication | Invalid or expired credentials prevent access | `AuthenticationTest.shouldAuthenticateAndReturnPaperAccount` | API | Main Regression / PR Quality Gate |
| TR-002 | Invalid authentication must be rejected | Unauthorized access | `InvalidAuthenticationTest` | API / Security | Main Regression / PR Quality Gate |
| TR-003 | Account response contract must remain compatible | Backend contract regression | `AccountContractTest` | API Contract | Main Regression |
| TR-004 | Order response contract must remain compatible | Payload/schema drift | `OrderContractTest` | API Contract | Main Regression |
| TR-005 | Account business rules must remain valid | Incorrect account state or financial data | `AccountBusinessRulesTest` | API / Business | Main Regression |
| TR-006 | Tradable assets must be discoverable | Invalid asset/order flow | `AssetsTest` | API | Main Regression |
| TR-007 | Invalid order rules must be rejected | Invalid financial transaction | `OrderBusinessRulesTest` | API / Business | Main Regression |
| TR-008 | Duplicate requests must not create unintended effects | Duplicate order processing | `OrderIdempotencyTest` | API / Reliability | Main Regression |
| TR-009 | Order status must follow its lifecycle correctly | Inconsistent transaction state | `OrderLifecycleTest` | API / Business | Main Regression |
| TR-010 | Orders must be traceable by client identifier | Loss of transaction traceability | `OrderLookupByClientIdTest` | API | Main Regression |
| TR-011 | Valid user must access controlled UI | Broken login journey | `LoginTest.shouldLoginWithValidCredentials` | UI | Main Regression |
| TR-012 | Invalid UI credentials must be rejected | Unauthorized UI access | `LoginTest.shouldRejectInvalidCredentials` | UI / Security | Main Regression |
| TR-013 | Asset search must support valid and invalid symbols | Incorrect asset discovery | `AssetsTest` | UI | Main Regression |
| TR-014 | UI must allow a valid buy order | Broken investment journey | `OrderTest.shouldCreateValidBuyOrder` | UI / Business | Main Regression |
| TR-015 | UI must reject invalid orders | Invalid transaction accepted | `OrderTest.shouldRejectInvalidOrder` | UI / Business | Main Regression |
| TR-016 | Portfolio information must be displayed | Incorrect customer financial view | `PortfolioTest` | UI | Main Regression |
| TR-017 | Positions must reflect expected states | Position inconsistency | `PositionsTest` | UI / Business | Main Regression |
| TR-018 | Account must remain active and balance non-negative | Invalid persisted financial state | `DatabaseIntegrityTest` | Database | Main Regression |
| TR-019 | Orders must reference existing accounts | Broken relational integrity | `DatabaseIntegrityTest` foreign-key validation | Database | Main Regression |
| TR-020 | client_order_id must remain unique | Duplicate transaction persistence | `DatabaseIntegrityTest` unique constraint | Database | Main Regression |
| TR-021 | Order quantity must be greater than zero | Invalid financial order persisted | `DatabaseIntegrityTest` quantity constraint | Database | Main Regression |
| TR-022 | Order side must accept only valid values | Invalid business state persisted | `DatabaseIntegrityTest` side constraint | Database | Main Regression |
| TR-023 | Position quantity and average price must remain valid | Invalid portfolio persistence | `DatabaseIntegrityTest` position validation | Database | Main Regression |
| TR-024 | Order event must be publishable and consumable | Lost asynchronous event | `RabbitMqIntegrationTest.shouldPublishAndConsumeOrderEvent` | Messaging | Main Regression |
| TR-025 | Correlation ID and metadata must survive messaging flow | Loss of observability and tracing | `RabbitMqIntegrationTest.shouldPreserveCorrelationIdAndMetadata` | Messaging / Observability | Main Regression |
| TR-026 | Rejected event must reach DLQ | Failed event silently lost | `RabbitMqIntegrationTest.rejectedMessageShouldGoToDeadLetterQueue` | Messaging / Resilience | Main Regression |
| TR-027 | Customer data must synchronize with controlled CRM | Integration failure | `CustomerCrmSyncTest.shouldSynchronizeCustomerWithCrm` | Integration | Main Regression |
| TR-028 | CRM validation errors must be surfaced | Invalid customer payload accepted | `CustomerCrmSyncTest.shouldReturnValidationErrorFromCrm` | Integration / Negative | Main Regression |
| TR-029 | CRM unavailability must be observable | External dependency outage hidden | `CustomerCrmSyncTest.shouldSurfaceCrmUnavailableResponse` | Integration / Resilience | Main Regression |
| TR-030 | Secrets must not be committed | Credential exposure | `scripts/security/security_gate.py` | Security | PR / Main / Nightly / Release |
| TR-031 | .env and private key material must remain outside Git | Secret leakage | Security Gate tracked-file validation | Security | PR / Main / Nightly / Release |
| TR-032 | Regression execution must expose quality metrics | Pipeline gives only pass/fail without diagnostic value | `generate_test_summary.py` | Observability | GitHub Step Summary |
| TR-033 | Slow tests must be identifiable | Suite degradation goes unnoticed | Slowest-tests report | Observability | GitHub Step Summary / Artifact |
| TR-034 | Regression evidence must be retained | Loss of execution evidence | Surefire + observability artifact | Evidence | `main-regression-evidence` |
| TR-035 | API smoke performance must remain within threshold | API latency regression | `performance/smoke/api-smoke.js` | Performance | Local performance evidence |
| TR-036 | Controlled load must remain within threshold | Performance degradation under sustained load | `performance/load/orders-load.js` | Performance | Local performance evidence |
| TR-037 | Controlled stress behavior must be measurable | Capacity degradation | `performance/stress/orders-stress.js` | Performance | Local performance evidence |
| TR-038 | Controlled spike behavior must be measurable | Sudden traffic instability | `performance/spike/orders-spike.js` | Performance | Local performance evidence |
| TR-039 | Heavy performance tests must not target Alpaca | Unsafe load against external service | `performance-guard.js` | Safety / Performance | Execution guard |
| TR-040 | PR must receive a fast API quality gate | Defect reaches main branch | `pr-quality-gate.yml` | CI/CD | PR Quality Gate |
| TR-041 | Main branch must execute complete functional regression | Integrated regression escapes detection | `main-regression.yml` | CI/CD | Main Regression |
| TR-042 | Scheduled regression must be available | Regressions between deliveries go unnoticed | `nightly.yml` | CI/CD | Nightly Regression |
| TR-043 | Release must have an explicit quality gate | Release without regression evidence | `release.yml` | CI/CD | Release Quality Gate |

---

## Current Automated Functional Coverage

| Layer | Tests |
|---|---:|
| REST API / REST Assured | 12 |
| UI / Selenium | 9 |
| PostgreSQL / JDBC | 6 |
| RabbitMQ | 3 |
| Controlled CRM Integration | 3 |
| **Total** | **33** |

Current validated status:

- 33 tests executed
- 33 tests passed
- 0 failures
- 0 errors
- 100% pass rate
- Main Regression passing
- PR Quality Gate passing
- Security Gate active
- Automated observability summary generated
- Regression evidence published as CI artifact

---

## Traceability Flow

```text
Requirement
    ->
Risk
    ->
Test Scenario
    ->
Automated Test
    ->
Execution
    ->
Quality Gate
    ->
Evidence
```

