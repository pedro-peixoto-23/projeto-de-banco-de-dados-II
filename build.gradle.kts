plugins {
    id("java")
}

group = "ifpb"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JPA
    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")

    // Hibernate
    implementation("org.hibernate.orm:hibernate-core:7.2.1.Final")

    // H2
    implementation("com.h2database:h2:2.4.240")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.40")
    annotationProcessor("org.projectlombok:lombok:1.18.40")

    // Validação de e-mail
    implementation("commons-validator:commons-validator:1.10.1")

    // Envio de e-mail
    implementation("org.apache.commons:commons-email2-javax:2.0.0-M1")

    // Geração de PDF
    implementation("com.itextpdf:kernel:9.6.0")
    implementation("com.itextpdf:layout:9.6.0")
}

tasks.test {
    useJUnitPlatform()
}