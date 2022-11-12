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

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.5")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.12")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
    testImplementation("org.assertj:assertj-core:3.23.1")
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

tasks {
    "compileJava" {
        dependsOn("openApiGenerate")
    }
    named<Test>("test") {
        useJUnitPlatform()
    }
}
