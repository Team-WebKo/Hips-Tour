plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("common-convention")
}

dependencies {
}

tasks.named("bootJar") {
    enabled = true
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
