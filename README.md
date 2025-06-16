# 📁 Digital Documents Wallet – PFE 2024/2025

A secure and modern **digital wallet for personal documents** – built for the **graduation project (PFE)** of *BTS Développement des Systèmes d'Information* 2024/2025.

> 🎓 Created by **Abdelilah EL-Guennouni** & **Yassine AitAouicha**  
> 👨‍💻 Fully open source and ready for future improvements

---

## 📸 Screenshots

| Android Login (Secure) | Web QR Code Login | Dashboard |
|------------------------|-------------------|-----------|
| ![Android Login](screenshots/android-login.png) | ![QR Login](screenshots/web-qr-login.png) | ![Dashboard](screenshots/dashboard.png) |

---

## ✨ Features

- 🔐 **QR Code Authentication** (like WhatsApp Web)
- 🗂️ Upload & View Documents (PDF, JSON, TXT…)
- 🧠 AES + RSA Encryption
- 📱 Mobile App (Android)
- 💻 Web App (HTML, JS, Bootstrap)
- 🔐 Token-based Authentication with JWT
- 🗃️ PostgreSQL + File Storage
- 👤 User Sessions & Access History

---

## 🧱 Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Java + Spring Boot |
| Security | Spring Security + JWT + AES + RSA |
| Frontend (Web) | HTML, CSS, JavaScript, Bootstrap |
| Frontend (Mobile) | Android (XML, Java) |
| Database | PostgreSQL |
| Server | Linux (Contabo) |
| Hosting | Self-hosted, Nginx + PM2 |

---

## 🚀 Getting Started

### 🔧 Prerequisites

- Java 17+
- Node.js
- Android Studio
- PostgreSQL
- Git

### 🛠️ Backend Setup

```bash
cd backend/
mvn clean install
java -jar target/digitalWallet-0.0.1-SNAPSHOT.jar

---

🌐 Frontend (Web)
cd front-end/
node server/server.js
# Runs on http://localhost:3000


📱 Android
Open with Android Studio and run on emulator/device.


🧠 Cryptography Flow (Simplified)
We use hybrid encryption to protect user documents:
1. Generate AES Key (for document)
2. Encrypt AES key using user's RSA public key
3. Store (Encrypted File + Encrypted AES key)
4. On view: decrypt AES key using RSA private key, then decrypt file

See EncryptionUtils.java & RSAKeyService.java for full implementation.

🧩 Project Architecture
📦 digital-wallet
├── backend/
│   └── src/main/java/pfe/digitalWallet/...
├── front-end/
│   ├── public/
│   └── server/
├── android-app/
│   └── src/
├── database/
│   └── schema.sql
└── screenshots/


🛤 Roadmap
✅ QR Code Login System
✅ Basic Dashboard
✅ Secure File Upload
✅ Login History Tracking
✅ Login Attempts Tracking
✅ Encrypted Cloud Storage (S3 / GCP)

🤝 Contributing
We welcome contributions to enhance the project.
git clone https://github.com/elguennouni-dev/PFE_DIGITAL_WALLET.git
Feel free to create PRs, suggest features, or report bugs in the Issues tab.

📄 License
This project is licensed under the MIT License.

📬 Contact
    💼 Abdelilah EL-Guennouni – LinkedIn | Email

🧠 Built with passion, security, and the future in mind.
Please ⭐ the repo if you find it useful!
