package main

import (
	"fmt"
	"os"

	"github.com/gin-gonic/gin"
)

func main() {

	engine := gin.New()
	group := engine.Group("/order")
	groupHealth := engine.Group("/")

	//router := gin.Default()

	AutoMigration()
	database := dbConnect()

	handlers := Handlers{
		db: database,
	}

	groupHealth.GET("/", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"message": "Go Health!",
		})
	})

	group.GET("/oid/:id", handlers.getOrder)
	group.GET("/uid/:id", handlers.getOrdersByUser)

	group.POST("/", handlers.postOrder)
	//group.PUT("/:username", handlers.putUsers)

	go handlers.initConsumer()

	engine.Run(fmt.Sprintf(":%v", os.Getenv("PORT")))

}
