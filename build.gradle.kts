plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.etoxto"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    implementation("net.datafaker:datafaker:1.9.0")

    implementation("org.openjdk.jmh:jmh-core:1.37")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    testImplementation("org.openjdk.jmh:jmh-core:1.37")
    testAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")

    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<JavaExec>("jmh") {
    group = "benchmark"
    description = "Run JMH benchmarks"
    mainClass.set("org.openjdk.jmh.Main")
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs = listOf("-Xms2G", "-Xmx2G")
}
