FROM adoptopenjdk:11-jdk-hotspot
VOLUME ["/tmp", "/data", "/config", "/logs"]
EXPOSE 8080
ENV PORT=8080
COPY backend/build/libs/jass-*.jar app.jar
CMD java -jar /app.jar
