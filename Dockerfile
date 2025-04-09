FROM gradle:8.13

WORKDIR /app

COPY /app .

RUN gradle installDist

CMD java -jar app/build/libs/app-1.0-SNAPSHOT-all.jar