plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	id("common-convention")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("com.h2database:h2")
	testImplementation("com.h2database:h2")
	runtimeOnly("com.mysql:mysql-connector-j")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation ("org.springframework.boot:spring-boot-starter-oauth2-client")

}
