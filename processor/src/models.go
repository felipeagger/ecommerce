package main

import (
	"time"

	"github.com/jinzhu/gorm"
	uuid "github.com/satori/go.uuid"
)

//Order is a principal table
type Order struct {
	ID        string    `gorm:"primary_key" json:"id"`
	UserID    string    `gorm:"not null" json:"user_id"`
	Order     uint64    `gorm:"auto_increment" json:"order"`
	Date      time.Time `gorm:"default:NOW()" json:"date"`
	Value     float64   `gorm:"not null" json:"value"`
	Status    int       `json:"status"`
	CreatedAt time.Time `gorm:"default:NOW()" json:"created_at"`
	UpdatedAt time.Time `gorm:"default:NOW()" json:"updated_at"`
}

type orderItems struct {
	ID       string  `gorm:"primary_key" json:"id"`
	OrderID  string  `gorm:"not null" json:"order_id"`
	ItemUUID string  `gorm:"not null" json:"item_uuid"`
	Amount   float64 `json:"amount"`
	Price    float64 `json:"price"`
}

//Cart is a data from cart-svc
type Cart struct {
	UserID string       `json:"user_id"`
	Date   string       `json:"date"`
	Value  float64      `json:"value"`
	Status string       `json:"status"`
	Items  []orderItems `json:"items"`
}

//BeforeCreate execute before insert data on DB
func (o *Order) BeforeCreate(scope *gorm.Scope) error {

	uuid := uuid.NewV4().String()
	return scope.SetColumn("ID", uuid)
}

//BeforeCreate execute before insert data on DB
func (i *orderItems) BeforeCreate(scope *gorm.Scope) error {

	uuid := uuid.NewV4().String()
	return scope.SetColumn("ID", uuid)
}
