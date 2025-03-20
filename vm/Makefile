BIN_DIR ?= bin  # Default to VM_OUT_DIR if set
SRC_DIR := src
REPL_MAIN_CLASS := NET._87k.fletch.vm.Repl
JAVAC_FLAGS := -source 1.3 -target 1.1 -d $(BIN_DIR) -sourcepath $(SRC_DIR)
JAVA_FLAGS := -cp $(BIN_DIR)

# Ensure the VM output directory exists
repl:
	mkdir -p $(BIN_DIR)
	javac $(JAVAC_FLAGS) $(SRC_DIR)/NET/_87k/fletch/vm/Repl.java

all: repl

run-repl: repl
	java $(JAVA_FLAGS) $(REPL_MAIN_CLASS)

clean:
	rm -rf $(BIN_DIR)
