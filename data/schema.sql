-- ===============================
--   BASE DE DATOS DAMA SPORTS
--   Autor: Luis & Javi
-- ===============================

-- Elimina la base si ya existe
DROP DATABASE IF EXISTS dama;

-- Crea la base de datos
CREATE DATABASE dama;
USE dama;

-- ---------- TABLA SOCIOS ----------
CREATE TABLE socios (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(100) NOT NULL,
                        email VARCHAR(150) NOT NULL UNIQUE,
                        activo BOOLEAN DEFAULT TRUE,
                        fecha_alta DATE NOT NULL
);

-- ---------- TABLA PISTAS ----------
CREATE TABLE pistas (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(100) NOT NULL UNIQUE,
                        tipo ENUM('TENIS', 'PADEL', 'FUTBOL') NOT NULL,
                        precio_base DECIMAL(10,2) NOT NULL
);

-- ---------- TABLA RESERVAS ----------
CREATE TABLE reservas (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          id_socio INT NOT NULL,
                          id_pista INT NOT NULL,
                          inicio DATETIME NOT NULL,
                          fin DATETIME NOT NULL,
                          precio DECIMAL(10,2) NOT NULL,
                          CONSTRAINT fk_reserva_socio FOREIGN KEY (id_socio) REFERENCES socios(id),
                          CONSTRAINT fk_reserva_pista FOREIGN KEY (id_pista) REFERENCES pistas(id)
);

-- ---------- FUNCIÓN PARA CALCULAR PRECIO ----------
DELIMITER $$

CREATE FUNCTION fn_calcular_precio(
    p_id_pista INT,
    p_inicio DATETIME,
    p_fin DATETIME
)
    RETURNS DECIMAL(10,2)
    DETERMINISTIC
BEGIN
    DECLARE v_precio_base DECIMAL(10,2);
    DECLARE v_horas DECIMAL(10,2);

    -- Obtiene precio base de la pista
SELECT precio_base INTO v_precio_base
FROM pistas
WHERE id = p_id_pista;

-- Calcula horas (mínimo 1)
SET v_horas = TIMESTAMPDIFF(MINUTE, p_inicio, p_fin) / 60;
    IF v_horas < 1 THEN
        SET v_horas = 1;
END IF;

RETURN v_horas * v_precio_base;
END $$

DELIMITER ;

-- ---------- PROCEDIMIENTO PARA CREAR RESERVA ----------
DELIMITER $$

CREATE PROCEDURE sp_crear_reserva(
    IN p_id_pista INT,
    IN p_id_socio INT,
    IN p_inicio DATETIME,
    IN p_fin DATETIME,
    OUT p_precio DECIMAL(10,2)
)
BEGIN
    -- Calcula precio usando la función
    SET p_precio = fn_calcular_precio(p_id_pista, p_inicio, p_fin);

    -- Inserta reserva
INSERT INTO reservas(id_socio, id_pista, inicio, fin, precio)
VALUES (p_id_socio, p_id_pista, p_inicio, p_fin, p_precio);
END $$

DELIMITER ;

-- ---------- DATOS DE EJEMPLO ----------
INSERT INTO pistas(nombre, tipo, precio_base) VALUES
                                                  ('Pista Tenis 1', 'TENIS', 10),
                                                  ('Pista Pádel 1', 'PADEL', 8),
                                                  ('Pista Fútbol Sala', 'FUTBOL', 20);

INSERT INTO socios(nombre, email, fecha_alta) VALUES
                                                  ('Juan Pérez', 'juan@example.com', CURDATE()),
                                                  ('Ana Gómez', 'ana@example.com', CURDATE());
