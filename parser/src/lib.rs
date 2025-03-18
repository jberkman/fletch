use lrlex::lrlex_mod;
use lrpar::lrpar_mod;

lrlex_mod!("java.l");
lrpar_mod!("java.y");

pub use java_l::*;
pub use java_y::*;
