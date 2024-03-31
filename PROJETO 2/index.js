const express = require('express');
const path = require('path');
const port = 3000;
const app = express();
const mongoose = require('mongoose');

app.get('/favicon.ico', (req, res) => res.status(204).end());

// Set up static file serving from the 'static' directory
app.use(express.static('static', {
  setHeaders: (res, path, stat) => {
    if (path.endsWith('.css')) {
      res.set('Content-Type', 'text/css');
    } else if (path.endsWith('.js')) {
      res.set('Content-Type', 'text/javascript');
    }
  }
}));

// Conectar ao banco de dados MongoDB
mongoose.connect('mongodb://localhost:27017/projetoRP', { useNewUrlParser: true, useUnifiedTopology: true });
const db = mongoose.connection;

// Verificar a conexão ao banco de dados
db.on('error', console.error.bind(console, 'Erro de conexão ao MongoDB:'));
db.once('open', function () {
  console.log('Conectado ao MongoDB');
});

// Definir o esquema do modelo para a coleção sensor
const sensorSchema = new mongoose.Schema({
  dado: Number,
  timestamp: Number
});


const Sensor = mongoose.model('sensor', sensorSchema, 'sensor');

app.get('/atualizarDados', async (req, res) => {
  try {
    const dadosMaisRecentes = await Sensor.find().sort({ timestamp: -1 }).limit(7);
    console.log(dadosMaisRecentes)
    res.json(dadosMaisRecentes);
  } catch (error) {
    console.error('Erro ao obter os dados mais recentes:', error);
    res.status(500).send('Erro interno do servidor');
  }
});

// Rota para o arquivo atualizarDados.js
app.get('/atualizarDados.js', (req, res) => {
  res.sendFile('atualizarDados.js', { root: path.join(__dirname, 'static') });
});

// Create routes for each HTML file
app.get('/', (req, res) => {
  res.sendFile('index.html', { root: path.join(__dirname, 'static') });
});

app.get('/estufas', (req, res) => {
  res.sendFile('estufas.html', { root: path.join(__dirname, 'static') });
});

app.get('/consultoria', (req, res) => {
  res.sendFile('consultoria.html', { root: path.join(__dirname, 'static') });
});

app.get('/negocios', (req, res) => {
  res.sendFile('negocios.html', { root: path.join(__dirname, 'static') });
});

app.get('/recursos', (req, res) => {
  res.sendFile('recursos.html', { root: path.join(__dirname, 'static') });
});

app.get('/service', (req, res) => {
  res.sendFile('service.html', { root: path.join(__dirname, 'static') });
});

// Start the server
app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
