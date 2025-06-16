
function initializeSettings() {
    const userData = JSON.parse(localStorage.getItem('userData'));
    if (!userData?.token || !userData?.username) {
        window.location.href = 'index.html';
        return;
    }

    const state = {
        username: userData.username,
        theme: localStorage.getItem('theme') || 'light',
        language: localStorage.getItem('language') || 'en',
    };

    const selectors = {
        usernameInput: document.getElementById('usernameInput'),
        themeSelect: document.getElementById('themeSelect'),
        languageSelect: document.getElementById('languageSelect'),
        headerThemeToggle: document.getElementById('themeToggle'),
        headerLangSwitcher: document.getElementById('langSwitcher'),
    };

    const setTheme = (theme) => {
        state.theme = theme;
        localStorage.setItem('theme', theme);
        document.body.className = theme === 'dark' ? 'dark-theme' : 'light-theme';
        if (document.body.classList.contains('rtl')) document.body.classList.add('rtl');
        if(selectors.themeSelect) selectors.themeSelect.value = theme;
    };

    const setLanguage = (lang) => {
        state.language = lang;
        localStorage.setItem('language', lang);
        if(selectors.languageSelect) selectors.languageSelect.value = lang;
        if(selectors.headerLangSwitcher) selectors.headerLangSwitcher.value = lang;
    };

    const setupEventListeners = () => {
        selectors.themeSelect?.addEventListener('change', (e) => {
            setTheme(e.target.value);
        });

        selectors.languageSelect?.addEventListener('change', (e) => {
            setLanguage(e.target.value);
        });

        selectors.headerThemeToggle?.addEventListener('click', () => {
             const newTheme = document.body.classList.contains('dark-theme') ? 'dark' : 'light';
             if (selectors.themeSelect) selectors.themeSelect.value = newTheme;
        });

        selectors.headerLangSwitcher?.addEventListener('change', (e) => {
            if (selectors.languageSelect) selectors.languageSelect.value = e.target.value;
        });
    };

    const init = () => {
        if (selectors.usernameInput) {
            selectors.usernameInput.value = state.username;
        }
        if (selectors.themeSelect) {
            selectors.themeSelect.value = state.theme;
        }
        if (selectors.languageSelect) {
            selectors.languageSelect.value = state.language;
        }

        setupEventListeners();
    };

    init();
}