-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS ociojaen;

USE ociojaen;

-- Crear la tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    token VARCHAR(255)
);

-- Insertar usuario de prueba con contraseña hasheada previamente
INSERT INTO usuarios (username, password , token) VALUES
('admin', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd1c54ca5f9a6a0ab58', ''); 

-- Asegúrate de que la base de datos existe
CREATE DATABASE IF NOT EXISTS ociojaen;

USE ociojaen;

-- Crear la tabla de eventos
CREATE TABLE IF NOT EXISTS eventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    imagen TEXT NOT NULL
);

-- Insertar eventos de prueba
INSERT INTO eventos (titulo, descripcion, imagen) VALUES
('Castillo de Santa Catalina', 'Visita guiada al Castillo', 'https://photo620x400.mnstatic.com/dbd103641c66eb7575141183099b0a7c/castillo-de-santa-catalina.jpg'),
('Real Jaén CF', 'Información sobre partidos del Real Jaen', 'https://img.extrajaen.com/publicaciones/1200_Real-Jaen.jpg'),
('Festival LaMonaFest', 'II edición del festival', 'https://tickets.evezing.com/images/events/658bf545160fe.jpeg?w=555&h=295&fit=crop&crop=edges&auto=format'),
('Feria de San Lucas', 'Feria tradicional con atracciones, casetas y eventos culturales', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/Portal_San_Lucas_2005.jpg/800px-Portal_San_Lucas_2005.jpg'),
('Semana Santa en Jaén', 'Procesiones y actos religiosos durante la Semana Santa', 'https://www.turjaen.es/wp-content/uploads/2024/03/Semana-santa-en-la-provincia-de-Jaen-TURJAEN.jpg'),
('Catedral de Jaén', 'Visita guiada a la Catedral de la Asunción de la Virgen', 'https://multimedia.andalucia.org/content_images/main_image_43177.jpeg');