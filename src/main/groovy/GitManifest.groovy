import groovy.transform.CompileStatic
import groovy.transform.Field
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.GradleVersion

class Global {
    static GROUP = 'Gradle Git manifest.xml'
}

class GitManifestPluginExtension {
    String fileName = 'manifest.xml'
    String sourcesDirName = 'sources'
}

@CompileStatic
class GitManifestPlugin implements Plugin<Project> {

    void apply(Project project) {
        // Add the 'gitManifest' extension object
        def extension = project.extensions.create('gitManifest', GitManifestPluginExtension)

        if (GradleVersion.current() < GradleVersion.version('3.0')) {
            throw new GradleException('GradleGitManifest requires Gradle 3.0 or later')
        } else {
            new Tasks(project).addTasks()
        }
    }
}

