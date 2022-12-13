package portb

import org.ajoberstar.grgit.Grgit
import org.gradle.api.Project

import java.nio.file.Path
import java.nio.file.Paths

class Repo {
    private static def gradleVersionRegex = ~/gradle-([0-9.]+)-/

    private RepoConfig.Options options
    Path directory
    String gradleVersion

    static def fromFolder(Path directory) {
        return new Repo(directory)
    }

    private Repo(Path directory) {
        options = RepoConfig.getConfigurationForRepo(directory.fileName.toString())
        this.directory = directory

        if (options.ignore)
            return

        ensureSettingsGradleExists()
    }

    private def ensureSettingsGradleExists() {
        def file = directory.resolve("settings.gradle").toFile()

        if (!file.exists())
            file.createNewFile()
    }

    private def modifyGradle() {
        directory.resolve(options.buildGradle).append(generateCustomGradleCode())
    }

    private def findGradleVersion() {
        def wrapperConfigText = Paths.get(directory.toString(), "gradle", "wrapper", "gradle-wrapper.properties").text
        def matcher = gradleVersionRegex.matcher(wrapperConfigText)

        matcher.find()

        return matcher.group(1)
    }

    def generateCustomGradleCode() {
        return new StringBuilder().with(false) { _ ->
            use(AppendLine) {
                appendLine("""
                    task _create_sources_jar(type: Jar) {
                        from ${options.sourceJarSrcSet}
                        classifier 'sources'
                        ${gradleSetOutputDir(directory.resolve(options.jarDirectory))}
                    }
            
                    ${options.jarTask}.${gradleSetOutputDir(directory.resolve(options.jarDirectory))}
                    ${options.jarTask}.classifier "${options.enableDeobf ? "deobf" : "obf"}"
                """.stripIndent())

                /*if (useAtRuntime)
                appendLine("""
                    archivesBaseName = "RUNTIME-" + archivesBaseName
                """.stripIndent())*/

                if (options.enableDeobf) {
                    appendLine("""
                    afterEvaluate {
                        tasks.${options.reobfTaskName}.enabled = false;
                    }                    
                    """.stripIndent())
                }

                appendLine(options.customGradleCode)
                // }
            }
        }
    }

    def gradleSetOutputDir(Path directory) {
        def dir = directory.toAbsolutePath().toString().replace("\\", "\\\\")

        if (gradleVersion >= "5.1")
            return "destinationDirectory.set(file('$dir'))"
        else
            return "destinationDir = file('$dir')s"
    }

    def build(Project project) {
        if (options.ignore)
            return

        println("===== BUILD ${directory.fileName} =====")

        def repo = Grgit.open {
            currentDir = directory
        }

        repo.reset(mode: 'hard')

        gradleVersion = findGradleVersion()

        modifyGradle()

        project.exec {
            executable "cmd"
            workingDir directory
            args "/c", "gradlew ${options.gradleArgs} ${options.tasks.join(" ")} _create_sources_jar"
            ignoreExitValue options.ignoreErrors
        }

        println("===== FINISH ${directory.fileName} =====")
    }

    def getJar() {
        def jarDir = directory.resolve(options.jarDirectory)

        if (jarDir == null)
            return null

        return jarDir.toFile().listFiles().find { it.name.endsWith("obf.jar") }
    }

    def getIsDeobfEnabled() {
        return options.enableDeobf
    }

    def getIsRuntime() {
        return options.runtime
    }

    def getIsCompileTime() {
        return options.compileTime
    }

    def getIsIgnored() {
        return options.ignore
    }
}
