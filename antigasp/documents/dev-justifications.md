# Justifications for development

In this document i explain why some choices were made based in my logic and problems that i've encountered along the way.

## Model / Entity

I changed the entities because i've found that i was starting in a very complex way.

For the sake of consistency, I changed the package `entity` to `model`, so the entities stayed in entity package and the enums business in the enumsBusiness package. Both packages are in the package model because it's what they have in common - both work for the database modeling.

### User entity

Unlike other entities, the User entity has its table name explicitly defined because the word `user` is reserved by PostgreSQL.

## Application.properties

PostgreSQLDialect does not need to be specified explicitly using 'hibernate.dialect' - it will be selected by default.

## Code

All injected fields must be final; otherwise, **@RequiredArgsConstructor** will not include them in the generated constructor, and they will remain null.

> Lombok generates a constructor only for fields marked *final* or **@NonNull**. Fields marked without a final attribute are ignored.