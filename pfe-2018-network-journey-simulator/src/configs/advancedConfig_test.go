package configs

import (
	"fmt"
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestGetAdvancedConfigInstance(t *testing.T) {
	var config = GetAdvancedConfigInstance()
	assert.NotNil(t, config)

	var mapC = config.MapC
	assert.NotNil(t, mapC)
	fmt.Printf("%+v\n", mapC)

	assert.True(t, len(mapC.Lines) > 0)
	assert.True(t, len(mapC.Stations) > 0)

	for _, station := range mapC.Stations {
		assert.NotNil(t, station.Name)
	}
}

func TestAdvancedConfig_CheckRelations(t *testing.T) {
	var config = GetAdvancedConfigInstance()
	assert.NotNil(t, config)

	assert.Nil(t, config.CheckRelations())
}

func TestAdvancedConfig_ReattributeIds(t *testing.T) {
	var config = GetAdvancedConfigInstance()
	assert.NotNil(t, config)

	config.ReattributeIds()
	assert.Nil(t, config.CheckRelations())
	fmt.Printf("%+v\n", config.MapC)
}