package models

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

/*
Unused function
func assertEqualFloat(t *testing.T, expected float64, actual float64, allowedError float64, message string) {
	if math.Abs(expected-actual) < allowedError {
		return
	} else {
		message += fmt.Sprintf("%v expected, got %v", expected, actual)
		t.Fatal(message)
	}
}
*/

func TestEquals(t *testing.T) {
	points := []Point{
		{0, 0},
		{0, 0},
		{3, 4},
		{0, 5},
		{5, 0},
	}

	assert.True(t, points[0].equals(points[1]))
	assert.False(t, points[0].equals(points[2]))
}
