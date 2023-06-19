FROM tomcat:9-jdk17-openjdk-slim
COPY ./target/game2048.war /usr/local/tomcat/webapps