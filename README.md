ProactiveOne Android Application
Proactive Sensing
Robert Douglass

All files programmed and created by me are located in...
	\ProactiveOne\app\src\main\java\com\proactivesensing\bobbydouglass\proactiveone\...
	\ProactiveOne\app\src\main\res\layout\...
	\ProactiveOne\app\src\main\res\drawable\...
	\ProactiveOne\app\src\main\AndroidManifest.xml

To install the application and test it out, install this file to your android device...
	\ProactiveOne\app-release.apk

Notes:
	Only a few screens are functioning as of right now.  The ones that are have not been thoroughly
	tested as the app is currently undergoing a total make over.

Menu:
	Read Status
		This screen has two modes.  The first mode, Modbus mode, reads a raw string
		through NFC from the Watchdog device.  The second mode, Debug mode, reads back data sent
		through NFC from the other features in the app.
	
	Configure Sensors
		External Hardwired
			This screen allows the user to configure a number of options per each sensor plugged in
			to the ProactiveOne.  Upon clicking one the six different options the screen will expand,
			showing the different attributes for that option.  The user can also select which of they
			four sensors they want to configure at the top of the screen.  If the user makes any
			changes and presses the back button, the app will prompt the user asking if they want to
			save the changes made, or to discard.  If the user chooses to save the changes, the app
			will redirect to the NFC screen where it will guide you through sending the data over NFC.
			Once the user sends the changes over nfc, the app saves the changes to the phone,
			so that the user can come back to the changes they made later on even if the phone is
			restarted.
			
		External I2C
			Currently does nothing.
			
		Internal
			Currently does nothing.

	System Parameters
		This screen acts very similar the External Hardwired screen.  The user can click any of the
		various dropdowns, expanding the screen with a number of options; However these options
		apply to the ProactiveOne device itself.  It is also similar inthat it will prompt the
		user and redirect them to the NFC screen, thus saving the changes made if any.
		
	Advanced
		Configure Data Packets
			This screen acts very similar the External Hardwired screen.  The user can click any of the
			various dropdowns, expanding the screen with a number of options; However these options
			apply to custom configured data packets that the ProactiveOne device will send to the
			satelite.  It is also similar inthat it will prompt the user and redirect them to the NFC
			screen, thus saving the changes made if any.
		
		Send Diagnostic Command
			Currently does nothing.