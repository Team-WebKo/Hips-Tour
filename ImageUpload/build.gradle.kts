plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("common-convention")
}

dependencies {
    implementation("net.coobird:thumbnailator:0.4.20")
    testRuntimeOnly("com.h2database:h2")
}