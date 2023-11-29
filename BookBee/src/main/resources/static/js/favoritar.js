document.addEventListener('DOMContentLoaded', (event) => 
coracao.addEventListener('click', function(){
      var isChecked = $(this).is(':checked');
        const idLivro = document.querySelector("#idLivros").value;
        console.log(isChecked)
  
        // Enviar requisição para o backend
        $.ajax({
            type: 'POST',
            url: '/livros/adiciona-favorito',
            contentType: 'application/json',
            data: JSON.stringify({
                idLivro: idLivro,
                favorito: isChecked
            }),
            success: function (data) {
                // Tratar a resposta do backend, se necessário
                console.log('Sucesso: ' + data);
            },
            error: function (error) {
                // Tratar erros, se necessário
                console.log('Erro: ' + error.responseText);
            }
        });
    })
);





