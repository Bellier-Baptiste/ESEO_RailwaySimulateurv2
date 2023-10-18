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
	businessDayOffset := config.BusinessDayStart()
	businessDay, err := time.ParseDuration(businessDayOffset)
	if err != nil {
		log.Fatal(err, "BusinessDayStart - couldn't parse duration")
	}
	return currentTime.Add(-businessDay)
}
