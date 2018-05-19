CREATE DATABASE IF NOT EXISTS bd_app DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE USER 'usuario'@'localhost' IDENTIFIED BY '12345';
GRANT ALL PRIVILEGES ON bd_app.* TO 'usuario'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;
USE bd_app;

CREATE TABLE IF NOT EXISTS app_usuarios (
  id int(11) NOT NULL AUTO_INCREMENT,
  rut char(12) DEFAULT NULL,
  alias char(50) DEFAULT NULL,
  email char(100) DEFAULT NULL,
  estado int(11) DEFAULT '1',
  roles char(50) DEFAULT NULL,
  grupos char(50) DEFAULT NULL,
  cargo char(50) DEFAULT NULL,
  login char(50) NOT NULL,
  clave char(100) DEFAULT NULL,
  sesiones int(11) NOT NULL DEFAULT '0',
  ultimasesion datetime DEFAULT NULL,
  temporal char(50) DEFAULT NULL,
  ultimasincro char(15) DEFAULT NULL,
  versionapp char(10) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY login (login),
  KEY alias (alias),
  KEY email (email),
  KEY estado (estado),
  KEY rut (rut)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

INSERT INTO `app_usuarios` (`id`, `rut`, `alias`, `email`, `estado`, `roles`, `grupos`, `cargo`, `login`, `clave`, `sesiones`, `ultimasesion`, `temporal`, `ultimasincro`, `versionapp`) VALUES 
(1, '', 'Administrador', 'soporte@masexperto.com', 1, '1', '0', '', 'root', '12345', 0, NULL, NULL, NULL, NULL),
(2, '', 'Carlos Barria', 'carlosbarriav@hotmail.cl', 1, '2,3', '0', '', 'cbarria', '12345', 0, NULL, NULL, NULL, NULL),
(3, '', 'Daniel Perez', 'danoperez_21@hotmail.com', 1, '2,3', '0', '', 'dperez', '12345', 0, NULL, NULL, NULL, NULL),
(4, '', 'Emilio Muñoz', 'miloprofe@gmail.com', 1, '2,3', '0', '', 'emunoz', '12345', 0, NULL, NULL, NULL, NULL),
(5, '', 'Manuel Vargas', 'manuel.vargas.t@ug.uchile.cl', 1, '2,3', '0', '', 'mvargas', '12345', 0, NULL, NULL, NULL, NULL),
(6, '', 'Patricio Plaza', 'plaza.patricio@gmail.com', 1, '2,3', '0', '', 'pplaza', '12345', 0, NULL, NULL, NULL, NULL),
(7, '', 'Paul Martinez', 'paulvenci@gmail.com', 1, '2,3', '0', '', 'pmartinez', '12345', 0, NULL, NULL, NULL, NULL),
(8, '', 'Sebastian Sitja', 'sebasitjamed@gmail.com', 1, '2,3', '0', '', 'ssitja', '12345', 0, NULL, NULL, NULL, NULL);
