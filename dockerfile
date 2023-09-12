FROM openjdk:17-jdk
WORKDIR /app
COPY build/libs/woorimock-0.0.1-SNAPSHOT.jar /app/woorimock.jar
CMD ["java", "-jar", "/app/woorimock.jar"]