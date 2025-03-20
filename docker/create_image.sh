#!/bin/bash

docker build -t davidccurjc/libace -f ../docker/Dockerfile ../
docker tag davidccurjc/libace davidccurjc/libace:latest
docker push davidccurjc/libace:latest
docker compose up