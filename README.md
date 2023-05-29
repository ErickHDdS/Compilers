# Compilador em Java

Este repositório contém um compilador Java para uma linguagem de programação especificada. A linguagem de programação é definida pela gramática abaixo:

```
program ::= program identifier begin [decl-list] stmt-list end "."

decl-list ::= decl ";" { decl ";"}

decl ::= ident-list is type

ident-list ::= identifier {"," identifier}

type ::= int | float | char

stmt-list ::= stmt {";" stmt}

stmt ::= assign-stmt | if-stmt | while-stmt | repeat-stmt | read-stmt | write-stmt

assign-stmt ::= identifier "=" simple_expr

if-stmt ::= if condition then stmt-list end | if condition then stmt-list else stmt-list end

condition ::= expression

repeat-stmt ::= repeat stmt-list stmt-suffix

stmt-suffix ::= until condition

while-stmt ::= stmt-prefix stmt-list end

stmt-prefix ::= while condition do

read-stmt ::= read "(" identifier ")"

write-stmt ::= write "(" writable ")"

writable ::= simple-expr | literal

expression ::= simple-expr | simple-expr relop simple-expr

simple-expr ::= term | simple-expr addop term

term ::= factor-a | term mulop factor-a

fator-a ::= factor | "!" factor | "-" factor

factor ::= identifier | constant | "(" expression ")"

relop ::= "==" | ">" | ">=" | "<" | "<=" | "!="

addop ::= "+" | "-" | "||"

mulop ::= "*" | "/" | "&&"

constant ::= integer_const | float_const | char_const
```

### Padrões dos tokens

```
digit ::= [0-9]

carac ::= um dos caracteres ASCII

caractere ::= um dos caracteres ASCII, exceto quebra de linha

integer_const ::= digit+

float_const ::= digit+ “.”digit+

char_const ::= " ‘ " carac " ’ "

literal ::= "{" caractere* "}"

identifier ::= letter (letter | digit | " _")*

letter ::= [A-Za-z]
```

## Funcionalidades

O compilador implementado neste repositório é capaz de realizar as seguintes funcionalidades:

- [x] Análise léxica
- [x] Análise sintática
- [ ] Análise semântica
- [ ] Geração de código

## Como utilizar

Para utilizar o compilador, siga os passos abaixo:

1. Clone este repositório em sua máquina local.
2. Abra o terminal e navegue até o diretório onde o repositório foi clonado.
3. Entre na pasta src `cd src/`
4. Compile o código-fonte utilizando o comando `javac Compiler.java `
5. Execute o compilador utilizando o comando `java Compiler <file-path>`

> **Observação:** O compilador espera receber como parâmetro o caminho para um arquivo de código-fonte. Também é possível passar o parâmetro `-d` que ira ativar o modo de debug, que exibe informações sobre o processo de compilação. Exemplo: `java Compiler <file-path> -d`

## Realizado por:

[**Erick H. D. de Souza**](https://github.com/ErickHDdS) <br>
[**Lucas C. Dornelas**](https://github.com/lucascdornelas) <br>
[**Násser Rafael**](https://github.com/nasserrafaelfk)
