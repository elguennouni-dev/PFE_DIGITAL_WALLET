const express = require('express');
const path = require('path');
const jwt = require('jsonwebtoken');

const JWT_SECRET = process.env.JWT_SECRET;

module.exports = (publicPath) => {
    const router = express.Router();

    const sendHtml = (res, fileName, notFoundMsg) => {
        const filePath = path.join(publicPath, fileName);
        res.sendFile(filePath, (err) => {
            if (err && !res.headersSent) {
                console.error(`Error sending ${fileName}:`, err);
                res.status(404).send(notFoundMsg);
            }
        });
    };

    router.get('/', (req, res) => {
        const authHeader = req.headers.authorization;
        if (!authHeader || !authHeader.startsWith("Bearer ")) {
            return res.status(401).send("Access Denied. No token provided.");
        }
    
        const token = authHeader.split(" ")[1];
    
        try {
            const decoded = jwt.verify(token, JWT_SECRET);
            sendHtml(res, 'dashboard-loader.html', 'Dashboard page not found');
        } catch (err) {
            console.error("Invalid token:", err);
            return res.status(403).send("Invalid or expired token.");
        }
    });

    router.get('/dashboard', (req, res) => {
        const authHeader = req.headers.authorization;
        if (!authHeader || !authHeader.startsWith("Bearer ")) {
            return res.status(401).send("Access Denied. No token provided.");
        }
    
        const token = authHeader.split(" ")[1];
    
        try {
            const decoded = jwt.verify(token, JWT_SECRET);
            sendHtml(res, 'dashboard-loader.html', 'Dashboard page not found');
        } catch (err) {
            console.error("Invalid token:", err);
            return res.status(403).send("Invalid or expired token.");
        }
    });
    
    return router;
};
