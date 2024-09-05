# Usa una imagen base de OpenJDK para Java 17
FROM openjdk:17-jdk-slim AS build

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo pom.xml y los archivos de código fuente para que Maven pueda construir el proyecto
COPY pom.xml .
COPY src ./src

# Instala Maven en la imagen y compila el proyecto
RUN apt-get update && apt-get install -y maven && mvn clean package -DskipTests

# Segunda fase: crear la imagen final
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo JAR generado desde la fase anterior
COPY --from=build /app/target/linkito-0.0.1-SNAPSHOT.jar /app/linkito.jar

# Expone el puerto 8080 para la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/linkito.jar"]
