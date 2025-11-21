# Ogólna zasada portów i adapterów

W architekturze heksagonalnej obowiązuje prosta, uniwersalna zasada:

### ✔ **Port IN → implementowany przez Use Case**

Świat zewnętrzny (np. HTTP) wywołuje logikę aplikacji poprzez port IN.
UseCase implementuje port IN, ponieważ to on wykonuje logikę biznesową.

**Przepływ:**
Controller → Port IN → Use Case

### ✔ **Port OUT → implementowany przez Adapter OUT**

UseCase potrzebuje wykonać operację na świecie zewnętrznym (baza, API, Kafka).
Dlatego port OUT jest implementowany przez adapter OUT — to on „wychodzi” poza aplikację.

**Przepływ:**
Use Case → Port OUT → Adapter OUT → Zewnętrzny system

---

# Schemat przepływu (Hexagonal)

```text
        [ Świat zewnętrzny / HTTP ]
                     |
                     v
           +----------------------+
           |  Adapter IN (Web)    |
           |  Controller + DTO    |
           +----------+-----------+
                      |
                      v
                 [ Port IN ]
                      |
                      v
           +----------------------+
           |     Use Case         |
           |  (logika aplikacji)  |
           +----------+-----------+
                      |
                      v
                 [ Port OUT ]
                      |
                      v
           +-----------------------+
           | Adapter OUT (Postgres)|
           |  Repo + Encje JPA     |
           +----------+------------+
                      |
                      v
                [  Baza danych ]
```

---

# Uruchomienie bazy PostgreSQL

## Projekt — opis i architektura heksagonalna (Hexagonal Architecture)

Poniżej znajduje się opis działania projektu oraz wyjaśnienie zastosowanej architektury heksagonalnej.

---

## Struktura projektu

### **application — use case’y (logika aplikacyjna)**

Warstwa odpowiedzialna za wykonywanie logiki aplikacji. Zawiera dwie główne grupy operacji:

* **CommandUseCase** — operacje zmieniające stan (Create, Update, Delete)
* **QueryUseCase** — operacje odczytu (getAll, getById)

Use case nie ma pojęcia, jak działają adaptery — widzi tylko **porty**:

> „Wstrzyknięto mi coś, co implementuje CommandPort.
> Nie wiem jak to działa, ale wiem że spełnia kontrakt.”

UseCase implementuje interfejsy z `core/port/in`, ponieważ:

* controller (web) również nie zna implementacji logiki,
* wstrzykuje jedynie port **in** (interfejs), a nie klasę.

W ramach uproszczenia (bo projekt jest jedną aplikacją Spring Boot), klasy UseCase mają adnotacje `@Service`, aby Spring mógł je utworzyć.
W pełnym podziale na moduły Maven warstwa application nie miałaby zależności od Springa.

---

### **core — domena i porty**

#### **Domena**

Opisuje świat biznesowy: modele takie jak `Car`, `Invoice`, `Customer` itp.
Mogą zawierać logikę domenową, np. `Car.age()`.

#### **Porty**

Kontrakty definiujące zachowania:

* **port-in** — używane przez kontrolery (wejście do aplikacji),
* **port-out** — implementowane przez adaptery (wyjście z aplikacji, np. zapis do bazy).

Przykłady portów:

* `CarRepositoryCommandPort`
* `CarRepositoryQueryPort`

---

### **adapters — wejścia i wyjścia**

#### **adapters/postgres**

Implementacje portów **out**. Tu znajduje się cała obsługa bazy:

* repozytoria,
* encje,
* mapowanie domena ↔ encja.

#### **adapters/web**

Kontrolery HTTP — również adaptery (wejściowe).
Odpowiadają za:

* przyjęcie requestu,
* mapowanie DTO → domena,
* wywołanie portu-in,
* mapowanie domena → DTO.

> Controller jest adapterem wejściowym,
> PostgreSQL jest adapterem wyjściowym.

---

# Uruchomienie bazy PostgreSQL

## Bash (Linux / macOS)

```bash
# Wejście do kontenera Postgresa
# Otwiera psql bezpośrednio w bazie `mydb`
docker exec -it pg-local psql -U postgres -d mydb

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
