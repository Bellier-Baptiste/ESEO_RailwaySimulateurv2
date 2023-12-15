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
	"os"
	"path/filepath"
	"testing"
	"time"
)

/*
TestArchiveOutputAndConfigFiles tests the ArchiveOutputAndConfigFiles function.

# It tests if the output and config files are archived properly

Input : t *testing.T

Expected : The output and config files are archived properly
*/
func TestArchiveOutputAndConfigFiles(t *testing.T) {
	basePath, err := setupTestEnvironment()
	if err != nil {
		t.Fatalf("Error when configuring the test environment: %v", err)
	}
	defer func(basePath string) {
		err := teardownTestEnvironment(basePath)
		if err != nil {

		}
	}(basePath)

	ArchiveOutputAndConfigFiles(basePath)

	archivesFolderPath := filepath.Join(basePath, "archives")
	timestamp := time.Now().Format("2006-01-02_15-04")
	archiveFolderPath := filepath.Join(archivesFolderPath, timestamp)

	if _, err := os.Stat(archiveFolderPath); os.IsNotExist(err) {
		t.Fatalf("The archive folder %s was not created",
			archiveFolderPath)
	}

	archivedFile := filepath.Join(archiveFolderPath, "config.json")
	if _, err := os.Stat(archivedFile); os.IsNotExist(err) {
		t.Errorf("Le fichier %s n'a pas été archivé", archivedFile)
	}
}

/*
TestIncrementFolderName tests the incrementFolderName function.

# It tests if the folder name is incremented properly

Input : t *testing.T

Expected : The folder name is incremented properly
*/
func TestIncrementFolderName(t *testing.T) {
	basePath, err := setupTestEnvironment()
	if err != nil {
		t.Fatalf("Error setting up test environment: %v", err)
	}
	defer func(basePath string) {
		err := teardownTestEnvironment(basePath)
		if err != nil {

		}
	}(basePath)

	originalPath := filepath.Join(basePath, "testFolder")
	err = os.Mkdir(originalPath, 0777)
	if err != nil {
		return
	}

	incrementedPath := incrementFolderName(originalPath)

	if incrementedPath == originalPath {
		t.Errorf("IncrementFolderName did not change the folder name")
	}

}

/*
TestGetBasePath tests the GetBasePath function.

# It tests if the base path is returned properly

Input : t *testing.T

Expected : The base path is returned properly
*/
func TestGetBasePath(t *testing.T) {
	expectedBasePath, err := os.Getwd()
	if err != nil {
		t.Fatalf("Error getting current directory: %v", err)
	}
	expectedBasePath = filepath.Dir(expectedBasePath)

	actualBasePath := GetBasePath()

	if actualBasePath != expectedBasePath {
		t.Errorf("GetBasePath returned %s; want %s", actualBasePath,
			expectedBasePath)
	}
}

/*
setupTestEnvironment creates a temporary environment for the tests.

Return :
  - basePath string : The path of the temporary environment
  - error : The error that occurred during the creation of the environment
*/
func setupTestEnvironment() (string, error) {
	basePath, err := os.MkdirTemp("", "testProject")
	if err != nil {
		return "", err
	}

	outputDir := filepath.Join(basePath, "network-journey-simulator", "output")
	configDir := filepath.Join(basePath, "network-journey-simulator", "src",
		"configs")
	if err := os.MkdirAll(outputDir, 0755); err != nil {
		return basePath, err
	}
	if err := os.MkdirAll(configDir, 0755); err != nil {
		return basePath, err
	}

	if err := os.WriteFile(filepath.Join(outputDir, "testOutput.csv"),
		[]byte("test data"), 0644); err != nil {
		return basePath, err
	}
	if err := os.WriteFile(filepath.Join(configDir, "runThisSimulation.xml"),
		[]byte("<xml></xml>"), 0644); err != nil {
		return basePath, err
	}
	if err := os.WriteFile(filepath.Join(configDir, "config.json"),
		[]byte("{}"), 0644); err != nil {
		return basePath, err
	}

	return basePath, nil
}

/*
teardownTestEnvironment deletes the temporary environment for the tests.

Param :
  - basePath string : The path of the temporary environment

Return :
  - error : The error that occurred during the deletion of the environment
*/
func teardownTestEnvironment(basePath string) error {
	return os.RemoveAll(basePath)
}
