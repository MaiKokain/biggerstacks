#!/bin/sh
#
git am -3 --ignore-whitespace -k ./submodules/BiggerStacksTransformerLib.patch
git am -3 --ignore-whitespace -k ./submodules/MinecraftSubmoduleDependencyPlugin.patch
