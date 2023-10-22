/*
Package main

File : metro_simulator.go

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
package main

import (
	"bufio"
	"flag"
	"fmt"
	"log"
	"math"
	"os"
	"path/filepath"
	"pfe-2018-network-journey-simulator/src/configs"
	"pfe-2018-network-journey-simulator/src/models"
	"pfe-2018-network-journey-simulator/src/simulator"
	"strings"
	"time"
)

const (
	dayTypeWD string = "working day"
	dayTypeWE string = "week-end"
	dayTypeBH string = "bank holiday"
	dayTypeHD string = "holidays"
)

func printHeader(text string) {
	var charFiller = "-"
	var length = 51
	if text == "" {
		fmt.Println(strings.Repeat(charFiller, length))
		return
	}

	if len(text) > length {
		fmt.Println(charFiller+charFiller+" ", text)
	} else {
		var middle = float64(length-4-len(text)) / float64(2)
		var before = int(math.Floor(middle))
		var after = int(math.Ceil(middle))
		fmt.Println(strings.Repeat(charFiller, before), " "+text+" ",
			strings.Repeat(charFiller, after))
	}
}

func createAdvancedConfig(name string) {
	var mapObject, err = models.CreateMap()
	if err != nil {
		log.Fatal("failed to create map", err)
	}

	adConfig := mapObject.ExportMapToAdConfig()

	err = adConfig.SaveXML(name)
	if err != nil {
		log.Fatal("failed to save config", err)
	}
	fmt.Println("new config created and saved with the name ", name)
}

/*
dayTyperAttribution return a dayType depending on the value of the parameter

Param :
ans string : input of the user

Return :
string : the dayType corresponding to ans
  - week-day if 1
  - weekend if 2
  - bank holiday if 3
  - holiday if 4
*/
func dayTypeAttribution(ans string) string {
	if len(ans) > 0 {
		if strings.HasPrefix(ans, "1") {
			fmt.Println("Thanks")
			return dayTypeWD
		} else if strings.HasPrefix(ans, "2") {
			fmt.Println("Thanks")
			return dayTypeWE
		} else if strings.HasPrefix(ans, "3") {
			fmt.Println("Thanks")
			return dayTypeBH
		} else if strings.HasPrefix(ans, "4") {
			fmt.Println("Thanks")
			return dayTypeHD
		} else {
			fmt.Println("Wrong answer, default value has been attribued")
			return dayTypeWD
		}
	} else {
		fmt.Println("Wrong answer, default value has been attribued")
		return dayTypeWD
	}
}

/*
checkFinishedTrips is used to check how many trips are finished or not
in the simulation

Param :
sim *simulator.Simulator : the current simulation

Return :
int : the number of trips not finished (tripsNotFinished)
int : the number of trips finished (tripsFinished)
*/
func checkFinishedTrips(sim *simulator.Simulator) (int, int) {
	var tripsNotFinished = 0
	var tripsFinished = 0
	for _, passenger := range sim.Population().Passengers() {
		for _, trip := range passenger.Trips() {
			if !trip.IsCompleted() {
				tripsNotFinished++
			} else {
				tripsFinished++
			}
		}
	}
	return tripsNotFinished, tripsFinished
}

func main() {
	//flags
	reader := bufio.NewReader(os.Stdin)
	printHeader("Metro Simulator")
	printHeader("")
	fmt.Print("\n\n")
	fmt.Println("HELLO WORLD")

	regenerateConfig := flag.Bool("regenconfig", false,
		"use it to generate the map_config.xml from the config.json file.")
	configName := flag.String("configname", "map_config.xml",
		"name of the config to create / use. the file must be in ./configs")
	runTurns := flag.Int("maxruns", -1,
		"number of singular events (train arriving in station) to "+
			"execute. -1 to run until the end")
	withVerbose := flag.Bool("verbose", false,
		"to have details printed during the run")

	flag.Parse()

	//change the verbose
	var config = configs.GetInstance()
	config.ChangeParam("print debug", *withVerbose)

	currentPath, err := os.Getwd()
	if err != nil {
		fmt.Println(err)
		os.Exit(1)
	}
	// Delete "src\main" if there (when launching newSimulator with command line)
	basePath := strings.Replace(currentPath, "src\\main", "", -1)

	var advancedConfigPath = filepath.Join(basePath, "src\\configs\\", *configName)

	printHeader("Verifying Config")
	if *regenerateConfig {
		fmt.Println("Creating new config file...")
		createAdvancedConfig(*configName)
	}

	//verify if an advanced config file is present
	if _, err := os.Stat(advancedConfigPath); os.IsNotExist(err) {
		fmt.Println("No advanced config file found (", advancedConfigPath,
			"), do you want to generate one ? y/n")
		ans, _ := reader.ReadString('\n')
		if len(ans) > 0 && strings.HasPrefix(strings.ToUpper(ans), "Y") {
			fmt.Println("Creating new config file...")
			createAdvancedConfig(*configName)
		} else {
			fmt.Println("exiting...")
			os.Exit(1)
		}
	} else {
		fmt.Println("advanced config file found (", advancedConfigPath,
			"), running with it.")
	}

	configs.InitAdvancedConfigInstance(*configName)
	adConfig := configs.GetAdvancedConfigInstance()

	fmt.Println("\n-- from config.json")
	fmt.Println("Number of passengers : ", config.Population())
	fmt.Println("Time start : ", config.TimeStart().String())
	fmt.Println("Time end : ", config.TimeEnd().String())

	fmt.Println("\n-- from " + *configName)
	fmt.Println("Number of stations : ", len(adConfig.MapC.Stations))
	fmt.Println("Number of lines : ", len(adConfig.MapC.Lines))

	//the user choose the type of day
	fmt.Println("Please, choose a day type beyond the following list " +
		"(enter the corresponding number) :")
	fmt.Println("1 : working day")
	fmt.Println("2 : week-end")
	fmt.Println("3 : bank holiday")
	fmt.Println("4 : holidays")
	ans, _ := reader.ReadString('\n')
	var dayType string
	dayType = dayTypeAttribution(ans)

	printHeader("Initializing Simulation")
	startTime := time.Now()
	newSimulator := simulator.NewSimulator()
	var output bool
	output, err = newSimulator.Init(dayType)
	fmt.Println("NewSimulator =", output)
	if err != nil {
		fmt.Println(err)
	}

	elapsedTime := time.Since(startTime)
	fmt.Println("init time : ", elapsedTime.String())

	printHeader("Running Simulation")
	startTime = time.Now()
	newSimulator.Run(*runTurns)
	runTime := time.Since(startTime)
	fmt.Println("\nrun time : ", runTime.String())

	printHeader("Saving results")
	newSimulator.ToCSV()

	elapsedTime = time.Since(startTime)

	tripsNotFinished, tripsFinished := checkFinishedTrips(newSimulator)

	fmt.Println("")
	printHeader("Simulation completed")
	fmt.Println("run time : ", runTime.String())
	fmt.Println("trips completed : ", tripsFinished)
	fmt.Println("trips not completed :", tripsNotFinished)
}
