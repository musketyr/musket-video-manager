package cz.orany.musket.download.multishare

import geb.Browser

import java.util.regex.Pattern

class MultishareDownloader {

    private static final Pattern DOWNLOAD_TEXT_PATTERN = Pattern.compile('file|stah?nout soubor', Pattern.CASE_INSENSITIVE)

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

            waitFor { $('a', text: contains(DOWNLOAD_TEXT_PATTERN)).displayed }
            url = $('a', text: contains(DOWNLOAD_TEXT_PATTERN)).attr('href')
            waitFor { $('.file-detail h1').displayed }

            filename = $('.file-detail h1').text()
        }
        return new MultishareFile(filename, url)
    }

}
