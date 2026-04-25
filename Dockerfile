FROM ubuntu:latest
LABEL authors="solom"

ENTRYPOINT ["top", "-b"]