document.addEventListener('DOMContentLoaded', () => {
    // General Page Top Button
    const pageTop = document.getElementById('page-top');
    if (pageTop) {
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
    }

    // Handle all menu toggles
    const menuToggles = document.querySelectorAll('.menu-toggle');
    menuToggles.forEach(toggle => {
        toggle.addEventListener('click', () => {
            const nav = toggle.closest('nav');
            if (nav) {
                const navLinks = nav.querySelector('ul');
                if (navLinks) {
                    navLinks.classList.toggle('active');
                }
            }
        });
    });
});
