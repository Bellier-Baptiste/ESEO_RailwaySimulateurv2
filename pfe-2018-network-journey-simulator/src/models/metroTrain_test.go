/*
File : metroTrain_test.go

Brief : metroTrain_test.go runs tests on the metroTrain.go file.

Date : 24/01/2019

Author :
  - Team v1
  - Team v2
  - Paul TRÉMOUREUX (quality check)

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
)

/*
generateTrain generates a MetroTrain struct.

Return :
  - *MetroTrain : the generated MetroTrain struct
*/
func generateTrain() *MetroTrain {
	return &MetroTrain{
		id:       1,
		capacity: 120,
		line:     &MetroLine{id: 1, name: "Trafalgar"},
	}
}

/*
TestMetroTrain_StationsBeforeReturn tests the StationsBeforeReturn method of
the MetroTrain struct.

# It tests if the method works properly

Input : t *testing.T

Expected : The method works properly
*/
func TestMetroTrain_StationsBeforeReturn(t *testing.T) {
	mapTest := generateMapSimpleLine()

	trainTest := MetroTrain{
		id:          1,
		line:        mapTest.lines[0],
		nextStation: mapTest.lines[0].stations[1],
		direction:   "up",
	}

	assert.Equal(t, 2, len(trainTest.StationsBeforeReturn()))
	assert.Equal(t, mapTest.lines[0].stations[1].id, trainTest.StationsBeforeReturn()[0].id)
	assert.Equal(t, mapTest.lines[0].stations[2].id, trainTest.StationsBeforeReturn()[1].id)

	trainTest.direction = "down"
	stations := trainTest.StationsBeforeReturn()
	assert.Equal(t, 2, len(stations))
	assert.Equal(t, mapTest.lines[0].stations[1].id, stations[0].id)
	assert.Equal(t, mapTest.lines[0].stations[0].id, stations[1].id)
}
