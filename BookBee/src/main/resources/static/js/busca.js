// Adicionar livros; 
const btnBusca = document.getElementById('btnBuscar')
const busca = document.getElementById('search-txt')
const livros = document.getElementById('livros')



const hadleEvent = async (event) => {
  if(event.type === 'click' || (event.type === 'keydown' && event.key === 'Enter')){
    event.preventDefault();
    console.log()
    const PrincipalBuscar = busca.value.replace(' ', '+');
    console.log(busca, " valores livros")
    const res = await fetch(`https://www.googleapis.com/books/v1/volumes?q=${PrincipalBuscar}`);
    const dados = await res.json();
    console.log(dados)
    if(!dados.items || dados.items.length === 0){
     // criar algo para informa que o resultado não foi encontrado
    }else{
      if(location.href != 'http://localhost:9090/livros/pagina-de-busca'){
        console.log('na condição')
        localStorage.setItem('searchResults', JSON.stringify(dados.items));
        location.href = '/livros/pagina-de-busca';
        
      }else{
        livros.innerHTML = '';
        
        dados.items.forEach(item => {
          let capaImagem;
          if (item && item.volumeInfo && item.volumeInfo.imageLinks && item.volumeInfo.imageLinks.thumbnail) {
            capaImagem = item.volumeInfo.imageLinks.thumbnail;
          } else {
            capaImagem = '/img/1682512674678.png';
          }

          livros.innerHTML = livros.innerHTML + 
          `<div class="conteudoLivros">
            <img src="${capaImagem}" >
            <li> ${item.volumeInfo.title} Pag: ${item.volumeInfo.pageCount}- ${item.volumeInfo.authors}
              <button onclick="adicionaLivro('${item.id}')" >+</button>
            </li>
          </div>`; 
        });
      } 
    }
  }
}

btnBusca.addEventListener('click', hadleEvent);
  
window.addEventListener('load', () => {
  console.log('dentro do load')
    if(location.href === 'http://localhost:9090/livros/pagina-de-busca'){

    console.log('depois do load')
    const livros = document.getElementById('livros');
    const searchResults = JSON.parse(localStorage.getItem('searchResults'));
    livros.innerHTML = '';
    console.log(searchResults);
          
    searchResults.forEach(item => {
      let capaImagem;
      if (item && item.volumeInfo && item.volumeInfo.imageLinks && item.volumeInfo.imageLinks.thumbnail) {
        capaImagem = item.volumeInfo.imageLinks.thumbnail;
      } else {
        capaImagem = '/img/1682512674678.png';
      }
      
      livros.innerHTML = livros.innerHTML + 
      `<div class="conteudoLivros">
        <img src="${capaImagem}"  >
        <li> ${item.volumeInfo.title}; Pag: ${item.volumeInfo.pageCount} - ${item.volumeInfo.authors}
          <button onclick="adicionaLivro('${item.id}')" >+</button>
        </li>
      </div>`;  
    });
  } 
});

function adicionaLivro(id) {
  alert(id)

  fetch(`https://www.googleapis.com/books/v1/volumes/${id}`)
  .then(response => response.json())
  .then(data => {
    $.ajax({
      url: '/livros/adicionar',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({
        'titulo': data.volumeInfo.title,
        'linkImagem': data.volumeInfo.imageLinks && data.volumeInfo.imageLinks.thumbnail ? data.volumeInfo.imageLinks.thumbnail : '/img/1682512674678.png',
        'autor': data.volumeInfo.authors ? data.volumeInfo.authors.join(', ') : 'não definido',
        'genero': data.volumeInfo.categories ? data.volumeInfo.categories.join(', ') : 'não definido',
        'paginas': data.volumeInfo.pageCount,
        'pgLidas': 0,
        'favorito': false,
        'classificacao': 0,
        'dataDeIni': null,
        'dataDeFim': null
      }),
      success: function(result) {
        location.replace("redirect:/livros");
      },
      error: function(xhr, status, error) {
        if(error === 'MethodArgumentTypeMismatchException'){
          location.replace("redirect:/livros");
        }
      }
    });
  })
}

