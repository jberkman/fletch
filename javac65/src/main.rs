use getopts::Options;
use std::{env, fs::File, io::Read, process::exit};

use fletch_parser::{lexerdef, parse, token_epp};

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
    println!("Compilng file: {} with args to main: {:?}", files[0], args);

    let mut data = String::new();
    File::open(files[0].clone())
        .expect("Failed to read file")
        .read_to_string(&mut data)
        .expect("Failed to read file");

    let lexerdef = lexerdef();
    let lexer = lexerdef.lexer(&data);
    let (ast, errs) = parse(&lexer);
    if errs.is_empty() {
        if let Some(ast) = ast {
            match ast {
                Ok(ast) => println!("{:#?}", ast),
                Err(_) => {
                    eprintln!("Unknown parse error");
                    exit(1);
                }
            }
        }    
    } else {
        for err in errs {
            eprintln!("{}", err.pp(&lexer, &token_epp))
        }
    }
}
