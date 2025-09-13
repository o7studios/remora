import studio.o7.remora.plugin.ApiVersion

plugins {
    id("studio.o7.remora")
}


group = "studio.o7"

information {
    artifactId = "playground"
}

plugin {
    enabled = true
    main = "studio.o7.playground.PlaygroundPlugin"
    apiVersion = ApiVersion.PAPER_1_21_8

    libraries.addAll(listOf(
        "fastutil:fastutil:5.0.9"
    ))
}