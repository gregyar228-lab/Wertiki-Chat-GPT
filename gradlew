#!/usr/bin/env sh
set -eu
VERSION="8.1.1"
BASE="${GRADLE_USER_HOME:-$HOME/.gradle}/skyrotor-wrapper"
HOME_DIR="$BASE/gradle-$VERSION"
if [ ! -x "$HOME_DIR/bin/gradle" ]; then
  mkdir -p "$BASE"
  ARCHIVE="$BASE/gradle-$VERSION-bin.zip"
  if [ ! -f "$ARCHIVE" ]; then
    if command -v curl >/dev/null 2>&1; then
      curl -fL "https://services.gradle.org/distributions/gradle-$VERSION-bin.zip" -o "$ARCHIVE"
    elif command -v wget >/dev/null 2>&1; then
      wget "https://services.gradle.org/distributions/gradle-$VERSION-bin.zip" -O "$ARCHIVE"
    else
      echo "Install curl or wget to download Gradle." >&2; exit 1
    fi
  fi
  unzip -q -o "$ARCHIVE" -d "$BASE"
fi
exec "$HOME_DIR/bin/gradle" "$@"
