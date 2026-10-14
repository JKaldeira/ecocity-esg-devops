# =============================================================================
# EcoCity ESG - Multi-stage build para otimização da imagem final
# =============================================================================

# --------- Estágio 1: Builder (compila e empacota a aplicação) ---------------
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# --------- Estágio 2: Runtime (apenas JRE, sem ferramentas de build) --------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Usuário sem privilégios de root para execução da aplicação
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder --chown=appuser:appgroup /app/target/*.jar app.jar

USER appuser

# 8080 = produção | 8081 = staging (definido pelo profile ativo)
EXPOSE 8080 8081

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
