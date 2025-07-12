import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test

class CommonConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        //플러그인이 적용된 모듈에서 실행됨

        project.group = "com.project.hiptour"
        project.version = "0.0.1-SNAPSHOT"

        project.plugins.apply("java")
        // Java 버전 17
        project.extensions.configure(JavaPluginExtension::class.java) {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        // 공통 dependencies
        project.dependencies {
            add("implementation", "org.springframework.boot:spring-boot-starter-web")
            add("testImplementation", "org.springframework.boot:spring-boot-starter-test")
            add("implementation","org.springframework.boot:spring-boot-starter-data-jpa")
            add("implementation", "org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
            add("compileOnly", "org.projectlombok:lombok")
            add("annotationProcessor", "org.projectlombok:lombok")
            add("runtimeOnly", "com.h2database:h2")
            add("testImplementation", "org.springframework.boot:spring-boot-starter-test")
        }

        // JUnit5  설정
        project.tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }
}
