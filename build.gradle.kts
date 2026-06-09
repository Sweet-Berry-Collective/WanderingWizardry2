plugins {
	`maven-publish`
	alias(libs.plugins.loom)
	alias(libs.plugins.yumi.licenser)
}

version = providers.gradleProperty("mod_version").get()
group = providers.gradleProperty("maven_group").get()

repositories {
	exclusiveContent {
		forRepository {
			maven {
				name = "Sylv"
				url = uri("https://maven.sylv.gay/releases/")
			}
		}

		filter {
			includeGroup("gay.sylv.missingno")
		}
	}
}

loom {
	splitEnvironmentSourceSets()

	mods {
		register("wandering_wizardry") {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets.getByName("client"))
		}
	}
}

dependencies {
	minecraft(libs.minecraft)

	implementation(libs.fabric.loader)
	implementation(libs.fabric.api)

	implementation(libs.yumi.commons.core)
	include(libs.yumi.commons.core)

	runtimeOnly(libs.missingno.fix)
}

tasks.processResources {
	val version = version
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand("version" to version)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 25
}

java {
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}
}

publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	repositories {
	}
}

license {
	rule(file("codeformat/HEADER"))

	include("**/*.java")
}
