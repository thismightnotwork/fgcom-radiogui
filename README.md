# FGCom-mumble radioGUI — SkyHigh Network Edition

A fork of [hbeni/fgcom-mumble](https://github.com/hbeni/fgcom-mumble) radioGUI client modified for use with the [SkyHigh Network](https://skyhighnetwork.co.uk).

## Changes from upstream

- **SimConnect removed entirely** — no `jsimconnect` dependency required.
- **SkyHighFetcher added** — polls the SkyHigh OpenFSD JSON API every 5 seconds:  
  `https://fsd.skyhighnetwork.co.uk/api/v1/data/openfsd-data.json`
- **CID input box** added to the main window.
- **"Slave to SkyHigh"** menu item replaces "Slave to SimConnect".
- `org.json` added as a Maven dependency for JSON parsing.

## Building

```bash
# 1. Install jmapviewer locally (download jmapviewer-2.14.jar first)
mvn install:install-file \
  -Dfile=lib/jmapviewer-2.14.jar \
  -DgroupId=org.openstreetmap.jmapviewer \
  -DartifactId=jmapviewer \
  -Dversion=2.14 \
  -Dpackaging=jar

# 2. Build fat JAR
mvn package

# 3. Run
java -jar target/FGCom-mumble-radioGUI-1.2.1-skyhigh-jar-with-dependencies.jar
```

## Usage

1. Enter your **SkyHigh Network CID** in the CID field at the top of the window.
2. Click **RadioGUI → Slave to SkyHigh** — the app will start polling the API and update LAT/LON/ALT automatically every 5 seconds.
3. Press **Connect** to start sending UDP packets to the local fgcom-mumble Mumble plugin (port 16661 by default).
4. Set your radio frequencies in the COM panels.

## Project structure

```
pom.xml
README.md
src/
  main/
    java/hbeni/fgcom_mumble/
      radioGUI.java          ← entry point
      State.java             ← data model
      Radio.java             ← single radio model
      UDPclient.java         ← sends fgcom-mumble UDP packets
      SkyHighFetcher.java    ← polls SkyHigh JSON API (replaces SimConnect)
      MapWindow.java         ← location picker (stub; needs jmapviewer)
      gui/
        MainWindow.java      ← main window (CID box + SkyHigh menu)
        RadioInstance.java   ← per-radio panel
        OptionsWindow.java   ← options dialog
        About_Help.java      ← about dialog
        LicenseWindow.java   ← license dialog
    resources/
      project.properties     ← version info
```
