FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app

COPY applications/app-service/build/libs/*.jar Franchise.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "Franchise.jar"]
