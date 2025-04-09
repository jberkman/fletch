BIN_DIR ?= target/debug

CLASSPATH_FLAGS := -cp $(BIN_DIR)
SOURCEPATH_FLAGS = -sourcepath $(SRC_DIR)

JAVAC_FLAGS := -Xlint:-options -source 1.3 -target 1.1
#JAVADOC_FLAGS := -source 1.3 -d $(DOC_DIR) $(CLASSPATH_FLAGS) $(SOURCEPATH_FLAGS)

all: vm-jvm hello-jvm vm-6502

javac65:
	cargo b

vm-6502: javac65
	cargo run -p fletch-javac65 --bin javac65 -- -S hello/src/Hello.java World

vm-jvm:
	mkdir -p $(BIN_DIR)/$@
	javac $(JAVAC_FLAGS) -d $(BIN_DIR)/$@ -sourcepath vm/src vm/src/NET/_87k/fletch/vm/jvm/InterpreterImpl.java

hello-jvm:
	mkdir -p $(BIN_DIR)/$@
	javac $(JAVAC_FLAGS) -d $(BIN_DIR)/$@ -sourcepath libjava/src:hello/src hello/src/Hello.java

empty libjava hello:
	$(MAKE) -C ../$@

#doc:
#	javadoc $(JAVADOC_FLAGS) -private -subpackages NET._87k.fletch.vm

clean:
	cargo clean
	rm -rf $(BIN_DIR) $(DOC_DIR)

.PHONY: javac65
