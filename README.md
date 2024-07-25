## Tecnologias a serem utilizadas

### Keycloak
### Para DEV
- `docker run -d -p 8081:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -e KC_HOSTNAME_STRICT_HTTPS=false -e KC_HTTP_ENABLED=true quay.io/keycloak/keycloak:25.0.2 start-dev`
- user: admin; password: admin
- docker exec -it <nome_do_container> /bin/bash
- /opt/keycloak/bin/kcadm.sh config credentials --server http://localhost:8080 --realm master --user admin --password admin
- /opt/keycloak/bin/kcadm.sh update realms/master -s sslRequired=NONE
### Para PROD
- Deixar como padrão `<adicionar>`

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




