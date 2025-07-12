plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("common-convention")
}

dependencies {
    implementation(project(":common"))
    implementation ("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine") // JUnit4 제거
    }
}

tasks.named("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
