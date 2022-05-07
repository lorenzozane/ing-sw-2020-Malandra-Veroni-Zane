# Final Test of Software Engineering - a.a. 2019-2020
<img src="https://cf.geekdo-images.com/opengraph/img/aL3ylg4WfWekpXaOq9fij-eRgHg=/fit-in/1200x630/pic3283110.png" width=300px height=300px align="right" />


Final project of the **Software Engineering** course at **Politecnico di Milano** during 2019/2020 a.y.

**Final Score**: 30L


The aim of the project is to implement the [Santorini](https://www.craniocreations.it/prodotto/santorini/) game following the model of Model View Controller for the realization of the model according to the object-oriented programming paradigm. The final result completely covers the rules defined by the game and allows you to interact with a command line interface (CLI) and a graphical interface (GUI), the network has been managed with the traditional socket approach.


## Documentation
The following documentation includes the documents created for the design of the problem, first of all the UML diagram will be listed then the documentation of the code (javadoc).

### UML

The following class diagrams represent the first, the model according to which the game should have been implemented, the second instead contains the final product diagrams in the critical parts found.
- [Initials UML](https://github.com/lorenzozane/ing-sw-2020-Malandra-Veroni-Zane/blob/master/deliverables/final/uml/uml_iniziale.pdf)
- [Final UML](https://github.com/lorenzozane/ing-sw-2020-Malandra-Veroni-Zane/tree/master/deliverables/final/uml/completed)

### JavaDoc
The following documentation includes a description for most of the classes and methods used, it follows Java documentation techniques and can be consulted at the following address: [Javadoc](https://github.com/lorenzozane/ing-sw-2020-Malandra-Veroni-Zane/tree/master/deliverables/final/javadoc)  


### Libraries and Plugins
|Lib/Plugin|Descripton|
|---------------|-----------|
|__maven__|management tool for Java project and build automation|
|__junit__|java testing framework|
|__mockito__|testing support tool for create "dummy" object|
|__JavaFx__|java graphics libraries|



## Functionality
### Developed Features
| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](https://via.placeholder.com/20/64dd17?text=+)](#) |
| Complete rules | [![GREEN](https://via.placeholder.com/20/64dd17?text=+)](#) |
| Socket | [![GREEN](https://via.placeholder.com/20/64dd17?text=+)](#) |
| CLI | [![GREEN](https://via.placeholder.com/20/64dd17?text=+)](#) |
| GUI | [![GREEN](https://via.placeholder.com/20/64dd17?text=+)](#) |
| Multiple games | [![GREEN](https://via.placeholder.com/20/64dd17?text=+)](#) |
| Undo | [![GREEN](https://via.placeholder.com/20/64dd17?text=+)](#) |
| 5 Advanced Gods | [![RED](https://via.placeholder.com/20/d50000?text=+)]() |
| Persistence | [![RED](https://via.placeholder.com/20/d50000?text=+)]() |


### How to play
### Client
The client can decide to play with a Command Line Interface with his terminal or can choose a Graphics User Interface. His decision is independent of the others. All dependency are managed by Maven when build the jars.  
#### CLI
To run, requires at least Java 9 (Version 53).  
For a better experience is recommended a terminal that support ANSI escape code (WSL, Linux).  
To launch the client with CLI use the following command:  
```
java -jar GC49-1.0-CLI.jar [parameter1] [parameter2]
```
- `parameter1` : "server ip" (if null ip will be localhost).
- `parameter2` : "server port" (if null port will be 12345)

#### GUI
To run, requires at least Java 10 (Version 54).  
Since WSL does not support graphical user interface, to run it is recommended to use cmd.exe.  
To launch the client with GUI use the following command:  
```
java -jar GC49-1.0-GUI-jar-with-dependencies.jar
```

### Server
To run, requires at least Java 9 (Version 53).  
To launch the client with GUI use the following command:  
```
java -jar GC49-1.0-SERVER.jar [parameter1] [parameter2]
```

- `parameter1` : "server ip" (if null ip will be localhost).
- `parameter2` : "server port" (if null port will be 12345)


### How to generate jars
In Maven there are 3 profile: server, cli, gui.  
To create server's and cli's jar  
- Go to Maven  
- Select the server/cli profile  
- Launch the following maven command:  
```
mvn clean install
```
- The jar will be in deliveries folder

To create gui's jar  
- Go to Maven  
- Select the gui profile  
- Launch the following maven command:  
```
mvn clean compile assembly:single
```
- The jar will be in target folder


## Group members
- [__Matteo Malandra__](https://github.com/matteomalandra)
- [__Michele Veroni__](https://github.com/micheleveroni)
- [__Lorenzo Zane__](https://github.com/lorenzozane)
