# Investment Quality Engineering

Quality Engineering portfolio focused on risk-based testing, API automation, UI automation, database integrity, asynchronous messaging, controlled integrations, performance, security, CI/CD, observability and traceability.

This project is a controlled engineering laboratory. It does not reproduce the architecture or production systems of any financial institution.

---

## Current Quality Status

| Quality Layer | Automated Tests | Status |
|---|---:|---|
| REST API / REST Assured | 12 | Passing |
| UI / Selenium | 9 | Passing |
| PostgreSQL / JDBC | 6 | Passing |
| RabbitMQ | 3 | Passing |
| Controlled CRM Integration | 3 | Passing |
| **Total Functional Tests** | **33** | **100% passing** |

Current validated state:

- 33 tests executed
- 33 tests passed
- 0 failures
- 0 errors
- PR Quality Gate passing
- Main Regression passing
- Nightly Regression passing
- Release Quality Gate passing
- Security Gate active
- Automated observability summary generated
- Regression evidence published as CI artifact

---

## Quality Engineering Scope

The project demonstrates:

- risk-based testing;
- REST API functional testing;
- contract testing;
- authentication and negative testing;
- business-rule validation;
- idempotency;
- Selenium UI automation;
- Page Object Model;
- PostgreSQL integrity validation;
- JDBC testing;
- RabbitMQ messaging;
- correlation IDs and metadata;
- Dead Letter Queue validation;
- controlled CRM HTTP integration;
- resilience scenarios;
- k6 performance testing;
- CI/CD quality gates;
- secret scanning;
- observability;
- execution evidence;
- requirement-to-test traceability.

---

## Architecture

    GitHub Actions / Quality Gates
                |
        +-------+-------+
        |       |       |
        v       v       v
       API      UI   Integration
       |        |       |
       v        v       +----------------------+
    Alpaca   Controlled | PostgreSQL           |
    Paper    UI Lab     | RabbitMQ             |
                        | Controlled CRM HTTP  |
                        +----------------------+

Supporting layers:

    Performance   -> k6
    Security      -> tracked-secret gate
    Observability -> Surefire + GitHub Summary
    Evidence      -> GitHub Actions artifacts
    Traceability  -> requirement -> risk -> test -> evidence

---

## Technology Stack

### Automation

- Java 21
- Maven
- JUnit 5
- REST Assured
- Selenium WebDriver
- Java HTTP Client

### Data and Integration

- PostgreSQL 17
- JDBC
- RabbitMQ
- Docker Compose

### Performance

- k6
- smoke
- load
- stress
- spike

### CI/CD

- GitHub Actions
- PR Quality Gate
- Main Regression
- Nightly Regression
- Release Quality Gate
---

## Repository Structure

    investment-quality-engineering/
    |
    |-- api-tests/
    |   `-- REST Assured API tests
    |
    |-- ui-tests/
    |   `-- Selenium UI automation
    |
    |-- integration-tests/
    |   |-- controlled CRM integration
    |   |-- PostgreSQL validation
    |   `-- RabbitMQ messaging
    |
    |-- test-support/
    |   `-- shared API clients, factories and configuration
    |
    |-- ui-lab/
    |   `-- controlled local UI
    |
    |-- infrastructure/
    |   `-- docker/
    |       |-- postgres/
    |       `-- rabbitmq/
    |
    |-- performance/
    |   |-- smoke/
    |   |-- load/
    |   |-- stress/
    |   |-- spike/
    |   |-- helpers/
    |   `-- local/
    |
    |-- scripts/
    |   |-- security/
    |   `-- reporting/
    |
    |-- docs/
    |   `-- traceability-matrix.md
    |
    `-- .github/
        `-- workflows/
            |-- pr-quality-gate.yml
            |-- main-regression.yml
            |-- nightly.yml
            `-- release.yml

---

## API Testing

The API layer uses REST Assured and JUnit 5.

Current automated coverage includes:

- valid authentication;
- invalid authentication;
- account contract validation;
- order contract validation;
- account business rules;
- asset lookup;
- negative order rules;
- order idempotency;
- order lifecycle;
- order lookup by client order ID.

The external API laboratory uses Alpaca Paper Trading only.

The project does not execute live financial trading.

---

## UI Automation

The UI layer uses Selenium WebDriver against a controlled local application.

Covered journeys include:

- valid login;
- invalid login;
- valid asset search;
- invalid asset search;
- valid buy order;
- invalid order;
- portfolio information;
- initial positions state;
- position after order.

The UI automation uses Page Object Model, explicit waits, stable data-testid selectors,
isolated WebDriver lifecycle and headless execution in CI.

---

## Database Testing

PostgreSQL runs through Docker Compose.

The database layer validates:

- active account state;
- non-negative balance;
- referential integrity;
- unique client_order_id;
- positive order quantity;
- valid BUY and SELL values;
- valid position quantity;
- valid average price.

JDBC is used to validate database behavior directly.

---

## Messaging Testing

RabbitMQ runs through Docker Compose.

The automated messaging scenarios validate:

- publish and consume of an order event;
- preservation of correlation ID;
- preservation of content type;
- event metadata;
- source metadata;
- rejected event routing to a Dead Letter Queue.

Temporary test queues are isolated and removed automatically after execution.

---

## Controlled CRM Integration

The CRM scenario is implemented as a controlled HTTP integration laboratory.

It validates:

- successful customer synchronization;
- request payload transmission;
- correlation ID propagation;
- validation error handling;
- external service unavailability.

This provides deterministic integration testing without claiming access to a production CRM.
---

---

## Performance Engineering

Performance validation is implemented with k6.

The strategy separates external smoke validation from controlled load generation.

### External API Smoke

The Alpaca Paper Trading API receives only a lightweight smoke test.

The validated smoke scenario uses:

- 1 virtual user;
- 3 iterations;
- HTTP error threshold below 1%;
- p95 response-time threshold below 3000 ms.

Heavy traffic is intentionally not generated against Alpaca.

### Controlled Performance Laboratory

Load, stress and spike tests run against a controlled local HTTP server.

Implemented scenarios:

    performance/
    |-- smoke/api-smoke.js
    |-- load/orders-load.js
    |-- stress/orders-stress.js
    |-- spike/orders-spike.js
    |-- helpers/performance-guard.js
    `-- local/perf-server.py

The performance guard prevents heavy scenarios from targeting Alpaca Paper Trading.

This protects the external service while still demonstrating performance engineering techniques.

---

## Security Engineering

Security controls include an automated tracked-secret quality gate.

Script:

    scripts/security/security_gate.py

The gate inspects tracked files for:

- committed .env files;
- private key material;
- PEM files;
- PKCS12 files;
- AWS access-key patterns;
- GitHub token patterns;
- generic secret-token patterns;
- hardcoded Alpaca credentials.

Real Alpaca credentials are provided through environment variables locally and GitHub Secrets in CI.

The local .env file is excluded from Git.

Current result:

    SECURITY GATE: PASSED

---

## CI/CD Quality Gates

Four GitHub Actions workflows are implemented.

### PR Quality Gate

Provides fast feedback before integration.

Flow:

    Checkout
        ->
    Security Gate
        ->
    Java Setup
        ->
    Secret Validation
        ->
    API Quality Gate

### Main Regression

Runs the complete functional regression after changes reach main.

It automatically starts:

- controlled UI;
- PostgreSQL;
- RabbitMQ.

It then executes API, UI, database, messaging and CRM tests.

### Nightly Regression

Runs the complete regression on a schedule and can also be triggered manually.

### Release Quality Gate

Provides a complete release-oriented regression with retained evidence.

Current validated workflow status:

    PR Quality Gate       PASS
    Main Regression       PASS
    Nightly Regression    PASS
    Release Quality Gate  PASS

---

## Observability and Test Evidence

Observability is generated automatically from Maven Surefire XML reports.

Script:

    scripts/reporting/generate_test_summary.py

The generated summary includes:

- total tests;
- passed tests;
- failures;
- errors;
- skipped tests;
- pass rate;
- execution duration;
- suite duration;
- slowest tests.

The Markdown summary is published directly in GitHub Actions.

The JSON summary and Surefire reports are retained as workflow artifacts.

Current validated functional result:

    Total tests: 33
    Passed: 33
    Failures: 0
    Errors: 0
    Pass rate: 100%

---

## Traceability

The project contains a formal quality traceability matrix:

    docs/traceability-matrix.md

The traceability model is:

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

This allows each automated validation to be connected to the risk that motivated it and to the evidence produced during execution.
---

## Running Locally

### Requirements

- Java 21
- Maven
- Docker
- Docker Compose
- Python 3
- Chrome or Chromium
- k6 for performance scenarios

### Environment Configuration

Create a local .env file based on .env.example.

Required variables:

    TEST_ENV=paper
    ALPACA_BASE_URL=https://paper-api.alpaca.markets
    ALPACA_API_KEY=<your-paper-api-key>
    ALPACA_SECRET_KEY=<your-paper-secret-key>

Real credentials must never be committed.

### Start PostgreSQL

    docker compose -f infrastructure/docker/postgres/docker-compose.yml up -d

### Start RabbitMQ

    docker compose -f infrastructure/docker/rabbitmq/docker-compose.yml up -d

### Start Controlled UI

    python -m http.server 8081 -d ui-lab

### Run Complete Functional Regression

    mvn clean test

Expected functional coverage:

    API          12
    UI            9
    Database      6
    RabbitMQ      3
    CRM           3
    ----------------
    TOTAL        33

### Run Security Gate

    python scripts/security/security_gate.py

Expected result:

    SECURITY GATE: PASSED

### Generate Observability Report

    python scripts/reporting/generate_test_summary.py

Generated files:

    reports/observability/test-summary.md
    reports/observability/test-summary.json

These reports are generated dynamically and are not committed.

---

## Quality Engineering Decisions

### Why separate API and UI?

The Alpaca Paper Trading API and the controlled local UI are independent laboratories.

No artificial end-to-end dependency was created between systems that are not known to
belong to the same real production architecture.

### Why use controlled PostgreSQL, RabbitMQ and CRM laboratories?

Controlled environments allow deterministic validation of:

- data integrity;
- asynchronous messaging;
- failure handling;
- correlation IDs;
- resilience behavior;
- integration contracts.

This avoids claiming access to internal company infrastructure.

### Why protect performance testing?

Heavy load, stress and spike tests are restricted to controlled local infrastructure.

The external Alpaca Paper Trading service receives only a lightweight smoke scenario.

### Why generate observability in CI?

A green pipeline alone provides limited diagnostic information.

The project also exposes:

- test counts;
- pass rate;
- failures and errors;
- execution duration;
- slowest tests;
- Surefire evidence;
- pipeline artifacts.

---

## Scope and Limitations

This project intentionally does not claim:

- access to Santander internal systems;
- access to Toro internal systems;
- reproduction of a bank production architecture;
- production CRM integration;
- production cloud infrastructure;
- live financial trading;
- performance capacity of Alpaca or any financial institution.

Alpaca is used only as an authorized Paper Trading API laboratory.

The UI, PostgreSQL, RabbitMQ and CRM components are controlled Quality Engineering
laboratories created specifically for this portfolio.

---

## Final Validated State

    Functional automation       33 tests passing

    API                         12
    UI                           9
    Database                     6
    RabbitMQ                     3
    CRM                          3

    PR Quality Gate             PASS
    Main Regression             PASS
    Nightly Regression          PASS
    Release Quality Gate        PASS

    Security Gate               PASS
    Observability               ACTIVE
    Traceability                DOCUMENTED
    Performance Guard           ACTIVE

---

## Interview Summary

A concise explanation of the project:

"I structured this project from a Quality Engineering perspective rather than only creating
automation scripts. I started from risk and separated the validation into API, UI, database,
messaging, integration and performance layers. I added CI/CD quality gates, secret protection,
observability and traceability so that each automated test can be related to a risk and to
execution evidence. The current functional regression has 33 automated tests across REST
Assured, Selenium, PostgreSQL, RabbitMQ and a controlled CRM integration."

---

## Author

Jaqueline Fernandes de Andrade

Quality Assurance / Quality Engineering