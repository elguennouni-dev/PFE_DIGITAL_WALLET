
// Dashboard redirection ( Loader )

(function () {
    const userDataStr = localStorage.getItem("userData") ? localStorage.getItem("userData") : null;
    console.log("userDataStr:", userDataStr);
    if (!userDataStr) {
        console.log("No userData found in localStorage");
        window.location.href = "/auth";
        return;
    }
    
    try {
        const userData = JSON.parse(userDataStr);
        const token = userData.token;
        
        if (!token) {
            console.log("Token is missing in userData");
            localStorage.removeItem("userData");
            window.location.href = "/auth";
            return;
        }

        console.log("Token found:", token);

        window.location.href = "/";

    } catch (err) {
        console.error("Error :", err);
        localStorage.removeItem("userData");
        window.location.href = "/auth";
    }
})();