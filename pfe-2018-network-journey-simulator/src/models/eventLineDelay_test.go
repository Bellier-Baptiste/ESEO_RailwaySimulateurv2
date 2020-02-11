package models

import (
	"fmt"
//	"log"
//	"strconv"
	"testing"
	"time"
	
//	"github.com/stretchr/testify/assert"
)

func TestEventLineDelay(t *testing.T) {
	eventld := NewEventLineDelay(0, 1, 1, time.Now(), time.Now().Add(10))
	fmt.Println(eventld.Finished())
}


