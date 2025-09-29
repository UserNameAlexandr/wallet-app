FROM openjdk:17-jdk-slim AS build
RUN apt-get update && apt-get install -y maven
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y maven
WORKDIR /app
COPY --from=build /app/target/wallet-app-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/pom.xml .
COPY --from=build /app/src ./src
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]