FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY taskflow-server/pom.xml .
COPY taskflow-server/src ./src
RUN apk add --no-cache maven && mvn -q -DskipTests package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/taskflow-server-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
