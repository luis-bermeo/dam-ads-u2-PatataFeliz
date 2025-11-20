-- ========================================
-- BASE DE DATOS DAMA SPORTS - FINAL
-- Autores: Luis y Javi
-- ========================================

DROP DATABASE IF EXISTS dama;
CREATE DATABASE dama;
USE dama;

-- ---------- TABLA SOCIOS ----------
CREATE TABLE socios (
                        id_socio VARCHAR(50) PRIMARY KEY,
                        dni VARCHAR(20) NOT NULL UNIQUE,
                        nombre VARCHAR(100) NOT NULL,
                        apellidos VARCHAR(100),
                        telefono VARCHAR(20),
                        email VARCHAR(150) NOT NULL UNIQUE
);

-- ---------- TABLA PISTAS ----------
CREATE TABLE pistas (
                        id_pista VARCHAR(50) PRIMARY KEY,
                        deporte VARCHAR(50) NOT NULL,
                        descripcion VARCHAR(200),
                        disponible BOOLEAN DEFAULT TRUE
);

-- ---------- TABLA RESERVAS ----------
CREATE TABLE reservas (
                          id_reserva VARCHAR(50) PRIMARY KEY,
                          id_socio VARCHAR(50) NOT NULL,
                          id_pista VARCHAR(50) NOT NULL,
                          fecha DATE NOT NULL,
                          hora_inicio TIME NOT NULL,
                          duracion_min INT NOT NULL,
                          precio DECIMAL(10,2) NOT NULL,
                          CONSTRAINT fk_reserva_socio FOREIGN KEY (id_socio) REFERENCES socios(id_socio),
                          CONSTRAINT fk_reserva_pista FOREIGN KEY (id_pista) REFERENCES pistas(id_pista)
);

-- ---------- FUNCIÓN PARA CALCULAR PRECIO ----------
DELIMITER $$

CREATE FUNCTION fn_calcular_precio(
    p_id_pista VARCHAR(50),
    p_duracion_min INT
)
    RETURNS DECIMAL(10,2)
    DETERMINISTIC
BEGIN
    DECLARE v_precio_base DECIMAL(10,2);
    DECLARE v_horas DECIMAL(10,2);

    SELECT 10 INTO v_precio_base -- Puedes ajustar según tu lógica o tabla
    FROM pistas
    WHERE id_pista = p_id_pista;

    SET v_horas = p_duracion_min / 60;
    IF v_horas < 1 THEN
        SET v_horas = 1;
    END IF;

    RETURN v_horas * v_precio_base;
END $$

DELIMITER ;

-- ---------- PROCEDIMIENTO PARA CREAR RESERVA ----------
DELIMITER $$

CREATE PROCEDURE sp_crear_reserva(
    IN p_id_reserva VARCHAR(50),
    IN p_id_pista VARCHAR(50),
    IN p_id_socio VARCHAR(50),
    IN p_fecha DATE,
    IN p_hora_inicio TIME,
    IN p_duracion_min INT,
    OUT p_precio DECIMAL(10,2)
)
BEGIN
    SET p_precio = fn_calcular_precio(p_id_pista, p_duracion_min);

    INSERT INTO reservas(
        id_reserva, id_socio, id_pista, fecha, hora_inicio, duracion_min, precio
    ) VALUES (p_id_reserva, p_id_socio, p_id_pista, p_fecha, p_hora_inicio, p_duracion_min, p_precio);
END $$

DELIMITER ;

-- ---------- DATOS DE EJEMPLO ----------
INSERT INTO socios(id_socio,dni,nombre,apellidos,telefono,email) VALUES
                                                                     ('S001','12345678A','Juan','Pérez','600123456','juan@example.com'),
                                                                     ('S002','87654321B','Ana','Gómez','600654321','ana@example.com');

INSERT INTO pistas(id_pista,deporte,descripcion,disponible) VALUES
                                                                ('P001','TENIS','Pista de tenis exterior',TRUE),
                                                                ('P002','PADEL','Pista de pádel cubierta',TRUE),
                                                                ('P003','FUTBOL','Pista de fútbol sala interior',TRUE);
