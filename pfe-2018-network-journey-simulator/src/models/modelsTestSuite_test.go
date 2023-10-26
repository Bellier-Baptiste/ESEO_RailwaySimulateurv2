package models

import (
	"github.com/stretchr/testify/suite"
	"testing"
)

type ModelsTestSuite struct {
	suite.Suite
}

func TestModelsTestSuite(t *testing.T) {
	suite.Run(t, new(ModelsTestSuite))
}
