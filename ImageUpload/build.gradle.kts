plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("java-library")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("net.coobird:thumbnailator:0.4.20")
    implementation("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")

    testRuntimeOnly("com.h2database:h2")
}