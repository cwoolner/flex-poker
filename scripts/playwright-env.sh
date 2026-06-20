#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

export PATH="$ROOT_DIR/node_modules/.bin:$PATH"
export PLAYWRIGHT_BROWSERS_PATH=$HOME/.cache/ms-playwright
export TMPDIR="${TMPDIR:-$ROOT_DIR/.tmp}"

mkdir -p "$PLAYWRIGHT_BROWSERS_PATH" "$TMPDIR"

exec "$@"