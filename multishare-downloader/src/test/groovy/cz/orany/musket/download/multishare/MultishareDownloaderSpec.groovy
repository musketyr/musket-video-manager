package cz.orany.musket.download.multishare

import spock.lang.Specification
import spock.lang.Stepwise


@Stepwise
class MultishareDownloaderSpec extends Specification {

    MultishareDownloader downloader = MultishareDownloader.create(System.getenv('MULTISHARE_USERNAME') ?: System.getProperty('multishare.username'), System.getenv('MULTISHARE_PASSWORD') ?: System.getProperty('multishare.password'))

    def "test setup"() {
        expect: true
    }

    def "open multishare"() {
        when: downloader.login()
        then: noExceptionThrown()
    }


    def "open a file"() {
        MultishareFile file = downloader.getFile("https://www.multishare.cz/stahnout/767839/texticek-txt")

        expect:
        file
        file.url
        file.url.startsWith('http://dl')
        file.filename == 'texticek.txt'

        when:
        File newFile = file.saveInto new File("/tmp"), DownloadProgressListener.CONSOLE

        then:
        newFile.exists()
        newFile.size()
    }

}
