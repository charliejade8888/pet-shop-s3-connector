FROM openjdk:17
MAINTAINER PsiCom
COPY build/libs/pet-shop-s3-connector-0.0.1-SNAPSHOT.jar pet-shop-s3-connector-0.0.1-SNAPSHOT.jar
CMD java -jar /pet-shop-s3-connector-0.0.1-SNAPSHOT.jar
#to debug - uncomment below and add a default 'Remote JVM Debug' configuration!
#ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
#EXPOSE 5005
EXPOSE 8080
