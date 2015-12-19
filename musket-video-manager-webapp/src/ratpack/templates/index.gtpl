import cz.orany.musket.webapp.InMemoryDownloadProgressListener

yieldUnescaped '<!DOCTYPE html>'
html {
  head {
    meta(charset:'utf-8')
    title("Ratpack: $title")

    meta(name: 'apple-mobile-web-app-title', content: 'Ratpack')
    meta(name: 'description', content: '')
    meta(name: 'viewport', content: 'width=device-width, initial-scale=1')

    link rel: "stylesheet", href: "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css", integrity: "sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7", crossorigin:"anonymous"

    link(href: '/images/favicon.ico', rel: 'shortcut icon')
  }
  body {
    div class: 'container', {
       hr()
       div class: 'row', {
        div class: 'col-md-12', {
         form action: '/add-download', method: 'POST', class: 'form', {
           input type: 'text', name: 'url', class: 'form-control', placeholder: 'Paste a link to Multishare file'
           input type: 'submit', value: 'Download', class: 'form-control btn btn-primary btn-block'
         }
        }

       }
        hr()
       div class: 'row', {
        div class: 'col-md-12', {
           table class: 'table table-striped', {
               InMemoryDownloadProgressListener.INSTANCE.FILES.asMap().each { String filename, double value ->
                   tr {
                       th filename, class: 'col-md-4'
                       td class: 'col-md-8', {
                           div class:"progress", {
                            div class:"progress-bar ${value == 1 ? 'progress-bar-success' : 'progress-bar-info progress-bar-striped active'}", role: "progressbar", 'aria-valuenow': "${Math.floor(value * 100)}",  'aria-valuemin': "0",  'aria-valuemax': "100", style: "width: ${Math.floor(value * 100)}%;", {
                                span class: "sr-only", "${Math.floor(value * 100)}% Complete"
                            }
                           }
                       }
                   }
               }
           }
        }
       }

       footer {}
    }
  }
}