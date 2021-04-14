Version 1.2 - 17.5.2005
=======================

Bugs
----

- Added a "nothing thanks" dialog option to Iblis
- Fixed a bug that caused music to be turned on (even when it was turned off) when game ended
- Fixed a missing picture for soldier when purchasing his services
- Fixed a typo in the picture name for the Budapest army
- Fixed a bug in the dialog choices for Budapest's "church to mosque" invitation
- Removed old music definition from witch's cave that caused a stack trace
- Fixed a few problems with attacking the London pub owner
- Fixed a typo in the picture name of Constantinople's bank manager (showed up in dialog when starting the marriage quest)
- Fixed a bug in the Iblis' new demand, when sacrificing your only wife (and especially Emine) did not work correctly
- Fixed (finally) the "wifes" typo in the status dialog :-)
- Fixed a bug that caused uncle Adbullah to lend money infinitely after paying back the first loan
- Fixed a bug in the lovers' dialogs, so that the world map now shows up when getting a travel permit from him/her
- Fixed a bug that caused the building permit quests for museum, shrine and library were not marked as "done" in the diary when bribing the official
- A bunch of changes to the logic in getting married with Umm
- Changed the file I/O routines to use the ISO-8859-1 character encoding explicitly
- Fixed a reoccurring bug in the sound system that resulted in a stack trace on a machine with no sound card

Changes
-------

- Tweaked cardemon and basic food prices so that the planting cardemon would be profitable while playing with the normal difficulty setting
- Tweaked Yima's threshold for talking to player
- Changed the size of the book of life hot spot to very large (whole screen), so people would not miss it


Version 1.1 - 29.4.2005
=======================

Bugs
----

- Fixed a bug that caused the quest titles to disappear when loading
- Fixed a bug that caused poem books to sometimes dissappear in European museums
- Fixed a bug in the loading and saving routines, which left the directory information unused (thanks pablo)
- Fixed a bug in the library quest in Baghdad
- Fixed a bug in the gotoBattle routine, the endTask was not included in the next round script
- Fixed the name Anjoudan to Anjudan
- Fixed home and shops abroad, could not buy/rent in most cities
- Fixed a bug in the London hotel owner hotspots
- Fixed a (null) problem when displaying certain quest items in the journal
- Added the trading information for Budapest tavern keeper
- Fixed a problem in bying permits in Amsterdam
- Some missing pictures added and some pictures changed (church and museum transformations, format changes)
- Fixed the Constantinople bank officials dialog
- No more randomly "jumping people". Changed the shopping and battling screen logic so that the background is not redrawn at every transaction.
- Removed the reappearing items from the graves
- Music will now be automatically turned off for machines with no sound cards

Changes
-------

- General game balancing
- Vienna church transition picture changed
- Caravan can be bought in other cities close to Baghdad
- Book of life pictures appear in cronological order
- Age display has been changed
- Added description about the "missing items" in the museum help description
- More random encounters
- Changed the sound loop, should remove cutting of sound on slower machines
- Changed the DEATH! link in the death status screen so that it also ends the game (you can also click the corpse to end the game)
- Added full screen mode (although the game still requires 1024*768 resolution and does not scale the pictures)
- Added a confirmation dialog to the exit functions
