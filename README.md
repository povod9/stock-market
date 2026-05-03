# Simple Stock Market Service

## Description

This is a REST service simulating a simplified stock market, featuring:

- **Bank** — manages stock pool (see `/stocks`)
- **Wallets** — user stock portfolios (`/wallets`)
- **Audit log** — all wallet-side trades (`/logs`)
- **High Availability**: killing one instance does not take down the service (`/chaos`)

---

## Quick Start

### Requirements

- **Docker** & **Docker Compose** (runs on Windows, Linux, macOS, amd64/arm64)
- Maven & JDK

### Build and Run PowerShell

```powershell
# Build the app
mvn clean package

# Run 2 instances of the app for HA
$env:APP_PORT=8099; docker compose up --build --scale market-service=2
```
Example: Access at http://localhost:8099

### Build and Run cmd
```cmd
mvn clean package
set APP_PORT=8099
docker compose up --build --scale market-service=2
```
Example: Access at http://localhost:8099

### Build and Run Linux/macOS
```bash
mvn clean package
export APP_PORT=8888
docker compose up --build --scale market-service=2
```
Example: Access at http://localhost:8888
- Set APP_PORT to desired port (default is 8080). The app will be available at http://localhost:APP_PORT
---

## Endpoints

### Wallets
- `POST /wallets/{wallet_id}/stocks/{stock_name}`  
  **Body:** `{ "type": "buy" | "sell" }` – Buy or sell one stock; creates wallet if missing
- `GET /wallets/{wallet_id}` – Wallet state
- `GET /wallets/{wallet_id}/stocks/{stock_name}` – Number of a given stock in this wallet

### Bank
- `GET /stocks` – All stocks in the bank
- `POST /stocks` – Add a new stock

### Audit
- `GET /logs` – Returns the full successful wallet operation log

### Chaos Engineering
- `POST /chaos` – Kills this instance

---

## Notes

- All API contracts and error handling.
- No order books, wallet balances or price changes. Each stock always costs 1.
- **/log** capped at 10,000 entries (internally limited).
- **HA:** With `docker compose up --scale market-service=2`, the app is highly available: killing an instance does not break the service.
- Uses Postgres as persistent backend.

---

## Development

- Standard Spring Boot / Java 21 / Nginx (Load Balancer) / Docker / JPA / Lombok / Flyway / Validation / MapStruct setup.
- To rebuild after changes: `mvn clean package && docker compose up --build --scale market-service=2`
- All source code, configs, and mapping logic are in this repository.
- Flyway handles DB migrations automatically on startup
- Bean Validation is used for DTO and input validation.
---