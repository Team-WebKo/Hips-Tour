plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("common-convention")
}

dependencies {
        implementation(project(":common"))
    testImplementation(platform("org.junit:junit-bom:5.12.2"))
}

tasks.named("bootJar") {
    enabled = false
}
4
tasks.getByName<Jar>("jar") {
    enabled = true
}
