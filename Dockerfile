# --- ETAPA 1: BUILD (Compilação) ---
# Usamos uma imagem oficial do Maven com Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia tudo do seu projeto para dentro do container
COPY backend/ .

# Roda o Maven para gerar o arquivo .jar (Pula testes pra agilizar)
RUN mvn clean package -DskipTests

# --- ETAPA 2: RUN (Execução) ---
# Usamos uma imagem leve do Java 21 (JRE) apenas para rodar
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia apenas o .jar gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta e roda
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]