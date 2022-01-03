## Overview
Welcome to Ghostblocks, where blocks are only an illusion! Ghostblocks enables players to craft and build disappearing blocks: these "Ghost blocks" look real, but disappear when players get too close. Ghost blocks are perfect for building secret bases, mob traps, hiding entrances, trolling, and more!

This plugin was originally a Premium resource of mine on Spigot, and having moved on from plugin development, I recently decided to publish the source code for free.

## Usage
To make a Ghostblock, you need to drag a Ghostdrop onto your block of choice within your inventory. Ghostdrops can be crafted by surrounding a Ghast Tear with either Iron Ingots or Gold Ingots. Once crafted, a single Ghostdrop can create a single Ghostblock.

### Commands
/gb get  
/gb reset [radius]  
/gb reload  
/gb truesight

### Permissions
ghostblocks.get - Spawn a Ghostdrop in your inventory!  
ghostblocks.reset - Remove all Ghostblocks from the system.  
ghostblocks.reload - Reloads the plugin configuration.  
ghostblocks.truesight - Enable this option to see through Ghostblocks. Useful for admin, VIPs, and more!  
ghostblocks.place - Allows players to place GhostBlocks (given by default)  
ghostblocks.admin - Includes all permissions

## Installation (Server)
To use the plugin, simply download a `.jar` binary from the [Releases](https://github.com/hwdotexe/Ghostblocks/releases) page for your server's version. You will also need the applicable version of [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/). Place both of these files in your server's `plugins` folder and restart.

## Installation (Development)
If you'd like to install the codebase and make changes, simply follow these instructions:

### Step 1: Clone
In a terminal, use `git clone https://github.com/hwdotexe/Ghostblocks.git`

### Step 2: Import as a Maven Project
This plugin uses Maven as its build system. As such, you'll need to import it using Maven as well. This process varies by IDE, so please seek out their instructions if needed.

### Step 3: Building
When you're ready to compile the plugin locally, you can use `mvn clean package` to generate your `.jar` file. 

## Contributing
If you like this plugin and want to contribute, please do! I welcome any and all Pull Requests from eager developers who want to help out. 
