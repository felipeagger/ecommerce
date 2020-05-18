docker:
	@docker-compose down
	@docker-compose build
	@docker-compose up -d

dockerbuild:
	@docker build -f  src/main/docker/Dockerfile.native  -t quarkus/rest-api-quarkus .

dockerdown:
	@docker-compose down

addextension:
	@./mvnw quarkus:add-extension -Dextension="$(ext)"

listextensions:
	@./mvnw quarkus:list-extensions

build:
	@echo "---- Building Application ----"
	@./mvnw package

buildnative:
	@./mvnw package -Pnative

buildnative2:
	@./mvnw package -Pnative -Dquarkus.native.container-build=true

run:
	@echo "---- Running Application ----"
	@mvn compile quarkus:dev -Dquarkus.profile=dev
