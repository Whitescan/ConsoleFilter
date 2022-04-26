# ConsoleFilter
A lightweight plugin to filter and hide console messages and remove old log files.

# Java Version
Java 17 is used within this plugin and is strongly recommended.

# Minecraft Support
Currently this plugin only supports Spigot / Paper and Waterfall (BungeeCord Fork from PaperMC) for Minecraft Java Edition.

# Commands
Spigot / Paper:
/reloadconsolefilter - Reload the configuration

Waterfall:
/breloadconsolefilter - Reload the configuration

# Permissions
- consolefilter.reload - Allow the usage of the config reload commands

# Why are things the way they are?
Evenso there are plugins on spigot that do similar already, they don't work the way I want them to. This is a all in solution for both Proxy and Backend.
I have no interest in providing ancient 1.8 support.
BungeeCord does not use org.apache.logging.log4j framework and I do.
