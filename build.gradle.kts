plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.openapi.generator") version "6.2.1"
    id("com.google.cloud.tools.jib") version "3.3.1"
    java
    jacoco
}

group = "de.timpavone1990"
description = "Service to filter the AWS IP address ranges"

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
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.12")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("io.micrometer:micrometer-core:1.10.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.awaitility:awaitility:4.2.0")
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

jib {
    from {
        image = "gcr.io/distroless/java17-debian11"
    }
    to {
        image = "timpavone1990/aws-ip-address-range-filter"
        tags = setOf(version.toString())
    }
    container {
        labels.put("maintainer", "Tim Pavone <tim.pavone@t-online.de>")
        ports = listOf("8080")
    }
}

tasks {
    "check" {
        finalizedBy("jacocoTestReport", "jacocoTestCoverageVerification")
    }
    "compileJava" {
        dependsOn("openApiGenerate")
    }
    named<Test>("test") {
        useJUnitPlatform()
    }
    named<AbstractCopyTask>("processResources") {
        filesMatching("application.yaml") {
            expand(project.properties)
        }
    }

    jacocoTestReport {
        reports {
            xml.required.set(false)
            csv.required.set(false)
            html.required.set(true)
        }
    }

    jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    minimum = "0.7".toBigDecimal()
                }
            }
        }
    }
}
