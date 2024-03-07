#!/bin/sh
#
# git am -3 --ignore-whitespace -k ./submodules/BiggerStacksTransformerLib.patch
# git am -3 --ignore-whitespace -k ./submodules/MinecraftSubmoduleDependencyPlugin.patch
patch ./submodules/MinecraftSubmoduleDependencyPlugin/build.gradle ./submodules/module_dep/SubmoduleDependency.build.patch
patch ./submodules/MinecraftSubmoduleDependencyPlugin/src/main/groovy/portb/submoduleplugin/Repo.groovy ./submodules/module_dep/SubmoduleDependency.repo.patch

patch ./submodules/BiggerStacksTransformerLib/build.gradle ./submodules/TransformerLib.patch