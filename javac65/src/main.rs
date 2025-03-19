use getopts::Options;
use lrpar::{Lexeme, Lexer, NonStreamingLexer};
use std::{env, fs::File, io::Read, process::exit};

fn main() {
    let args: Vec<String> = env::args().collect();
    let program = args[0].clone();

    let mut opts = Options::new();
    opts
        .optflag("c", "", "Compile and assemble, but don't link")
        .optflag("h", "help", "Help (this text)")
        .optflag("S", "", "Compile but don't assemble and link")
        .optflag("V", "version", "Print the version number")
        .optopt("C", "config", "Use linker config", "name")
        .optopt("o", "", "Name the output file", "name")
        .optopt("t", "target", "Set the target system", "sys");

    let matches = match opts.parse(&args[1..]) {
        Ok(m) => m,
        Err(f) => panic!("{}", f),
    };

    if matches.opt_present("help") {
        println!(
            "{}",
            opts.usage(&format!("Usage: {} [options] <source-file> [args...]", program))
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

    let lexerdef = fletch_parser::lexerdef();
    let mut data = String::new();
    File::open(files[0].clone())
        .expect("Failed to read file")
        .read_to_string(&mut data)
        .expect("Failed to read file");
    let lexer = lexerdef.lexer(&data);
    for (i, lexeme) in lexer.iter().enumerate() {
        match lexeme {
            Ok(lexeme) => {
                println!(
                    "[{:4}] ({:2}) {}",
                    i,
                    lexeme.tok_id(),
                    lexer.span_str(lexeme.span())
                );
            }
            Err(err) => {
                println!("[{:4}] Error: {}", i, err);
            }
        }
    }
}
