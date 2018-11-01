# How to release

## Setup

1. Get a permission to release MavenCentral(Sonatype)
1. Setup PGP. See [Working with PGP Signatures](https://central.sonatype.org/pages/working-with-pgp-signatures.html)
1. Fill following properties in `~/.gradle/gradle.properties`
```properties
signing.keyId=
signing.password=
signing.secretKeyRingFile=
sonatypeUsername=
sonatypePassword=
```

## Release

1. Upgrade `version` in `build.gradle.kts`
1. Run `./gradlew clean uploadArchives`