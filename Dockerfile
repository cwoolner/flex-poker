ARG ECLIPSE_TEMURIN_VERSION=25.0.3_9

FROM eclipse-temurin:${ECLIPSE_TEMURIN_VERSION}-jdk-alpine AS builder

RUN apk add nodejs-current # v20.8.1
RUN apk add npm # v9.6.6
RUN apk add maven # v3.9.2

RUN mkdir flex-poker

COPY / /flex-poker/

WORKDIR /flex-poker

RUN npm install
RUN npm run prod
RUN mvn package -DskipTests


FROM eclipse-temurin:${ECLIPSE_TEMURIN_VERSION}-jre-alpine

COPY --from=builder /flex-poker/target/flexpoker.war .

ENTRYPOINT java -jar flexpoker.war