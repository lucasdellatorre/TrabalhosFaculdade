class LexerUtil {
  constructor(table) {
    this.table = table;
    this.lexema = Object.keys(table);
  }

  isLexemaValid(token) {
    return this.lexema.includes(token);
  }

  returnTokenValue(token) {
    return this.table[token] || this.table.default
  }

  isDigit(input) {
    return input.match(/^\d+$/)
  }

  isLetter(input) {
    return input.match(/[a-z]/i)
  }
}

(() => {
  const table = {
    '(': 'LPAR',
    ')': 'RPAR',
    '+': 'ADDOP',
    '-': 'SUBOP',
    '*': 'MULOP',
    '/': 'DIVOP',
    '>': 'LTOP',
    '<': 'STOP',
    '==': 'EQOP',
    ':=': 'ASSIGNOP',
    default: false
  }

  const lexer = new LexerUtil(table);

  const input = 'a := (aux - 1) * 100 / 20'

  const splittedInput = input.split([' '])

  const result = []

  splittedInput.forEach(e => {
    let variable = ""
    let aux = ""
    let aChar
    let nextChar
    for (let i = 0; i < e.length; i++) {
      aChar = e.charAt(i)
      aux += aChar;
      const response = lexer.isLexemaValid(aux)
      if (response !== false) {
        const value = lexer.returnTokenValue(aux)
        result.push(value);
        aux = "";
        continue;
      }
      if (lexer.isLetter(aChar) || lexer.isDigit(aChar)) {
        variable += aChar;
        nextChar = e.charAt(i + 1)
        if (!lexer.isLetter(nextChar) && !lexer.isDigit(nextChar)) {
          if (lexer.isLetter(variable.charAt(0))) {
            result.push('VAR')
          }
          if (lexer.isDigit(variable.charAt(0)))
            result.push('NUM')
        }
        aux = ""
      }
    }
  })
  console.log(result)
})();
