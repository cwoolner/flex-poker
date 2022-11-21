FROM eclipse-temurin:17.0.5_8-jdk-alpine

RUN apk add curl

# installs node v18.9.1 and npm 8.10.0 at the time of commit
RUN apk add nodejs-current npm

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz https://apache.osuosl.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

RUN mkdir flex-poker

COPY / /flex-poker/

WORKDIR /flex-poker

RUN npm install
RUN mvn package


FROM eclipse-temurin:17.0.5_8-jre-alpine

COPY --from=0 /flex-poker/target/flexpoker.war .

ENTRYPOINT java -jar flexpoker.war
