BrewNinja
=========

Simple Java project to automate the temperature control for the burners on
on my [Brutus 10](http://www.alenuts.com/Alenuts/brutus.html) beer homebrew stand.
The project is setup to run on a [Raspberry Pi](http://www.raspberrypi.org/) and use
a custom BrewNinja (link to come) board to control relays, which control propane flow
to valves.  It reads in temperature data from
[DS18B20's](http://learn.adafruit.com/adafruits-raspberry-pi-lesson-11-ds18b20-temperature-sensing)
sensors attached to a one wire interface.


##Installation
The package manager used is Maven, which will download and install all needed
JAR files in a local repository on build.  Log4J-2 and Hibernate config files need
to be setup first before Maven builds, using a custom properties keyword in the filename.
See the examples setup in the resources directory.
This keyword is then passed in as an argument to Maven on compile and your custom
config files will be copied to the correct location where they will be read by 
Hibernate and Log4J-2 at runtime.

More here to come.

##Customizing the Data
...

##Using the Interface
...


