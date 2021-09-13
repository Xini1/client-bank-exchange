plugins {
    java
}

group = "ru.1c"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.compileTestJava {
    options.encoding = "UTF-8"
}

dependencies {
    compileOnly(Dependencies.lombok)
    annotationProcessor(Dependencies.lombok)

    testCompileOnly(Dependencies.junitApi)
    testRuntimeOnly(Dependencies.junitEngine)
    testImplementation(Dependencies.assertj)
}

tasks.test {
    useJUnitPlatform()
}