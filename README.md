# Boat API
## API developed in Java Spring boot

### Tecnologies / dependecies

- Docker (postgresql)
- Security (jwt token)
- Swagger (springdoc-openapi-starter-webmvc-ui)


### How to run the project
To run the database, make sure you have docker running and do the following:
```sh
cd database_script
docker-compose up -d 
```

After this just run the project and you will have the API running!
> Note: `Everytime you run the code, it will delete and create the tables!`

The `Admin token` will be printed in the console!

> Note: Postman v2.1 collection available at /postman_collection/boat_API_requests.postman_collection.json

### Important urls
- localhost:8080/ (API running here)
- localhost:8080/swagger-ui/ (Swagger with the API documentation)


##### Project developed by: Francisco Melícias

## License

MIT