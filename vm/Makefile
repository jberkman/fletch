BIN_DIR ?= bin
DOC_DIR ?= doc
SRC_DIR := src

CLASSPATH_FLAGS := -cp $(BIN_DIR)
SOURCEPATH_FLAGS = -sourcepath $(SRC_DIR)

JAVAC_FLAGS := -Xlint:-options -source 1.3 -target 1.1 -d $(BIN_DIR) $(SOURCEPATH_FLAGS)
JAVADOC_FLAGS := -source 1.3 -d $(DOC_DIR) $(CLASSPATH_FLAGS) $(SOURCEPATH_FLAGS)
JAVA_FLAGS := $(CLASSPATH_FLAGS)

# Ensure the VM output directory exists
all:
	mkdir -p $(BIN_DIR)
	javac $(JAVAC_FLAGS) $(SRC_DIR)/NET/_87k/fletch/vm/jvm/InterpreterImpl.java

empty libjava hello:
	$(MAKE) -C ../$@

run: all libjava hello
	java $(JAVA_FLAGS) NET._87k.fletch.vm.jvm.InterpreterImpl ../libjava/bin:../hello/bin Hello World

doc:
	javadoc $(JAVADOC_FLAGS) -private -subpackages NET._87k.fletch.vm

clean:
	rm -rf $(BIN_DIR) $(DOC_DIR)
