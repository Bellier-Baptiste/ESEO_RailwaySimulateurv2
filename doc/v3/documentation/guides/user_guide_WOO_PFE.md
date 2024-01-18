# MNJS user guide

## Introduction 

The purpose of this document is to help users manipulate the MNJS simulator and 
HMI.

## Summary

- [MNJS user guide](#mnjs-user-guide)
  - [Introduction](#introduction)
  - [Summary](#summary)
  - [MNJS simulator](#mnjs-simulator)
    - [Generate the executable](#generate-the-executable)
  - [HMI](#hmi)
    - [Launch the HMI](#launch-the-hmi)
    - [Destinations](#destinations)
    - [Lines](#lines)
      - [Add lines](#add-lines)
      - [Delete lines](#delete-lines)
      - [Change lines](#change-lines)
    - [Stations](#stations)
      - [Add stations](#add-stations)
      - [Delete stations](#delete-stations)
      - [Merge stations](#merge-stations)
    - [Areas](#areas)
      - [Add areas](#add-areas)
      - [Delete areas](#delete-areas)
      - [Modify area distribution](#modify-area-distribution)
    - [Events](#events)
      - [Add events](#add-events)
      - [Delete events](#delete-events)
    - [Edit a configuration](#edit-a-configuration)
    - [Run a simulation](#run-a-simulation)
    - [Import/Export system](#importexport-system)
    - [Archiving system](#archiving-system)
  

## MNJS simulator

We have created an HMI so that we don't have to interact with the simulator 
with a large number of command lines. As a result, it is simply necessary to
generate the executable that will be used from the HMI to run the simulation.

### Generate the executable

To generate the executable, you need to run the following command from 
the /network-journey-simulator folder:

```bash
go build .\src\main\metro_simulator.go 
```

## HMI

### Launch the HMI

To launch the HMI, you need to run the RailwayEditor.main() method from the
[RailwayEditor.java](..%2F..%2F..%2F..%2Frailway-editor%2Fsrc%2Fmain%2Fjava%2Forg%2Fexample%2Fmain%2FRailwayEditor.java)  
file.

### Destinations

### Lines

#### Add lines
#### Delete lines
#### Change lines

### Stations

#### Add stations
#### Delete stations
#### Merge stations

### Areas

#### Add areas
#### Delete areas
#### Modify area distribution

### Events

#### Add events
#### Delete events

### Edit a configuration

### Run a simulation

### Import/Export system

### Archiving system





