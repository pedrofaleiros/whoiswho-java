# Etapa 1: Construir a aplicação usando Maven
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

# Copiar o arquivo pom.xml e o código-fonte
COPY pom.xml .
COPY src ./src

# Empacotar a aplicação
RUN mvn package -DskipTests

# Etapa 2: Executar a aplicação usando uma imagem mais leve do OpenJDK
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar o arquivo JAR empacotado da etapa de construção
COPY --from=build /app/target/*.jar app.jar

# Expor a porta que a aplicação usa (por exemplo, 8080)
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
