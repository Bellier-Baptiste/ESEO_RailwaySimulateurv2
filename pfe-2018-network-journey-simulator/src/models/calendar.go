package models

import (
	"log"
	"pfe-2018-network-journey-simulator/src/configs"
	"time"
)

type Calendar struct {
}

func GetBusinessDay(currentTime time.Time) time.Time {
	config := configs.GetInstance()
	buisnessDayOffset := config.BusinessDayStart()
	buisnessDay, error := time.ParseDuration(buisnessDayOffset)
	if error != nil {
		log.Fatal(error, "BusinessDayStart - couldn't parse duration")
	}
	return currentTime.Add(-buisnessDay)
}
