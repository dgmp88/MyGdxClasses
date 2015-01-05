### Installation instructions

Pull the repo, then install it to maven local: 

```gradle install```

Modify the build.gradle of the project you want to include these in with:

```
allprojects {
	...
	boondogImportsVersion = '0.1.0-SNAPSHOT'
	repositories {
	...
	mavenLocal()
	}
}

project(":core") {
	dependencies {
		compile "com.boondog.imports:core:$boondogImportsVersion"
	}
}

project(":android") {
    dependencies {
        compile "com.boondog.imports:core:$boondogImportsVersion"
    }
}

```
Right click the project, gradle, refresh all.