plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes["Main-Class"] = "Main"
    }
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes["Main-Class"] = "Main"
    }
    // Этот блок упаковывает все runtime-зависимости внутрь JAR-файла
    val dependencies = configurations.runtimeClasspath.get().map { zipTree(it) }
    from(dependencies)
    
    // Предотвращает ошибку дублирования служебных файлов (манифестов зависимостей)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE 
}
