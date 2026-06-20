#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

export PLAYWRIGHT_BROWSERS_PATH="${PLAYWRIGHT_BROWSERS_PATH:-$ROOT_DIR/.playwright-browsers}"
export TMPDIR="${TMPDIR:-$ROOT_DIR/.tmp}"

mkdir -p "$PLAYWRIGHT_BROWSERS_PATH" "$TMPDIR"

exec "$@"