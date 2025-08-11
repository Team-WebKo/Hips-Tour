plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("common-convention")
}

dependencies {
    implementation(project(":common"))
    implementation ("com.h2database:h2")
        testImplementation(platform("org.junit:junit-bom:5.12.2"))
}

tasks.named("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
