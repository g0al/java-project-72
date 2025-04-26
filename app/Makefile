.PHONY: build test checkstyle run-dist report

build:
	./gradlew build

test:
	./gradlew test

checkstyle:
	./gradlew checkstyleMain checkstyleTest

run-dist:
	./build/install/app/bin/app

report:
	./gradlew jacocoTestReport