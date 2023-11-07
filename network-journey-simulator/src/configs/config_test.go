/*
File : config_test.go

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
package configs

import (
	"testing"
)
import "github.com/stretchr/testify/assert"

func TestConfig_changeParam(t *testing.T) {
	config := GetInstance()

	assert.NotNil(t, config.Population())
	var before = config.Population()
	config.ChangeParam("population", before+10)
	assert.Equal(t, config.Population(), before+10)
}

//Commented this out, since the config is being checked on the first getInstance
//func TestConfig_Check(t *testing.T) {
//
//	correct, err := configuration.Check()
//	if !correct {
//		assert.EqualError(t, err, " Not enough names for lines Not enough names for stations There is zero train per lines")
//	}
//
//}
