package main

import (
	"strconv"

	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
)

//Handlers is a default handler
type Handlers struct {
	db *gorm.DB
}

func (h *Handlers) getOrder(c *gin.Context) {

	ID, _ := strconv.Atoi(c.Param("id"))

	order := selectOrder(ID, h, c)

	c.JSON(200, order)
}

func (h *Handlers) getOrdersByUser(c *gin.Context) {

	ID := c.Param("id")

	order := selectOrdersByUser(ID, h, c)

	c.JSON(200, order)
}

func (h *Handlers) postOrder(c *gin.Context) {

	var order Order
	if err := c.ShouldBindJSON(&order); err != nil {
		c.JSON(400, gin.H{"error": err.Error()})
		return
	}

	h.db.Create(&order)

	c.JSON(201, order)
}
