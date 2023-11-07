/*
Package tools

File : csvCreator.go

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
package tools

import (
	"fmt"
	"os"
	"path/filepath"
	"pfe-2018-network-journey-simulator/src/configs"
	"strings"
)

// var basePath = os.Getenv("GOPATH")
var projectPath = ""

type CsvFile struct {
	path      string
	delimiter string
}

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func NewFileWithDelimiter(name string, columns []string,
	delimiter string) CsvFile {
	return createFile(name, columns, delimiter)
}

func NewFile(name string, columns []string) CsvFile {
	return createFile(name, columns, ",")
}

func createFile(name string, columns []string, delimiter string) CsvFile {
	currentPath, err := os.Getwd()
	if err != nil {
		fmt.Println(err)
		os.Exit(1)
	}
	basePath := strings.Replace(currentPath, "src\\main", "", -1)
	basePath = strings.Replace(basePath, "\\src", "", -1)
	basePath = strings.Replace(basePath, "\\configs", "", -1)
	basePath = strings.Replace(basePath, "\\models", "", -1)
	basePath = strings.Replace(basePath, "\\simulator", "", -1)
	basePath = strings.Replace(basePath, "\\tools", "", -1)
	outputFolderPath := filepath.Join(basePath, "output/")

	outputExists, err := exists(outputFolderPath)

	check(err)

	if !outputExists {
		err := os.Mkdir(outputFolderPath, 0777)
		if err != nil {
			return CsvFile{}
		}
	}

	// Delete "src\main" if there (when launching simulator with command line)
	filePath := filepath.Join(basePath, projectPath, "output/"+name+".csv")

	file, err := os.Create(filePath)
	check(err)

	defer func(file *os.File) {
		err := file.Close()
		if err != nil {
			return
		}
	}(file)

	config := configs.GetInstance()

	check(err)

	if config.CsvHeaders() {

		firstRow := strings.Join(columns, delimiter)

		firstRow = firstRow + "\n"

		d1 := []byte(firstRow)

		_, err = file.Write(d1)
	}

	mode := 0644
	csvMode := os.FileMode(mode)

	err = file.Chmod(csvMode)
	if err != nil {
		return CsvFile{}
	}

	check(err)

	err = file.Sync()
	if err != nil {
		return CsvFile{}
	}

	csv := CsvFile{
		path:      filePath,
		delimiter: delimiter,
	}

	return csv
}

func (csv *CsvFile) Write(content []string) {

	file, err := os.OpenFile(csv.path, os.O_APPEND|os.O_WRONLY, os.ModeAppend)
	check(err)

	defer func(file *os.File) {
		err := file.Close()
		if err != nil {
			return
		}
	}(file)

	//fmt.Println("file opened")

	rowToAdd := ""

	for i := range content {
		rowToAdd += content[i]
		if i != len(content)-1 {
			rowToAdd += csv.delimiter
		}
	}

	//rowToAdd := strings.Join(content, csv.delimiter)

	rowToAdd = rowToAdd + "\n"

	_, err = file.WriteString(rowToAdd)

	//fmt.Println("wat")

	check(err)

}

func (csv *CsvFile) WriteMultiple(contents [][]string) {
	file, err := os.OpenFile(csv.path, os.O_APPEND|os.O_WRONLY, os.ModeAppend)
	check(err)

	defer func(file *os.File) {
		err := file.Close()
		if err != nil {
			return
		}
	}(file)

	//fmt.Println("file opened")

	var rowsToAdd = ""

	for i := range contents {
		rowToAdd := ""
		for j := range contents[i] {
			rowToAdd += contents[i][j]
			if j != len(contents[i]) {
				rowToAdd += csv.delimiter
			}
		}
		//rowsToAdd += strings.Join(contents[i], csv.delimiter) + "\n"
		//TODO this is slowing the whole thing down
	}

	_, err = file.WriteString(rowsToAdd)

	//fmt.Println("wat")

	check(err)

}

func exists(path string) (bool, error) {
	_, err := os.Stat(path)
	if err == nil {
		return true, nil
	}
	if os.IsNotExist(err) {
		return false, nil
	}
	return true, err
}
