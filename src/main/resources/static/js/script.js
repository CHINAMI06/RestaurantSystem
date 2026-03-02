const pageTop = document.getElementById('page-top');

window.addEventListener('scroll', () => {
    if (window.scrollY > 600) {
        pageTop.classList.add('show');
    } else {
        pageTop.classList.remove('show');
    }
});

pageTop.addEventListener('click', () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
});
