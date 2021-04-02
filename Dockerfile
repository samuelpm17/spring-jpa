FROM openjdk:8-jdk-alpine
EXPOSE 5005 
ADD target/spring-jpa.jar spring-jpa.jar
ENTRYPOINT ["sh", "-c", "java -jar /spring-jpa.jar"]
