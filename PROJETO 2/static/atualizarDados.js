$(document).ready(function () {
  setInterval(function () {
    // Fazer solicitação AJAX para obter dados mais recentes
    $.get('/atualizarDados', function (data) {
      // Atualizar os valores no HTML com base nos dados recebidos
      console.log(data)
      data.forEach(function (item, index) {
        const id = getIdByIndex(index); // Função para obter o ID correspondente com base no índice
        const formattedValue = item.dado.toFixed(2); // Formatando para 2 casas decimais
        $('#' + id).text(formattedValue);
      });
    });
  }, 10000);

  // Função para mapear índices para IDs correspondentes
  function getIdByIndex(index) {
    const ids = ['nutrientes', 'ultrav', 'temperatura', 'tempo', 'Umidade', 'irrigacao', 'quantidade'];
    return ids[index];
  }
});
