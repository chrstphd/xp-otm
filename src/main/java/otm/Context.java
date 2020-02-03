package otm;

import java.nio.file.Path;

public class Context {

    private Path cachePath;
    private Path workPath;

    private Context() {}

    private static class ContextLazyHolder {
        static final Context INSTANCE = new Context();
    }

    public static Context getInstance() {
        return ContextLazyHolder.INSTANCE;
    }

    public void setWorkspacePath(Path workspacePath) throws Exception {
        if (!workspacePath.toFile().exists()) {
            if (!workspacePath.toFile().mkdirs()) {
                throw new Exception("unable to init workspace folder: " + workspacePath);
            }
        }

        cachePath = workspacePath.resolve("cache");
        if (!cachePath.toFile().exists()) {
            if (!cachePath.toFile().mkdirs()) {
                throw new Exception("unable to init cache folder: " + cachePath);
            }
        }

        workPath = workspacePath.resolve("work");
        if (!workPath.toFile().exists()) {
            if (!workPath.toFile().mkdirs()) {
                throw new Exception("unable to init work folder: " + workPath);
            }
        }
    }

    public Path getCachePath() {
        return cachePath;
    }

    public Path getWorkPath() {
        return workPath;
    }
}
