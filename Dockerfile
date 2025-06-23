FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . /app/.
RUN mvn clean package
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
COPY --from=build /app/target/AccountBalance.jar /app/app.jar
RUN chown -R appuser:appgroup /app
USER appuser
ENTRYPOINT ["java", "-jar", "/app/app.jar"]