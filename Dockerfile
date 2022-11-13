FROM openjdk:17.0.2-jdk-slim-buster

RUN apt update
RUN apt install curl xz-utils -y

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz https://apache.osuosl.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

RUN curl https://nodejs.org/dist/v18.12.1/node-v18.12.1-linux-x64.tar.xz > node-v18.12.1-linux-x64.tar.xz
RUN tar xvf node-v18.12.1-linux-x64.tar.xz -C /
ENV PATH="${PATH}:/node-v18.12.1-linux-x64/bin"

RUN mkdir flex-poker

COPY / /flex-poker/

WORKDIR /flex-poker

RUN npm install
RUN mvn package


FROM openjdk:17.0.2-jdk-slim-buster

COPY --from=0 /flex-poker/target/flexpoker.war .

ENTRYPOINT java -jar flexpoker.war
