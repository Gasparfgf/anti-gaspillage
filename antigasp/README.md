# Backend

---

## Architecture

![Architecture of the unsold goods platform](assets/architecture_overview.svg "architecture overview")

---

## Set up

### PostgreSQL database

1. Log in to PostgreSQL as superuser:

```bash
psql -U postgres
```

2. Execute these commands in psql shell or pgAdmin:

```sql
-- Create the user
CREATE USER <invendus_user> WITH PASSWORD <'invendus_pass'>;

-- Create database
CREATE DATABASE invendus_db OWNER <invendus_user>;

-- Granting rights
GRANT ALL PRIVILEGES ON DATABASE invendus_db TO invendus_user;

-- Quit
\q
```

Test the connection with the correct user (verification):

```bash
psql -U <invendus_user> -d invendus_db -h localhost
```

If you see the prompt `invendus_db=>`, it's good.

3. Define properties:

* Option A — Directly in _application.properties_:

```properties
# Replace <...> by your true property values

# Server
server.port=<port>

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/invendus_db
spring.datasource.username=<username>
spring.datasource.password=<password>

# JPA / Hibernate

# replace the value 'update' by 'create' (only) if it's the first time running the backend
# it will alow Spring Boot to automatically create the tables
spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# JWT
# app.jwt.secret=
app.jwt.expiration=86400000

# CORS (for dev)
app.cors.allowed-origins=<frontend_url>
```

* Option B — Using a .env file :

  - Create a _.env_ file at the root of your backend project (**antigasp/**):

```env
DB_URL=jdbc:postgresql://localhost:5432/invendus_db
DB_USERNAME=<username>
DB_PASSWORD=<password>
DB_DRIVER=org.postgresql.Driver
PORT=<port_number>

FRONTEND=<http://localhost:4200>

JWT_SECRET=<a_very_long_and_secure_secret_key_to_be_changed_in_production>
```

  - Then make sure they keep referenced in application.properties:

```properties
# Server
server.port=${PORT}

spring.application.name=antigasp

# Database
spring.datasource.url=${DB_URL:NOT_FOUND}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# JWT
# app.jwt.secret=
app.jwt.expiration=86400000

# CORS (for dev)
app.cors.allowed-origins=${FRONTEND}
```
> Spring will retrieve these values ​​from the system environment variables.

---

## Running

If you use _.env_ file, in **antigasp/** directory:

```bash
./bin/run.sh
```
> Specially for the first time. It exposes the properties of .env file before launching the backend.

If not:

```bash
./mvnw spring-boot:run
```

---

## Checking if everything is ok

In the logs, you should see Hibernate generating the tables:

```
Hibernate: create table users (...)
Hibernate: create table offer (...)
Hibernate: create table reservation (...)
```

You can also check directly:

* in the database:

```bash
psql -U postgres
```

```sql
\c invendus_db
\dt
```

* or test with curl or Postman:

```bash
# Inscription
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstname": "Jean",
    "surname": "Dupont",
    "address": "12 rue de Paris",
    "birthDate": "1990-05-15",
    "email": "jean@example.com",
    "password": "motdepasse123"
  }'

# Connexion
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jean@example.com",
    "password": "motdepasse123"
  }'
```

You should receive a JSON object with a `token` like:

```
{
 "token":"eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqZWFuQGV4YW1wbGUuY29tIiwicm9sZSI6IkNMSUVOVCIsImlhdCI6MTc3ODc3NDk3NiwiZXhwIjoxNzc4ODYxMzc2fQ.ZOlz9tE3jfAA8NMOgwYjMvwUsgqrgRbXUyEe8SjU7pzx5uynuBIBWSE-wleHSPDQ",
 "userId":1,
 "email":"jean@example.com",
 "firstname":"Jean",
 "role":"CLIENT"
}
```
