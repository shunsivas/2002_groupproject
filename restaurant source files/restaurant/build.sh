#!/bin/sh
set -e
cd "$(dirname "$0")"
mkdir -p out
javac -d out $(find src -name '*.java')
echo "Built into ./out. Run: java -cp out restaurant.Main"
