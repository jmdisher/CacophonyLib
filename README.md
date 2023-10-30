# CacophonyLib

Support code for Cacophony and Cacophony-based tools.

## How to build

CacophonyLib is a Maven project so can be built and tested using the top-level command:

```
mvn clean install
```

## Maven coordinates

CacophonyLib components can be accessed as Maven dependencies by adding this repository information:

```
	<repositories>
		<repository>
			<id>CacophonyLib-repo</id>
			<url>https://github.com/jmdisher/CacophonyLib/raw/maven/repo</url>
		</repository>
	</repositories>
```

From there, the `4.1.1` release (for example - check the tag list for updated releases:  https://github.com/jmdisher/CacophonyLib/tags), can be included:

```
		<dependency>
			<groupId>com.jeffdisher.cacophony</groupId>
			<artifactId>cacophony-common</artifactId>
			<version>4.1.1</version>
		</dependency>
		<dependency>
			<groupId>com.jeffdisher.cacophony</groupId>
			<artifactId>cacophony-data</artifactId>
			<version>4.1.1</version>
		</dependency>
		<dependency>
			<groupId>com.jeffdisher.cacophony</groupId>
			<artifactId>cacophony-net</artifactId>
			<version>4.1.1</version>
		</dependency>
```

