FROM openjdk:11.0.2-jdk-slim-stretch

RUN apt update
RUN apt install maven curl -y

RUN curl https://nodejs.org/dist/v8.9.4/node-v8.9.4-linux-x64.tar.xz > node-v8.9.4-linux-x64.tar.xz
RUN tar xvf node-v8.9.4-linux-x64.tar.xz -C /
ENV PATH="${PATH}:/node-v8.9.4-linux-x64/bin"

RUN mkdir flex-poker

COPY / /flex-poker/

WORKDIR /flex-poker

RUN npm install
RUN mvn package


FROM openjdk:11.0.2-jre-slim-stretch

COPY --from=0 /flex-poker/target/flexpoker.war .
COPY --from=0 /flex-poker/target/dependency/jetty-runner.jar .

ENTRYPOINT java -jar jetty-runner.jar flexpoker.war
