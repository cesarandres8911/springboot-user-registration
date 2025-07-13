plugins {
	java
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

repositories {
	mavenCentral()
}

object Versions {
    const val SPRINGDOC = "2.8.9"
    const val LOMBOK = "1.18.32"
    const val H2 = "2.2.224"
    const val JUNIT_PLATFORM_LAUNCHER = "1.10.2"
}


dependencies {
	// --- Implementation dependencies ---
	implementation("org.projectlombok:lombok:${Versions.LOMBOK}")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springdoc:springdoc-openapi-starter-common:${Versions.SPRINGDOC}")

	// --- Annotation processors ---
	annotationProcessor("org.projectlombok:lombok:${Versions.LOMBOK}")
	testAnnotationProcessor("org.projectlombok:lombok:${Versions.LOMBOK}")

	// --- Runtime dependencies ---
	runtimeOnly("com.h2database:h2:${Versions.H2}")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher:${Versions.JUNIT_PLATFORM_LAUNCHER}")

	// --- Test dependencies ---
	testImplementation("org.projectlombok:lombok:${Versions.LOMBOK}")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
