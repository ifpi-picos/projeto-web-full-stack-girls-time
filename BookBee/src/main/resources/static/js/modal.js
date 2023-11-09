//modal
const parametrosURL = new URLSearchParams(window.location.search);
var modal1 = document.getElementById("myModal1");
var modal2 = document.getElementById("myModal2");
var btn1 = document.getElementById("btn1");
var btn2 = document.getElementById("btn2");
var btnSave1 = document.getElementById("btnSalvar1");
var btnSave2 = document.getElementById("btnSalvar2");
var span1 = document.getElementsByClassName("close-notas")[0];
var span2 = document.getElementsByClassName("close-res")[0];
let btnSalvaNota = document.getElementById("salarModalNotas");
let arrayDeNotas = [];

// Função para abrir o modal 1
function abrirModal1() {
  modal1.style.display = "block";
  const imgmodal = parametrosURL.get('imagem');
  document.querySelector("#img-capa-modal-notas").setAttribute('src', imgmodal);
  document.querySelector("#titulo-modal-notas").textContent = livro.titulo;
}


btnSalvaNota.addEventListener('click', function () {
  let reviewTexArea = document.getElementById('reviewTextArea');
  let textNota = reviewTexArea.value;

  arrayDeNotas.push(textNota);
  if(arrayDeNotas.length > 3){
    arrayDeNotas = arrayDeNotas.slice(arrayDeNotas.length - 3);
  }

  let areaNotasSalvas = document.getElementById("textNotasSalvas");
  areaNotasSalvas.innerHTML = "";

  for(let i = 0; i < arrayDeNotas.length; i++){
  
    let novoParagrafo = document.createElement('p');
    novoParagrafo.classList.add('nota');
    let textoNota = document.createElement('span')
    textoNota.classList.add('textoNota');
    textoNota.textContent = arrayDeNotas[i];
    novoParagrafo.appendChild(textoNota);
    areaNotasSalvas.appendChild(novoParagrafo);
  }

  reviewTexArea.value = ""

  console.log(arrayDeNotas)
})

// Função para fechar o modal 1
function fecharModal1() {
  modal1.style.display = "none";
}

// Função para abrir o modal 2
function abrirModal2() {
  modal2.style.display = "block";
  const imgmodal = parametrosURL.get('imagem');
  document.querySelector("#img-capa-modal").setAttribute('src', imgmodal);
  document.querySelector("#titulo-modal").textContent = livro.titulo;
}

// Função para fechar o modal 2
function fecharModal2() {
  modal2.style.display = "none";
}

// Atribuindo as funções aos botões correspondentes
btn1.onclick = abrirModal1;
btn2.onclick = abrirModal2;


// Atribuindo as funções de fechar os modais aos botões de fechamento correspondentes
span1.onclick = fecharModal1;
span2.onclick = fecharModal2;


const modalDescricaoLivro = document.getElementById("modalDescricaoLivro");
function fecharModal() {
  
  modalDescricaoLivro.style.display = "none";
}

function abrirModal() {
  modalDescricaoLivro.style.display = "block";
  const descricaoM = parametrosURL.get('descricao')
  
  let descricaoLivro = document.querySelector("#descricaoLivroM");
  descricaoLivro.textContent = formatarDescricao(descricaoM);
}

let btn3 = document.getElementById("btn3");
let closeBtn3 = document.getElementsByClassName("closeDetalhe")[0];
btn3.addEventListener("click", abrirModal);
closeBtn3.addEventListener("click", fecharModal);

window.onclick = function(event) {
  if (event.target == modal1) {
    fecharModal1();
  }
  if (event.target == modal2) {
    fecharModal2();
  }
  if(event.target == modalDescricaoLivro){
    fecharModal();
  }
}


const livro ={
  id: parametrosURL.get('id'),
  titulo: parametrosURL.get('titulo'),
  autor: parametrosURL.get('autor'),
  descricao: parametrosURL.get('descricao'),
  paginas: parametrosURL.get('paginas'),
  imagem: parametrosURL.get('imagem')
};

document.querySelector("#tituloLivro").textContent = livro.titulo;
document.querySelector("#autorLivro").textContent = livro.autor;
document.querySelector("#descricaoLivro").textContent = formatarDescricao(livro.descricao);
document.querySelector("#numPagina").textContent = livro.paginas;
document.querySelector("#imgCapaLivro").setAttribute('src', livro.imagem);

function adicionarClassificacaoEstrela() {
  const estrelas = document.querySelectorAll(".exibirClassificacao li i");

  estrelas.forEach((estrela, index) => {
    estrela.addEventListener("mouseenter", () => {
      resetarClassificacaoEstrela();

      for (let i = 0; i <= index; i++) {
        estrelas[i].classList.remove("bi-star");
        estrelas[i].classList.add("bi-star-fill");
      }
    });
  });
}

function resetarClassificacaoEstrela() {
  const estrelas = document.querySelectorAll(".exibirClassificacao li i");

  estrelas.forEach((estrela) => {
    estrela.classList.remove("bi-star-fill");
    estrela.classList.add("bi-star");
  });
}
