
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
      if(location.href != 'http://127.0.0.1:5500/BookBee/src/main/resources/templates/busca.html'){
        // if(location.href != 'http://localhost:9090/livros/busca'){
      console.log('na condição')
        localStorage.setItem('searchResults', JSON.stringify(dados.items));
        // location.href = '/livros/pagina-de-busca';
        location.href = 'http://127.0.0.1:5500/BookBee/src/main/resources/templates/busca.html';
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

// txtprocura.addEventListener('keydown', hadleEvent);
btnBusca.addEventListener('click', hadleEvent);
  
window.addEventListener('load', () => {
  console.log('dentro do load')
  if(window.location.href == 'http://127.0.0.1:5500/BookBee/src/main/resources/templates/busca.html'){
    // if(location.href != 'http://localhost:9090/livros/busca'){

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
        'linkImagens': data.volumeInfo.imageLinks && data.volumeInfo.imageLinks.thumbnail ? data.volumeInfo.imageLinks.thumbnail : '/img/1682512674678.png',
        'autor': data.volumeInfo.authors ? data.volumeInfo.authors.join(', ') : 'não definido',
        'editora': data.volumeInfo.publisher,
        'genero': data.volumeInfo.categories ? data.volumeInfo.categories.join(', ') : 'não definido',
        'pagina': data.volumeInfo.pageCount,
        'pglidas': 0,
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
        }else{
          location.replace(":/404");
        }
      }
    });
  })
}

// const searchIcon = document.querySelector('.iconeBusca');
// const searchInput = document.querySelector('input');
// const searchButton = document.querySelector('.barraDeBusca button');
// const searchSpace = document.querySelector('.barraDeBusca');


// searchIcon.addEventListener('click', () => {

//   if(window.innerWidth <= 950){
//     if (searchInput.style.display === 'none') {
//         searchInput.style.display = 'inline-block';
//         searchButton.style.display = 'flex';
//         searchButton.style.justifyContent = 'center';
//         searchButton.style. alignItems= 'center';
//         searchSpace.style.width = '150%';
//         searchSpace.style.height = 'auto';
//         searchSpace.style.borderRadius = '0';
//     } else {
//         searchInput.style.display = 'none';
//         searchButton.style.display = 'none';
//         searchSpace.style.width = '40px';
//         searchSpace.style.height = '40px';
//         searchSpace.style.borderRadius = '50%';
//   }
    
//   }
// });
