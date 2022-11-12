plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.openapi.generator") version "6.2.1"
    `java`
}

group = "de.timpavone1990"

val javaVersion = 17

tasks.compileJava {
    options.release.set(javaVersion)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.12")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/specs/aws-ip-address-ranges-filter-v1.0.yaml")
    outputDir.set("$buildDir/generated")
    configFile.set("$rootDir/config/api-config.yaml")
}

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated/src/main/java")
        }
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks {
    "compileJava" {
        dependsOn("openApiGenerate")
    }
}
