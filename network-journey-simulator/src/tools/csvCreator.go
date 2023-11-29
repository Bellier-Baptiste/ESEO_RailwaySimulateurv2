/*
Package tools

File : csvCreator.go

Brief : This file contains the tools to create csv files.

Date : 24/01/2019

Author :
  - Team v1
  - Team v2
  - Paul TRÉMOUREUX (quality check)
  - Benoît VAVASSEUR

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
	"network-journey-simulator/src/configs"
	"os"
	"path/filepath"
	"strings"
	"time"
)

// var basePath = os.Getenv("GOPATH")

/*
CsvFile is a struct that represents a csv file.

Attributes :
  - path string : The path of the file
  - delimiter string : The delimiter of the file

Methods :
  - Write(content []string) : Writes a row in the csv file
  - WriteMultiple(contents [][]string) : Writes multiple rows in the csv file
*/
type CsvFile struct {
	path      string
	delimiter string
}

/*
check is a utility function that panics if the passed error is not nil.

Param :
  - e error : The error to check.
*/
func check(e error) {
	if e != nil {
		panic(e)
	}
}

/*
NewFileWithDelimiter creates a new csv file with a given name, columns and
delimiter.

Param :
  - name string : The name of the file
  - columns []string : The columns of the file
  - delimiter string : The delimiter of the file

Return :
  - CsvFile : The created file
*/
func NewFileWithDelimiter(name string, columns []string,
	delimiter string) CsvFile {
	return createFile(name, columns, delimiter)
}

/*
NewFile creates a new csv file with a given name and columns.

Param :
  - name string : The name of the file
  - columns []string : The columns of the file

Return :
  - CsvFile : The created file
*/
func NewFile(name string, columns []string) CsvFile {
	return createFile(name, columns, ",")
}

/*
createFile creates a new csv file in the 'output' directory and archives it in
a timestamped folder.
It writes the headers to the file if configured in the global config.

Param :
  - name string : The name of the file.
  - columns []string : The columns of the file.
  - delimiter string : The delimiter of the file.

Return :
  - CsvFile : The created and archived CsvFile instance.
*/
func createFile(name string, columns []string, delimiter string) CsvFile {
	currentPath, err := os.Getwd()
	check(err)

	basePath := strings.Replace(currentPath, "src\\main", "", -1)
	basePath = strings.Replace(basePath, "\\src", "", -1)
	basePath = strings.Replace(basePath, "\\configs", "", -1)
	basePath = strings.Replace(basePath, "\\models", "", -1)
	basePath = strings.Replace(basePath, "\\simulator", "", -1)
	basePath = strings.Replace(basePath, "\\tools", "", -1)

	outputFolderPath := filepath.Join(basePath, "output/")
	checkAndCreateDir(outputFolderPath)

	filePath := filepath.Join(outputFolderPath, name+".csv")
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
		writeCsvHeaders(file, columns, delimiter)
	}

	check(file.Chmod(0644))
	check(file.Sync())

	csv := CsvFile{path: filePath, delimiter: delimiter}

	archiveFile(filePath, basePath)

	return csv
}

/*
checkAndCreateDir checks for the existence of a directory at the given path and
creates it if it doesn't exist.

Param :
  - path string : The path of the directory to check and create.
*/
func checkAndCreateDir(path string) {
	exists, err := exists(path)
	check(err)
	if !exists {
		check(os.MkdirAll(path, 0777))
	}
}

/*
writeCsvHeaders writes the provided columns as headers to the given file using
the specified delimiter.

Param :
  - file *os.File : The file to write headers to.
  - columns []string : The headers to write.
  - delimiter string : The delimiter used in the CSV file.
*/
func writeCsvHeaders(file *os.File, columns []string, delimiter string) {
	header := strings.Join(columns, delimiter) + "\n"
	_, err := file.Write([]byte(header))
	check(err)
}

/*
archiveFile copies the specified file to an archive directory with a timestamp.

Param :
  - filePath string : The path of the file to archive.
  - basePath string : The base path of the project from which the archive
    directory is derived.
*/
func archiveFile(filePath, basePath string) {
	timestamp := time.Now().Format("2006-01-02_15-04")

	archivesBasePath := filepath.Join(basePath, "..", "archives")

	checkAndCreateDir(archivesBasePath)

	archiveFolderPath := filepath.Join(archivesBasePath, timestamp)

	exists, err := exists(archiveFolderPath)
	check(err)
	if exists {
		archiveFolderPath = incrementFolderName(archiveFolderPath)
	}
	check(os.MkdirAll(archiveFolderPath, 0777))

	archiveFilePath := filepath.Join(archiveFolderPath, filepath.Base(filePath))
	check(copyFile(filePath, archiveFilePath))
}

/*
incrementFolderName appends a numerical suffix to a folder name until
a non-existent name is found.

Param :
  - path string : The original folder path.

Return :
  - string : The new folder path with a unique numerical suffix.
*/
func incrementFolderName(path string) string {
	i := 2
	newPath := fmt.Sprintf("%s (%d)", path, i)
	for {
		exists, err := exists(newPath)
		check(err)
		if !exists {
			break
		}
		i++
		newPath = fmt.Sprintf("%s (%d)", path, i)
	}
	return newPath
}

/*
copyFile copies a file from the source path to the destination path.

Param :
  - src string : The source file path.
  - dst string : The destination file path.

Return :
  - error : Error if any occurred during the file copy.
*/
func copyFile(src, dst string) error {
	input, err := os.ReadFile(src)
	if err != nil {
		return err
	}
	return os.WriteFile(dst, input, 0644)
}

/*
Write writes a row in the csv file.

Param :
  - csv CsvFile : The csv file to write in
  - content []string : The content of the row
*/
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

/*
WriteMultiple writes multiple rows in the csv file.

Param :
  - csv CsvFile : The csv file to write in
  - contents [][]string : The content of the rows
*/
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

/*
exists checks if a file or directory exists at a given path.

Param :
  - path string : The path to check.

Return :
  - bool : True if the file or directory exists, false otherwise.
  - error : Error if any occurred during the check.
*/
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
