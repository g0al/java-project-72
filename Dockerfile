FROM gradle:8.13

WORKDIR /app

COPY /app .

RUN gradle installDist

COPY /app/build/libs/app-1.0-SNAPSHOT-all.jar /app.jar

# This is the port that your javalin application will listen on
EXPOSE 7070

ENTRYPOINT ["java", "-jar", "/app.jar"]

# CMD java -jar ./build/libs/app-1.0-SNAPSHOT-all.jar