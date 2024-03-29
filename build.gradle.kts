import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

group = "dev.rollczi"
version = "1.3.0"

repositories {
    mavenCentral()

    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://repo.panda-lang.org/releases") }
    maven { url = uri("https://repository.minecodes.pl/snapshots") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    implementation("dev.rollczi.litecommands:bukkit:2.8.9")
    implementation("net.dzikoysk:cdn:1.14.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

bukkit {
    main = "dev.rollczi.minecraftlista.MinecraftListaPlugin"
    apiVersion = "1.13"
    author = "Rollczi"
    name = "MinecraftListaPlugin"
    version = "${project.version}"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("minecraft-lista-plugin-v${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "META-INF/**"
    )

    mergeServiceFiles()
    minimize()

    val prefix = "dev.rollczi.minecraftlista.libs"
    listOf(
        "panda.std",
        "panda.utilities",
        "net.dzikoysk",
        "okhttp3",
        "okio",
        "org.json",
        "kotlin",
        "dev.rollczi.litecommands"
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }
}

tasks {
    runServer {
        minecraftVersion("1.19")
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
