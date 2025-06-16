
// Web dashboard js code
// By ABDELILAH EL GUENNOUNI

function initializeDashboard() {
  const userData = JSON.parse(localStorage.getItem("userData"));
  const API_BASE_URL = "http://84.247.142.209:8080/documents";
  const MAX_FILE_SIZE_MB = 100;
  const MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024;

  const state = {
    username: userData.username,
    token: userData.token,
    documents: [],
    currentDocumentId: null,
    language: localStorage.getItem("language") || "en",
    theme: localStorage.getItem("theme") || "dark",
  };

  const translations = {
    en: {
      settings: "Settings",
      security: "Security",
      theme: "Theme",
      language: "Language",
      light: "Light",
      dark: "Dark",
      uploadDocument: "Upload Document",
      welcome: "Welcome, ",
      manageDocs: "Here you can manage your documents.",
      logout: "Logout",
      edit: "Edit",
      save: "Save",
      download: "Download",
      delete: "Delete",
      saving: "Saving...",
      downloading: "Downloading...",
      uploadNewDoc: "Upload New Document",
      docTitleLabel: "Document Title",
      docFileLabel: "File",
      dropAreaText: "Drag & drop files here or click to select",
      upload: "Upload",
      uploading: "Uploading...",
      confirmDeleteTitle: "Confirm Deletion",
      confirmDeleteMsg:
        "Are you sure you want to delete this document? This action cannot be undone.",
      cancel: "Cancel",
      confirmDeleteBtn: "Delete",
      deleting: "Deleting...",
      noDocumentsTitle: "No Documents Yet",
      noDocumentsText: "Click the upload button to add your first document.",
      aiSearchPlaceholder: "Ask AI about your documents...",
      fileTooLarge: `File is too large. Max size: ${MAX_FILE_SIZE_MB}MB.`,
      requiredFields: "Please provide a title and select a file.",
      uploadSuccess: "Document uploaded successfully!",
      uploadFailed: "Upload failed.",
      titleUpdateSuccess: "Title updated successfully.",
      titleUpdateFailed: "Failed to update title.",
      deleteSuccess: "Document deleted.",
      deleteFailed: "Failed to delete document.",
      loadDocsFailed: "Failed to load documents.",
      loadDocFailed: "Failed to load document content.",
      downloadFailed: "Failed to download file.",
      aiSearchFailed: "AI search failed.",
      SecureConnection: "Secure Connection",
    },
    fr: {
      settings: "Paramètres",
      security: "Sécurité",
      theme: "Thème",
      language: "Langue",
      light: "Clair",
      dark: "Sombre",
      uploadDocument: "Téléverser",
      welcome: "Bienvenue, ",
      manageDocs: "Gérez vos documents ici.",
      logout: "Déconnexion",
      edit: "Modifier",
      save: "Sauvegarder",
      download: "Télécharger",
      delete: "Supprimer",
      saving: "Sauvegarde...",
      downloading: "Téléchargement...",
      uploadNewDoc: "Téléverser un document",
      docTitleLabel: "Titre du document",
      docFileLabel: "Fichier",
      dropAreaText: "Glissez-déposez ou cliquez pour sélectionner",
      upload: "Téléverser",
      uploading: "Téléversement...",
      confirmDeleteTitle: "Confirmer la suppression",
      confirmDeleteMsg:
        "Êtes-vous sûr de vouloir supprimer ce document ? Cette action est irréversible.",
      cancel: "Annuler",
      confirmDeleteBtn: "Supprimer",
      deleting: "Suppression...",
      noDocumentsTitle: "Aucun document",
      noDocumentsText:
        "Cliquez sur le bouton de téléversement pour ajouter un document.",
      aiSearchPlaceholder: "Interrogez l'IA sur vos documents...",
      fileTooLarge: `Fichier trop volumineux. Taille max : ${MAX_FILE_SIZE_MB}Mo.`,
      requiredFields: "Veuillez fournir un titre et un fichier.",
      uploadSuccess: "Document téléversé avec succès !",
      uploadFailed: "Échec du téléversement.",
      titleUpdateSuccess: "Titre mis à jour.",
      titleUpdateFailed: "Échec de la mise à jour du titre.",
      deleteSuccess: "Document supprimé.",
      deleteFailed: "Échec de la suppression.",
      loadDocsFailed: "Échec du chargement des documents.",
      loadDocFailed: "Échec du chargement du document.",
      downloadFailed: "Échec du téléchargement.",
      aiSearchFailed: "La recherche IA a échoué.",
      SecureConnection: "Connéction Sécurisé",
    },
    ar: {
      settings: "الإعدادات",
      security: "الأمان",
      theme: "المظهر",
      language: "اللغة",
      light: "فاتح",
      dark: "داكن",
      uploadDocument: "حمّل وثيقة",
      welcome: "مرحبا، ",
      manageDocs: "من هنا تقدر تنظم وثائقك.",
      logout: "تسجيل الخروج",
      edit: "بدّل",
      save: "سيفي",
      download: "حمّل",
      delete: "مسح",
      saving: "كيحفظ...",
      downloading: "كيحمّل...",
      uploadNewDoc: "حمّل وثيقة جديدة",
      docTitleLabel: "عنوان الوثيقة",
      docFileLabel: "الملف",
      dropAreaText: "جرّ وطيّح الملفات هنا ولا كليكي باش تختار",
      upload: "حمّل",
      uploading: "كيمسح...",
      confirmDeleteTitle: "تأكيد المسح",
      confirmDeleteMsg:
        "واخا متأكد بلي باغي تمسح هاد الوثيقة؟ مايمكنش ترجّعها من بعد.",
      cancel: "إلغاء",
      confirmDeleteBtn: "مسح",
      deleting: "كيمسح...",
      noDocumentsTitle: "ما كاين حتى وثيقة",
      noDocumentsText: "كليكي على زر التحميل باش تضيف أول وثيقة.",
      aiSearchPlaceholder: "سول الذكاء الاصطناعي على وثائقك...",
      fileTooLarge: `الملف كبير بزاف. الحد الأقصى: ${MAX_FILE_SIZE_MB}MB.`,
      requiredFields: "خصك دير عنوان وتختار ملف.",
      uploadSuccess: "الوثيقة ترفعات بنجاح!",
      uploadFailed: "فشل التحميل.",
      titleUpdateSuccess: "العنوان تبدل بنجاح.",
      titleUpdateFailed: "ماقدرتش نبدل العنوان.",
      deleteSuccess: "الوثيقة تمسحات.",
      deleteFailed: "ماقدرتش نمسح الوثيقة.",
      loadDocsFailed: "ماقدرتش نحمل الوثائق.",
      loadDocFailed: "فشل تحميل المحتوى.",
      downloadFailed: "فشل التحميل.",
      aiSearchFailed: "فشل البحث بالذكاء الاصطناعي.",
      SecureConnection: "اتصال آمن",
    },
  };

  const selectors = {
    sidebar: document.querySelector(".sidebar"),
    menuToggleBtn: document.getElementById("menuToggleBtn"),
    sidebarCloseBtn: document.getElementById("sidebarCloseBtn"),
    documentsGrid: document.getElementById("documentsGrid"),
    emptyState: document.getElementById("emptyState"),
    themeToggle: document.getElementById("themeToggle"),
    langSwitcher: document.getElementById("langSwitcher"),
    userUsername: document.getElementById("userUsername"),
    logoutBtn: document.getElementById("logoutBtn"),
    notificationContainer: document.getElementById("notification-container"),
    viewModal: document.getElementById("viewModal"),
    uploadModal: document.getElementById("uploadModal"),
    deleteConfirmModal: document.getElementById("deleteConfirmModal"),
    settingsModal: document.getElementById("settingsModal"),
    securityModal: document.getElementById("securityModal"),
    settingsLink: document.getElementById("settingsLink"),
    securityLink: document.getElementById("securityLink"),
    themeSelect: document.getElementById("themeSelect"),
    langSelect: document.getElementById("langSelect"),
    allModals: document.querySelectorAll(".modal"),
    modalTitle: document.getElementById("modalTitle"),
    modalBody: document.getElementById("modalBody"),
    editTitleBtn: document.getElementById("editTitleBtn"),
    saveTitleBtn: document.getElementById("saveTitleBtn"),
    downloadBtn: document.getElementById("downloadBtn"),
    deleteBtn: document.getElementById("deleteBtn"),
    cancelDeleteBtn: document.getElementById("cancelDelete"),
    confirmDeleteBtn: document.getElementById("confirmDelete"),
    uploadBtnMain: document.getElementById("uploadBtnMain"),
    uploadForm: document.getElementById("uploadForm"),
    docTitleInput: document.getElementById("docTitle"),
    dropArea: document.getElementById("drop-area"),
    fileInput: document.getElementById("fileElem"),
    fileNameDisplay: document.getElementById("file-name-display"),
    aiSearchInput: document.getElementById("aiSearchInput"),
    aiSearchButton: document.querySelector(".ai-search-button"),

    securityModal: document.getElementById("securityModal"),
    securityTabs: document.querySelectorAll(".tab-link"),
    securityTabContents: document.querySelectorAll(".tab-content"),
  };

  const api = {
    async _fetch(url, options = {}) {
      const signal = AbortSignal.timeout(30000);
      const defaultHeaders = { Authorization: `Bearer ${state.token}` };
      if (!(options.body instanceof FormData)) {
        defaultHeaders["Content-Type"] = "application/json";
      }

      try {
        const response = await fetch(url, {
          ...options,
          headers: { ...defaultHeaders, ...options.headers },
          signal,
        });
        if (!response.ok) {
          const error = new Error(`HTTP error! Status: ${response.status}`);
          try {
            error.data = await response.json();
          } catch {
            error.data = { message: "An unknown error occurred." };
          }
          throw error;
        }
        return response.headers
          .get("content-type")
          ?.includes("application/json")
          ? response.json()
          : response.blob();
      } catch (err) {
        if (err.name === "TimeoutError") throw new Error("Request timed out.");
        throw err;
      }
    },
    getDocuments: () => api._fetch(`${API_BASE_URL}/user/${state.username}`),
    getDocumentById: (id) =>
      api._fetch(`${API_BASE_URL}/${id}?username=${state.username}`),
    updateTitle: (id, newTitle) =>
      api._fetch(`${API_BASE_URL}/${id}`, {
        method: "PUT",
        body: JSON.stringify({
          newDocumentTitle: newTitle,
          username: state.username,
        }),
      }),
    deleteDocument: (id) =>
      api._fetch(`${API_BASE_URL}/${id}?username=${state.username}`, {
        method: "DELETE",
      }),
    aiSearch: (query) =>
      api._fetch(`${API_BASE_URL}/ask-ai?username=${state.username}`, {
        method: "POST",
        body: JSON.stringify({ query }),
      }),
    uploadDocument: (formData) => {
      return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open("POST", API_BASE_URL, true);
        xhr.setRequestHeader("Authorization", `Bearer ${state.token}`);
        xhr.upload.onprogress = (event) => {
          if (event.lengthComputable) {
            const percentComplete = Math.round(
              (event.loaded / event.total) * 100
            );
            ui.showNotification(
              `${translations[state.language].uploading} ${percentComplete}%`,
              "info",
              null
            );
          }
        };
        xhr.onload = () => {
          if (xhr.status >= 200 && xhr.status < 300) {
            try {
              resolve(JSON.parse(xhr.responseText));
            } catch (e) {
              reject(new Error("Invalid JSON response from server."));
            }
          } else {
            const error = new Error(`HTTP error! Status: ${xhr.status}`);
            try {
              error.data = JSON.parse(xhr.responseText);
            } catch {
              error.data = { message: "Upload failed." };
            }
            reject(error);
          }
        };
        xhr.onerror = () => reject(new Error("Network error during upload."));
        xhr.send(formData);
      });
    },
  };
  const ui = {
    showNotification: (message, type = "info", duration = 3000) => {
      if (!selectors.notificationContainer) return;
      const notification = document.createElement("div");
      notification.className = `toast-notification ${type}`;
      notification.textContent = message;
      selectors.notificationContainer.appendChild(notification);
      setTimeout(() => {
        notification.classList.add("show");
        if (duration) {
          setTimeout(() => {
            notification.classList.remove("show");
            setTimeout(() => notification.remove(), 500);
          }, duration);
        }
      }, 10);
      return notification;
    },
    renderSkeletonLoaders: (count = 6) => {
      if (!selectors.documentsGrid) return;
      selectors.documentsGrid.innerHTML = "";
      selectors.emptyState.style.display = "none";
      selectors.documentsGrid.innerHTML = Array.from(
        { length: count },
        () => `
                <div class="doc-card skeleton">
                    <div class="doc-icon"></div><h3 class="doc-title"></h3><p class="doc-date"></p>
                </div>
            `
      ).join("");
    },
    getFileIcon: (fileType = "DEFAULT") => {
      const type = fileType.toUpperCase();
      if (type.includes("PDF")) return { icon: "file-text", class: "pdf-icon" };
      if (type.includes("DOC"))
        return { icon: "file-text", class: "docx-icon" };
      if (type.includes("PPT")) return { icon: "airplay", class: "pptx-icon" };
      if (type.includes("XLS"))
        return { icon: "bar-chart-2", class: "xlsx-icon" };
      if (["PNG", "JPG", "JPEG", "GIF", "SVG", "WEBP"].includes(type))
        return { icon: "image", class: "img-icon" };
      if (type.includes("JSON")) return { icon: "code", class: "json-icon" };
      return { icon: "file", class: "default-icon" };
    },
    renderDocuments: (documents) => {
      if (!selectors.documentsGrid) return;
      selectors.documentsGrid.innerHTML = "";
      if (!documents?.length) {
        selectors.emptyState.style.display = "block";
        return;
      }
      selectors.emptyState.style.display = "none";
      documents.forEach((doc, index) => {
        const { icon, class: iconClass } = ui.getFileIcon(doc.fileType);
        const card = document.createElement("div");
        card.className = "doc-card";
        card.dataset.id = doc.id;
        card.style.setProperty("--delay", `${index * 50}ms`);
        card.innerHTML = `
                    <div class="doc-icon ${iconClass}"><i data-feather="${icon}"></i></div>
                    <h3 class="doc-title">${doc.documentTitle}</h3>
                    <p class="doc-date">${new Date(
                      doc.createdAt
                    ).toLocaleDateString(state.language)}</p>`;
        selectors.documentsGrid.appendChild(card);
      });
      if (typeof feather !== "undefined") feather.replace();
    },
    renderDocumentContent: async (docId) => {
      try {
        const doc = await api.getDocumentById(docId);
        if (!doc?.decryptedFileContent)
          throw new Error("Content not available.");
        const type = doc.fileType.toUpperCase();
        if (["PNG", "JPG", "JPEG", "GIF", "SVG", "WEBP"].includes(type)) {
          selectors.modalBody.innerHTML = `<img src="data:image/${type.toLowerCase()};base64,${
            doc.decryptedFileContent
          }" alt="${doc.documentTitle}">`;
        } else if (type === "PDF") {
          selectors.modalBody.innerHTML = `<iframe src="data:application/pdf;base64,${doc.decryptedFileContent}" width="100%" height="500px" title="${doc.documentTitle}"></iframe>`;
        } else if (type === "JSON") {
          const decoded = atob(doc.decryptedFileContent);
          selectors.modalBody.innerHTML = `<pre>${JSON.stringify(
            JSON.parse(decoded),
            null,
            2
          )}</pre>`;
        } else {
          selectors.modalBody.innerHTML = `<p>Preview for <strong>.${type.toLowerCase()}</strong> is not supported.</p>`;
        }
      } catch {
        ui.showNotification(
          translations[state.language].loadDocFailed,
          "error"
        );
        selectors.modalBody.innerHTML = `<p class="error-message">${
          translations[state.language].loadDocFailed
        }</p>`;
      }
    },
    openModal: (modal) => modal && (modal.style.display = "flex"),
    closeModal: (modal) => modal && (modal.style.display = "none"),
    setTheme: (theme) => {
      document.body.className = theme === "dark" ? "dark-theme" : "light-theme";
      if (document.body.classList.contains("rtl"))
        document.body.classList.add("rtl");
      state.theme = theme;
      localStorage.setItem("theme", theme);
      if (selectors.themeSelect) selectors.themeSelect.value = theme;
    },
    setLanguage: (lang) => {
      document.documentElement.lang = lang;
      document.body.classList.toggle("rtl", lang === "ar");
      ui.setTheme(state.theme);
      document.querySelectorAll("[data-i18n]").forEach((el) => {
        const key = el.dataset.i18n;
        const translation = translations[lang]?.[key];
        if (translation) {
          if (key === "welcome") {
            el.childNodes[0].nodeValue = translation;
          } else if (el.tagName === "OPTION") {
            el.textContent = translation;
          } else {
            const icon = el.querySelector("i");
            const textNode = Array.from(el.childNodes).find(
              (node) => node.nodeType === Node.TEXT_NODE
            );
            if (textNode) textNode.textContent = ` ${translation}`;
            else if (!icon) el.textContent = translation;
          }
        }
      });
      document.querySelectorAll("[data-i18n-placeholder]").forEach((el) => {
        const key = el.dataset.i18nPlaceholder;
        const translation = translations[lang]?.[key];
        if (translation) el.placeholder = translation;
      });
      state.language = lang;
      localStorage.setItem("language", lang);
      ui.renderDocuments(state.documents);
      if (selectors.langSelect) selectors.langSelect.value = lang;
    },
    toggleButtonSpinner: (button, show = true, i18nKey = "saving") => {
      if (!button) return;
      button.disabled = show;
      if (show) {
        button.dataset.originalHTML = button.innerHTML;
        button.innerHTML = `<span class="spinner"></span> <span>${
          translations[state.language][i18nKey]
        }</span>`;
      } else {
        button.innerHTML = button.dataset.originalHTML || "Save";
      }
    },
  };
  const handlers = {
    loadAllDocuments: async () => {
      if (!selectors.documentsGrid) return;
      ui.renderSkeletonLoaders();
      try {
        const docs = await api.getDocuments();
        state.documents = docs || [];
        ui.renderDocuments(state.documents);
      } catch {
        ui.showNotification(
          translations[state.language].loadDocsFailed,
          "error"
        );
        selectors.documentsGrid.innerHTML = `<p class="error-message">${
          translations[state.language].loadDocsFailed
        }</p>`;
      }
    },
    handleCardClick: async (e) => {
      const card = e.target.closest(".doc-card:not(.skeleton)");
      if (!card) return;
      state.currentDocumentId = card.dataset.id;
      selectors.modalTitle.textContent =
        card.querySelector(".doc-title").textContent;
      selectors.modalBody.innerHTML =
        '<div class="spinner-container"><span class="spinner"></span></div>';
      ui.openModal(selectors.viewModal);
      await ui.renderDocumentContent(state.currentDocumentId);
    },
    handleTitleSave: async () => {
      const newTitle = selectors.modalTitle.textContent.trim();
      ui.toggleButtonSpinner(selectors.saveTitleBtn, true, "saving");
      try {
        await api.updateTitle(state.currentDocumentId, newTitle);
        const cardTitle = selectors.documentsGrid.querySelector(
          `.doc-card[data-id="${state.currentDocumentId}"] .doc-title`
        );
        if (cardTitle) cardTitle.textContent = newTitle;
        ui.showNotification(
          translations[state.language].titleUpdateSuccess,
          "success"
        );
      } catch {
        ui.showNotification(
          translations[state.language].titleUpdateFailed,
          "error"
        );
      } finally {
        ui.toggleButtonSpinner(selectors.saveTitleBtn, false);
        selectors.modalTitle.contentEditable = false;
        selectors.editTitleBtn.style.display = "inline-flex";
        selectors.saveTitleBtn.style.display = "none";
      }
    },
    handleDeleteConfirm: async () => {
      ui.toggleButtonSpinner(selectors.confirmDeleteBtn, true, "deleting");
      try {
        await api.deleteDocument(state.currentDocumentId);
        const card = selectors.documentsGrid.querySelector(
          `.doc-card[data-id="${state.currentDocumentId}"]`
        );
        if (card) card.remove();
        state.documents = state.documents.filter(
          (doc) => doc.id !== state.currentDocumentId
        );
        if (state.documents.length === 0) ui.renderDocuments([]);
        ui.closeModal(selectors.deleteConfirmModal);
        ui.closeModal(selectors.viewModal);
        state.currentDocumentId = null;
        ui.showNotification(
          translations[state.language].deleteSuccess,
          "success"
        );
      } catch {
        ui.showNotification(translations[state.language].deleteFailed, "error");
      } finally {
        ui.toggleButtonSpinner(selectors.confirmDeleteBtn, false);
      }
    },
    handleDownload: async () => {
      const button = selectors.downloadBtn;
      ui.toggleButtonSpinner(button, true, "downloading");
      try {
        const doc = await api.getDocumentById(state.currentDocumentId);
        if (!doc?.decryptedFileContent) throw new Error();
        const byteString = atob(doc.decryptedFileContent);
        const ab = new ArrayBuffer(byteString.length);
        const ia = new Uint8Array(ab);
        for (let i = 0; i < byteString.length; i++) {
          ia[i] = byteString.charCodeAt(i);
        }
        const blob = new Blob([ab], {
          type: `application/${doc.fileType.toLowerCase()}`,
        });
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        link.download = `${doc.documentTitle}.${doc.fileType.toLowerCase()}`;
        link.click();
        URL.revokeObjectURL(link.href);
      } catch {
        ui.showNotification(
          translations[state.language].downloadFailed,
          "error"
        );
      } finally {
        ui.toggleButtonSpinner(button, false);
      }
    },
    handleUpload: async (e) => {
      e.preventDefault();
      const title = selectors.docTitleInput.value;
      const file = selectors.fileInput.files[0];
      if (!title || !file) {
        return ui.showNotification(
          translations[state.language].requiredFields,
          "error"
        );
      }
      if (file.size > MAX_FILE_SIZE_BYTES) {
        return ui.showNotification(
          translations[state.language].fileTooLarge,
          "error"
        );
      }
      const formData = new FormData();
      formData.append("documentTitle", title);
      formData.append("username", state.username);
      formData.append("file", file);
      formData.append("fileType", file.name.split(".").pop().toUpperCase());
      const submitBtn = selectors.uploadForm.querySelector(
        'button[type="submit"]'
      );
      ui.toggleButtonSpinner(submitBtn, true, "uploading");
      ui.closeModal(selectors.uploadModal);
      try {
        await api.uploadDocument(formData);
        await handlers.loadAllDocuments();
        e.target.reset();
        selectors.fileNameDisplay.textContent = "";
        ui.showNotification(
          translations[state.language].uploadSuccess,
          "success"
        );
      } catch (error) {
        ui.showNotification(
          `${translations[state.language].uploadFailed}: ${
            error.data?.message || error.message
          }`,
          "error"
        );
      } finally {
        ui.toggleButtonSpinner(submitBtn, false);
      }
    },
    handleAiSearch: async () => {
      const query = selectors.aiSearchInput.value.trim();
      if (!query) return;
      ui.toggleButtonSpinner(selectors.aiSearchButton, true, "searching");
      try {
        const response = await api.aiSearch(query);
        selectors.aiResultTitle.textContent = `"${query}"`;
        selectors.aiResultBody.innerHTML = response.answer;
        ui.openModal(selectors.aiResultModal);
      } catch {
        ui.showNotification(
          translations[state.language].aiSearchFailed,
          "error"
        );
      } finally {
        ui.toggleButtonSpinner(selectors.aiSearchButton, false);
      }
    },
    handleLogout: () => {
      localStorage.clear();
      window.location.href = "/auth";
    },
    handleDragDrop: (e) => {
      e.preventDefault();
      e.stopPropagation();
      const type = e.type;
      if (type === "dragenter" || type === "dragover") {
        selectors.dropArea.classList.add("highlight");
      } else if (type === "dragleave" || type === "drop") {
        selectors.dropArea.classList.remove("highlight");
      }
      if (type === "drop" && e.dataTransfer.files.length) {
        selectors.fileInput.files = e.dataTransfer.files;
        selectors.fileNameDisplay.textContent = `File: ${selectors.fileInput.files[0].name}`;
      }
    },
  };

  const setupEventListeners = () => {
    selectors.menuToggleBtn?.addEventListener("click", (e) => {
      e.stopPropagation();
      selectors.sidebar.classList.toggle("is-open");
    });
    selectors.sidebarCloseBtn?.addEventListener("click", () => {
      selectors.sidebar.classList.remove("is-open");
    });
    document.addEventListener("click", (e) => {
      if (
        window.innerWidth <= 1024 &&
        selectors.sidebar?.classList.contains("is-open") &&
        !selectors.sidebar.contains(e.target) &&
        !selectors.menuToggleBtn.contains(e.target)
      ) {
        selectors.sidebar.classList.remove("is-open");
      }
    });

    if (selectors.documentsGrid)
      selectors.documentsGrid.addEventListener(
        "click",
        handlers.handleCardClick
      );

    selectors.allModals.forEach((modal) => {
      modal.addEventListener("click", (e) => {
        if (e.target === modal) ui.closeModal(modal);
      });
      modal
        .querySelector(".close-btn")
        ?.addEventListener("click", () => ui.closeModal(modal));
    });
    selectors.editTitleBtn?.addEventListener("click", () => {
      selectors.modalTitle.contentEditable = true;
      selectors.modalTitle.focus();
      selectors.editTitleBtn.style.display = "none";
      selectors.saveTitleBtn.style.display = "inline-flex";
    });
    selectors.saveTitleBtn?.addEventListener("click", handlers.handleTitleSave);
    selectors.downloadBtn?.addEventListener("click", handlers.handleDownload);
    selectors.deleteBtn?.addEventListener("click", () =>
      ui.openModal(selectors.deleteConfirmModal)
    );
    selectors.cancelDeleteBtn?.addEventListener("click", () =>
      ui.closeModal(selectors.deleteConfirmModal)
    );
    selectors.confirmDeleteBtn?.addEventListener(
      "click",
      handlers.handleDeleteConfirm
    );
    selectors.uploadBtnMain?.addEventListener("click", () =>
      ui.openModal(selectors.uploadModal)
    );

    selectors.settingsLink?.addEventListener("click", (e) => {
      e.preventDefault();
      ui.openModal(selectors.settingsModal);
    });

    selectors.securityLink?.addEventListener("click", (e) => {
      e.preventDefault();
      ui.openModal(selectors.securityModal);
    });

    selectors.themeSelect?.addEventListener("change", (e) =>
      ui.setTheme(e.target.value)
    );
    selectors.langSelect?.addEventListener("change", (e) =>
      ui.setLanguage(e.target.value)
    );

    selectors.uploadForm?.addEventListener("submit", handlers.handleUpload);
    selectors.dropArea?.addEventListener("click", () =>
      selectors.fileInput.click()
    );
    ["dragenter", "dragover", "dragleave", "drop"].forEach((e) =>
      selectors.dropArea?.addEventListener(e, handlers.handleDragDrop)
    );
    selectors.fileInput?.addEventListener("change", () => {
      if (selectors.fileInput.files.length > 0) {
        selectors.fileNameDisplay.textContent = `File: ${selectors.fileInput.files[0].name}`;
      }
    });
    selectors.aiSearchButton?.addEventListener(
      "click",
      handlers.handleAiSearch
    );
    selectors.aiSearchInput?.addEventListener("keypress", (e) => {
      if (e.key === "Enter") handlers.handleAiSearch();
    });
    selectors.themeToggle?.addEventListener("click", () =>
      ui.setTheme(state.theme === "light" ? "dark" : "light")
    );
    selectors.langSwitcher?.addEventListener("change", (e) =>
      ui.setLanguage(e.target.value)
    );
    selectors.logoutBtn?.addEventListener("click", handlers.handleLogout);
  };

  const init = () => {
    if (selectors.documentsGrid) {
      handlers.loadAllDocuments();
    }

    if (selectors.userUsername)
      selectors.userUsername.textContent = state.username;

    if (selectors.langSelect) selectors.langSelect.value = state.language;
    if (selectors.themeSelect) selectors.themeSelect.value = state.theme;

    ui.setLanguage(state.language);

    setupEventListeners();
    if (typeof feather !== "undefined") feather.replace();
  };

  init();
}

window.initializeDashboard = initializeDashboard;
