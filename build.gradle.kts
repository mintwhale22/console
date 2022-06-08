import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.moowork.gradle.node.npm.NpmTask

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    war
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    id("com.github.node-gradle.node") version "2.2.4"
}

group = "kr.mintwhale"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.2.2")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.4")
    implementation("org.json:json:20220320")
    implementation("org.bgee.log4jdbc-log4j2:log4jdbc-log4j2-jdbc4.1:1.16")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("commons-io:commons-io:20030203.000550")
    implementation("javax.servlet:jstl:1.2")
    implementation("com.auth0:java-jwt:3.13.0")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.220")
    implementation("com.drewnoakes:metadata-extractor:2.18.0")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


node{
    version = "16.14.2"
    distBaseUrl = "https://nodejs.org/dist"

    download = System.getenv("REQUIRE_NODE_INSTALL") != null && System.getenv("REQUIRE_NODE_INSTALL") == "TRUE"

    workDir = file("${project.buildDir}/nodejs")
    yarnWorkDir = file("${project.buildDir}/yarn")
    nodeModulesDir = file("${project.projectDir}")
    npmWorkDir = file("${project.buildDir}/npm")
}

val installDependencies by tasks.registering(com.moowork.gradle.node.yarn.YarnTask::class) {
    args = listOf("install", "--legacy-peer-deps")
    setExecOverrides(closureOf<ExecSpec> {
        setWorkingDir("${project.projectDir}/frontend")
    })
}

val buildReactTask by tasks.registering(com.moowork.gradle.node.yarn.YarnTask::class) {
    // Before buildWeb can run, installDependencies must run
    dependsOn(installDependencies)
    args = listOf("run", "build")
    setExecOverrides(closureOf<ExecSpec> {
        setWorkingDir("${project.projectDir}/frontend")
    })
}

val copyTask by tasks.registering(Copy::class) {
    dependsOn(buildReactTask)
    from(file("${project.projectDir}/frontend/build"))
    into(file("${project.buildDir}/resources/main/static"))
}

tasks.compileKotlin {
    dependsOn(copyTask)
}