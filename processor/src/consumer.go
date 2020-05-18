package main

import (
	"encoding/json"
	"fmt"
	"strconv"

	"gopkg.in/confluentinc/confluent-kafka-go.v1/kafka"
)

func (h *Handlers) initConsumer() {

	consumer, err := kafka.NewConsumer(&kafka.ConfigMap{
		"bootstrap.servers": "ecommerce.net",
		"group.id":          "processor",
		"auto.offset.reset": "earliest",
	})

	if err != nil {
		panic(err)
	}

	consumer.SubscribeTopics([]string{"order-topic"}, nil)

	for {
		msg, err := consumer.ReadMessage(-1)
		if err == nil {

			topic := msg.TopicPartition
			cart := Cart{}

			jsonMsg := string(msg.Value)
			json.Unmarshal([]byte(jsonMsg), &cart)

			status, _ := strconv.Atoi(cart.Status)

			order := &Order{
				UserID: cart.UserID,
				Date:   strToDate(cart.Date),
				Value:  cart.Value,
				Status: status,
			}

			h.db.Create(&order)

			for _, item := range cart.Items {

				orderitem := &orderItems{
					OrderID:  order.ID,
					ItemUUID: item.ItemUUID,
					Amount:   item.Amount,
					Price:    item.Price,
				}

				h.db.Create(&orderitem)
			}

			fmt.Printf("Msg %s processed\n", topic)

		} else {
			fmt.Printf("Consumer error: %v (%v)\n", err, msg)
		}
	}

	//consumer.Close()
}
