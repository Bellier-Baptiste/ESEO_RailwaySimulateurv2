/*
Package models

File : point.go

Brief :

Date : N/A

Author : Team v1, Team v2, Paul TRÉMOUREUX (quality check)

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
	"math"
)

type Point struct {
	x float64
	y float64
}

/*
Unused function
func NewPoint(x float64, y float64) Point {
	var point Point
	point.setX(x)
	point.setY(y)
	return point
}
*/

func (p *Point) X() float64 {
	return p.x
}

func (p *Point) setX(x float64) {
	p.x = x
}

func (p *Point) Y() float64 {
	return p.y
}

func (p *Point) setY(y float64) {
	p.y = y
}

/*
DistanceTo return the distance in meters
*/
func (p *Point) DistanceTo(p2 Point) float64 {
	var lat1 = p.y * math.Pi / 180
	var lon1 = p.x * math.Pi / 180
	var lat2 = p2.y * math.Pi / 180
	var lon2 = p2.x * math.Pi / 180
	var R = float64(6371)
	var d = R * math.Acos(math.Cos(lat1)*math.Cos(lat2)*
		math.Cos(lon2-lon1)+math.Sin(lat1)*math.Sin(lat2))

	return d * 1000
}

func (p *Point) equals(p2 Point) bool {
	return p.x == p2.x && p.y == p2.y
}
