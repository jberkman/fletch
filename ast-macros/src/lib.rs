use proc_macro::TokenStream;
use quote::quote;
use syn::{parse_macro_input, DeriveInput};

#[proc_macro_derive(Spanned)]
pub fn derive_spanned(input: TokenStream) -> TokenStream {
    let ast = parse_macro_input!(input as DeriveInput);

    let name = &ast.ident;

    // Find the first field named "span"
    let span_field = match &ast.data {
        syn::Data::Struct(data) => data.fields.iter().find(|f| f.ident.as_ref().map(|i| i == "span").unwrap_or(false)),
        _ => None,
    };

    let output = if let Some(_) = span_field {
        quote! {
            impl Spanned for #name {
                fn span(&self) -> Span {
                    self.span
                }
            }
        }
    } else {
        quote! {
            compile_error!("Struct must have a `span` field to derive `Spanned`");
        }
    };

    output.into()
}
