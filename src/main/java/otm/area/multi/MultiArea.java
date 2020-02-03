package otm.area.multi;


import otm.area.Area;

import java.awt.Color;
import java.nio.file.Path;
import java.util.List;

public final class MultiArea extends Area {

    private final List<Area> areas;

    public MultiArea(List<Area> areas) {
        this.areas = areas;
    }

    @Override
    protected void doGenerate(Path outputFolderPath) throws Exception {
        for (Area area : areas) {
            area.generate(outputFolderPath);
        }
    }

    @Override
    public void describe() {
        System.out.println("no description");
    }

    @Override
    protected void doColourLayer(String layerName, Color color, Path outputFolderPath) throws RuntimeException {

    }
}
