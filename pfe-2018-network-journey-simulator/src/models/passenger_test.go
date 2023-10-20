/*
File : passenger_test.go

Brief :

Date : N/A

Author : Team v2, Paul TRÉMOUREUX (quality check)

License : MIT License

Copyright (c) 2023 Équipe PFE_2023_16

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package models

import (
	"github.com/stretchr/testify/assert"
	"testing"
	"time"
)

/*
Unused function
Generate a random passenger for testing purposes
func generate_passenger() *Passenger {
	return &Passenger{id: "12345", kind: ADL}
}
*/

func TestCreatePassenger(t *testing.T) {
	someone := NewPassenger("abcd", ADL)
	assert.Equal(t, "abcd", someone.Id())
	assert.Nil(t, someone.Trips())
}

func TestPassenger_GetTrips(t *testing.T) {
	someone := NewPassenger("abcd", ADL)
	var n = NewTrip(time.Time{}, PathStation{})
	someone.AddTrip(&n)
	var trips = someone.trips
	assert.Equal(t, &trips[0], &someone.trips[0])
	assert.Equal(t, &trips, &someone.trips)
}

func TestPassenger_CalculateNextTrip(t *testing.T) {
	someone := NewPassenger("abcd", ADL)
	var n1 = NewTrip(time.Now(), PathStation{})
	someone.AddTrip(&n1)
	var timeNow = time.Now()
	var n2 = NewTrip(timeNow.Add(time.Minute), PathStation{})
	someone.AddTrip(&n2)

	someone.calculateNextTrip()

	assert.NotNil(t, someone.nextTrip)
	assert.Equal(t, someone.nextTrip.departureTime.String(), timeNow.String())

	someone.nextTrip.SetArrivalTime(timeNow)
	someone.calculateNextTrip()
	assert.Equal(t, someone.nextTrip.departureTime.String(), timeNow.Add(time.Minute).String())
}
