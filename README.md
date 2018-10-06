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
2. get the North-West (upper left) coordinates,
3. convert those coordinates (degrees) into decimal,
4. evaluate the size of the table (2x2 is a great start !),
5. prepare a working folder to export the files,
6. set the values into the OpenTopoMap source,
7. run the class,
8. the PNG files need to be migrated into TGA (see next point), the .map files are ready

### 2. convert the PNG to TGA

The .map already contains the link to a TGA file so you just need to convert the PNG image to a TGA format. 

I use the tool XnConvert to do so.

* change the color depth to 32 bits (because the PNG source is "indexed")
* set the output to TGA
  * no compression
  * bottom-up oriented

### 3. installation

Move the .map and .tga files into the plugins folder of the X-Trident's Tornado (or the Harrier, currently in dev)