# Uruchomienie bazy PostgreSQL

## Bash (Linux / macOS)

```bash

docker run --name pg-local \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=yourpassword \
  -e POSTGRES_DB=mydb \
  -p 5433:5432 \
  -d postgres:16
```

## Windows (PowerShell)

```powershell
docker run --name pg-local `
  -e POSTGRES_USER=postgres `
  -e POSTGRES_PASSWORD=yourpassword `
  -e POSTGRES_DB=mydb `
  -p 5433:5432 `
  -d postgres:16
```

## application.properties

```properties
spring.application.name=prosty_crud
server.port=8081
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## Wejście do kontenera PostgreSQL

### Linux / macOS / Windows (PowerShell)

```bash
docker exec -it pg-local bash
```

### Uruchomienie psql po wejściu do kontenera

```bash
psql -U postgres -d mydb
```
