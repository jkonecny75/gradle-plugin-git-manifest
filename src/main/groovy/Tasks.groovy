/**
 * Created by konecny on 28.5.18.
 */

import org.gradle.api.Project


class Tasks {
    GitManifestPluginExtension extension
    Project project

    Tasks(Project _p) {
        this.project = _p
        this.extension = (GitManifestPluginExtension) project.extensions.findByName('gitManifest')
    }

    def addTasks() {
        addTaskGitManifestHelp()
        addTaskPrepareSources()
    }

    private addTaskGitManifestHelp() {
        project.task('gitManifestHelp') {
            group = Global.GROUP
            description = "Show quick guide"
            doLast {
                println """
TODO
Properties:
- manifest file = ${extension.fileName}
                """
            }
        }
    }

    private addTaskPrepareSources() {
        project.task('prepareSources') {
            group = Global.GROUP
            description = 'Clone and check out all GIT repositories which constitute this project in "manifest.xml"'

            doLast {
                def parsedProjectXml = new XmlParser().parse(project.file(extension.fileName))
                parsedProjectXml.projects.project.each { p ->
                    println "Preparing >> source: " + p.@name + ", version : " + p.@revision
                    def dir = project.file("sources/${p.@path}")
                    if (!dir.exists()) {
                        dir.mkdirs()
                        project.exec {
                            def baseURL = parsedProjectXml.remotes.remote.find { r -> r.@name == p.@remote }.@fetch
                            def gitCommand = ['git', 'clone', '-v', '--progress', baseURL + '/' + p.@name, p.@path]
                            println gitCommand
                            def stdout = new ByteArrayOutputStream()
                            workingDir project.file('sources')
                            commandLine gitCommand
                            standardOutput = stdout
                            println stdout.toString().trim()
                        }
                    } else {
                        println "The source ${dir} has already cloned !"
                    }
                    project.exec {
                        def gitCommand = ['git', 'checkout', p.@revision]
                        println gitCommand
                        def stdout = new ByteArrayOutputStream()
                        workingDir dir
                        commandLine gitCommand
                        standardOutput = stdout
                        println stdout.toString().trim()
                    }
                }
            }
        }
    }
}
