import cz.orany.musket.download.multishare.MultishareDownloader
import cz.orany.musket.download.multishare.MultishareFile
import cz.orany.musket.webapp.InMemoryDownloadProgressListener
import ratpack.exec.Promise
import ratpack.form.Form
import ratpack.groovy.template.MarkupTemplateModule

import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.groovy.Groovy.ratpack


ratpack {

    bindings {
        module MarkupTemplateModule
    }

    handlers {

        get {
            render groovyMarkupTemplate("index.gtpl", title: "My Ratpack App")
        }

        post('add-download') { context ->
            Promise<Form> form = context.parse(Form)

            form.then { f ->
                String url = f['url']


                Promise.of { down ->
                    new Thread({
                        MultishareDownloader downloader = MultishareDownloader.create(System.getenv('MULTISHARE_USERNAME'), System.getenv('MULTISHARE_PASSWORD'))
                        MultishareFile file = downloader.getFile(url)
                        InMemoryDownloadProgressListener.INSTANCE.addFile(file.filename)
                        down.success('QUEUED')
                        file.saveInto(new File(System.getenv('MULTISHARE_FOLDER')), InMemoryDownloadProgressListener.INSTANCE)
                    }).start()
                }.then {
                    context.redirect '/'
                }
            }
        }
    }
}