package portb

import org.gradle.api.Plugin
import org.gradle.api.Project


class SubmodulesPlugin implements Plugin<Project> {
    private def depPattern = ~/([a-zA-z0-9]+)-(.+)-(.+)\.jar/

    @Override
    void apply(Project project) {
        def collector = project.extensions.create("submodules", RepoConfig)

        project.task("submodules") {
            project.afterEvaluate {

                assert RepoConfig.submodulesDirectory != null, "You must set submodulesDirectory"

                //configurations for dependencies
                def subImplementation = project.configurations.create("submodules")
                def subRuntime = project.configurations.create("submodules_runtime")
                def subCompiletime = project.configurations.create("submodules_compile")

                project.configurations.implementation.extendsFrom(subImplementation)
                project.configurations.runtimeOnly.extendsFrom(subRuntime)
                project.configurations.compileOnly.extendsFrom(subCompiletime)

                RepoConfig.submodulesDirectory.toFile().listFiles().each {
                    def path = it.toPath()

                    if (path == RepoConfig.submodulesDirectory)
                        return

                    def repo = Repo.fromFolder(path)

                    //don't do anything if the repo is ignored
                    if (repo.isIgnored)
                        return

                    //build the project if the jar doesn't exist
                    if (repo.jar == null)
                        repo.build(project)

                    //have to do this because fg.deobf doesn't support files(...) for stupid reason
                    /*project.repositories {
                        flatDir {
                            dir repo.jar.parent
                        }
                    }

                    def matcher = depPattern.matcher(repo.jar.name)
                    matcher.find()

                    def artifact = matcher.group(1)
                    def version = matcher.group(2)
                    def classifier = matcher.group(3)

                    def dep = "submodules:$artifact:$version:$classifier" */

                    def dep = project.files(repo.jar)

                    //add the dependency
                    if (repo.isRuntime) {
                        if (repo.isCompileTime)
                            subImplementation.dependencies.add(project.dependencies.create(dep))
                        else
                            subRuntime.dependencies.add(project.dependencies.create(dep))
                    } else if (repo.isCompileTime) {
                        subCompiletime.dependencies.add(project.dependencies.create(dep))
                    }
                }
            }
        }
    }
}
