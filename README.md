[![Divine Golem Logo](https://raw.githubusercontent.com/DevPieter/Divine-Golem/master/docs/images/divine-golem-icon.svg)](https://modrinth.com/mod/divine-golem)

# Divine Golem

[![Modrinth](https://img.shields.io/modrinth/dt/JZYRD8ij?color=blue&label=Downloads&logo=modrinth)](https://modrinth.com/mod/divine-golem)
![License](https://img.shields.io/github/license/DevPieter/Divine-Golem?color=blue&label=License&logo=github)

- [Download](#download)
- [Features](#features)
- [Description](#description)
- [Roadmap](#roadmap)

<img src="https://raw.githubusercontent.com/DevPieter/Divine-Golem/master/docs/images/golem-tracker-hud.png?raw=true" width="368" height="190"></img>
<br>
<img src="https://raw.githubusercontent.com/DevPieter/Divine-Golem/master/docs/images/golem-tracker-and-countdown-hud.png?raw=true" width="412" height="276"></img>
<br>
<img src="https://raw.githubusercontent.com/DevPieter/Divine-Golem/master/docs/images/golem-location.png?raw=true" width="600" height="500"></img>

## Download

> [!IMPORTANT]
> This mod is currently in very early stages of development and is by far not complete, expect bugs and missing features!

You can download Divine Golem on Modrinth: [Download Divine Golem](https://modrinth.com/mod/divine-golem)

![Mod loader: Fabric](https://img.shields.io/badge/Modloader-Fabric-1976d2?logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAAsTAAALEwEAmpwYAAAFHGlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDIgNzkuMTYwOTI0LCAyMDE3LzA3LzEzLTAxOjA2OjM5ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOCAoV2luZG93cykiIHhtcDpDcmVhdGVEYXRlPSIyMDE4LTEyLTE2VDE2OjU0OjE3LTA4OjAwIiB4bXA6TW9kaWZ5RGF0ZT0iMjAxOS0wNy0yOFQyMToxNzo0OC0wNzowMCIgeG1wOk1ldGFkYXRhRGF0ZT0iMjAxOS0wNy0yOFQyMToxNzo0OC0wNzowMCIgZGM6Zm9ybWF0PSJpbWFnZS9wbmciIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiIHBob3Rvc2hvcDpJQ0NQcm9maWxlPSJzUkdCIElFQzYxOTY2LTIuMSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDowZWRiMWMyYy1mZjhjLWU0NDEtOTMxZi00OTVkNGYxNGM3NjAiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MGVkYjFjMmMtZmY4Yy1lNDQxLTkzMWYtNDk1ZDRmMTRjNzYwIiB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9InhtcC5kaWQ6MGVkYjFjMmMtZmY4Yy1lNDQxLTkzMWYtNDk1ZDRmMTRjNzYwIj4gPHhtcE1NOkhpc3Rvcnk+IDxyZGY6U2VxPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0iY3JlYXRlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDowZWRiMWMyYy1mZjhjLWU0NDEtOTMxZi00OTVkNGYxNGM3NjAiIHN0RXZ0OndoZW49IjIwMTgtMTItMTZUMTY6NTQ6MTctMDg6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE4IChXaW5kb3dzKSIvPiA8L3JkZjpTZXE+IDwveG1wTU06SGlzdG9yeT4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4/HiGMAAAAtUlEQVRYw+XXrQqAMBQF4D2P2eBL+QIG8RnEJFaNBjEum+0+zMQLtwwv+wV3ZzhhMDgfJ0wUSinxZUQWgKos1JP/AbD4OneIDyQPwCFniA+EJ4CaXm4TxAXCC0BNHgLhAdAnx9hC8PwGSRtAFVMQjF7cNTWED8B1cgwW20yfJgAvrssAsZ1cB3g/xckAxr6FmCDU5N6f488BrpCQ4rQBJkiMYh4ACmLzwOQF0CExinkCsvw7vgGikl+OotaKRwAAAABJRU5ErkJggg==)
![Environment](https://img.shields.io/badge/Environment-client-1976d2)

![Modrinth Version](https://img.shields.io/modrinth/v/JZYRD8ij?color=blue&label=Version&logo=modrinth)
![Modrinth Game Versions](https://img.shields.io/modrinth/game-versions/JZYRD8ij?color=blue&label=Game%20Versions&logo=modrinth)

## Features

- **Stage 5 Notifications**: Get notified when the End Stone Protector reaches stage 4 and stage 5.
- **Location and Stage Display**: Always know where the End Stone Protector is and which stage it is at.
- **Spawn Countdown Timer**: A helpful countdown timer that shows when the End Stone Protector will spawn, also TPS adjusted.
- **Loot Detection**: Get notified about rare loot drops from the End Stone Protector, such as the Tier Boost Core and the Legendary Golem Pet.
- **Loot Quality Calculation**: Shows the quality of loot you could get from the End Stone Protector.
- **DPS Calculation**: Shows your current DPS on the End Stone Protector.
- **HUD Overlay with Editor**: A customizable HUD that displays all the important information you might need.

## Description

**Divine Golem** is a **Hypixel Skyblock Utility** mod focused on the **End Stone Protector**, it is currently in very
early stages of development and is by far not complete.

## Roadmap

- [x] Golem Location and Stage Detection
- [X] HUD Overlay with Editor
- [x] Stage 4/5 Notifications
- [x] Spawn countdown timer
- [x] Loot Detection (Tier Boost Core/Legendary Golem Pet notifications)
- [x] Loot Quality Calculation
- [x] DPS Calculation
- [ ] Warp Hotkeys
- [ ] And much more!
