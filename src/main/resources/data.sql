DELETE FROM equipo_usuario;
DELETE FROM tareasequipo;
DELETE FROM tareas;
DELETE FROM equipos;
DELETE FROM usuarios;

INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento,administrador,acceso, enabled) VALUES('1', 'user@ua', 'Usuario Ejemplo', '123', '2001-02-10', false, true, true);
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento,administrador,acceso, enabled) VALUES('2', 'luis@ua', 'Luis Alfonso Culiañez', 'hola', '1999-04-29', true, true, true);
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento,administrador,acceso, enabled) VALUES('3', 'raq@ua', 'Raquel', 'hola', '2000-12-02', false, false, true);
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento,administrador,acceso, enabled) VALUES('4', 'aaron@ua', 'Aaron', 'hola', '1999-02-10', false, true, true);

/* TAREAS de los usuarios */
INSERT INTO tareas (id, titulo, usuario_id) VALUES('1', 'Lavar coche', '1');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('2', 'Renovar DNI', '1');

INSERT INTO tareas (id, titulo, usuario_id) VALUES('3', 'Hacer MADS', '2');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('4', 'Hacer IW', '2');

INSERT INTO tareas (id, titulo, usuario_id) VALUES('5', 'Buscar hotel', '3');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('6', 'Editar fotos', '3');

INSERT INTO tareas (id, titulo, usuario_id) VALUES('7', 'Comprar zapatillas', '4');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('8', 'Entrenar red', '4');

/* EQUIPOS */
INSERT INTO equipos (id, nombre, descripcion) VALUES ('1', 'Ferrari', 'Equipo 1');
INSERT INTO equipos (id, nombre, descripcion) VALUES ('2', 'RedBull Racing', 'Equipo 2');
INSERT INTO equipos (id, nombre, descripcion) VALUES ('3', 'Alpine', 'Equipo 3');
INSERT INTO equipos (id, nombre, descripcion) VALUES ('4', 'Aston Martin', 'Equipo 4');

/* RELACION EQUIPOS-USUARIOS */
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES ('1', '1');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES ('2', '2');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES ('2', '3');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES ('3', '4');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES ('3', '2');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES ('4', '2');

/* DATOS TAREAS DE LOS EQUIPOS */
INSERT INTO tareasequipo (id, titulo, descripcion, fecha, estado, equipo_id)
            VALUES ('1', 'Tarea 1', 'Descripcion 1', '1999-04-29', 'POR_HACER','1');
INSERT INTO tareasequipo (id, titulo, descripcion, fecha,  estado,equipo_id)
            VALUES ('2', 'Tarea 2', 'Descripcion 2', '1999-04-29', 'EN_PROCESO','1');
INSERT INTO tareasequipo (id, titulo, descripcion, fecha,  estado, equipo_id)
            VALUES ('3', 'Tarea 3', 'Descripcion 3', '1999-04-29', 'EN_PROCESO','2');
INSERT INTO tareasequipo (id, titulo, descripcion, fecha,  estado, equipo_id)
            VALUES ('4', 'Tarea 4', 'Descripcion 4', '1999-04-29', 'FINALIZADA','3');
INSERT INTO tareasequipo (id, titulo, descripcion, fecha,  estado, equipo_id)
            VALUES ('5', 'Tarea 5', 'Descripcion 5', '1999-04-29', 'POR_HACER','4');