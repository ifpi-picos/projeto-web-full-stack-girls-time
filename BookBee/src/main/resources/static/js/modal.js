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
  document.querySelector("#img-capa-modal-notas");
  document.querySelector("#titulo-modal-notas");
}

// Função para fechar o modal 1
function fecharModal1() {
  modal1.style.display = "none";
}

// Função para abrir o modal 2
function abrirModal2() {
  modal2.style.display = "block";
  document.querySelector("#img-capa-modal");
  document.querySelector("#titulo-modal");
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
    
    estrela.addEventListener("click", () => {
      const idLivro = document.querySelector("#idLivros").value;
      enviarClassificacaoParaBackend(idLivro, index + 1); 
    });
    
  });
}
function enviarClassificacaoParaBackend(idLivro, classificacao) {
  const url = "/livros/atualizar-classificacao";
  const data = { "idLivro": idLivro,
            "classificacao": classificacao };

  fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
  })
  .then(response => response.json())
  .then(data => console.log(data))
  .catch((error) => {
      console.error('Error:', error);
  });
}

const classificacao = document.querySelector("#classificacao").value;
window.onload = function() {
  const classificacao = document.querySelector("#classificacao").value;
  exibirClassificacao(classificacao);
}

function exibirClassificacao(classificacao) {
  const estrelas = document.querySelectorAll(".exibirClassificacao li i");

  for (let i = 0; i < classificacao; i++) {
    estrelas[i].classList.remove("bi-star");
    estrelas[i].classList.add("bi-star-fill");
  }
}

function resetarClassificacaoEstrela() {
  const estrelas = document.querySelectorAll(".exibirClassificacao li i");

  estrelas.forEach((estrela) => {
    estrela.classList.remove("bi-star-fill");
    estrela.classList.add("bi-star");
  });
}
