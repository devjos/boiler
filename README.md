
# boiler-steuerung
 Energy costs in the night may be lower than in the day. With this software the boiler heats only during the night to save energy costs. The savings sum up to more than 100€ per year (in our household).


## Hardware requirements
- Temperature sensor (e. g. adafruit TMP006)
- RaspberryPi
- min. 3CH Power Relay

## Getting started
### Copy required configurations and system services
```shell
cp -r etc/* /etc
cp -r usr/* /usr
mkdir -p /var/log/temperature
``` 


### Build the control software
```shell
cd BoilerSteuerung
mvn clean package
cp target/boiler-1.jar /home/pi/boiler.jar
```
### Optional: Webserver
```shell
mkdir -r /srv/web
cp -r webserver/* /srv/web
cd /srv/web
npm install
```

### Start services
```shell
service sensor-ir start
service boiler start
service webserver start
```

## Architecture overview

### Measuring the temperature
The TMP006 uses the I2C connectors of the RaspberryPi.
```/usr/bin/sensor-ir``` measures the current temperature and stores it in ```/var/log/temperature/```

### Control software
The control software reads the current temperatures and heats the water via GPIO pins on RaspberryPi. Those GPIO pins are defined in ```BoilerControllerGPIOImpl.java```

### Webserver
The Webserver provides overview about the current water temperature and the heating costs, as well as configuration options.
! ATTENTION ! This webserver is not supposed to be publicly reachable.


## Additional energy savings 

Disable HDMI on RaspberryPI
Add  ```/usr/bin/tvservice -o``` to ```/etc/rc.local```

## Licensing
[Apache License 2.0](LICENSE)

