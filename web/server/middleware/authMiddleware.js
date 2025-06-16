function authMiddleware(callbackIfValid, callbackIfInvalid) {
    const userDataStr = localStorage.getItem("userData");

    if (!userDataStr) {
        console.warn("No userData found in localStorage");
        return callbackIfInvalid("No token found");
    }

    try {
        const userData = JSON.parse(userDataStr);
        const token = userData?.token;

        if (!token) {
            console.warn("No token inside userData");
            return callbackIfInvalid("Token missing");
        }

        const payloadBase64 = token.split('.')[1];
        const decodedPayload = JSON.parse(atob(payloadBase64));

        const currentTime = Math.floor(Date.now() / 1000);
        if (decodedPayload.exp && decodedPayload.exp < currentTime) {
            console.warn("Token expired");
            return callbackIfInvalid("Token expired");
        }

        callbackIfValid(decodedPayload);
    } catch (err) {
        console.error("Invalid token or userData:", err);
        callbackIfInvalid("Invalid token or corrupted userData");
    }
}