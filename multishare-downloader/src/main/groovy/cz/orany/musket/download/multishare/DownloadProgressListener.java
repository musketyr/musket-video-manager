package cz.orany.musket.download.multishare;

import java.text.DecimalFormat;

public interface DownloadProgressListener {

    DownloadProgressListener NULL = new DownloadProgressListener() {
        @Override
        public void onNext(String filename, double currentProgress) {
            // do nothing
        }

        @Override
        public void onComplete(String filename) {
            // do nothing
        }
    };

    DownloadProgressListener CONSOLE = new DownloadProgressListener() {

        private final DecimalFormat FORMAT = new DecimalFormat("000.00");

        @Override
        public void onNext(String filename, double currentProgress) {
            System.out.println( filename + ": " + FORMAT.format(currentProgress * 100) + " %");
        }

        @Override
        public void onComplete(String filename) {
            System.out.println("File downloaded");
        }
    };

    void onNext(String filename, double currentProgress);
    void onComplete(String filename);

}
