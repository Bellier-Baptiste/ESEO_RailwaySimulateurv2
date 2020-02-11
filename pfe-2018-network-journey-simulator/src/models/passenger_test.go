package models

import (
	"github.com/stretchr/testify/assert"
	"testing"
	"time"
)

// Generate a random passenger for testing purposes
func generate_passenger() *Passenger{
	return &Passenger{id:"12345",kind:ADL}
}

func TestCreatePassenger(t *testing.T) {
	someone := NewPassenger("abcd",ADL)
	assert.Equal(t,"abcd",someone.Id())
	assert.Nil(t,someone.Trips())
}

func TestPassenger_GetTrips(t *testing.T) {
	someone := NewPassenger("abcd",ADL)
	var n = NewTrip(time.Time{}, pathStation{})
	someone.AddTrip(&n)
	var trips = someone.trips
	assert.Equal(t, &trips[0], &someone.trips[0])
	assert.Equal(t, &trips, &someone.trips)
}

func TestPassenger_CalculateNextTrip(t *testing.T){
	someone := NewPassenger("abcd",ADL)
	var n1 = NewTrip(time.Now(), pathStation{})
	someone.AddTrip(&n1)
	var timeNow = time.Now()
	var n2 = NewTrip(timeNow.Add(time.Minute), pathStation{})
	someone.AddTrip(&n2)

	someone.calculateNextTrip()

	assert.NotNil(t, someone.nextTrip)
	assert.Equal(t, someone.nextTrip.departureTime.String(), timeNow.String())

	someone.nextTrip.SetArrivalTime(timeNow)
	someone.calculateNextTrip()
	assert.Equal(t, someone.nextTrip.departureTime.String(), timeNow.Add(time.Minute).String())
}