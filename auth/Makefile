export:
	@while read LINE; do export "$LINE"; done < go.env

create-migrations:
	@migrate create -ext sql -dir db -seq create_db_initial

buildtemplate:
	@go generate src/main.go

docker:
	@docker-compose down
	@docker-compose build
	@docker-compose up -d

dockerdown:
	@docker-compose down

build:
	@echo "---- Building Application ----"
	@go build -o catalog src/*.go

run:
	@echo "---- Running Application ----"
	@go run src/*.go