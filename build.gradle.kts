plugins {
	java
	id("org.springframework.boot") version "4.1.0-RC1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "wf.garnier.spring.security"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.htmlunit:htmlunit")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
