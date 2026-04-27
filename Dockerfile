# ================================
# Stage 1: Build
# ================================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copia o pom.xml e baixa dependências (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests -B

# ================================
# Stage 2: Runtime
# ================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Cria usuário não-root por segurança
RUN addgroup -S vestfatec && adduser -S vestfatec -G vestfatec

# Copia o JAR gerado no stage anterior
COPY --from=build /app/target/*.jar app.jar

# Permissão ao usuário
RUN chown vestfatec:vestfatec app.jar
USER vestfatec

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]