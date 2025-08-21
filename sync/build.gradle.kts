plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("common-convention")
}

dependencies {
    implementation(project(":common"))

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.named("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

tasks.test {
    useJUnitPlatform()
}