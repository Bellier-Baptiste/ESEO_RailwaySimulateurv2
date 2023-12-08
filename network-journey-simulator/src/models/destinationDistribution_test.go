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
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package models

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestDestinationDistribution(t *testing.T) {
	var dd = NewDestinationDistribution(10, 10, 10,
		10, 10, 40, 10)
	assert.Equal(t, 10, dd.Commercial())
	assert.Equal(t, 10, dd.Educational())
	assert.Equal(t, 10, dd.Industrial())
	assert.Equal(t, 10, dd.Leisure())
	assert.Equal(t, 10, dd.Office())
	assert.Equal(t, 40, dd.Residential())
	assert.Equal(t, 10, dd.Touristic())
}
