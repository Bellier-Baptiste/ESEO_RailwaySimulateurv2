package models

import (
	"math"
)

type Point struct {
	x float64
	y float64
}

func NewPoint(x float64, y float64) Point {
	var point Point
	point.setX(x)
	point.setY(y)
	return point
}

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

//return the distance in meters
func (p Point) DistanceTo(p2 Point) float64 {
	var lat1 = p.y * math.Pi / 180
	var	lon1 = p.x * math.Pi / 180
	var lat2 = p2.y * math.Pi / 180
	var lon2 = p2.x* math.Pi / 180
	var R = float64(6371)
	var d = R * math.Acos(math.Cos(lat1) * math.Cos(lat2) * math.Cos(lon2 - lon1) + math.Sin(lat1) * math.Sin(lat2))

	return d*1000
}

func (p Point) equals(p2 Point) bool {
	return p.x == p2.x && p.y == p2.y
}
