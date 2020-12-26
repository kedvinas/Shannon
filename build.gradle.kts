
plugins {
    `java-library`
}

tasks.withType<Jar> {
    manifest.attributes["Main-Class"] = "Main"
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile)) {
            exclude("META-INF/MANIFEST.MF", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
        }
    }
}