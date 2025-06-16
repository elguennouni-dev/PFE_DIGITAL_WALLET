
// QR Login Auth With WebSoccket <--> Backend (Real Time)
// By ABDELILAH EL GUENNOUNI

document.addEventListener("DOMContentLoaded", () => {

    const config = {
      baseUrl: "http://84.247.142.209:8080",
      endpoints: {
        createQr: "/qr/create",
        websocket: "/ws/qr",
      },
      redirectPath: "/dashboard-loader.html",
      qrExpirySeconds: 60,
      retryDelayMs: 3000,
    };
  
    const qrDiv = document.getElementById("qrCode");
    const statusEl = document.getElementById("status");
    const countdownEl = document.getElementById("countdown");
  
    let countdownInterval;
    let socket;
  
    const updateStatus = (text, type = 'info') => {
      const icons = {
        info: '<i class="fas fa-spinner fa-spin"></i>',
        success: '<i class="fas fa-check-circle"></i>',
        error: '<i class="fas fa-exclamation-triangle"></i>',
      };
      const colors = {
        info: 'blue',
        success: 'green',
        error: 'red',
      };
      statusEl.innerHTML = `${icons[type] || ''} ${text}`;
      statusEl.style.color = colors[type];
    };
  
    const cleanup = () => {
      if (countdownInterval) {
        clearInterval(countdownInterval);
        countdownInterval = null;
      }

      if (socket?.readyState === WebSocket.OPEN) {
        socket.close();
      }
      socket = null;
    };
  
    const startCountdown = (expiryTime) => {
      const update = () => {
        const now = new Date();
        const diffSeconds = Math.floor((expiryTime - now) / 1000);
  
        if (diffSeconds <= 0) {
          countdownEl.innerHTML = "⛔ Code expiré. Génération d'un nouveau code...";
          cleanup();
          setTimeout(createNewQRCode, 1500);
          return;
        }
        countdownEl.innerHTML = `⌛ Ce code expire dans ${diffSeconds} secondes...`;
      };
  
      update();
      countdownInterval = setInterval(update, 1000);
    };
  

    const connectWebSocket = (qrCodeData) => {

      const wsProtocol = window.location.protocol === 'https:' ? 'wss' : 'ws';
      const wsUrl = `${wsProtocol}://${config.baseUrl.split('//')[1]}${config.endpoints.websocket}?qrCodeData=${encodeURIComponent(qrCodeData)}`;
      
      socket = new WebSocket(wsUrl);
  
      socket.onopen = () => console.log("WebSocket connection established.");

      console.log("%cQR Code Data: " + qrCodeData, "color: red; font-size: 20px; font-weight: bold;");

  
      socket.onmessage = (event) => {
        try {
          const message = JSON.parse(event.data);

          if (message?.type === "token" && message?.data?.token) {
            localStorage.setItem("userData", JSON.stringify(message.data));
            console.log("Authentication successful. Token received.");
            console.log("Token: ", message.data.token);
            cleanup();
            window.location.href = config.redirectPath;
          } else {
            console.warn("Unexpected WebSocket message received:", message);
          }
        } catch (e) {
          console.error("Error parsing WebSocket message:", e);
        }
      };
  
      socket.onerror = (error) => {
        console.error("WebSocket error:", error);
        updateStatus("Erreur de connexion en temps réel.", 'error');
      };
  
      socket.onclose = () => console.log("WebSocket connection closed.");
    };

    const createNewQRCode = async () => {
      cleanup();
      updateStatus("Génération du nouveau QR Code...", 'info');
      countdownEl.innerHTML = "⏳ Chargement...";
      qrDiv.innerHTML = "";
  
      try {
        const response = await fetch(`${config.baseUrl}${config.endpoints.createQr}`);
  
        if (!response.ok) {
          throw new Error(`Server responded with status: ${response.status}`);
        }
  
        const data = await response.json();
        const { qrCodeData, generatedAt } = data;
  
        if (!qrCodeData || !generatedAt) {
          throw new Error("Invalid data received from server.");
        }
        
        const serverTimeAsLocal = new Date(generatedAt);
        const correctedTime = new Date(serverTimeAsLocal.getTime() - 3600000);
        const expiryTime = new Date(correctedTime.getTime() + config.qrExpirySeconds * 1000);
  
        new QRCode(qrDiv, {
          text: qrCodeData,
          width: 220,
          height: 220,
          colorDark: "#000000",
          colorLight: "#ffffff",
          correctLevel: QRCode.CorrectLevel.H,
        });
  
        updateStatus("QR Code généré avec succès.", 'success');
        startCountdown(expiryTime);
        connectWebSocket(qrCodeData);
  
      } catch (error) {
        console.error("Failed to create QR code:", error);
        updateStatus("Erreur de génération du QR Code.", 'error');
        setTimeout(createNewQRCode, config.retryDelayMs);
      }
    };

    createNewQRCode();
  
    window.addEventListener("beforeunload", cleanup);
  });




