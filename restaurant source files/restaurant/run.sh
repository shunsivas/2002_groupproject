#!/bin/sh
cd "$(dirname "$0")"
[ -d out ] || ./build.sh
exec java -cp out restaurant.Main "$@"
