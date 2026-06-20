#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

npm run prod
mvn -q compile spring-boot:run \
  -Dspring-boot.run.mainClass=com.flexpoker.ApplicationKt \
  -Dspring-boot.run.arguments=--server.port=8080