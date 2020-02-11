package main

import (
	"bufio"
	"flag"
	"fmt"
	"go/build"
	"log"
	"math"
	"os"
	"path/filepath"
	"configs"
	"models"
	"simulator"
	"strings"
	"time"
)

func main() {

	regenerateConfig := flag.Bool("regenconfig", false, "use it to generate the map_config.xml from the config.json file.")
	configName := flag.String("configname", "map_config.xml", "name of the config to create / use. the file must be in ./configs")
	runTurns := flag.Int("maxruns", -1, "number of singular events (train arriving in station) to execute. -1 to run until the end")
	withVerbose := flag.Bool("verbose", false, "to have details printed during the run")

	flag.Parse()

	//change the verbose
	var config = configs.GetInstance()
	config.ChangeParam("print debug", *withVerbose)

	basePath := os.Getenv("GOPATH")
	if basePath == "" {
		basePath = build.Default.GOPATH
	}

	var advancedConfigPath = filepath.Join(basePath, "src/configs/", *configName)

	reader := bufio.NewReader(os.Stdin)
	printHeader("Metro Simulator")
	printHeader("")
	fmt.Print("\n\n")

	printHeader("Verifying Config")
	if *regenerateConfig {
		fmt.Println("Creating new config file...")
		createAdvancedConfig(*configName)
	}

	//verify if an advanced config file is present
	if _, err := os.Stat(advancedConfigPath); os.IsNotExist(err) {
		fmt.Println("No advanced config file found (", advancedConfigPath, "), do you want to generate one ? y/n")
		ans, _ := reader.ReadString('\n')
		if len(ans) > 0 && strings.HasPrefix(strings.ToUpper(ans), "Y") {
			fmt.Println("Creating new config file...")
			createAdvancedConfig(*configName)
		} else {
			fmt.Println("exiting...")
			os.Exit(1)
		}
	} else {
		fmt.Println("advanced config file found (", advancedConfigPath, "), running with it.")
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
	fmt.Println("Please, choose a day type beyond the following list (enter the corresponding number) :")
	fmt.Println("1 : working day")
	fmt.Println("2 : week-end")
	fmt.Println("3 : bank holiday")
	fmt.Println("4 : holidays")
	ans, _ := reader.ReadString('\n')
	var dayType string
	if len(ans) > 0 {
		if strings.HasPrefix(ans, "1") {
			fmt.Println("Thanks")
			dayType = "working day"
		} else if strings.HasPrefix(ans, "2") {
			fmt.Println("Thanks")
			dayType = "week-end"
		} else if strings.HasPrefix(ans, "3") {
			fmt.Println("Thanks")
			dayType = "bank holiday"
		} else if strings.HasPrefix(ans, "4") {
			fmt.Println("Thanks")
			dayType = "holidays"
		} else {
			fmt.Println("Wrong answer, default value has been attribued")
			dayType = "working day"
		}
	}else {
		fmt.Println("Wrong answer, default value has been attribued")
		dayType = "working day"
	}

	printHeader("Initializing Simulation")
	startTime := time.Now()
	simulator := simulator.NewSimulator()
	simulator.Init(dayType)

	elapsedTime := time.Since(startTime)
	fmt.Println("init time : ", elapsedTime.String())

	printHeader("Running Simulation")
	startTime = time.Now()
	simulator.Run(*runTurns)
	runTime := time.Since(startTime)
	fmt.Println("\nrun time : ", runTime.String())

	printHeader("Saving results")
	simulator.ToCSV()

	elapsedTime = time.Since(startTime)

	var tripsNotFinished = 0
	var tripsFinished = 0
	for _, passenger := range simulator.Population().Passengers() {
		for _, trip := range passenger.Trips() {
			if !trip.IsCompleted() {
				tripsNotFinished++
			} else {
				tripsFinished++
			}
		}
	}

	fmt.Println("")
	printHeader("Simulation completed")
	fmt.Println("run time : ", runTime.String())
	fmt.Println("trips completed : ", tripsFinished)
	fmt.Println("trips not completed :", tripsNotFinished)
}

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
		fmt.Println(strings.Repeat(charFiller, before), " "+text+" ", strings.Repeat(charFiller, after))
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
