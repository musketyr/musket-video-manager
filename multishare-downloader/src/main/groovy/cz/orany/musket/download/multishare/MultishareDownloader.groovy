package cz.orany.musket.download.multishare

import geb.Browser

import java.util.regex.Pattern

class MultishareDownloader {

    private final String username
    private final String password

    static MultishareDownloader create(String username, String password) {
        return new MultishareDownloader(username, password)
    }

    private MultishareDownloader(String username, String password) {
        this.username = username
        this.password = password
    }

    void login() {
        Browser.drive {
            go "https://www.multishare.cz/uzivatele/prihlaseni"

            waitFor { $('#frmprihlaseni-jmeno').displayed }

            $('#frmprihlaseni-jmeno').findAll { it.displayed }.value(username)
            $('#frmprihlaseni-heslo').findAll { it.displayed }.value(password)
            $('#frmprihlaseni-send').findAll { it.displayed }.click()

            waitFor { $('.user').displayed }

            assert waitFor {
                $('.user').text()?.toLowerCase() == username
            }
        }
    }

    MultishareFile getFile(String fileUrl) {
        String url = null
        String filename = null
        Browser.drive {
            go fileUrl

            if ($('.user').text()?.toLowerCase() != username) {
                login()
                go fileUrl
            }

            waitFor { $('.file-detail h1').displayed }
            filename = $('.file-detail h1').text()

            waitFor { $('a').find{ it.text()?.toLowerCase()?.contains('file') && it.attr('href') }.displayed }
            url = $('a').find{ it.text()?.toLowerCase()?.contains('file') && it.attr('href') }.attr('href')

        }
        return new MultishareFile(filename, url)
    }

    public static final String USAGE = """Usage: username:password "https://www.mutlishare.cz/...url..." /path/to/download/folder"""

    static void main(String... args) {
        if (args.size() != 3) {
            println USAGE
            System.exit(1)
            return
        }

        if (!args[0].contains(':')) {
            println USAGE
            System.exit(1)
            return
        }

        String[] credentials = args[0].split(':')

        MultishareDownloader downloader = create(credentials[0], credentials[1])

        println downloader.getFile(args[1]).saveInto(new File(args[2]), DownloadProgressListener.CONSOLE).canonicalPath
        System.exit(0)
    }
}
