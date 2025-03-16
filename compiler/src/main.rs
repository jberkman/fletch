use lrlex::lrlex_mod;
use lrpar::{lrpar_mod, Lexer, NonStreamingLexer, Lexeme};

use getopts::Options;
use std::{env, fs::File, io::Read};

mod ast;

lrlex_mod!("java.l");
lrpar_mod!("java.y");

fn main() {
    let args: Vec<String> = env::args().collect();
    let program = args[0].clone();

    let mut opts = Options::new();
    opts.optflag("h", "help", "print this help menu");
    opts.optflag("", "version", "print the version number");
    let matches = match opts.parse(&args[1..]) {
        Ok(m) => m,
        Err(f) => panic!("{}", f),
    };

    if matches.opt_present("h") {
        println!(
            "{}",
            opts.usage(&format!("Usage: {} [options] <file>", program))
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

    let lexerdef = java_l::lexerdef();
    for file in matches.free.iter() {
        println!("Parsing file: {}", file);
        let mut data = String::new();
        File::open(file).expect("Failed to read file")
            .read_to_string(&mut data)
            .expect("Failed to read file");
        let lexer = lexerdef.lexer(&data);
        for lexeme in lexer.iter() {
            match lexeme {
                Ok(lexeme) => {
                    println!("{} => {}", lexeme.tok_id(), lexer.span_str(lexeme.span()));
                }
                Err(err) => {
                    println!("Error: {}", err);
                }
            }
        }
        //let (res, errs) = java_y::parse(&lexer);
        //for err in errs {
        //    println!("Error: {}", err);
        //}
    }
}
