package tools

import (
	"go/build"
	"os"
	"path/filepath"
	"configs"
	"strings"
)

var basePath = os.Getenv("GOPATH")
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

func NewFileWithDelimiter(name string, columns []string, delimiter string) CsvFile {
	return createFile(name, columns, delimiter)
}

func NewFile(name string, columns []string) CsvFile {
	return createFile(name, columns, ",")
}

func createFile(name string, columns []string, delimiter string) CsvFile {

	if basePath == "" {
		basePath = build.Default.GOPATH
	}

	outputFolderPath := filepath.Join(basePath, projectPath, "output/")

	outputExists, err := exists(outputFolderPath)

	check(err)

	if !outputExists {
		os.Mkdir(outputFolderPath, 0777)
	}

	filePath := filepath.Join(basePath, projectPath, "output/"+name+".csv")

	file, err := os.Create(filePath)
	check(err)

	defer file.Close()

	config := configs.GetInstance()

	check(err)

	if config.CsvHeaders() {

		firstRow := strings.Join(columns, delimiter)

		firstRow = firstRow + "\n"

		d1 := []byte(firstRow)

		_, err = file.Write(d1)
	}

	mode := int(0644)
	csvMode := os.FileMode(mode)

	file.Chmod(csvMode)

	check(err)

	file.Sync()

	csv := CsvFile{
		path:      filePath,
		delimiter: delimiter,
	}

	return csv
}

func (csv *CsvFile) Write(content []string) {

	file, err := os.OpenFile(csv.path, os.O_APPEND|os.O_WRONLY, os.ModeAppend)
	check(err)

	defer file.Close()

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

	defer file.Close()

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
		//rowsToAdd += strings.Join(contents[i], csv.delimiter) + "\n"//TODO this is slowing the whole thing down
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
