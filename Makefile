SHELL=/bin/bash -euo pipefail
SV4GIT := $(shell ./scripts/require-sv4git.sh)
NEXT_VERSION_FILE := .next-version.txt
CAT_NEXT_VERSION_FILE := $$(cat $(NEXT_VERSION_FILE))
RELEASE_NOTES_FILE := .release-notes.txt

.PHONY: release
release:
	$(SV4GIT) next-version > .next-version.txt
	echo "Releasing version $(CAT_NEXT_VERSION_FILE)"
	./gradlew --no-daemon -Pversion=$(CAT_NEXT_VERSION_FILE) build jibDockerBuild
	docker run \
		--detach \
		--name smoke-test-container \
		--publish 8080:8080 \
		--rm \
		timpavone1990/aws-ip-address-range-filter
	sleep 5
	curl --fail-with-body \
		--silent \
		"http://localhost:8080/v1/aws/ip-address-ranges?region=EU" \
		| tail -n 5
	docker stop smoke-test-container
	docker login \
		--username $$DOCKER_HUB_USERNAME \
		--password $$DOCKER_HUB_PASSWORD
	docker push \
		--all-tags \
		timpavone1990/aws-ip-address-range-filter
	$(SV4GIT) release-notes > $(RELEASE_NOTES_FILE)
	$(SV4GIT) tag
	gh release create $(CAT_NEXT_VERSION_FILE) \
	  	--notes-file $(RELEASE_NOTES_FILE) \
		--title v$(CAT_NEXT_VERSION_FILE) \
		build/libs/*-$(CAT_NEXT_VERSION_FILE)*.jar
