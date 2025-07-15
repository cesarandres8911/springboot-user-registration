plugins {
	java
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

object Versions {
    const val SPRINGDOC = "2.8.9"
    const val LOMBOK = "1.18.38"
    const val H2 = "2.2.224"
    const val JUNIT_PLATFORM = "1.12.2"
    const val JUNIT_JUPITER = "5.12.2"
    const val JWT = "0.11.5"
    const val MAPSTRUCT = "1.5.5.Final"
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
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${Versions.SPRINGDOC}")
	implementation("io.jsonwebtoken:jjwt-api:${Versions.JWT}")
	implementation("io.jsonwebtoken:jjwt-impl:${Versions.JWT}")
	implementation("io.jsonwebtoken:jjwt-jackson:${Versions.JWT}")
	implementation("org.mapstruct:mapstruct:${Versions.MAPSTRUCT}")

	// --- Annotation processors ---
	annotationProcessor("org.projectlombok:lombok:${Versions.LOMBOK}")
	annotationProcessor("org.mapstruct:mapstruct-processor:${Versions.MAPSTRUCT}")
	testAnnotationProcessor("org.projectlombok:lombok:${Versions.LOMBOK}")
	testAnnotationProcessor("org.mapstruct:mapstruct-processor:${Versions.MAPSTRUCT}")

	// --- Runtime dependencies ---
	runtimeOnly("com.h2database:h2:${Versions.H2}")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher:${Versions.JUNIT_PLATFORM}")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT_JUPITER}")

	// --- Test dependencies ---
	testImplementation("org.projectlombok:lombok:${Versions.LOMBOK}")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT_JUPITER}")
	testImplementation("org.junit.jupiter:junit-jupiter:${Versions.JUNIT_JUPITER}")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
