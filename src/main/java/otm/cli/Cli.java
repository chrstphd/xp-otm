package otm.cli;

import otm.Context;
import otm.area.Area;
import otm.area.AreaFactory;
import otm.util.CoordinatesHelper;
import picocli.CommandLine;

import java.nio.file.Paths;

public class Cli {
    public static void main(String[] args) {

        // --name "Faroe Islands North Test Cli" --zoom 12 --nw "N62°21.02' W7°1.99'" --se "N62°19.59' W6°58.74'" --workspace ""h:/XPlaneComplements/OTM"

        CliParser parser = new CliParser();
        new CommandLine(parser).parseArgs(args);

        try {
            Context.getInstance().setWorkspacePath(Paths.get(parser.getWorkspaceDir()));

            Area area = AreaFactory.buildRectangular(
                    parser.getName(), parser.getZoom(), parser.getPolicy(),
                    CoordinatesHelper.toCoordinates(parser.getNw()),
                    CoordinatesHelper.toCoordinates(parser.getSe())
            );

            area.generate();

            area.describe();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
