#!/bin/bash

docker build -t davidccurjc/libace -f ../docker/Dockerfile ../
docker tag davidccurjc/libace davidccurjc/libace:latest
