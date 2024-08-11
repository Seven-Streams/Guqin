.PHONY: build
build:
	find -name '*.java' | xargs javac -d bin -cp /ulib/antlr-4.13.1-complete.jar

.PHONY: run
run:
	cd bin && java -cp ../ulib/antlr-4.13.1-complete.jar:. Guqin