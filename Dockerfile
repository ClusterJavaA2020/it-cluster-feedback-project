FROM openjdk:11
COPY target/*.jar app.jar
COPY . .
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]