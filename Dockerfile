FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . /workspace/.
RUN mvn clean package
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /workspace
COPY --from=build /workspace/target/*.jar app.jar
RUN chown -R appuser:appgroup /workspace
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]