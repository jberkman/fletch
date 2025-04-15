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


use getopts::Options;
use std::{env, process::exit};

use fletch_parser::parse_file;
use fletch_type_checker::type_check;

fn main() {
    let args: Vec<String> = env::args().collect();
    let program = args[0].clone();

    let mut opts = Options::new();
    opts.optflag("c", "", "Compile and assemble, but don't link")
        .optflag("h", "help", "Help (this text)")
        .optflag("S", "", "Compile but don't assemble and link")
        .optflag("V", "version", "Print the version number")
        .optopt("C", "config", "Use linker config", "name")
        .optopt(
            "j",
            "java",
            "Where to place generated Java stubs",
            "directory",
        )
        .optopt("o", "", "Name the output file", "name")
        .optopt("t", "target", "Set the target system", "sys");

    let matches = match opts.parse(&args[1..]) {
        Ok(m) => m,
        Err(f) => panic!("{}", f),
    };

    if matches.opt_present("help") {
        println!(
            "{}",
            opts.usage(&format!(
                "Usage: {} [options] <source-file> [args...]",
                program
            ))
        );
        return;
    }

    if matches.opt_present("version") {
        println!(
            "Fletch Java Compiler, version {} ({} {})",
            env!("CARGO_PKG_VERSION"),
            env::consts::OS,
            env::consts::ARCH,
        );
        println!("Copyright (c) 2025 jacob berkman");
        return;
    }

    if matches.free.is_empty() {
        eprintln!("{}: No imput files", program);
        exit(1);
    }

    let (files, args) = matches.free.split_at(1);
    let file = &files[0];

    println!("Compiling file: {} with main({:?})", file, args);

    let ast = parse_file(file).unwrap_or_else(|e| {
        eprintln!("{}", e);
        exit(1);
    });

    type_check(&ast).unwrap_or_else(|e| {
        eprint!("Type checking error: {:?}", e);
        exit(1);
    });

    //if let Some(ast) = ast {
    //    match ast {
    //        Ok(ast) => println!("{:#?}", ast),
    //        Err(_) => {
    //        }
    //    }
    //}
}
