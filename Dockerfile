FROM openjdk:11.0.3-jdk-slim-stretch

RUN apt update
RUN apt install curl xz-utils -y

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz https://apache.osuosl.org/maven/maven-3/3.6.1/binaries/apache-maven-3.6.1-bin.tar.gz \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

RUN curl https://nodejs.org/dist/v10.16.0/node-v10.16.0-linux-x64.tar.xz > node-v10.16.0-linux-x64.tar.xz
RUN tar xvf node-v10.16.0-linux-x64.tar.xz -C /
ENV PATH="${PATH}:/node-v10.16.0-linux-x64/bin"

RUN mkdir flex-poker

COPY / /flex-poker/

WORKDIR /flex-poker

RUN npm install
RUN mvn package


FROM openjdk:11.0.3-jre-slim-stretch

COPY --from=0 /flex-poker/target/flexpoker.war .
COPY --from=0 /flex-poker/target/dependency/jetty-runner.jar .

ENTRYPOINT java -jar jetty-runner.jar flexpoker.war
