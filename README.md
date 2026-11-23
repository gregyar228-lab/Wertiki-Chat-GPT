# SkyRotor — Minecraft Forge 1.20.1

A small rideable helicopter mod for Forge 1.20.1.

## Features

- A custom helicopter entity with a 3D model and animated main and tail rotors.
- Right-click the helicopter to sit in the pilot seat. Shift dismounts as usual.
- Hold **K** for 2 seconds to start the engine. The helicopter then performs an automatic takeoff.
- Hold **WASD** to fly forward/backward and strafe. Your camera direction controls the helicopter's heading.
- Hold **Space** to rise and **Left Alt** to descend.
- Press **V** to release a small fireball straight down. There is a short cooldown.
- Hold **K** for 2 seconds again to switch the engine off; the helicopter will then fall under gravity.
- Multiplayer input is sent to the server and checked against the current pilot.

## Requirements

- Minecraft **1.20.1**
- Minecraft Forge **47.2.0 or newer in the 47.x line**
- Java **17** for building the project

The mod must be installed on both the client and the server for multiplayer use.

## How to use in-game

1. Find **SkyRotor — Вертолёт** in the creative inventory, or use `/give @s skyrotor:helicopter_spawner`.
2. Right-click the top of a solid block with the Helicopter Spawner.
3. Right-click the helicopter to sit in it.
4. Hold **K** until the engine starts (2 seconds).
5. Fly with **WASD**, use **Space / Left Alt** for altitude, and press **V** to drop a fireball.
6. Hold **K** again for 2 seconds to turn the engine off. Press **Shift** to leave the seat.

## Build

The project is a normal ForgeGradle project. Java 17 is required.

On Windows:

```bat
gradlew.bat build
```

On Linux/macOS:

```sh
./gradlew build
```

The custom Gradle launcher downloads Gradle 8.1.1 on first launch. The resulting mod jar is placed in `build/libs/`.

## GitHub Actions

The included `.github/workflows/build.yml` builds the mod whenever code is pushed, a pull request is opened, or the workflow is started manually. After a successful run, open the workflow run on GitHub and download the **SkyRotor-1.20.1** artifact.
