package otm.area;

import otm.OpenTopoMap;
import otm.tile.SubTilingPolicy;

import java.nio.file.Path;

public abstract class Area {
    public static final long MAX_SHARDS_TO_PROCESS = 50000L;

    private int zoom;
    private SubTilingPolicy policy;

    private String name;
    private Path areaWorkFolderPath;

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
        this.areaWorkFolderPath = OpenTopoMap.OTM_WORK_DIR.resolve(name);
        return this;
    }

    /**
     * To generate the area into a folder based on the area name
     *
     * @throws Exception
     */
    public void generate() throws Exception {
        if (areaWorkFolderPath.toFile().exists()) {
            System.out.println("aborting generation since the output folder already exists: " + areaWorkFolderPath);
            return;
        }

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

        doGenerate(outputFolderPath);
    }

    protected abstract void doGenerate(Path outputFolderPath) throws Exception;
}
