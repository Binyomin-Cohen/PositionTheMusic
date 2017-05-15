# PositionTheMusic
Android App that turns phone into drumstick and surroundings into drumset

This app will use the proximity sensor of the phone to determine when you "hit" the drum or cymbol.  If you come close to the 
surface of something ( like a table - or if you don't want to risk smashing your phone, a couch),
this will be considered you hit the instrument.  Selecting which instrument you hit is
automatically determined by the rotation vector (if you hold your device parallel to the ground
and rotate it in a way that it remains parallel to the ground).

At first, this app was based on the linear acceleration sensor, but that proved to need a much more complex
algorithm to filter out the hits from other movements, so the proximity seemed the most straightforward.

There is thought to develop a better algorithm for using the linear acceleration sensor instead, since
the proximity sensor is often on the same side as the screen, meaning you can't look at the screen and hit a table
with the back of your phone and have it register properly with the proximity sensor.

Looking to fine tune and tweak the functionality and aesthetics of this app a bit before publishing on the
Google Play Store.
