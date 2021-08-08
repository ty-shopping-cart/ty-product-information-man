FROM adoptopenjdk/openjdk11:alpine-jre
COPY target/productInformation.jar productInformation.jar
ENTRYPOINT ["java", "-jar", "/productInformation.jar"]