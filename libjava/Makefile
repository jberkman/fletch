BIN_DIR ?= bin
DOC_DIR ?= doc
SRC_DIR := src

CLASSPATH_FLAGS := -cp $(BIN_DIR) -bootclasspath $(BIN_DIR)
SOURCEPATH_FLAGS = -sourcepath $(SRC_DIR)

JAVAC_FLAGS := -source 1.3 -target 1.1 -d $(BIN_DIR) $(SOURCEPATH_FLAGS)
JAVADOC_FLAGS := -source 1.3 -d $(DOC_DIR) $(SOURCEPATH_FLAGS)
JAVA_FLAGS := $(CLASSPATH_FLAGS)

.PHONY: all doc clean

# Ensure the VM output directory exists
all:
	mkdir -p $(BIN_DIR)
	javac $(JAVAC_FLAGS) @sources.txt

doc:
	mkdir -p $(DOC_DIR)
	javadoc $(JAVADOC_FLAGS) -private -subpackages java

clean:
	rm -rf $(BIN_DIR) $(DOC_DIR)
