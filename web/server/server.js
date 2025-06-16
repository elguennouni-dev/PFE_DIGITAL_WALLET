const express = require('express');
const path = require('path');
const dotenv = require('dotenv');
require('dotenv').config();

dotenv.config();



const app = express();
const PORT = process.env.PORT || 3000;

const publicDirectoryPath = path.join(__dirname, '..', 'public');
const mobileAppPath = path.join(__dirname, '..', 'mobile-app');


app.use(express.static(publicDirectoryPath));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

const webRoutes = require('./routes/webRoutes');
const mobileRoutes = require('./routes/mobileRoutes');

app.use('/', webRoutes(publicDirectoryPath));
app.use('/mobile', mobileRoutes(mobileAppPath));

app.use((req, res) => {
    res.status(404).sendFile(path.join(publicDirectoryPath, 'index.html'));
});

app.use((err, req, res, next) => {
    console.error('Server error:', err);
    if (!res.headersSent) {
        res.status(500).send('Internal Server Error');
    }
});

app.listen(PORT, () => {
    console.log(`Server running at http://localhost:${PORT}`);
    console.log(`Serving static files from: ${publicDirectoryPath}`);
});

['SIGINT', 'SIGTERM'].forEach(signal => {
    process.on(signal, () => {
        console.log(`${signal} received, shutting down gracefully`);
        process.exit(0);
    });
});
