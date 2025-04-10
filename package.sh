#!/bin/bash

# Config
JAR_PATH="build/libs/building-store-0.0.1-SNAPSHOT.jar"
ZIP_NAME="deploy.zip"

# Check required files
if [[ ! -f "$JAR_PATH" || ! -f "Dockerfile" || ! -f "Dockerrun.aws.json" ]]; then
  echo "Missing one or more required files (JAR, Dockerfile, Dockerrun.aws.json)"
  exit 1
fi

# Clean old zip
rm -f "$ZIP_NAME"

# Prepare temp directory
mkdir -p tmp-deploy
cp "$JAR_PATH" tmp-deploy/app.jar
cp Dockerfile tmp-deploy/
cp Dockerrun.aws.json tmp-deploy/

# Create zip
cd tmp-deploy
zip -r "../$ZIP_NAME" . > /dev/null
cd ..
rm -rf tmp-deploy

echo "Created $ZIP_NAME"
