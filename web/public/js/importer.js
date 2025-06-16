
document.addEventListener("DOMContentLoaded", () => {
    const loadComponent = async (url, placeholderId) => {
        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error(`Could not fetch ${url}. Status: ${response.status}`);
            const html = await response.text();
            const placeholder = document.getElementById(placeholderId);
            if (placeholder) {
                placeholder.innerHTML = html;
            }
        } catch (error) {
            console.error("Failed to load component:", error);
            const placeholder = document.getElementById(placeholderId);
            if(placeholder) placeholder.innerHTML = `<p style="color:red; padding: 1rem;">Failed to load component: ${placeholderId}</p>`;
        }
    };

    const setActiveNavlink = () => {
        const currentPage = window.location.pathname.split('/').pop();
        const navLinks = document.querySelectorAll('.sidebar-nav a');
        
        navLinks.forEach(link => {
            const linkPage = link.getAttribute('href');
            if (linkPage === currentPage || (currentPage === '' && linkPage === 'Dashboard.html')) {
                link.classList.add('active');
            } else {
                link.classList.remove('active');
            }
        });
    };

    const initializeLayout = async () => {
        await Promise.all([
            loadComponent('components/_sidebar.html', 'sidebar-placeholder'),
            loadComponent('components/_navbar.html', 'navbar-placeholder')
        ]);

        setActiveNavlink();

        if (typeof feather !== 'undefined') {
            feather.replace();
        }

        const pageName = window.location.pathname.split('/').pop();

        if ((pageName === 'Dashboard.html' || pageName === '') && typeof initializeDashboard === 'function') {
            initializeDashboard();
        } else if (pageName === 'Settings.html' && typeof initializeSettings === 'function') {
            initializeSettings();
        } else if (pageName === 'Security.html' && typeof initializeSecurity === 'function') {
            initializeSecurity();
        }
    };

    initializeLayout();
});