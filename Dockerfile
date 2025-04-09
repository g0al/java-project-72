FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY /app .

RUN gradle installDist

CMD java -jar build/libs/app-1.0-SNAPSHOT-all.jar