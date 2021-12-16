# GraphQL Playground

Playground to test and play with GraphQL schema and queries.
The project utilises Scala and [Sangria - GraphQL Implementation](https://sangria-graphql.github.io/).

## Run

After cloning the project, simply run the following command:
```bash
sbt run
```

## How to use
1. Via [GraphiQL console](http://localhost:8080/graphql)

2. Via Command Line
```bash
# returns all movie data
curl -X POST localhost:8080/graphql \
-H "Content-Type:application/json" \
-d '{"query": "{movies {movieInfo {title, releaseDate, genre}, cast {name}}}"}'

# returns movie data based on movie id
curl -X POST localhost:8080/graphql \
-H "Content-Type:application/json" \
-d '{"query": "query($id: Int!){movie(id: $id) {movieInfo {title, releaseDate, genre}, cast {name}}}", "variables": {"id": 1}}'
```

## Content
Content displayed in the response is based on the `moviesData.json` static file, by default. If you want to experiment with an actual database connection, you may do so by updating the sql config in `application.conf`:
```bash
useDbConnection = true

# Optional - Only required if 'useDbConnection' is true
sql {
    host = "<HOST>"
    port = <PORT> 
    database = "<DATABASE NAME>"
    username = "<USERNAME>"
    password = "<PASSWORD>"
}
```

A `moviesData.sql` file has already been prepared to create the required tables accordingly. (Only Postgresql is supported at the moment.)