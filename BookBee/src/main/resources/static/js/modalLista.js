const lists = [];

// Botão para abrir o modal
const openModalButton = document.getElementById("openModalButton");

// Modal e elementos relacionados
const modal = document.getElementById("modal-list");
const closeModalButton = document.getElementById("closeModalButton");
const createListButton = document.getElementById("createListButton");
const newListName = document.getElementById("newListName");
const existingLists = document.getElementById("existingLists");


// Ao clicar no botão "Criar Nova Lista", o modal é exibido
openModalButton.addEventListener("click", () => {
    modal.style.display = "block";
    displayLists();
});

// Ao clicar no botão "Fechar", o modal é fechado
closeModalButton.addEventListener("click", () => {
    modal.style.display = "none";
});

// Fechar o modal se o usuário clicar fora dele
window.addEventListener("click", (event) => {
    if (event.target === modal) {
        modal.style.display = "none";
    }
});

// Garantir que o botão permaneça visível na tela após a ativação da modal
modal.style.display = "none"; // Inicialmente, oculte a modal
openModalButton.style.zIndex = "1"; // Ajuste a propriedade zIndex para manter o botão acima da modal
openModalButton.addEventListener("click", () => {
    openModalButton.style.zIndex = "-1"; // Ao abrir a modal, mova o botão para trás
});
