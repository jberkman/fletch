use std::env;
use std::path::PathBuf;
use std::process::Command;

fn main() {
    println!("cargo:rerun-if-changed=src/"); // Ensure build.rs reruns if Java changes

    // Get Cargo's output directory (`target/debug/build/vm-<hash>/out`)
    let out_dir = PathBuf::from(env::var("OUT_DIR").expect("Cargo should set OUT_DIR"));

    // Get the package target directory (target/debug or target/release)
    let target_dir = out_dir
        //.ancestors()
        //.nth(3) // Step up 3 levels from `OUT_DIR`
        //.expect("Failed to find target directory")
        .to_path_buf();

    println!("cargo:rerun-if-changed=Makefile");

    let status = Command::new("make")
        //.current_dir("src_java")  // Where Makefile is located
        .env("BIN_DIR", target_dir.to_str().unwrap()) // Pass target directory
        .arg("all")
        .status()
        .expect("Failed to build Java VM");

    if !status.success() {
        panic!("Java VM build failed");
    }
}
