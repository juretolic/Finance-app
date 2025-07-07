# Dockerfile za PersonalFinanceApp
FROM openjdk:11-jre-slim
VOLUME /tmp
COPY target/personal-finance-app-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"] 