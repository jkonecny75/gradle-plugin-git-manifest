import org.gradle.api.Plugin
import org.gradle.api.Project

class GitManifestPluginExtension {
    String fileName = 'manifest.xml'
}

class GitManifestPlugin implements Plugin<Project> {
    void apply(Project project) {
        // Add the 'gitManifest' extension object
        def extension = project.extensions.create('gitManifest', GitManifestPluginExtension)
        // Add a task that uses configuration from the extension object
        project.task('gitManifestHelp') {
            doLast {
                println extension.fileName
            }
        }
    }
}

