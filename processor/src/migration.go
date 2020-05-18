package main

//AutoMigration update the database schema
func AutoMigration() {
	db := dbConnect()
	defer db.Close()

	db.AutoMigrate(Order{})
	db.AutoMigrate(orderItems{})
	db.Model(&orderItems{}).AddForeignKey("order_id", "orders(id)", "RESTRICT", "RESTRICT")
	db.Model(&Order{}).AddIndex("idx_user_id", "user_id")
}
