const containerCarrossel = document.querySelector('.container-carrossel');
const gallery = document.querySelector('.gallery-wrapper');
const leftArrow = document.querySelector(".arrow-left");
const rightArrow = document.querySelector(".arrow-right");
const items = document.querySelectorAll('.item');
const maxItems = items.length;

let currentItem = 0;

function left() {
    currentItem = (currentItem - 1 + maxItems) % maxItems;
    gallery.scrollLeft -= 250
}

function right() {
    currentItem = (currentItem + 1) % maxItems;
    gallery.scrollLeft += 250
}

function updateGallery() {
    items.forEach((item, index) => {
        if (index === currentItem) {
            item.classList.add('current-item');
        } else {
            item.classList.remove('current-item');
        }
    });
}

// Inicializa o carrossel
updateGallery();

