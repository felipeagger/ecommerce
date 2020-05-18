package main

import (
	"fmt"

	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
	_ "github.com/jinzhu/gorm/dialects/postgres"
)

const (
	dbName = "ecommerce"
	dbHost = "192.168.15.5"
	dbUser = "postgres"
	dbPass = "postgres"
	dbPort = "5432"
)

func dbConnect() *gorm.DB {

	urlConn := fmt.Sprintf("host=%s port=%s user=%s dbname=%s password=%s sslmode=disable", dbHost, dbPort, dbUser, dbName, dbPass)

	db, err := gorm.Open("postgres", urlConn)

	db.DB().SetMaxIdleConns(10)
	db.DB().SetMaxOpenConns(100)

	if err != nil {
		fmt.Println("Error in conection with database!")
		panic(err)
	}

	return db
}

func selectOrder(ID int, h *Handlers, c *gin.Context) Order {

	var order Order

	if err := h.db.Where("id = ?", ID).First(&order).Error; err != nil {
		checkErr(err, c)
	}

	return order
}

func selectOrdersByUser(userID string, h *Handlers, c *gin.Context) []Order {

	var orders []Order

	if err := h.db.Where("user_id = ?", userID).Find(&orders).Error; err != nil {
		checkErr(err, c)
	}

	return orders
}

func checkErr(err error, c *gin.Context) {
	if err != nil {
		c.AbortWithError(500, err)
		return
		//panic(err)
	}
}
