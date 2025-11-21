# Porównanie dwóch wersji projektu: classic (Controller–Service–Repository) vs hexagonal (Ports & Adapters)

Ten dokument opisuje, co zmieniło się między dwoma wersjami tego samego projektu:

* **Wersja 1 – classic**: prosta struktura `Controller → Service → Repository` na H2.
* **Wersja 2 – hexagonal**: architektura Port & Adapters z PostgreSQL, warstwą domeny (`core`), use case’ami (`application`) i adapterami (`adapters`).

Celem jest pokazanie **co się zmieniło, dlaczego** oraz **jakie są zalety** nowego podejścia.

---

## 1. Struktura classic (Controller–Service–Repository)

Drzewo (uproszczone):

```text
src/main/java/com/example/prosty_crud
├── ProstyCrudApplication.java
└── car
    ├── controller
    │   └── CarController.java
    ├── model
    │   └── Car.java
    ├── repository
    │   └── CarRepository.java
    └── service
        └── CarService.java
```

### Cechy:

* Jeden **pakiet funkcjonalny** `car` z czterema warstwami:

    * **controller** – przyjmuje HTTP i wywołuje serwis,
    * **service** – logika aplikacyjna i biznesowa,
    * **repository** – dostęp do bazy (H2 / JPA),
    * **model** – encja JPA `Car` wykorzystywana wszędzie.
* Encja `Car` jest jednocześnie:

    * modelem domenowym,
    * modelem bazy danych,
    * obiektem zwracanym na zewnątrz (np. w JSON).
* Brak DTO – kontroler zwraca bezpośrednio encję.
* Baza: **H2** – baza in-memory, wygodna do prostych przykładów.

### Konsekwencje:

* Kod jest prosty i krótki – idealny na start / demo.
* Warstwy są zależne bezpośrednio od frameworków (Spring, JPA) i modelu bazy.
* Trudniej odróżnić **logikę biznesową** od szczegółów technicznych.
* Zmieniona baza danych lub sposób ekspozycji API wymaga często zmian w wielu miejscach.

---

## 2. Struktura hexagonal (Ports & Adapters)

Drzewo (uproszczone):

```text
src/main/java/com/example/prosty_crud
├── ProstyCrudApplication.java
├── adapters
│   ├── postgres
│   │   ├── CarCommandAdapter.java
│   │   ├── mapper
│   │   │   └── ModelDomainMapper.java
│   │   ├── model
│   │   │   └── CarEntity.java
│   │   └── repository
│   │       ├── CarQueryAdapter.java
│   │       └── CarRepository.java
│   └── web
│       ├── controller
│       │   └── CarController.java
│       ├── dto
│       │   ├── CarResponseDto.java
│       │   ├── CreateCarDto.java
│       │   └── UpdateCarDto.java
│       └── mapper
│           └── DtoDomainMapper.java
├── application
│   └── useCase
│       ├── CommandUseCase.java
│       └── QueryUseCase.java
└── core
    ├── domain
    │   └── Car.java
    └── port
        ├── in
        │   ├── ICommandPort.java
        │   └── IQueryPort.java
        └── out
            ├── CarRepositoryCommandPort.java
            └── CarRepositoryQueryPort.java
```

### Najważniejsze zmiany strukturalne:

1. **Rozdzielenie aplikacji na cztery główne obszary:**

    * `core` – domena i porty,
    * `application` – use case’y,
    * `adapters/web` – warstwa HTTP (kontrolery, DTO, mappery),
    * `adapters/postgres` – warstwa bazy (encje, repozytoria, adaptery).

2. **Wprowadzenie domeny (`core/domain/Car`) niezależnej od JPA i Springa.**

    * `Car` to czysty model (np. `record`) z logiką biznesową (np. metoda `age()`),
    * nie ma adnotacji `@Entity`, `@Table`, itd.

3. **Wprowadzenie portów (`core/port/in` i `core/port/out`).**

    * **Port IN** (np. `ICommandPort`, `IQueryPort`) – to, co aplikacja udostępnia światu,
    * **Port OUT** (np. `CarRepositoryCommandPort`, `CarRepositoryQueryPort`) – to, czego aplikacja potrzebuje od świata (np. bazy).

4. **Use case’y w warstwie `application`.**

    * `CommandUseCase` i `QueryUseCase` implementują **porty IN**,
    * znają **tylko interfejsy portów OUT**, nie znają konkretnych adapterów,
    * zawierają logikę aplikacyjną (np. scenariusze create/update/delete).

5. **Adaptery w folderze `adapters/`.**

    * `adapters/web` – kontroler, DTO, mapper DTO ↔ domena,
    * `adapters/postgres` – encja `CarEntity`, repozytoria JPA, mapper domena ↔ encja, adaptery implementujące porty OUT.

6. **Użycie DTO zamiast wystawiania encji/bazowego modelu na zewnątrz.**

    * `CreateCarDto`, `UpdateCarDto`, `CarResponseDto`,
    * kontroler pracuje tylko na DTO, domena pozostaje ukryta za mapperem.

7. **Zmiana bazy z H2 na PostgreSQL.**

    * Nowy adapter (Postgres), konfiguracja w `application.properties`,
    * aplikacja od strony domeny i use case’ów nie musi o tym „wiedzieć”.

---

## 3. Co dokładnie się zmieniło – krok po kroku

### 3.1. `car/model/Car` → `core/domain/Car` + `adapters/postgres/model/CarEntity`

W wersji classic:

* `Car` była jednocześnie encją JPA, modelem domenowym i obiektem JSON.

W wersji hexagonal:

* `core/domain/Car` – czysty model domeny (np. `record`), może mieć logikę (`age()`),
* `adapters/postgres/model/CarEntity` – encja JPA z adnotacjami, typami bazodanowymi,
* `ModelDomainMapper` – tłumaczy `Car` ↔ `CarEntity`.

**Zysk:** separacja domeny od technologii (baza danych, JPA).

---

### 3.2. `CarService` → `CommandUseCase` + `QueryUseCase` + porty

W wersji classic:

* `CarService` łączył w sobie wszystko: logikę biznesową, logikę aplikacji i użycie repo.

W wersji hexagonal:

* **Interfejsy IN** (`ICommandPort`, `IQueryPort`) opisują operacje dostępne dla świata,
* **Use case’y** (`CommandUseCase`, `QueryUseCase`) implementują porty IN,
* Use case’y korzystają z **portów OUT**, a nie bezpośrednio z repozytoriów.

**Zysk:** logika jest odklejona od frameworka i od szczegółów technicznych, można ją testować i rozwijać niezależnie.

---

### 3.3. Kontroler – z prostego wejścia HTTP do adaptera WEB

W wersji classic:

* `CarController` przyjmował request, wywoływał serwis, zwracał encję `Car`.

W wersji hexagonal:

* `CarController` jest **adapterem wejściowym** (IN adapter),
* pracuje na DTO (`CreateCarDto`, `UpdateCarDto`, `CarResponseDto`),
* używa mappera `DtoDomainMapper` do tłumaczenia DTO ↔ domena,
* nie wie nic o repozytoriach, encjach JPA, Postgresie – wywołuje tylko port IN (use case).

**Zysk:** web można wymienić (REST → GraphQL, gRPC) bez dotykania logiki.

---

### 3.4. Repozytoria – z bezpośredniego użycia do adaptera POSTGRES

W wersji classic:

* `CarRepository` (Spring Data JPA) było wstrzykiwane bezpośrednio do serwisu.

W wersji hexagonal:

* `CarRepository` nadal istnieje, ale jest szczegółem adaptera,
* **adapter POSTGRES** (`CarCommandAdapter`, `CarQueryAdapter`) implementuje porty OUT,
* przypadki użycia widzą tylko porty OUT, a nie konkretne repozytoria.

**Zysk:** można zmienić bazę lub sposób przechowywania danych (Postgres → in-memory → inne repo) bez zmian w logice.

---

## 4. Dlaczego to ma sens – główne zalety przejścia na hexagonal

### 4.1. Niezależność od frameworków i technologii

* Domenowy `Car` nie zna JPA, Springa ani JSON-a.
* Use case’y nie znają Spring Data, Postgresa ani HTTP.
* Wymiana bazy/transportu = zmiany **tylko** w adapterach.

### 4.2. Łatwiejsze testowanie

* Use case’y można testować jednostkowo, podając **mocki portów OUT**.
* Nie trzeba odpalać kontekstu Springa, bazy ani serwera HTTP.
* Domenę i przypadki użycia da się testować w czystej Javie.

### 4.3. Lepsza czytelność odpowiedzialności

* `core` – definicja świata i kontraktów (business language),
* `application` – logika przypadków użycia,
* `adapters/web` – wejście HTTP,
* `adapters/postgres` – wyjście do bazy.

Każda warstwa ma jasną odpowiedzialność i swoje granice.

### 4.4. Przygotowanie pod większe systemy / mikroserwisy

* Ten sam wzorzec skaluje się na osobne moduły / serwisy,
* Dodanie np. kolejki, drugiej bazy, API zewnętrznego wymaga tylko nowych adapterów,
* Rdzeń (`core` + `application`) pozostaje stabilny.

### 4.5. Edukacyjnie – czysty przykład architektury hexagonalnej

Projekt pokazuje bardzo konkretnie:

* różnicę między **encją bazy** a **modelem domenowym**,
* jak używać **portów IN/OUT**,
* jak kontroler jest **adapterem wejściowym**, a Postgres **adapterem wyjściowym**,
* jak przenieść prosty CRUD z klasycznego wzorca do nowoczesnego podejścia heksagonalnego.

---

## 5. Kiedy użyć classic, a kiedy hexagonal?

* **Classic (Controller–Service–Repository)**

    * mały projekt, demo, PoC,
    * jedna baza, jeden sposób komunikacji,
    * mało logiki, mało zmian.

* **Hexagonal (Ports & Adapters)**

    * projekt większy lub taki, który będzie rosnąć,
    * planowane różne interfejsy wejścia (REST, GraphQL, CLI, Cron),
    * potencjalna zmiana bazy / integracje z innymi systemami,
    * większe wymagania testowalności i utrzymania.

W tym projekcie **przejście z wersji classic na hexagonal** pozwala pokazać, jak te same operacje CRUD mogą być zaimplementowane w sposób bardziej elastyczny, testowalny i odporny na zmiany technologii.
