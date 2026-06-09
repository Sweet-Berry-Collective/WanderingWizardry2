dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			from(files("libs.versions.toml"))
		}
	}
}

pluginManagement {
	repositories {
		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
		mavenCentral()
		gradlePluginPortal()
	}
}

// Should match your modid
rootProject.name = "wandering_wizardry"
