/*
 *
 * Author :
 * 	- Benoît VAVASSEUR
 *
 * License : MIT License
 *
 * Copyright (c) 2023 Team PFE_2023_16
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *  IN THE SOFTWARE.
 */

package models

/*
DestinationDistribution is the structure that manage the destination
distribution of an area.

Attributes :
  - commercial int : the percentage of commercial destination in the area
  - educational int : the percentage of educational destination in the area
  - industrial int : the percentage of industrial destination in the area
  - leisure int : the percentage of leisure destination in the area
  - office int : the percentage of office destination in the area
  - residential int : the percentage of residential destination in the area
  - touristic int : the percentage of touristic destination in the area

Methods :
  - NewDestinationDistribution(commercial, educational, industrial,
    leisure, office, residential, touristic int) DestinationDistribution :
    create a new DestinationDistribution
  - Commercial() int : return the percentage of commercial destination in
    the area
  - Educational() int : return the percentage of educational destination in
    the area
  - Industrial() int : return the percentage of industrial destination in
    the area
  - Leisure() int : return the percentage of leisure destination in the
    area
  - Office() int : return the percentage of office destination in the area
  - Residential() int : return the percentage of residential destination
    in the area
  - Touristic() int : return the percentage of touristic destination in
    the area
*/
type DestinationDistribution struct {
	commercial  int
	educational int
	industrial  int
	leisure     int
	office      int
	residential int
	touristic   int
}

/*
NewDestinationDistribution creates a new DestinationDistribution.

It takes the percentage of people in each category as parameters.

It returns a DestinationDistribution.
*/

func NewDestinationDistribution(commercial, educational, industrial,
	leisure, office, residential, touristic int) DestinationDistribution {
	return DestinationDistribution{
		commercial:  commercial,
		educational: educational,
		industrial:  industrial,
		leisure:     leisure,
		office:      office,
		residential: residential,
		touristic:   touristic,
	}
}

func (d *DestinationDistribution) Commercial() int {
	return d.commercial
}

func (d *DestinationDistribution) Educational() int {
	return d.educational
}

func (d *DestinationDistribution) Industrial() int {
	return d.industrial
}

func (d *DestinationDistribution) Leisure() int {
	return d.leisure
}

func (d *DestinationDistribution) Office() int {
	return d.office
}

func (d *DestinationDistribution) Residential() int {
	return d.residential
}

func (d *DestinationDistribution) Touristic() int {
	return d.touristic
}
