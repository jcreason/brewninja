BrewNinja
=========

Copyright © 2013 Jarett Creason - jcreason@gmail.com

This work is available under the "GPLv3" licence.  Please see the file
[`COPYING`](https://github.com/jcreason/brewninja/blob/master/COPYING)
in this distribution for licence terms.

BrewNinja is a Java project to automate the temperature control for the burners on
on our [Brutus 10](http://www.alenuts.com/Alenuts/brutus.html) homebrewing stand.
The project is setup to run on a [Raspberry Pi](http://www.raspberrypi.org/) and work with
a custom fabricated BrewNinja board to control relays and read in information.
The relays control propane flow to valves and turn on and off pumps.
Temperature data is read in using waterproof
[DS18B20's](http://learn.adafruit.com/adafruits-raspberry-pi-lesson-11-ds18b20-temperature-sensing)
sensors attached to a one wire interface.

<i><b>NOTE:</b> This project is still in active development.
The master branch with SNAPSHOT versions should be relatively stable,
but use the latest tagged version (like [v0.1.1](https://github.com/jcreason/brewninja/releases/tag/v0.1.1))
if you want to be sure.
Please use the [issues](https://github.com/jcreason/brewninja/issues) page to file bugs or requests.</i>

## How to Run
BrewNinja should be compiled and run on real Raspberry Pi hardware.
The project will compile on other hardware, however, it uses the [Pi4J](http://pi4j.com/)
library which requires the Pi's architecture.
This means that when running on another architecture, the features of the program are severely limited.

### Requirements
- Raspberry Pi<br/>
I used [Raspbian "wheezy"](http://www.raspberrypi.org/downloads) as my install image.  All
of this should be possible regardless of which distro you use, but do be aware of the
difference between hardfloat vs. softfloat and which one your distro uses.

- BrewNinja Code<br/>
This code here, so clone the git repo or download the source code.

- Java 7+<br/>
I am using Java 8 downloaded from [here](http://jdk8.java.net/download.html).  It is an
early access release, but there is a specialized download to run on the
hardfloat ARM architecture.
[This guide](https://wiki.openjdk.java.net/display/OpenJFX/OpenJFX+on+the+Raspberry+Pi)
was handy for me to get it up and running.  Potentially you could use OpenJDK 7+, but it
is purportedly much slower and I have not yet tried that.

- MySQL 5.5<br/>
[MySQL](http://www.mysql.com/) is required (although a bit heavy handed in retrospect).
I installed it on the Pi using `apt-get install`.
You will also need to create an empty database, which can just be "brewninja",
or something else which you can customize in your Hibernate config file.
Additionally create a user which can access that database (S/I/U/D privileges).
You will also need a user with all permissions on that database which will run the
database migrations.

- Maven 3.0.x<br/>
Maven serves as the Java package manager, and will need to be installed.
[Go here](http://maven.apache.org/download.cgi) to download it and follow the install instructions.
You need to also setup Maven security settings if you don't already have them.
Follow [this guide](http://maven.apache.org/guides/mini/guide-encryption.html)
to create a master password and then encrypt the password belonging to the database
user you created above for application access to the database.  Additionally
(I know, it's a lot), you need to edit (or create) your ~/.m2/settings.xml file
and define the credentials which the [Flyway](http://flywaydb.org/) database migration
user needs.  The "Authentication" at the bottom of [this page](http://flywaydb.org/documentation/maven/)
is an example of what is needed there.

- Log4J-2 and Hibernate Config Files<br/>
These must be setup first before building.  Copy the two config files
(one hibernate, one log4j2) located in
[src/main/resources/](https://github.com/jcreason/brewninja/tree/master/src/main/resources)
as new file names.  Replace the word "example" in each file name with
something different (like your name).  This keyword is then passed in as an
argument to Maven on compile and your custom config files will be copied
to the correct location where they will be read by Hibernate and Log4J-2
at runtime.  Make sure and customize these files by adding in your database
credentials and location and any special logging you'd like.

### Running
Once all requirements are satisfied, change directories on your Pi to the
location of the BrewNinja source code.

**If you're not running Java 1.8**, you must put JavaFX on your classpath.
To read more about why, look [here](http://zenjava.com/javafx/maven/fix-classpath.html).

```
sudo mvn com.zenjava:javafx-maven-plugin:2.0:fix-classpath
```

Next, fill your empty database it with tables and data:

```
mvn flyway:migrate
```

Then, compile:

```
mvn compile -Dproperties=<keyword_for_config_files>
```

Finally, to run, you use the [exec-maven-plugin](http://mojo.codehaus.org/exec-maven-plugin/):

```
mvn exec:java -Dproperties=<keyword_for_config_files>
```

In addition, once installed, the [`brewninja`](https://github.com/jcreason/brewninja/blob/master/misc/brewninja)
startup service script can be configured and used to create a system
service that allows brewninja to start on system boot at the desired
boot level.


## Customizing the Data
After first running the flyway migration, you can customize the burners,
GPIOs, and temperature sensors in the database.  Currently, there is not admin
interface to do these things.

## Using the Interface
Once running, this should be fairly intuitive.  However, much more functionality
will be coming in the future.

