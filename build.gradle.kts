import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project
val kmongo_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.22"
    id("io.ktor.plugin") version "2.2.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.22"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.sample"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    project.setProperty("mainClassName", mainClass.get())

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

val sshAntTask = configurations.create("sshAntTask")

dependencies {
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core-jvm:2.2.2")
    implementation("io.ktor:ktor-server-auth-jvm:2.2.2")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.2.2")
    implementation("io.ktor:ktor-server-websockets-jvm:2.2.2")
    implementation("io.ktor:ktor-server-cors-jvm:2.2.2")
    implementation("io.ktor:ktor-server-default-headers-jvm:2.2.2")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.2.2")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.2.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.2.2")
    implementation("io.ktor:ktor-server-netty-jvm:2.2.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // KMongo
    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")

    // Koin core features
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    // Test dependencies

    // Gson
    implementation("com.google.code.gson:gson:2.10")
    testImplementation("com.google.code.gson:gson:2.10")
    // Koin
    testImplementation("io.insert-koin:koin-test:$koin_version")
    // Truth
    testImplementation("com.google.truth:truth:1.1.3")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("io.ktor:ktor-server-tests-jvm:2.2.2")

    sshAntTask("org.apache.ant:ant-jsch:1.9.2")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    manifest {
        attributes(
            "Main-Class" to application.mainClass.get()
        )
    }
}

ant.withGroovyBuilder {
    "taskdef"(
        "name" to "scp",
        "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.Scp",
        "classpath" to configurations.get("sshAntTask").asPath
    )
    "taskdef"(
        "name" to "ssh",
        "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.SSHExec",
        "classpath" to configurations.get("sshAntTask").asPath
    )
}

task("deploy") {
    dependsOn("clean", "shadowJar")
    ant.withGroovyBuilder {
        doLast {
            val knownHosts = File.createTempFile("knownhosts", "txt")
            val user = "dyc"
            val host = "59.110.165.140"
            val pk = file("keys/appkeynewest")
            val jarFileName = "backendsample-$version-all.jar"
            try {
                "scp"(
                    "file" to file("build/libs/$jarFileName"),
                    "todir" to "root@59.110.165.140:/home/dyc/deployment",
                    "keyfile" to pk,
                    "trust" to true,
                    "knownhosts" to knownHosts
                )
                "ssh"(
                    "host" to host,
                    "username" to user,
                    "keyfile" to pk,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "mv /home/dyc/deployment/$jarFileName /home/dyc/deployment/app-service.jar"
                )
                "ssh"(
                    "host" to host,
                    "username" to user,
                    "keyfile" to pk,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "systemctl stop app"
                )
                "ssh"(
                    "host" to host,
                    "username" to user,
                    "keyfile" to pk,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "systemctl start app"
                )
            } finally {
                knownHosts.delete()
            }
        }
    }
}
