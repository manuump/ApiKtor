-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS ociojaen;

USE ociojaen;

-- Crear la tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL
);

-- Insertar usuario de prueba con contraseña hasheada previamente
INSERT INTO usuarios (username, password) VALUES
('admin', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd1c54ca5f9a6a0ab58');
