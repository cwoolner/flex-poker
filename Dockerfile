FROM eclipse-temurin:21.0.1_12-jdk-alpine

RUN apk add nodejs-current # v20.8.1
RUN apk add npm # v9.6.6
RUN apk add maven # v3.9.2

RUN mkdir flex-poker

COPY / /flex-poker/

WORKDIR /flex-poker

RUN npm install
RUN npm run prod
RUN mvn package -DskipTests


FROM eclipse-temurin:21.0.1_12-jre-alpine

COPY --from=0 /flex-poker/target/flexpoker.war .

ENTRYPOINT java -jar flexpoker.war
