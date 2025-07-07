# Personal Finance App

Jednostavna Spring Boot aplikacija za upravljanje osobnim financijama.

## Funkcionalnosti

- ✅ Login i registracija korisnika (Spring Security + Thymeleaf)
- ✅ CRUD operacije nad transakcijama (unos, uređivanje, brisanje, prikaz)
- ✅ Svaki korisnik vidi samo svoje transakcije
- ✅ ADMIN korisnik može pregledavati sve korisnike i njihove transakcije
- ✅ MySQL baza podataka
- ✅ Thymeleaf za HTML stranice

## Tehnologije

- **Spring Boot 2.7.18**
- **Spring Security** - autentikacija i autorizacija
- **Spring Data JPA** - pristup bazi podataka
- **Thymeleaf** - template engine
- **MySQL** - baza podataka
- **Bootstrap 5** - CSS framework
- **Maven** - build tool

## Struktura projekta

```
src/main/java/com/example/personalfinanceapp/
├── PersonalFinanceAppApplication.java
├── config/
│   └── DataInitializer.java
├── controller/
│   ├── AuthController.java
│   ├── TransactionController.java
│   └── AdminController.java
├── model/
│   ├── User.java
│   └── Transaction.java
├── repository/
│   ├── UserRepository.java
│   └── TransactionRepository.java
├── service/
│   ├── UserService.java
│   └── TransactionService.java
└── security/
    └── SecurityConfig.java
```

## Postavljanje baze podataka

1. Kreirajte MySQL bazu:
```sql
CREATE DATABASE finance;
```

2. Kreirajte korisnika:
```sql
CREATE USER 'financeuser'@'localhost' IDENTIFIED BY 'financepass';
GRANT ALL PRIVILEGES ON finance.* TO 'financeuser'@'localhost';
FLUSH PRIVILEGES;
```

## Dockerizacija i pokretanje

### 1. Buildanje JAR-a

Prvo buildaj aplikaciju (iz root direktorija):

```bash
mvn clean package
```

### 2. Build i pokretanje s Dockerfile

```bash
docker build -t finance-app .
docker run -p 8080:8080 finance-app
```

### 3. Pokretanje s docker-compose (preporučeno)

Ovo će pokrenuti i MySQL i aplikaciju zajedno:

```bash
docker-compose up --build
```

Aplikacija će biti dostupna na [http://localhost:8080](http://localhost:8080)

MySQL će biti dostupan na portu 3306 (unutar kontejnera: host je `db`)

---

**Napomena:**
- Ako koristiš docker-compose, ne moraš lokalno imati instaliran MySQL – baza će se pokrenuti u kontejneru.
- Ako deployaš na cloud, koristi iste ove korake na serveru.

## Default korisnici

Aplikacija automatski kreira ove korisnike:

**Admin:**
- Username: `admin`
- Password: `admin123`

**User:**
- Username: `user`
- Password: `user123`

## Putanje

- `/login` - prijava
- `/register` - registracija
- `/transactions` - pregled transakcija
- `/transactions/add` - dodavanje nove transakcije
- `/transactions/edit/{id}` - uređivanje transakcije
- `/transactions/delete/{id}` - brisanje transakcije
- `/admin/users` - pregled svih korisnika (admin)
- `/admin/transactions` - pregled svih transakcija (admin)

## Entiteti

### User
- id: Long
- username: String
- email: String
- password: String
- role: Enum (USER, ADMIN)

### Transaction
- id: Long
- amount: BigDecimal
- type: Enum (INCOME, EXPENSE)
- date: LocalDate
- description: String
- user: User (ManyToOne)

## Thymeleaf stranice

- `login.html` - forma za prijavu
- `register.html` - forma za registraciju
- `transactions.html` - pregled transakcija
- `transaction_form.html` - unos ili uređivanje transakcije
- `admin_users.html` - pregled svih korisnika
- `admin_transactions.html` - pregled svih transakcija 