# Backend

## Set up

1. Create a database in PostgreSQL:

```sql
-- Replace <database_name> by a name of your choice
CREATE DATABASE <database_name>;
```

2. Configure database connection

Option A — Directly in _application.properties_:

```properties
# Replace <...> by your true database properties
spring.datasource.url=jdbc:postgresql://localhost:5432/<database_name>
spring.datasource.username=<username>
spring.datasource.password=<password>
```

Option B — Using a .env file :

  - Create a _.env_ file at the root of backend project (**antigasp/**):

```env
# Replace <...> by your true database properties
DB_URL=jdbc:postgresql://localhost:5432/<database_name>
DB_USERNAME=<username>
DB_PASSWORD=<password>
```

  - Then make sure they keep referenced in application.properties:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```
> Spring will retrieve these values ​​from the system environment variables.

---

## Running

If you use _.env_ file, in **antigasp/** directory:

```bash
./run.sh
```

If not:

```bash
./mvnw spring-boot:run
```
