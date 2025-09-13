plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("common-convention")
}

dependencies {
}

tasks.named("bootJar") {
    enabled = enabled
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
