FROM eclipse-temurin:17.0.5_8-jdk-alpine

RUN apk add nodejs-current # v18.9.1
RUN apk add npm # v8.10.0
RUN apk add maven # v3.8.5

RUN mkdir flex-poker

COPY / /flex-poker/

WORKDIR /flex-poker

RUN npm install
RUN npm run prod
RUN mvn package


FROM eclipse-temurin:17.0.5_8-jre-alpine

COPY --from=0 /flex-poker/target/flexpoker.war .

ENTRYPOINT java -jar flexpoker.war
