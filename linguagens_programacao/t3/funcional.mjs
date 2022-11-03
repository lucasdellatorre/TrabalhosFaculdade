class ProgramacaoFuncionalJS {
  static reduceDemo(lista) {
    // soma todos os elementos da lista
    return lista.reduce(function(acumulador, valorAtual) {
      return acumulador + valorAtual;
    })
  }

  static mapDemo(lista) {
    // converter para inteiro
    return lista.map(Number);
  }

  static filterDemo(lista) {
    // filtra os pares
    return lista.filter((element) => element % 2 == 0);
  }

  static forEachDemo(lista) {
    // copiar uma lista
    const auxList = []
    lista.forEach((e) => auxList.push(e))
    return auxList;
  }

  static findDemo(lista, elemento) {
    // encontrar algum elemento da lista
    return lista.find(e => e == elemento);
  }
}

class ProgramacaoJS {
  static reduceDemo(lista) {
    let acumulador = 0;
    for (let i = 0; i < lista.length; i++) {
      acumulador += lista[i];
    }
    return acumulador
  }

  static mapDemo(lista) {
    // converter para inteiro
    for (let i = 0; i < lista.length; i++) {
      lista[i] = parseInt(lista[i]);
    }
    return lista;
  }

  static filterDemo(lista) {
    const auxList = []
    // filtra os pares
    for (let i = 0; i < lista.length; i++) {
      if (lista[i] % 2 == 0) {
        auxList.push(lista[i])
      }
    }
    return auxList;
  }

  static forEachDemo(lista) {
    // copiar uma lista
    const auxList = []
    for (let i = 0; i < lista.length; i++) {
      auxList.push(lista[i]);
    }
    return auxList;
  }

  static findDemo(lista, elemento) {
    // encontrar algum elemento da lista
    for (let i = 0; i < lista.length; i++) {
      if (lista[i] == elemento) {
        return elemento
      }
    }
    return -1;
  }
}

const experimentoProgramacaoFuncional = () => {
  const reduceText = 'Somar todos os elementos da lista'
  const reduceList = [1, 2, 3, 4, 5]
  const resultReduce = ProgramacaoFuncionalJS.reduceDemo(reduceList)

  const mapText = 'Converter para inteiro'
  const mapList = ['1', '2', '3', '4', '5']
  const resultMap = ProgramacaoFuncionalJS.mapDemo(mapList)

  const filterText = 'Filtrar por pares'
  const filterList = [1, 2, 3, 4, 5]
  const resultFilter = ProgramacaoFuncionalJS.filterDemo(filterList)

  const foreachText = 'Clonar uma lista'
  const foreachList = [1, 2, 3, 4, 5]
  const resultForeach = ProgramacaoFuncionalJS.forEachDemo(foreachList)

  const findText = 'Encontrar algum número em uma lista'
  const findList = [1, 2, 3, 4, 5]
  const resultFind = ProgramacaoFuncionalJS.findDemo(findList, 3);


  const table = [
    [reduceText, reduceList.toString(), resultReduce.toString()],
    [mapText, mapList.toString(), resultMap.toString()],
    [filterText, filterList.toString(), resultFilter.toString()],
    [foreachText, foreachList.toString(), resultForeach.toString()],
    [findText, findList.toString(), resultFind.toString()]
  ]

  console.table(table);
}

const experimentoProgramacao = () => {
  const reduceText = 'Somar todos os elementos da lista'
  const reduceList = [1, 2, 3, 4, 5]
  const resultReduce = ProgramacaoJS.reduceDemo(reduceList)

  const mapText = 'Converter para inteiro'
  const mapList = ['1', '2', '3', '4', '5']
  const resultMap = ProgramacaoJS.mapDemo(mapList)

  const filterText = 'Filtrar por pares'
  const filterList = [1, 2, 3, 4, 5]
  const resultFilter = ProgramacaoJS.filterDemo(filterList)

  const foreachText = 'Clonar uma lista'
  const foreachList = [1, 2, 3, 4, 5]
  const resultForeach = ProgramacaoJS.forEachDemo(foreachList)

  const findText = 'Encontrar algum número em uma lista'
  const findList = [1, 2, 3, 4, 5]
  const resultFind = ProgramacaoJS.findDemo(findList, 3);


  const table = [
    [reduceText, reduceList.toString(), resultReduce.toString()],
    [mapText, mapList.toString(), resultMap.toString()],
    [filterText, filterList.toString(), resultFilter.toString()],
    [foreachText, foreachList.toString(), resultForeach.toString()],
    [findText, findList.toString(), resultFind.toString()]
  ]

  console.table(table);
}

console.log('Programação Funcional')
experimentoProgramacaoFuncional()

console.log('Programação Convencional')
experimentoProgramacao()