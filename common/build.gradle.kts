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
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.5.1")
// https://mvnrepository.com/artifact/com.auth0/java-jwt
    implementation("com.auth0:java-jwt:4.5.0")
	implementation(platform("org.junit:junit-bom:5.10.0"))
}
