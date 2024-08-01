#!/bin/bash
# Inicializa o Keycloak em modo de desenvolvimento
/opt/keycloak/bin/kc.sh start-dev &

# Configura o admin CLI precisa executar depois de inicializar. 
/opt/keycloak/bin/kcadm.sh config credentials --server http://localhost:8080 --realm master --user $KEYCLOAK_ADMIN --password $KEYCLOAK_ADMIN_PASSWORD

# Atualiza o realm master para não requerer SSL
/opt/keycloak/bin/kcadm.sh update realms/master -s sslRequired=NONE

# Mantém o container em execução
wait
