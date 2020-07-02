# Final Test of Software Engineering - a.a. 2019-2020
![alt text](https://cf.geekdo-images.com/opengraph/img/aL3ylg4WfWekpXaOq9fij-eRgHg=/fit-in/1200x630/pic3283110.png)

The aim of the project is to implement the [Santorini](https://www.craniocreations.it/prodotto/santorini/) game following the model of Model View Controller for the realization of the model according to the object-oriented programming paradigm. The final result completely covers the rules defined by the game and allows you to interact with a command line interface (CLI) and a graphical interface (GUI), the network has been managed with the traditional socket approach.

## Documentation
The following documentation includes the documents created for the design of the problem, first of all the UML diagram will be listed then the documentation of the code (javadoc).

### UML

The following class diagrams represent the first, the model according to which the game should have been implemented, the second instead contains the final product diagrams in the critical parts found.
- [Initials UML](https://github.com/)
- [Final UML](https://github.com/)

### JavaDoc
The following documentation includes a description for most of the classes and methods used, it follows Java documentation techniques and can be consulted at the following address: [Javadoc](https://github.com/)


### Librerie e Plugins
|Lib/Plugin|Descripton|
|---------------|-----------|
|__maven__|management tool for Java project and build automation|
|__junit__|java testing framework|
|__mockito__|testing support tool for create "dummy" object|
|__JavaFx__|java graphics libraries|



## Functionality
### Developed Features
- Complete rules
- CLI
- GUI
- Socket


### Additional Features Developed
- Multiple games
- Undo

### How to play
### Client
The client can decide to play with a Command Line Interface with his terminal or can choose a Graphics User Interface. His decision is independent of the others. All dependency are managed by Maven when build the jars. 
#### CLI
For a better experience is recommended a terminal that support ANSI escape code. 
To launch the client with CLI use the following command:
```
java -jar cli.jar [parameter1] [parameter2]
```
parameter1: "server ip" (if null ip will be localhost).
parameter2: "server port" (if null port will be 12345)

#### GUI
To launch the client with GUI use the following command:
```
java -jar gui.jar
```

### Server
To launch the client with GUI use the following command:
```
java -jar server.jar [parameter1] [parameter2]
```
parameter1: "server ip" (if null ip will be localhost).
parameter2: "server port" (if null port will be 12345)


### How to generate jars
In Maven there are 3 profile: server, cli, gui.
To create server's and cli's jar 
-Go to Maven
-Select the server/cli profile
-Launch the following maven command:
```
mvn clean install
```
-the jar will be in deliveries folder

To create gui's jar
-Go to Maven
-Select the gui profile
-Launch the following maven command:
```
mvn clean compile assembly:single
```
-the jar will be in deliveries folder


## Group members
- [__Matteo Malandra__](https://github.com/matteomalandra)
- [__Michele Veroni__](https://github.com/micheleveroni)
- [__Lorenzo Zane__](https://github.com/lorenzozane98)
