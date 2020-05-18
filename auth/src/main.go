package main

import (
	"flag"
	"fmt"
	"log"

	"github.com/buaazp/fasthttprouter"
	fasthttp "github.com/valyala/fasthttp"
)

var (
	addr     = flag.String("addr", ":8088", "TCP address to listen to")
	compress = flag.Bool("compress", false, "Whether to enable transparent response compression")
)

var (
	corsAllowHeaders     = "authorization"
	corsAllowMethods     = "HEAD,GET,POST,PUT,DELETE,OPTIONS"
	corsAllowOrigin      = "*"
	corsAllowCredentials = "true"
)

//CORS Cross-origin resource sharing
func CORS(next fasthttp.RequestHandler) fasthttp.RequestHandler {
	return func(ctx *fasthttp.RequestCtx) {

		ctx.Response.Header.Set("Access-Control-Allow-Credentials", corsAllowCredentials)
		ctx.Response.Header.Set("Access-Control-Allow-Headers", corsAllowHeaders)
		ctx.Response.Header.Set("Access-Control-Allow-Methods", corsAllowMethods)
		ctx.Response.Header.Set("Access-Control-Allow-Origin", corsAllowOrigin)

		next(ctx)
	}
}

//go:generate qtc -dir=templates -ext=html

func main() {

	router := fasthttprouter.New()

	dynamoSession := getDynamoSession()

	handler := handlers{
		dynamo: dynamoSession,
	}

	router.GET("/", handler.getHealth)
	router.GET("/login", handler.getLogin)
	router.POST("/login", handler.postLogin)
	router.GET("/signup", handler.getRegister)
	router.POST("/signup", handler.postRegister)

	router.POST("/validator", handler.postValidator)

	fmt.Println("Listening in PORT: 8088")
	log.Fatal(fasthttp.ListenAndServe(":8088", CORS(router.Handler))) //os.Getenv("PORT")

}
