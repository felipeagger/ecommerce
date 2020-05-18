FROM golang:1.13-alpine

ENV GOPATH="$HOME/go"

WORKDIR $GOPATH/src

COPY . $GOPATH/src

RUN apk update && apk add curl git && \
    go build

EXPOSE 8800

ENTRYPOINT ["sh","app.sh"]
