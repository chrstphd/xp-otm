package otm.util;

import java.util.ArrayList;
import java.util.List;

public class ProgressBar implements AutoCloseable {
    private final String title;
    private List<ProgressItem> items = new ArrayList<>();

    public ProgressBar(String title) {
        this.title = title;
    }

    public ProgressItem createProgressItem(String label, double maxValue) {
        ProgressItem p = new ProgressItem(this, label, maxValue);
        items.add(p);
        return p;
    }

    public void show() {
        StringBuilder builder = new StringBuilder();
        for (ProgressItem item : items) {
            builder.append(item.show());
            builder.append(" ");
        }
        System.out.print("\r" + title + ": " + builder.toString());
    }

    public void remove(ProgressItem item) {
        items.remove(item);
    }

    @Override
    public void close() {
        System.out.println("\r" + title + ": job complete.");
    }

    public class ProgressItem implements AutoCloseable {
        private final ProgressBar progressbar;
        private final String label;
        private final double maxValue;
        private final double STRING_SIZE = 25;
        private double currentValue = 0;
        private String currentDescription;

        public ProgressItem(ProgressBar progressBar, String label, double maxValue) {
            this.progressbar = progressBar;
            this.label = label;
            this.maxValue = maxValue;
        }

        public void increment(String description) {
            currentDescription = description;
            increment();
        }

        public void increment() {
            this.currentValue++;
            progressbar.show();
        }

        protected String show() {
            int marks = (int) ((STRING_SIZE / maxValue) * currentValue);
            StringBuilder builder = new StringBuilder(label);
            builder.append(": [");
            for (int i = 0; i < STRING_SIZE; i++) {
                if (i < marks) {
                    builder.append("#");
                } else {
                    builder.append(" ");
                }
            }
            builder.append("]");

            if(currentDescription!=null){
                builder.append(" ").append(currentDescription);
            }

            return builder.toString();
        }

        @Override
        public void close() {
            progressbar.remove(this);
        }
    }
}
