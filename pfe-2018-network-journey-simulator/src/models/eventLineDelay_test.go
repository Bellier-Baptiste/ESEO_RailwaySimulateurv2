/*
File : eventLineDelay_test.go

Brief : eventLineDelay_test.go runs tests on the eventLineDelay.go file.

Date : 10/02/2020

Author :
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
	"fmt"
	//	"log"
	//	"strconv"
	"testing"
	"time"
	//	"github.com/stretchr/testify/assert"
)

/*
TestEventLineDelay tests the EventLineDelay struct and its methods.

# It tests if the struct and its methods work properly

Input : t *testing.T

Expected : The struct and its methods work properly
*/
func TestEventLineDelay(t *testing.T) {
	eventld := NewEventLineDelay(0, 1, 1, time.Now(), time.Now().Add(10))
	fmt.Println(eventld.Finished())
}
