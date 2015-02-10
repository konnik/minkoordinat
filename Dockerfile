FROM jeanblanchard/busybox-java:8
MAINTAINER Niklas Konstenius "niklas.konstenius@gmail.com"

RUN  mkdir /minkoordinat-api
COPY target/minkoordinat-api-0.0.1-SNAPSHOT.jar /minkoordinat-api/minkoordinat-api.jar

EXPOSE 8080

WORKDIR /minkoordinat-api
CMD ["/opt/jdk/bin/java", "-Djava.net.preferIPv4Stack=true", "-Dserver.address=0.0.0.0", "-Djava.security.egd=file:///dev/urandom", "-jar", "minkoordinat-api.jar"]
