const lists = [];

// Botão para abrir o modal
const openModalButton = document.getElementById("openModalButton");

// Modal e elementos relacionados
const modal = document.getElementById("modal-list");
const closeModalButton = document.getElementById("closeModalButton");
const createListButton = document.getElementById("createListButton");
const newListName = document.getElementById("newListName");
const existingLists = document.getElementById("existingLists");

// Função para exibir as listas existentes na modal
function displayLists() {
    existingLists.innerHTML = "";
    lists.forEach((list, index) => {
        const listItem = document.createElement("li");
        listItem.textContent = list;
        listItem.addEventListener("click", () => {
            // Simule a rota da lista selecionada
            window.location.href = `/lista/${index}`;
        });
        existingLists.appendChild(listItem);
    });
}

// Ao clicar no botão "Criar Nova Lista", o modal é exibido
openModalButton.addEventListener("click", () => {
    modal.style.display = "block";
    displayLists();
});

// Ao clicar no botão "Fechar", o modal é fechado
closeModalButton.addEventListener("click", () => {
    modal.style.display = "none";
});

// Ao clicar no botão "Criar Lista", a nova lista é criada e o modal é fechado
createListButton.addEventListener("click", () => {
    const listName = newListName.value;
    if (listName) {
        // Adicionar a nova lista ao array
        lists.push(listName);
        // Limpar o campo de entrada
        newListName.value = "";
        // Atualizar a exibição das listas na modal
        displayLists();
    }
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
