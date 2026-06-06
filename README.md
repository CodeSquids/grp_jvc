# Gestion de visites de sites 

This project is developed to demonstrate our group handle of **Java** language to create **Desktop Application**.

For the creation, we used various technology.


## Installation

Each group members have used their own technology for the development. It didn't keep us from successin into the creation of this application.

 - For the development, we used the IDE offered by **JetBrains**, specialising in java development : **IntelliJ IDEA 2025.3.2**

 - For the Java version, the version **java version "21.0.10" 2026-01-20 LTS** was applied

 - For the build to desktop, **WiX Toolset v3.14.1.8722** helped transform the application into an executable ( *.exe* ) type.

 - **...** and many other tools we may use but forget the presence during the creation of app

**As for the backend**, yeah because the backend is by default on the address *http://localhost:5000/*. It's repository is here but in the directory *backend* when the desktop application is on *frontend*.

## Desktop creation

With other IDE, the execution may be automatic. As for us, we used the following commands to run the application :

```
javac -cp 'lib\*;src' -d bin src\com\projet\Main.java


Copy-Item -Recurse -Force src\com\projet\images bin\com\projet\


java -cp "bin;lib\*" com.projet.Main

```

Then to create the desktop application, the instructions are as followed, as we are in Windows 11 OS during the creation:

```
javac -cp "lib/*" -d bin src/com/projet/*.java src/com/projet/Pages/*.java src/com/projet/Dialog/*.java src/com/projet/Services/*.java src/com/projet/Tables/*.java


jar cfm HereVisit.jar MANIFEST.MF -C bin .


jpackage `
--input . `                                                                      
--name HereVisit `                                                          
--main-jar HereVisit.jar `
--main-class com.projet.Main `
--type exe `    
--win-menu `
--win-menu-group "CodeSquids" `                                                  
--win-shortcut `                                                                 
--icon .\favicon.ico       
```

At the end of these command, we have to executable application of this project
