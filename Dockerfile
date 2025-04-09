FROM gradle:8.13 as builder

WORKDIR /app

# Скопируйте все файлы проекта
COPY . .

# Установите зависимости и соберите проект
RUN gradle installDist

# Создайте финальный образ
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/app-1.0-SNAPSHOT-all.jar /app.jar

EXPOSE 7070

ENTRYPOINT ["java", "-jar", "/app.jar"]