function formatarDescricao(descricao){
    const descricaoFormatada = descricao.replace(/<\/?p>/g, '')
    .replace(/<br\s*\/?/gi, '\n').replace(/\<i\s*\>/gi, '')
    .replace(/\<\/b\s*\>/gi, '\n').replace(/\<b\s*\>/gi, '')
    .replace(/\<\/i\s*\>/gi, '');
    return limitarDescricao(descricaoFormatada);
  }
  
  function limitarDescricao(descricao){
    if(descricao.length <= 400){
      return descricao;
    } else{
      const descricaoLimitada = descricao.slice(0, 400).trim() + '...';
      return descricaoLimitada;
    }
  }
  
  async function exibirDetalheDoLivro(bookId){
    const response = await fetch(`https://www.googleapis.com/books/v1/volumes/${bookId}`);
    const data = await response.json();
  
    if(data.error){
        console.error('Ocorreu um erro ao obter as informações do livro');
        return;
    }
  
    const livro = {
        id: bookId,
        titulo: data.volumeInfo.title ,
        autor: data.volumeInfo.authors ? data.volumeInfo.authors[0] : 'Autor Desconhecido',
        descricao: data.volumeInfo.description ? data.volumeInfo.description : 'Sem Descrição',
        paginas: data.volumeInfo.pageCount,
        imagem: data.volumeInfo.imageLinks ? data.volumeInfo.imageLinks.thumbnail : 'imagens/1682512674678.png'
    };
  
    const parametrosURL = new URLSearchParams(livro).toString();
    const urlDetalheLivros = `http://127.0.0.1:5500/BookBee/src/main/resources/templates/detalhesdolivro.html${parametrosURL}`;
    window.location.href = urlDetalheLivros;
  }