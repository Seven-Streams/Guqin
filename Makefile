.PHONY: build
build:
	find -name '*.java' | xargs javac -d bin -cp /ulib/antlr-4.13.1-complete.jar

.PHONY: run
run:
	cd bin && java -cp /ulib/antlr-4.13.1-complete.jar:. Guqin

.PHONY: compile
compile:
	find -name '*.java' | xargs javac -d bin -cp ulib/antlr-4.13.1-complete.jar

.PHONY: test
test:
	@cd bin && java -cp ../ulib/antlr-4.13.1-complete.jar:. Guqin

.PHONY: visit
visit:
	java -cp ulib/antlr-4.13.1-complete.jar org.antlr.v4.Tool $* -no-listener -visitor guqin.g4

