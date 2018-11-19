# X-Plane - OpenTopoMap

The goal of this small project is to provide data to a Moving Map software written by X-Trident for the flight simulation X-Plane.

## Process

This tool will fetch the images from the OpenTopoMap servers.

The area to cover will be splitted into tiles. Each tile represents a 1° wide and 1° high rectangle.

Each tile will be splitted into shards. A shard represents the base unit, linked to the zoom level: an image retrieved from the servers.

An image is fixed to 256x256 pixels.

To facilitate the management/sharing/storage of thousand of mini-maps, the process will "unshard" each tile and will merge a part of those images.
The policy of the merge is quite simple:
* a complete merge: one image per tile, easy to manipulate but the image could be heavy on the VRAM;
* no merge: hard to manipulate but really light on the VRAM;
* 4 by 4: the shards will be regrouped 4 by 4, the main sub-tiles will be 1024x1024 pixels images;
* 8 by 8: the shards will be regrouped 8 by 8, the main sub-tiles will be 2048x2048 pixels images; 

## How to launch the process ?

In this version, there is no UI yet.

### 1. generate the data

1. look at the area you want map; (on www.skyvector.com e.g.)
2. select the upper left airport ICAO id and the opposite lower right airport ICAO id;
3. specify a zoom level (13 by default);
4. specify a SubTiling policy to regroup shards;
3. run the class;
4. the PNG files need to be migrated into TGA (see next point).

### 2. convert the PNG to TGA

The .map already contains the link to a TGA file so you just need to convert the PNG image to a TGA format. 

I use the tool XnConvert to do so by batch.

* change the color depth to 32 bits (because the PNG source is "indexed")
* set the output to TGA
  * no compression
  * bottom-up oriented

### 3. installation

Move the .map and .tga files into the plugins folder of the X-Trident's Tornado (or the Harrier, currently in dev)

## To-Do List

* re-introducing the creation of a map based on 2 Coordinates: DONE
* dividing the whole map in smaller images (but greater than 256x256 source): DONE
* create folders to regroup those smaller images: DONE
* rework the whole error management
* add UI
* package the tool for final user


