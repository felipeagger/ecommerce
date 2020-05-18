package main

import "time"

func strToDate(data string) time.Time {
	date, _ := time.Parse("2006-01-02 15:04:05", data)
	return date
}
