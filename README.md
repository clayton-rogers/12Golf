# 12Golf
A new spin on the card game Golf.

Starting with an initially face down hand, your goal is to accumulate the least amount of points over the course of 10 rounds.


### Release History
v1.4 (27 Aug 2015):
- Fixed an issue where if a player went to the next round before the other player had gotten to the 
score screen then the game would get into an unrecoverable state.
- Fixed an issue where the client wouldn't recognize tied games.
- Improved the network utilization by only sending the seed of the game, rather than the whole state.
- Updated the server so that clients disconnecting before the game starts doesn't break the server.
- Implemented a GUI scene toolkit. This will allow multiple screens to be handled better. 
(ex. game screen, score screen, menu screen, etc.)
- Added an icon.
- Randomized which player goes first for a given round.
- Added suits to the cards.
- Variety of code cleanup, GUI, and documentation improvements.

v1.3 (22 Jul 2015):
- Fixed an issue where all players would show up as "Joe Luigi".
- Made it so that a player's cards appear directly in front of them.
- Fine tuned the location of a number of GUI elements.
- Other GUI improvements.
- Deck now shows how many cards are in it.
- Discard pile is now shuffled back into the draw pile when the draw pile is empty.

v1.2 (19 Jul 2015):
- Added a score screen which is displayed when the round finishes.
- A new round can be started after viewing the score screen.

v1.1 (18 Jul 2015):
- Fixed issue where game would never end. Game will now end and display game over.
- Fixed issue where dragging the mouse, even slightly, would cause the mouse click
  not to be registered.


v1.0 (14 Jul 2015):
- Initial release

### How to Build
The easiest way to build the project is to use IntelliJ IDEA with the included project
files. If that is not an option you can also build it as follows:

```
cd 12Golf
mkdir out
find . -name "*.java" > sources.txt
javac -verbose `cat sources.txt` -d out
cd out
find . -name "*.class" > classes.txt
jar cfm Client.jar ../Client/META-INF/MANIFEST.MF `cat classes.txt`
```

You can build the server in the same way, but substituting the server manifest at:
12Golf/Server/META-INF/MANIFEST.MF
