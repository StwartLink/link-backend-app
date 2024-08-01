# Dockerfile para o Keycloak
FROM quay.io/keycloak/keycloak:25.0.2

USER root

# Copia o script de entrada para o container
COPY entrypoint.sh /opt/keycloak/entrypoint.sh

# Dá permissão de execução para o script de entrada
RUN chmod +x /opt/keycloak/entrypoint.sh

# Define o diretório de dados como volume persistente
VOLUME /opt/keycloak/data

# Retorna ao usuário padrão do container
USER keycloak

# Define o script de entrada como o ponto de entrada do container
ENTRYPOINT ["/opt/keycloak/entrypoint.sh"]
