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
    - [Dark mode / Light mode](#dark-mode--light-mode)
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
    - [Stop the HMI](#stop-the-hmi)
  - [Power BI dashboard](#power-bi-dashboard)
  

## MNJS simulator

We have created an HMI so that we don't have to interact with the simulator 
with a large number of command lines. 

As a result, it is simply necessary to
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
[RailwayEditor.java](..%2F..%2F..%2F..%2Frailway-editor%2Fsrc%2Fmain%2Fjava%2Forg%2Fexample%2Fmain%2FRailwayEditor.java) file.

### Dark mode / Light mode

The HMI has a dark mode and a light mode. 

To change modes, you need to click on the "Dark Mode" or "Light Mode" button at
the top left of the HMI.

Dark mode:
![Dark mode](..%2Fresources%2Fdark_mode.png)

Light mode:
![Light mode](..%2Fresources%2Flight_mode.png)

### Destinations

You can 'teleport' to a destination by selecting it in the top right-hand 
corner. 

You can enter the destination to find it in the drop-down list.

Focusing on a destination is essential if we want to have coherent distance 
scales for a metro network.

Destination drop down list:
![Destination drop down list](..%2Fresources%2Fdestination_drop_down_list.png)

### Lines
You can add, delete and change lines.

Lines are numbered from 0 upwards.

#### Add lines

To add a line, you need to click on the "ADD" button in the "Line" section of
the toolbar.

![Add line](..%2Fresources%2Fadd_line.png)

#### Delete lines

To delete a line, you need to click on the "DELETE" button in the "Line" section
of the toolbar.

![Delete line](..%2Fresources%2Fdelete_line.png)

#### Change lines
In order to change lines, you need to create at least two lines.

To change lines, you need to click on the down arrow to decrease the line 
number and the up arrow to increase it.

Increment line number:
![Increment line number](..%2Fresources%2Fincrement_line.png)

Decrement line number:
![Decrement line number](..%2Fresources%2Fdecrement_line.png)


### Stations

You can add, delete and merge stations.

Stations are numbered from 0 upwards.

#### Add stations

In order to add stations, you need to create at least one line.

To add a station, you need to click on the "ADD" button in the "Station" 
section.

You can either add one from the end of the line or from the start of the line 
by switching buttons.

![Add station](..%2Fresources%2Fadd_station.png)

#### Delete stations

To delete a station, you need to toggle the "DELETE" button in the "Station"
section and then right-click on the station you want to delete.

![Delete station](..%2Fresources%2Fdelete_station.png) 


#### Merge stations

To merge stations, double-click on the first station.

A dialogue box will ask you to select the second station to be merged with the 
first.

Double-click the second station.

![Merge stations](..%2Fresources%2Fmerge_stations.png) 

### Areas

You can add areas and modify area distributions.

#### Add areas

To add an area, you need to click on the "ADD" button in the "Area" section.

![Add area](..%2Fresources%2Fadd_area.png) 

#### Delete areas

Despite the presence of a "DELETE" button in the "Area" section, it is not
possible to delete an area yet.

#### Modify area distribution

To modify the distribution of an area, you need to double-click on the area.

Then, a dialogue box will allow you to modify the distribution of the area.

In order to save the changes, the percentage of the distribution must be equal
to 100%. Otherwise, you cannot click on the "Save" button.

Distribution of 100%:
![Edit distribution](..%2Fresources%2Fedit_distribution.png) 

Distribution not equal to 100%:
![Edit distribution](..%2Fresources%2Fedit_distribution_wrong.png) 

### Events

You can add and delete events.

In order to add an event, you need to create at least one station.

The list of events created appears on the right of the HMI.

#### Add events

To add an event, you need to click on the "ADD" button in the "Event" section.

Then, a dialogue box will allow you to select the type of event you want to
create.

Double-click on the type of event you want to create.

Fill in the dates of the event, the station(s) concerned and potentially other 
parameters.

For the stations concerned, you can either enter the station number or press the 
button to select the station from the HMI.

Click on the "Done" button.

The concerned station(s), the colour of the circle should change.

ADD button:
![Add event](..%2Fresources%2Fadd_event.png)

Dialogue box:
![Event editor](..%2Fresources%2Fevent_editor.png) 

Event selected:
![Event editor event selected](..%2Fresources%2Fevent_editor_event_selected.png)

Event created:
![Event created](..%2Fresources%2Fevent_created.png) 

#### Delete events

To delete an event, from the list of events on the right, click on "remove"
for the event concerned.

![Delete event](..%2Fresources%2Fdelete_event.png) 

### Edit the configuration

You can edit the JSON configuration from the HMI by clicking on 
Configuration > Edit.

Then a dialogue box will appear. 

Modify the parameters you want, then click "OK" to save the changes.

![Edit configuration](..%2Fresources%2Fedit_configuration.png)
### Run a simulation

### Import/Export system

### Archiving system

### Stop the HMI

## Power BI dashboard

Power BI is a data visualization tool that allows you to create dashboards.
We have created a dashboard that allows you to visualize the data generated by
the simulator. To use it, you need to install Power BI Desktop and open the
file located in doc/v3/power_bi/dashboard.pbix :

Link: [dashboard](..%2Fpower_bi%2Fdashboard.pbix)

![dashboard](..%2Fresources%2Fdashboard.png)


### Change the path to the tickets table

tickets.csv is the file that contains the data on the transactions made by the
passengers. It is located in network-journey-simulator/output/tickets.csv :

[output/tickets.csv](..%2F..%2F..%2F..%2Fnetwork-journey-simulator%2Foutput%2Ftickets.csv)

You can either use the csv file or transform it into an Excel file.

After opening the dashboard, you need to change the path to your tickets file.
To do this, you need to follow these steps:

- Go to Home > Transform data
- A window opens
- Check that the "tickets" query is selected in the left column
- Click on the gear icon next to the "Source" step in the right column
- Change to your path to tickets.csv




