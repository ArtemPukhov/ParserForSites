FROM ubuntu:latest
RUN apt-get update && apt-get install -y openjdk-19-jdk
COPY Example_parser.jar out/parser_jar/Example_parser.jar
WORKDIR out/parser_jar/
RUN apt-get update && apt-get install -y mysql-server
EXPOSE 3306
CMD ["java", "-jar", "Example_parser.jar"]