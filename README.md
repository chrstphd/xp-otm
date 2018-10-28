# X-Plane - OpenTopoMap

The goal of this small project is to provide data to a Moving Map software written by X-Trident for the flight simulation X-Plane.

This is a very rough draft, it's working but some features needs to be added:

* merging tiles together to reduce the number of files;
* defining an area based on lat,lon coordinates tuples instead of lat,lon coordinates + number of tiles;
* refactoring the exception handling;
* add UI;
* package the code;
* ...

## How to ?

### 1. generate the data

1. look at the area you want map, 
2. select the Lat/Lon degrees (52,-4 e.g.)
The toll will generate a one-degree wide map in this version
3. run the class,
4. the PNG files need to be migrated into TGA (see next point), the .map files are ready

### 2. convert the PNG to TGA

The .map already contains the link to a TGA file so you just need to convert the PNG image to a TGA format. 

I use the tool XnConvert to do so.

* change the color depth to 32 bits (because the PNG source is "indexed")
* set the output to TGA
  * no compression
  * bottom-up oriented

### 3. installation

Move the .map and .tga files into the plugins folder of the X-Trident's Tornado (or the Harrier, currently in dev)

## To-Do List

* re-introducing the creation of a map based on 2 Coordinates
* dividing the whole map in smaller images (but greater than 256x256 source)
* create folders to regroup those smaller images


