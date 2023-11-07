# Metro Simulator

## Dependencies

### Go

* Ensure that go is correctly installed and set up, with the GOPATH variable defined, The GOPATH is the location of your go workspace (by default $HOME/go). You can find the go installation guide here : https://golang.org/doc/install 

  * You can check that that go is installed with `go version`
  
  * You can check that your GOPATH is properly set up with a `echo $GOPATH` or `go env`, if this environement variable is empty here is a guide about it :
https://github.com/golang/go/wiki/SettingGOPATH

  * You might need to define more environment variable, like GOBIN ($GOPATH/bin) that is used by the command go install.


Go code organisation is a bit uncommon, you can read more about it here : 
https://golang.org/doc/code.html#Organization

* Following those guidelines you should git clone the project in $GOPATH/src

### Tests Dependencies

At the current state of the project, dependencies are only used in tests,
So if you just want to run the project without the test you can directly go
to the part "How to run it"

* In a terminal go to $GOPATH/src/pfe-2018-network-journey-simulator
* Do a `go get -t ./...`
* Go should download for you all the dependencies of the project

## How to run it

There is two way you can generate an executable from Go code.

### With go build

The go build command will build the whole project

* In a terminal go to $GOPATH/src/pfe-2018-network-journey-simulator/src/main
* build the program with a `go build metro_simulator.go` (use `go mod tidy` and `go mod vendor` if necessary before)
* mark the file as executable thanks to the command `chmod +x metro_simulator.go` if necessary
* execute it with `./metro_simulator` or `./metro_simulator.exe` 
* You should get all the output in $GOPATH/src/pfe-2018-network-journey-simulator/output

### With go install

The go install command will build the whole project and also cache packages (except the main package) in $GOPATH/bin. The install command will also create the executable in $GOPATH/bin

e.g after a go install we have the files : models.a, configs.a, simulator.a, tools.a. In$GOPATH/bin/pfe-2018-network-journey-simulator/linux_amd64/ 
if a change is made on models go install will only rebuild the package models.a

* In a terminal go to $GOPATH/src/pfe-2018-network-journey-simulator/main
* build the program with a `go install metro_simulator.go`
* cd to $GOPATH/bin/
* execute it with `./metro_simulator`
* You should get all the output in $GOPATH/src/pfe-2018-network-journey-simulator/output


## How to run the tests

To run all the tests you can use the command `go test ./...` at the root of the project.
you can run a specific test in a file using the command `go test <path/to/file> <required/files>` required files are files that are required to build the test file. e.g : `go test foo_test.go foo.go`
However running test in CLI is not super readable, we recommend you to use an IDE.
