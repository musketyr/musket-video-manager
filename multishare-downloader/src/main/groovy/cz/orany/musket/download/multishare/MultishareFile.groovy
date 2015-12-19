package cz.orany.musket.download.multishare

class MultishareFile {

    final String filename
    final String url

    MultishareFile(String filename, String url) {
        this.filename = filename
        this.url = url
    }


    File saveInto(File destinationFolder, DownloadProgressListener progressListener = DownloadProgressListener.NULL) {
        File newFile = new File(destinationFolder, filename)


        URLConnection connection = new URL(url).openConnection()

        int contentLength = connection.getHeaderFieldInt('Content-Length', -1)

        connection.inputStream.withStream { InputStream is ->
            newFile.withOutputStream { OutputStream os ->
                copyStream(is, os, contentLength, progressListener)
            }
        }

        newFile
    }

    private void copyStream(InputStream input, OutputStream output, int contentLength, DownloadProgressListener progressListener) throws IOException {
        byte[] buffer = new byte[4 * 1024]; // Adjust if you want
        int bytesRead;
        int total = 0;

        while ((bytesRead = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
            if (contentLength > 0) {
                progressListener.onNext(filename, ((double) (total += bytesRead)) / (double) contentLength)
            }
        }
        progressListener.onComplete(filename)
    }

}
