#!/bin/bash

cp -r ./default/* ../jaac

if [ -n "$JENKINS_INSTANCE_NAME" ]; then
  echo "Looking for an instance configuration: $JENKINS_INSTANCE_NAME in git repo folder..."

  if [ -d "$JENKINS_INSTANCE_NAME" ]; then
    echo "Found $JENKINS_INSTANCE_NAME folder..."
    cp -r ./"$JENKINS_INSTANCE_NAME"/* ../jaac
  fi
else
  echo "Jenkins config not found or not set!"
fi