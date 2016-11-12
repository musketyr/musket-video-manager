package cz.orany.musket.download.multishare

import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.LaxRedirectStrategy

class MultishareFile {

    final String filename
    final String url

    MultishareFile(String filename, String url) {
        this.filename = filename
        this.url = url
    }


    File saveInto(File destinationFolder, DownloadProgressListener progressListener = DownloadProgressListener.NULL) {
        File newFile = new File(destinationFolder, filename)

        HttpClient client = HttpClientBuilder
                .create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build()

        HttpGet get = new HttpGet(url)
        HttpResponse response = client.execute(get)
        response.entity.contentLength

        newFile.withOutputStream { OutputStream os ->
            copyStream(response.entity.content, os, response.entity.contentLength, progressListener)
        }

        newFile
    }

    private void copyStream(InputStream input, OutputStream output, long contentLength, DownloadProgressListener progressListener) throws IOException {
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
