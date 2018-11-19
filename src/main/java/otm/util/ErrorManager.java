package otm.util;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ErrorManager {
    private List<Error> errors = new ArrayList<>();

    private static ErrorManager ourInstance = new ErrorManager();

    private static class ErrorManagerHolder {
        static final ErrorManager INSTANCE = new ErrorManager();
    }

    public static ErrorManager getInstance() {
        return ErrorManagerHolder.INSTANCE;
    }

    private ErrorManager() {/* empty */}

    public void addError(String context, String message, Exception exception) {
        errors.add(new Error(context, message, exception));
    }

    public void dump(PrintStream stream) {
        if (errors.isEmpty()) {
            return;
        }

        stream.println("\nerror report:");
        for (int i = 0; i < errors.size(); i++) {
            Error error = errors.get(i);
            stream.println(MessageFormat.format("error #{0}/{1}: {2}: {3}", (i + 1), errors.size(), error.getContext(), error.getMessage()));
            if (error.getException() != null) {
                stream.println("   exception: " + error.getException().toString());
            }
        }
        stream.println("end of report.");
        errors.clear();
    }

    private class Error {
        private final String context;
        private final String message;
        private final Exception exception;

        public Error(String context, String message, Exception exception) {
            this.context = context;
            this.message = message;
            this.exception = exception;
        }

        public String getContext() {
            return context;
        }

        public String getMessage() {
            return message;
        }

        public Exception getException() {
            return exception;
        }
    }
}