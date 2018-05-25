import org.gradle.api.Plugin
import org.gradle.api.Project

class Global {
    static GROUP = 'Gradle Git manifest.xml'
}

class GitManifestPluginExtension {
    String fileName = 'manifest.xml'
}

class GitManifestPlugin implements Plugin<Project> {
    void apply(Project project) {
        // Add the 'gitManifest' extension object
        def extension = project.extensions.create('gitManifest', GitManifestPluginExtension)
        // Add a task that uses configuration from the extension object
        project.task('gitManifestHelp') {
            group = Global.GROUP
            description = "manifest file = ${extension.fileName}"
          doLast {
            println extension.fileName
          }
        }
       project.task('prepareSources') {
          group = Global.GROUP
          description = 'Clone and check out all GIT repositories which constitute this project in "manifest.xml"'

          doLast {
              def parsedProjectXml = new XmlParser().parse(project.file('manifest.xml'))
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

