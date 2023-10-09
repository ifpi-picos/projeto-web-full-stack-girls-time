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



const carouselContainer = document.querySelector('.carousel-container');
const carouselGallery = document.querySelector('.carousel-wrapper');
const carouselArrowLeft = document.querySelector(".carousel-arrow-left");
const carouselArrowRight = document.querySelector(".carousel-arrow-right");
const carouselItems = document.querySelectorAll('.carousel-item');
const maxCarouselItems = carouselItems.length;

let currentCarouselItem = 0;

function moveCarouselLeft() {
    currentCarouselItem = (currentCarouselItem - 1 + maxCarouselItems) % maxCarouselItems;
    carouselGallery.scrollLeft -= 250;
    updateCarousel();
}

function moveCarouselRight() {
    currentCarouselItem = (currentCarouselItem + 1) % maxCarouselItems;
    carouselGallery.scrollLeft += 250;
    updateCarousel();
}

function updateCarousel() {
    carouselItems.forEach((item, index) => {
        if (index === currentCarouselItem) {
            item.classList.add('current-carousel-item');
        } else {
            item.classList.remove('current-carousel-item');
        }
    });
}

// Inicializa o carrossel
updateCarousel();