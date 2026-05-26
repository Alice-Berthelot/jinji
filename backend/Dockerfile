# ===== Build stage =====
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copie uniquement pom pour cache des dépendances
COPY pom.xml .

# Télécharge dépendances (cache Docker)
RUN mvn dependency:go-offline

# Copie le code source
COPY src ./src

# Build du jar (sans tests pour l’instant)
RUN mvn clean package -DskipTests


# ===== Runtime stage =====
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copie du jar depuis l’étape build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Lancement de l'application
ENTRYPOINT ["java", "-jar", "app.jar"]