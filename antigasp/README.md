# Backend

## Set up

To run the app:

1. Create a database in PostgreSQL:

```bash
# Replace <database_name> by a name at your choice
CREATE DATABASE <database_name>;
```

2. Add the properties to connect to the database:

- You can add it directly in the file _application.properties_ like the example below:

```properties
# Replace <...> by your true database properties
spring.datasource.url=jdbc:postgresql://localhost:5432/<database_name>
spring.datasource.username=<username>
spring.datasource.password=<password>
```

- Or, create a _.env_ file and declare your properties there:

```env
# Replace <...> by your true database properties
DB_URL=jdbc:postgresql://localhost:5432/<database_name>
DB_USERNAME=<username>
DB_PASSWORD=<password>
```
> Spring will retrieve these values ​​from the system environment variables.

