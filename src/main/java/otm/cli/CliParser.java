package otm.cli;

import otm.tile.SubTilingPolicy;
import picocli.CommandLine;

public class CliParser {
    @CommandLine.Option(names = "--name", description = "set name of the map")
    private String name;

    @CommandLine.Option(names = "--nw", description = "set northwest coordinates")
    private String nw;

    @CommandLine.Option(names = "--se", description = "set southeast coordinates")
    private String se;

    @CommandLine.Option(names = "--zoom", description = "set the zoom")
    private int zoom = 12;

    @CommandLine.Option(names = "--policy", description = "set the shard tiling policy")
    private SubTilingPolicy policy = SubTilingPolicy.EIGHT_BY_EIGHT_SHARDS;

    @CommandLine.Option(names = "--workspace", description = "set the workspace path")
    private String workspaceDir;

    public String getName() {
        return name;
    }

    public String getNw() {
        return nw;
    }

    public String getSe() {
        return se;
    }

    public int getZoom() {
        return zoom;
    }

    public SubTilingPolicy getPolicy() {
        return policy;
    }

    public String getWorkspaceDir() {
        return workspaceDir;
    }
}
