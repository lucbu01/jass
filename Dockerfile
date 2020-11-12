
FROM adoptopenjdk:11-jdk-hotspot
VOLUME ["/tmp", "/data", "/config", "/logs"]
COPY backend/build/libs/jass-*.jar app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]