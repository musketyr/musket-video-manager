package cz.orany.musket.download.multishare

import geb.spock.GebReportingSpec

import spock.lang.Stepwise


@Stepwise
class ScraperSpec extends GebReportingSpec {

    MultishareDownloader downloader = MultishareDownloader.create(System.getenv('MULTISHARE_USERNAME') ?: System.getProperty('multishare.username'), System.getenv('MULTISHARE_PASSWORD') ?: System.getProperty('multishare.password'))

    def "test setup"() {
        expect: true
    }

    def "open multishare"() {
        when: downloader.login()
        then: noExceptionThrown()
    }


    def "open a file"() {
        MultishareFile file = downloader.getFile("https://www.multishare.cz/stahnout/765179/02x02-small-tears-srt")

        expect:
        file
        file.url
        file.url.startsWith('http://dl')
        file.filename == '02x02 - Small Tears.srt'

        when:
        File newFile = file.saveInto new File("/tmp")

        then:
        newFile.exists()
        newFile.size()
    }

}
