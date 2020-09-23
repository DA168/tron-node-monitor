FROM openjdk:8-jre-slim

VOLUME /tmp
ADD target/tronmonitor-0.0.1-SNAPSHOT.jar tron-monitor.jar
RUN sh -c 'touch /tron-monitor.jar' && \
    mkdir config

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Duser.timezone=UTC -jar /tron-monitor.jar