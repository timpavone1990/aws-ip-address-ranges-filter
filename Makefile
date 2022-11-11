SV4GIT := $(shell ./scripts/require-sv4git.sh)
NEXT_VERSION_FILE := .next-version.txt
CAT_NEXT_VERSION_FILE := $$(cat $(NEXT_VERSION_FILE))
RELEASE_NOTES_FILE := .release-notes.txt

.PHONY: release
release:
	$(SV4GIT) next-version > .next-version.txt
	echo "Releasing version $(CAT_NEXT_VERSION_FILE)"
	./gradlew --no-daemon build -Pversion=$(CAT_NEXT_VERSION_FILE)
	$(SV4GIT) release-notes > $(RELEASE_NOTES_FILE)
	$(SV4GIT) tag
	gh release create $(CAT_NEXT_VERSION_FILE) \
	  	--notes-file $(RELEASE_NOTES_FILE) \
		--title v$(CAT_NEXT_VERSION_FILE) \
		build/libs/*-$(CAT_NEXT_VERSION_FILE)*.jar
