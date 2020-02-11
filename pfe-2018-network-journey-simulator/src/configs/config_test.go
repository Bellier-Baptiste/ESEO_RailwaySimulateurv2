package configs

import (
	"testing"
)
import "github.com/stretchr/testify/assert"

func TestConfig_changeParam(t *testing.T){
	config := GetInstance()

	assert.NotNil(t, config.Population())
	var before = config.Population()
	config.ChangeParam("population",before+10)
	assert.Equal(t, config.Population(),before+10 )
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
