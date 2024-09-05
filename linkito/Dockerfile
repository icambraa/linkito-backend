# Usa una imagen base de OpenJDK para Java 17
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo JAR generado al contenedor
COPY target/linkito-0.0.1-SNAPSHOT.jar /app/linkito.jar

# Expone el puerto 8080 para la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/linkito.jar"]
