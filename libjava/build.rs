// MIT License
//
// Copyright (c) 2025 jacob berkman
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.


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
        .expect("Failed to build libjava");

    if !status.success() {
        panic!("libjava build failed");
    }
}
