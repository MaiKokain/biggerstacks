package portb

import groovy.transform.ToString

import java.nio.file.Path
import java.nio.file.Paths

@ToString
class RepoConfig {
    static Path submodulesDirectory
    static Map<String, Options> repoConfigs = [:]

    RepoConfig() {}

    def methodMissing(String name, Object _args) {
        def args = _args as ArrayList<Object>

        if (args.last() instanceof Closure) {
            def closure = args.last() as Closure

            closure.delegate = repoConfigs[name] = new Options()

            closure()
        } else {
            throw new MissingMethodException(name, RepoConfig, _args)
        }
    }

//    def getAt(String repoName)
//    {
//        return repoConfigs.getOrDefault(repoName, new Options())
//    }

    static def getConfigurationForRepo(String repoName) {
        return repoConfigs.getOrDefault(repoName, new Options())
    }

    def setSubmodulesDirectory(Path dir) {
        submodulesDirectory = dir
    }

    @ToString
    static class Options {
        String reobfTaskName = "reobfJar",
               sourceJarSrcSet = "sourceSets.main.allSource",
               gradleArgs = "",
               jarTask = "jar", //the task that produces the jar, e.g. jar or shadowJar
               customGradleCode = ""

        List<String> tasks = ["jar"] //used to do whatever is needed to build the project

        Boolean enableDeobf = true,
                runtime = true,
                compileTime = true,
                ignore = false,
                ignoreErrors = false

        Path buildGradle = Paths.get("build.gradle"),
             jarDirectory = Paths.get("build", "submoduleJars")
    }
}
