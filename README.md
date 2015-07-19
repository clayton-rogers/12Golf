# 12Golf
A new spin on the card game Golf.

Starting with an initially face down hand, your goal is to accumulate the least amount of points over the course of 10 rounds.


### Release History
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
