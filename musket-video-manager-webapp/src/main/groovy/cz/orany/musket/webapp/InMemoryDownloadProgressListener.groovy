package cz.orany.musket.webapp

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import cz.orany.musket.download.multishare.DownloadProgressListener
import groovy.util.logging.Slf4j

import java.text.DecimalFormat

@Slf4j
enum InMemoryDownloadProgressListener implements DownloadProgressListener {

    INSTANCE;

    private final DecimalFormat FORMAT = new DecimalFormat("000.00");

    final Cache<String, Double> FILES = CacheBuilder.newBuilder().maximumSize(100).build()


    public void addFile(String filename) {
        FILES.put(filename, 0)
    }

    @Override
    public void onNext(String filename, double currentProgress) {
        FILES.put(filename, currentProgress)
        log.debug( filename + ": " + FORMAT.format(currentProgress * 100) + " %");
    }

    @Override
    public void onComplete(String filename) {
        FILES.put(filename, 1)
        log.info ("$filename downloaded");
    }

}
