# GPSTester - Changelog


## Version 1.1 - January 30, 2013

- Fixed a fatal bug related to the network location provider returning a null value. All null values related to both the location providers (GPS and Network) should be properly trapped now. This condition can manifest in a number of different ways such as under device Location Services the Google Location Service option is disabled. And second some cellular carriers do not provide this service.


## Version 1.0 - January 21, 2013

- Supports Androids v3.0+ because of use of UI fragments. 