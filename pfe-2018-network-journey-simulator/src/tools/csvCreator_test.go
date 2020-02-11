package tools

import (
	"testing"
)

func TestCsvCreator(t *testing.T) {

	testCsv := NewFile("test", []string{"test1", "test2"})

	testCsv.Write([]string{"on test", "dur"})
}
