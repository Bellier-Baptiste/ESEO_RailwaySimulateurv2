/*
Package tools

File : archiver.go

Brief : This file contains the tools to archive files

Date : 11/2023

Author :
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
	"os"
	"path/filepath"
	"time"
)

/*
ArchiveOutputAndConfigFiles archives the output and config files of the
simulator.

The output and config files are archived in the 'archives' folder at the root
of the project. The archive folder is named with the current timestamp at the
end of the simulation.

Param :
  - basePath string : The base path of the project
*/
func ArchiveOutputAndConfigFiles(basePath string) {
	outputFolderPath := filepath.Join(basePath, "network-journey-simulator",
		"output")
	configFolderPath := filepath.Join(basePath, "network-journey-simulator",
		"src", "configs")
	archivesFolderPath := filepath.Join(basePath, "archives")

	timestamp := time.Now().Format("2006-01-02_15-04")
	archiveFolderPath := filepath.Join(archivesFolderPath, timestamp)

	if exists, _ := exists(archiveFolderPath); exists {
		archiveFolderPath = incrementFolderName(archiveFolderPath)
	}
	checkAndCreateDir(archiveFolderPath)

	ArchiveDirectory(outputFolderPath, archiveFolderPath)
	ArchiveSpecificFiles(configFolderPath, archiveFolderPath,
		[]string{"runThisSimulation.xml", "config.json"})
}

/*
ArchiveDirectory archives all the files of a directory.

Param :
  - sourceDir string : The path of the directory to archive
  - destinationDir string : The path of the archive directory
*/
func ArchiveDirectory(sourceDir, destinationDir string) {
	files, err := os.ReadDir(sourceDir)
	check(err)

	for _, file := range files {
		if !file.IsDir() {
			sourceFilePath := filepath.Join(sourceDir, file.Name())
			destinationFilePath := filepath.Join(destinationDir, file.Name())
			fmt.Printf("Archivage de %s vers %s\n", sourceFilePath,
				destinationFilePath)
			check(copyFile(sourceFilePath, destinationFilePath))
		}
	}
}

/*
ArchiveSpecificFiles archives specific files of a directory.

Param :
  - sourceDir string : The path of the directory to archive
  - destinationDir string : The path of the archive directory
  - fileNames []string : The names of the files to archive
*/
func ArchiveSpecificFiles(sourceDir, destinationDir string,
	fileNames []string) {
	for _, fileName := range fileNames {
		sourceFilePath := filepath.Join(sourceDir, fileName)
		destinationFilePath := filepath.Join(destinationDir, fileName)
		fmt.Printf("Archivage de %s vers %s\n", sourceFilePath,
			destinationFilePath)
		err := copyFile(sourceFilePath, destinationFilePath)
		if err != nil {
			fmt.Printf("Erreur lors de l'archivage du fichier %s: %v\n",
				fileName, err)
		}
	}
}

/*
GetBasePath returns the base path of the project.

Return :
  - string : The base path of the project
*/
func GetBasePath() string {
	currentPath, err := os.Getwd()
	check(err)
	basePath := filepath.Dir(currentPath)
	return basePath
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
