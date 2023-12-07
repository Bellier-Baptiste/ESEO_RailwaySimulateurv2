/*
File : point_test.go

Brief : point_test.go runs tests on the point.go file.

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

/*
TestEquals tests the equals method of the Point struct.

# It tests if the method works properly

Input : t *testing.T

Expected : The method works properly
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