package cz.orany.musket.download.multishare

class MultishareFile {

    final String filename
    final String url

    MultishareFile(String filename, String url) {
        this.filename = filename
        this.url = url
    }


    File saveInto(File destinationFolder) {
        File newFile = new File(destinationFolder, filename)

        new URL(url).withInputStream { InputStream is ->
            newFile.withOutputStream { OutputStream os ->
                os << is
            }
        }

        newFile
    }
}
