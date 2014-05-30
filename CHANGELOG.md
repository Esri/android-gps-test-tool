# GPSTester - Changelog

## Version 1.5 - May 30, 2014
Updated to work with ArcGIS SDK for Android v10.2.3 and Android v4.2.2

## Version 1.4.2 - November 5, 2013
Updated to work with ArcGIS SDK for Android v10.2

## Version 1.4.1 - July 18, 2013
- App now detects when internet connection goes off and back on.

## Version 1.4 - July 17, 2013
- Fixed issues #10, #11 and #12.
- Added functionality to keep screen turned on while application is visible.
- Built and tested app against ArcGIS SDK for Android v10.1.1 Update 1.

## Version 1.3 - June 11, 2013
- Tested app to work with EsriQuickStart v1.2.1 
- Internal: Updated SettingsActivity to include a PreferenceFragment.
- Added timestamps to Network Provider and GPS Provider text output.
- Fixed issue #8: Send email did not contain satellite data.
- Fixed issue #9: Very long decimal values for accuracy numbers. Added a four place decimal formatter to the network and GPS accuracy values.

## Version 1.2.1.1 - February 3, 2013
- Field testing showed a few more tweaks needed on the threading.

## Version 1.2.1 - February 3, 2013
- Fixed several more threading issues as related to UI lagginess. 

## Version 1.2 - February 1, 2013
- Fixed tricky threading bugs in the delayedStartLocationProvider() and delayedStartCachedLocationProviders() methods. Previous construction might have been causing crashes when the application restarted after being idle and minimized for some time period.
- Rewrote the entire crazy if/else structure in startLocation() 

## Version 1.1 - January 30, 2013

- Fixed a fatal bug related to the network location provider returning a null value. All null values related to both the location providers (GPS and Network) should be properly trapped now. This condition can manifest in a number of different ways such as under device Location Services the Google Location Service option is disabled. And second some cellular carriers do not provide this service.


## Version 1.0 - January 21, 2013

- Supports Androids v3.0+ because of use of UI fragments. 