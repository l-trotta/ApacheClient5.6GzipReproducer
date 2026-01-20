plugins {
    id("java")
}

group = "co.elastic"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.httpcomponents.client5:httpclient5:5.6")
    implementation("org.junit.jupiter:junit-jupiter-api:6.0.0")
}

tasks.test {
    useJUnitPlatform()
}
