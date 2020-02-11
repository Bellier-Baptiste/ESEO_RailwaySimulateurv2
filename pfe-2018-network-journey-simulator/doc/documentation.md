# PFE METRO SIMULATOR

## Introduction

The aim of the project is to simulate a metro and more broadly a railway network, to obtain life-like data, usable in NAIA. This to allow experiments with rare scenarios and an ease to demonstrate NAIA capabilities.

It is written in Go. It's relies on classes to describe 
aspects of the network  (e.g a 
Train, a line, a station, etc). 
Beneath, the network is described as a weighted graph by all the paths possibles in the network between stations.

This document details the organisation of the different parts of the software ([Classes diagram](#classes-diagram) & [Classes description](#classes-description)) and how they interact with each others ([How it works](#how-it-works)).

## Table of contents
- [PFE METRO SIMULATOR](#pfe-metro-simulator)
  - [Introduction](#introduction)
  - [Table of contents](#table-of-contents)
  - [Configuration](#configuration)
    - [General configuration](#general-configuration)
      - [config.json](#configjson)
      - [nameLineList.json](#namelinelistjson)
      - [nameStationList.json](#namestationlistjson)
    - [Advanced configuration](#advanced-configuration)
  - [Classes diagram](#classes-diagram)
  - [Classes description](#classes-description)
    - [Models](#models)
      - [Calendar](#calendar)
      - [EventTimetableTrain](#eventtimetabletrain)
      - [Map](#map)
      - [MetroLine](#metroline)
      - [MetroStation](#metrostation)
      - [MetroTrain](#metrotrain)
      - [Passenger](#passenger)
      - [PathStation](#pathstation)
      - [Point](#point)
      - [PopulationManager](#populationmanager)
      - [Timetable](#timetable)
      - [TimetableReal](#timetablereal)
      - [Trip](#trip)
    - [Configs](#configs)
      - [Config](#config)
      - [AdvancedConfig](#advancedconfig)
    - [Tools](#tools)
      - [CsvConfig](#csvconfig)
    - [Simulator](#simulator)
      - [Simulator](#simulator-1)
  - [How it works](#how-it-works)
    - [Initialization](#initialization)
    - [Run Period](#run-period)
    - [Results Savings](#results-savings)

## Configuration
The configuration of the simulation is done using two steps, one for the general configuration and one for the configuration of the map with advanced options like, where the stations are placed and, what event will happen during the simulation. 

### General configuration
The files config.json, nameLineList.json, and nameStationList.json act on the configuration of the general parameters for the simulation. Here are details about each files.

#### config.json
  * csv headers : true or false, enable header on csv files.
  * lines : integer, number of lines in the simulation (max 2)
  * interchange stations : integer, number of interchanges (max 1)
  * population : integer, number of passenger in the simulation
  * seed : integer, seed for the random 
  * stations : integer, number stations in the simulation
  * trains per line : integer, number of trains per lines
  * capacity per train : integer, maximum number of passengers in a train
  * time in station : time a train will stay in a station
  * start time : date ISO-8601,  beginning of the simulation
  * stop time : date ISO-8601, end of the simulation
  * business day bounds (start is at day D end is at D+1) :
    * business day start : time XXh:XXm, time difference between midnight and the start of the business day. e.g if the business day start at 5am this should be set at 5h:00m.
    * business day end : time XXh:XXm, time difference between midnight of the next day and the end of the business day. e.g if the business day end at 1am this should be set at 1h:00m.
  * The sum of following fields should be equal to 1 :
    * population commuters proportion : real, part of population doing commuting trips, an two way trip 
    * population randoms proportion : real,part of the population taking one random trip everyday
  * The sum of following fields should be equal to 1 :
    * population adults proportion : real, part of the population that are adults
    * population senior proportion : real, part of the population that are seniors
    * population disabled proportion : real, part of the population that are disabled
    * population students proportion : real, part of the population that are students
    * population children proportion : real, part of the population that are children
  * morning commute time : time XXh:XXm, time around which passenger will do their morning commute.
  * evening commute time : time XXh:XXm, time around which passenger will do their evening commute. In the same day as morning commute.
  * commute period duration : time XXh:XXm, duration of the commuting period (standard deviation)
  * print debug : true or false, enable prints helpful for debug. 

#### nameLineList.json
an array of name for the lines e.g :
["Red",
"Green",
"Blue"]

#### nameStationList.json
an array of name for the stations e.g :
["Lorem",
"Ipsum",
"Dolor",
"Sit",
"Amet"] 

### Advanced configuration
The file map_config describe details of the map, where each station is located and what are the events that should happen during the simulation. It can be customised manually or using the software "railway editor" in this project(more detail about this software is available in its documentation). The map is generated by the software when it is not provided.



## Classes diagram
Here is the class diagram, for readability concern the members functions are not extensive.

![Image of class diagram](images/class_diagram.png)

## Classes description
### Models
#### Calendar
This class allow to retrieve the business day, in future improvement it should keep track of different kind of event during the day (e.g : Horse race) and changes to be made on the timetable to adapt the traffic.

#### EventTimetableTrain
The brick of timetable, contains informations for an event in the timetable.

Uses : [MetroLine](#metroline), [MetroStation](#metrostation), [MetroTtrain](#metrotrain)

#### Map
The class Map manage the map. it allow to create a railway network and to access it later on. Moreover after the creation of the map. This class generate the weighted graph representing the network. It have various functions to generate various kind of networks(i.e a single line network, a multiple line network).

Uses : [MetroLine](#metroline), [MetroStation](#metrostation), [PathStation](#pathstation)

Used by : [Timetable](#timetable)

#### MetroLine
The Metro line class represent a line. Member functions allow to create a line and add stations to it.
It also have methods to obtain the list of stations between two station 

Uses : [MetroStation](#metrostation)

Used by : [Map](#map), [MetroLine](#metroline)

#### MetroStation
This class represent a station it keeps its location, the line which is on, and its identifiers.
There is also the possibility to close a station.
Members methods allow to add/remove a line and also compute if on its lines there is a said station.

Uses : [MetroLine](#metroline), [Passenger](#passenger), [Point](#position)

Used by :  [Map](#map), [MetroTrain](#metrotrain),  [MetroLine](#metroline) 

#### MetroTrain
This class is used to represent a train, it has a capacity, and keep track of the current station and next station. A Metro train object also has the line on which it operate. It also keep realtime date, lime the current station and next stations and the time of arrival.

Uses : [MetroLine](#metroline), [MetroStation](#metrostation)

#### Passenger
Represent an passenger, it keep track of trips planned thorough the simulation. It can have various kind like adult, senior, student, disabled or child. Members methods allow to add/remove trip

#### PathStation
Describe a path between two stations, as a succession of Stations and lines.


Uses : [MetroLine](#metroline), [MetroStation](#metrostation)

Used by : [Trip](#trip)

#### Point
Represent a point with 2 coordinates

Used by :  [Map](#map), [MetroTrain](#metrotrain),  [MetroLine](#metroline) 

#### PopulationManager
The Class that handle the population in the simulation. It create the population (a map of passengers). It is this class that affect trips to passengers.
It also have methods to move passengers from outside to station, from station to train and form train to station and finally for station to outside. 

Uses [Passenger](#passenger)
#### Timetable
The class that will generate the ideal following parameter in the config.

Uses : [EventTimeTableTrain](#eventtimetabletrain)

#### TimetableReal
The real timetable accounting problems on during the simulation.

Uses : [EventTimeTableTrain](#eventtimetabletrain)

#### Trip 
The class describing a trip. Basically it is a path with a departure time and a arrival time.
It has different constructors to build different kind of trips (e.g commuting trips).
Trips are bounded by business day start/end

### Configs
#### Config
The class that abstract the configuration file. it firstly read it the configuration file and then store the parameter in its fields. To avoid multiple read its a singleton

#### AdvancedConfig
Similar to config.go.  Abstract the advanced configuration file.

### Tools
#### CsvConfig
The class that abstract the outputs files, it allows to easily create a csv files and to append data to it

### Simulator
#### Simulator
The class that act as the brain of our application, it instantiate and use directly or indirectly all the others classes.
It also keep track of time as our simulation evolves.


Uses : [Config](#config) [AdvancedConfig](#advancedConfig) [Map](#map) [Population](#population) [MetroTrain](#metroTrain) [Time](#time) [Timetable](#timetable) [TimetableReal](#timetableReal) 

Used by : [Main](#Main)





## How it works

The main class is a CLI overlay to control the Simulator class. It enables the user to run the simulation using a custom map.
If a parameter is present in both config.json and one of the following CLI parameters, please note the CLI parameter will be used while using the main class.

CLI parameters : 

|parameter|default value|description|
|---|---|---|
|regenconfig|false|regenerate the `configname` file from config.json|
|configname|map_config.xml|the name of the map file|
|maxruns|-1|the number of events to run|
|verbose|false|whether or not to print all details. supplant the `print debug` parameter in config.json|

example : `go run metro_simulator.go -configname myConfig.xml`

The simulation can be split in 3 phases : 
- initialization
- run period
- results saving

### Initialization

In order to run, the simulator need multiple elements : the map, the passengers with their trips, the train and their associated lines / directions, the differents events...

The Initialization phase is when these elements are loaded into the simulation. 
It is controlled by the function simulator.Init, which creates the following elements :
- loads config.json into memory
- loads the map file into memory
- generates the events from the map file
- creates the map
- creates the passengers
- creates the trains

### Run Period

The simulator motor is based on events, specifically the events linked to the trains (trainEvents).
The motor is the function simulator.Run, which basically calls the function simulator.RunOnce in a loop until the simulation is finished.

simulator.RunOnce enacts a handful of trainEvents, which are normally at the same time period (the time period is less than the time trains take in station, in order to allow concurrent calculus while reducing risks of inconsistency ).

A runOnce call do the followings :
- get the next trainsEvents according to the last event's time it executed
- check and execute StationClosed events and LineDelay events
- check and make the passengers exit from the stations to the outside
- check and make the passengers enter the stations from the outside
- for each trainEvent:
    - check and put out the passengers from the train to the station
    - check and put the passengers in the train from the station

### Results Savings

Once the simulation is ended, results are saved in CSV files for NAIA to parse. note that some files are created in previous phases and are populated during the init and the run phase as it allows us to not load in memory useless (to the simulation) information.

the files created are : 

|name|created in|function|
|---|---|---|
|stations.csv|init phase (map.WriteCSV)|list all stations|
|stations_lines.csv|init phase (map.WriteCSV)|list all lines|
|Stations_mapping.csv|init phase (map.WriteCSV)|list all stations|
|tickets.csv|Run phase|describe the entry and exits of the passengers|
|timetable.csv|init phase (if config.json `pre timetable` is set to true)|expected timetable of the trains, before actual simulation.|
|timetableReal.csv|Results Savings|actual timetable of the trains.|

