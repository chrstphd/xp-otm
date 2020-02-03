package otm.area;

import otm.Context;
import otm.OpenTopoMap;
import otm.tile.SubTilingPolicy;

import java.awt.Color;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class Area {
    public static final long MAX_SHARDS_TO_PROCESS = 50000L;

    private int zoom;
    private SubTilingPolicy policy;

    private String name;
    private Path areaWorkFolderPath;

    private Map<String, Color> colouredLayers = new HashMap<>();

    public Area() { /* empty */ }

    public int getZoom() {
        return zoom;
    }

    public Area setZoom(int zoom) {
        this.zoom = zoom;
        return this;
    }

    public SubTilingPolicy getPolicy() {
        return policy;
    }

    public Area setPolicy(SubTilingPolicy policy) {
        this.policy = policy;
        return this;
    }

    public String getName() {
        return name;
    }

    public Area setName(String name) {
        this.name = name;
        this.areaWorkFolderPath = Context.getInstance().getWorkPath().resolve(name);
        return this;
    }

    public Area withColouredLayer(String name, Color color) {
        colouredLayers.put(name, color);
        return this;
    }

    /**
     * To generate the area into a folder based on the area name
     *
     * @throws Exception
     */
    public void generate() throws Exception {
        generate(areaWorkFolderPath);
    }

    /**
     * To generate the area into a folder based on an external parameter, from MultiArea probably.
     *
     * @param outputFolderPath
     * @throws Exception
     */
    public void generate(Path outputFolderPath) throws Exception {
        if (!outputFolderPath.toFile().exists()) {
            outputFolderPath.toFile().mkdirs();
        }

        outputFolderPath.resolve("10-maps").toFile().mkdirs();
        doGenerate(outputFolderPath.resolve("10-maps"));

        colouredLayers.entrySet().stream()
                .forEach(entry -> {
                    File outputFolderFile = outputFolderPath.resolve("20-layer-" + entry.getKey()).toFile();
                    outputFolderFile.mkdirs();
                    doColourLayer(entry.getKey(), entry.getValue(), outputFolderFile.toPath());
                });

    }

    protected abstract void doGenerate(Path outputFolderPath) throws Exception;

    public abstract void describe();

    //FIXME should not throw a RuntimeException to please to the caller's lambda
    protected abstract void doColourLayer(String name, Color color, Path outputFolderPath) throws RuntimeException;
}
