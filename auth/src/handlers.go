package main

import (
	"encoding/json"
	"fmt"

	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/service/dynamodb"
	"github.com/aws/aws-sdk-go/service/dynamodb/dynamodbattribute"
	"github.com/google/uuid"
	fasthttp "github.com/valyala/fasthttp"
	"org.felipeagger.ecommerce.catalog/src/templates/index"
	"org.felipeagger.ecommerce.catalog/src/templates/layout"
)

type handlers struct {
	dynamo *dynamodb.DynamoDB
}

func (h *handlers) getHealth(ctx *fasthttp.RequestCtx) {
	ctx.SetContentType("application/json; charset=utf8")

	jsonStr := "{\"status\": \"health\"}"

	fmt.Fprint(ctx, jsonStr)
}

func (h *handlers) getLogin(ctx *fasthttp.RequestCtx) {
	ctx.SetContentType("text/html")

	p := &index.LoginPage{
		CTX: ctx,
	}

	layout.WriteBaseLayout(ctx, p)
}

func (h *handlers) getRegister(ctx *fasthttp.RequestCtx) {
	ctx.SetContentType("text/html")

	p := &index.SignupPage{
		CTX: ctx,
	}

	layout.WriteBaseLayout(ctx, p)
}

func (h *handlers) postLogin(ctx *fasthttp.RequestCtx) {
	ctx.SetContentType("application/json; charset=utf8")

	form, err := ctx.MultipartForm()

	jsonStr := "{\"status\": \"Unauthorized\"}"
	statusCode := 403

	if err != nil {
		fmt.Fprint(ctx, jsonStr)
		ctx.SetStatusCode(statusCode)
		return
	}

	userFind := &login{
		Username: form.Value["username"][0],
		Password: form.Value["password"][0],
	}

	if userFind.Username == "" || userFind.Password == "" {
		fmt.Fprint(ctx, jsonStr)
		ctx.SetStatusCode(statusCode)
		return
	}

	user := h.findUserByUsername(userFind.Username)

	if userFind.Username == user.Username && userFind.Password == user.Password {

		token, err := createToken(user.ID)

		if err != nil {
			fmt.Fprint(ctx, jsonStr)
			ctx.SetStatusCode(statusCode)
			return
		}

		jsonStr = fmt.Sprintf("{\"status\": \"Authorized\", \"userId\": \"%s\", \"token\": \"%s\"}", user.ID, token)
		statusCode = 200

	}

	fmt.Fprint(ctx, jsonStr)
	ctx.SetStatusCode(statusCode)
}

func (h *handlers) postValidator(ctx *fasthttp.RequestCtx) {
	ctx.SetContentType("application/json; charset=utf8")

	var validate token
	json.Unmarshal(ctx.PostBody(), &validate)

	err := tokenValid(validate.Token)

	var jsonStr string
	var statusCode int

	if err != nil {
		jsonStr = "{\"status\": \"Unauthorized\"}"
		statusCode = 403
	} else {
		jsonStr = "{\"status\": \"Authorized\"}"
		statusCode = 200
	}

	fmt.Fprint(ctx, jsonStr)
	ctx.SetStatusCode(statusCode)
}

func (h *handlers) postRegister(ctx *fasthttp.RequestCtx) {
	ctx.SetContentType("application/json; charset=utf8")

	form, err := ctx.MultipartForm()

	if err != nil {
		fmt.Fprint(ctx, "{\"status\": \"Unauthorized\"}")
		ctx.SetStatusCode(fasthttp.StatusUnauthorized)
		return
	}

	user := &register{
		Name:     form.Value["name"][0],
		Username: form.Value["username"][0],
		Password: form.Value["password"][0],
	}

	if user.Name == "" || user.Username == "" || user.Password == "" {
		fmt.Fprint(ctx, "{\"status\": \"Unauthorized\"}")
		ctx.SetStatusCode(fasthttp.StatusUnauthorized)
		return
	}

	userDB := h.findUserByUsername(user.Username)

	if userDB.Username != "" {
		fmt.Fprint(ctx, "{\"status\": \"Unauthorized\", \"Error\": \"User already exists! :(\"}")
		ctx.SetStatusCode(fasthttp.StatusUnauthorized)
		return
	}

	newID, _ := uuid.NewUUID()

	newUser := register{
		ID:              newID.String(),
		Name:            user.Name,
		Username:        user.Username,
		Password:        user.Password,
		PermissionLevel: 1,
	}

	av, err := dynamodbattribute.MarshalMap(newUser)

	input := &dynamodb.PutItemInput{
		Item:      av,
		TableName: aws.String("users"),
	}

	_, err = h.dynamo.PutItem(input)
	if err != nil {
		fmt.Println(err.Error())
		fmt.Fprint(ctx, `{"status": "error on register user!"}`)
		ctx.SetStatusCode(fasthttp.StatusInternalServerError)
		return
	}

	fmt.Fprint(ctx, `{"status": "Created!"}`)
	ctx.SetStatusCode(fasthttp.StatusCreated)
}
