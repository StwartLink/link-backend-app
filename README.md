## Tecnologias a serem utilizadas

### Keycloak
- `docker run -p 8081:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:25.0.2 start-dev`
- user: admin; password: admin

### Zipkin
- `docker run -d -p 9411:9411 openzipkin/zipkin`


### RabbitMQ
- docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management
- user: guest; password: guest;

## Microsserviços

- Eureka Server
- - `http://${eureka.instance.hostname}:${server.port}/eureka/`

- Gateway
- - 

## Postgres (usar um para cada microsserviço)
- user: postgres
- senha: docker
- porta: 5432

- Autenticação
- - Nome: auth-service
- - Acesso: via gateway `{gateway-address}/auth`

## Instruções ao criar microsserviço

- Retirar spring security dos microsserviços (segurança é feita pelo gateaway)




