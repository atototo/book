# Start with a base image containing Java runtime
FROM openjdk:11 as build

# Add Author info
LABEL maintainer="atoto0311@gmail.com"

# Add a volume to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=build/libs/search-kakao-boot.jar

# Add the application's jar to the container
ADD ${JAR_FILE} search-kakao-boot.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/search-kakao-boot.jar"]

