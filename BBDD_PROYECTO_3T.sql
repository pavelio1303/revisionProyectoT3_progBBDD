-- ELIMINA LA BASE DE DATOS AGE_OF_STEMPIRES SI YA EXISTE, PARA EVITAR CONFLICTOS.
DROP DATABASE IF EXISTS AGE_OF_STEMPIRES;

-- CREA UNA NUEVA BASE DE DATOS LLAMADA AGE_OF_STEMPIRES.
CREATE DATABASE AGE_OF_STEMPIRES;

-- SELECCIONA LA BASE DE DATOS RECIÉN CREADA PARA SU USO.
USE AGE_OF_STEMPIRES;

-- CREA LA TABLA DINASTIA PARA ALMACENAR INFORMACIÓN SOBRE LAS DINASTÍAS.
CREATE TABLE DINASTIA (
    ID_DINASTIA INT AUTO_INCREMENT PRIMARY KEY, -- IDENTIFICADOR ÚNICO DE LA DINASTÍA.
    NOMBRE_DINASTIA VARCHAR(255) UNIQUE NOT NULL -- NOMBRE ÚNICO DE LA DINASTÍA.
);

-- CREA LA TABLA DINASTIA_TIENE_NOMBRE PARA ALMACENAR LOS NOMBRES DE LAS DINASTÍAS.
CREATE TABLE DINASTIA_TIENE_NOMBRE (
    NOMBRE_DINASTIA VARCHAR(255) NOT NULL, -- NOMBRE DE LA DINASTÍA.
    NOMBRE_PROPIO VARCHAR(255) NOT NULL, -- NOMBRE PROPIO ASOCIADO A LA DINASTÍA.
    
    PRIMARY KEY(NOMBRE_DINASTIA, NOMBRE_PROPIO), -- CLAVE PRIMARIA COMPUESTA.
    FOREIGN KEY (NOMBRE_DINASTIA) REFERENCES DINASTIA(NOMBRE_DINASTIA) -- CLAVE FORÁNEA HACIA LA TABLA DINASTIA.
);

-- CREA LA TABLA MONARCA PARA ALMACENAR INFORMACIÓN SOBRE LOS MONARCAS.
CREATE TABLE MONARCA (
    ID_MONARCA INT AUTO_INCREMENT PRIMARY KEY,
    NOMBRE_DINASTIA VARCHAR(255),
    NOMBRE VARCHAR(255) NOT NULL,
    ORDINAL_NOMBRE VARCHAR(10) NOT NULL,
    ESCUDO VARCHAR(255) NOT NULL,
    DESCRIPCION TEXT NOT NULL,
    NIVEL_ESTRATEGIA TINYINT NOT NULL DEFAULT 1,
    NIVEL_DIPLOMACIA TINYINT NOT NULL DEFAULT 1,
    NIVEL_EXPERIENCIA TINYINT NOT NULL,
    VIVO BOOLEAN NOT NULL DEFAULT TRUE,
    
    UNIQUE (NOMBRE_DINASTIA, NOMBRE, ORDINAL_NOMBRE),
    FOREIGN KEY (NOMBRE_DINASTIA, NOMBRE) REFERENCES DINASTIA_TIENE_NOMBRE(NOMBRE_DINASTIA, NOMBRE_PROPIO),
    FOREIGN KEY (NOMBRE_DINASTIA) REFERENCES DINASTIA(NOMBRE_DINASTIA),
    CONSTRAINT CK_NIVEL_ESTRATEGIA_ENTRE_1_10 CHECK (NIVEL_ESTRATEGIA BETWEEN 1 AND 10), -- Verifica que el nivel de estrategia esté entre 1 y 10.
    CONSTRAINT CK_NIVEL_DIPLOMACIA_ENTRE_1_10 CHECK (NIVEL_DIPLOMACIA BETWEEN 1 AND 10), -- Verifica que el nivel de diplomacia esté entre 1 y 10.
    CONSTRAINT CK_NIVEL_EXPERIENCIA_MAYOR_QUE_14 CHECK (NIVEL_EXPERIENCIA >= 14) -- Verifica que el nivel de experiencia sea al menos 14.
); 

-- CREA LA TABLA REINO PARA ALMACENAR INFORMACIÓN SOBRE LOS REINOS.
CREATE TABLE REINO (
    ID_REINO INT AUTO_INCREMENT PRIMARY KEY,
    NOMBRE VARCHAR(20) NOT NULL,
    DESCRIPCION TEXT,
    ANO_FUNDACION INT NOT NULL,
    CAPITAL VARCHAR(20) NOT NULL,
    ID_MONARCA INT NOT NULL,
    SUPERFICIE DECIMAL(10,2) NOT NULL,
    POBLACION INT NOT NULL,
    BANDERA_URL VARCHAR(255),
    PIEDRA INT,
    MADERA INT,
    ORO INT,
    FOREIGN KEY (ID_MONARCA) REFERENCES MONARCA(ID_MONARCA),
    CONSTRAINT CK_ANO_FUNDACION_ENTRE_400_1400 CHECK (ANO_FUNDACION BETWEEN 400 AND 1400), -- Verifica que el año de fundación esté entre 400 y 1400.
    CONSTRAINT CK_SUPERFICIE_MAYOR_QUE_0 CHECK (SUPERFICIE > 0), -- Verifica que la superficie sea mayor que 0.
    CONSTRAINT CK_POBLACION_MAYOR_QUE_1 CHECK (POBLACION >= 1) -- Verifica que la población sea al menos 1.
);

-- CREA LA TABLA MONARCA_REINA_REINO PARA ESTABLECER LA RELACIÓN ENTRE MONARCAS, REINAS Y REINOS.
CREATE TABLE MONARCA_REINA_REINO (
    ID INT AUTO_INCREMENT PRIMARY KEY, -- IDENTIFICADOR ÚNICO DE LA RELACIÓN MONARCA-REINO.
    ID_MONARCA INT NOT NULL, -- IDENTIFICADOR DEL MONARCA.
    ID_REINO INT NOT NULL, -- IDENTIFICADOR DEL REINO.
    FOREIGN KEY (ID_MONARCA) REFERENCES MONARCA(ID_MONARCA), -- CLAVE FORÁNEA HACIA LA TABLA MONARCA.
    FOREIGN KEY (ID_REINO) REFERENCES REINO(ID_REINO) ON DELETE CASCADE -- CLAVE FORÁNEA HACIA LA TABLA REINO, CON ELIMINACIÓN EN CASCADA.
);
-- CAMBIA EL DELIMITADOR PARA PERMITIR LA CREACIÓN DE TRIGGERS.
DELIMITER //

-- CREA UN TRIGGER PARA REGISTRAR CAMBIOS EN EL MONARCA DE UN REINO EN LA TABLA MONARCA_REINA_REINO.
CREATE TRIGGER MONARCA_CAMBIO AFTER UPDATE ON REINO
FOR EACH ROW
BEGIN
    IF OLD.ID_MONARCA != NEW.ID_MONARCA THEN -- COMPRUEBA SI EL MONARCA HA CAMBIADO.
        INSERT INTO MONARCA_REINA_REINO (ID_MONARCA, ID_REINO) VALUES (OLD.ID_MONARCA, NEW.ID_REINO); -- INSERTA UNA NUEVA FILA EN LA TABLA MONARCA_REINA_REINO CON EL NUEVO MONARCA Y EL REINO ACTUALIZADO.
    END IF;
END;
//
-- RESTAURA EL DELIMITADOR PREDETERMINADO.
DELIMITER ;

-- CAMBIA EL DELIMITADOR PARA PERMITIR LA CREACIÓN DE TRIGGERS.
DELIMITER //
-- ESTE TRIGGER COMPRUEBA SI LA POBLACIÓN DE UN NUEVO REINO ESTÁ DENTRO DEL RANGO DESEADO ANTES DE LA INSERCIÓN EN LA TABLA REINO.
CREATE TRIGGER POBLACION_CHECK BEFORE INSERT ON REINO
FOR EACH ROW
BEGIN
    DECLARE POBLACION_MAXIMA INT;
    SET POBLACION_MAXIMA = NEW.SUPERFICIE * 10;
    IF NEW.POBLACION < 1 OR NEW.POBLACION > POBLACION_MAXIMA THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'LA POBLACIÓN DEBE ESTAR ENTRE 1 Y 10 VECES LA SUPERFICIE DEL REINO.';
    END IF;
END;
//
-- RESTAURA EL DELIMITADOR PREDETERMINADO.
DELIMITER ;

INSERT INTO DINASTIA (NOMBRE_DINASTIA) VALUES
('ALFONSO'),
('ANJOU'),
('ARAGÓN'),
('BORGOÑA'),
('BRAGANZA'),
('CASTILLA'),
('HABSBURGO'),
('LANCASTER'),
('NORMANDÍA'),
('TUDOR'),
('VALOIS'),
('WITTELSBACH'),
('YORK');

-- NOMBRES PARA LA DINASTÍA DE ALFONSO
INSERT INTO DINASTIA_TIENE_NOMBRE (NOMBRE_DINASTIA, NOMBRE_PROPIO) VALUES
('ALFONSO', 'ALFONSO'),
('ALFONSO', 'LEONOR'),
('ALFONSO', 'PEDRO'),
('ALFONSO', 'ISABEL'),
('ALFONSO', 'JOÃO'),
('ALFONSO', 'BEATRIZ'),
('ALFONSO', 'CATARINA'),
('ALFONSO', 'MANUEL'),
('ALFONSO', 'ANTONIA'),
('ALFONSO', 'MARIA');

-- NOMBRES PARA LA DINASTÍA DE ANJOU
INSERT INTO DINASTIA_TIENE_NOMBRE (NOMBRE_DINASTIA, NOMBRE_PROPIO) VALUES
('ANJOU', 'LOUIS'),
('ANJOU', 'MARGUERITE'),
('ANJOU', 'CHARLES'),
('ANJOU', 'CATHERINE'),
('ANJOU', 'PHILIPPE'),
('ANJOU', 'MARIE'),
('ANJOU', 'RENÉ'),
('ANJOU', 'ISABELLE'),
('ANJOU', 'HENRI'),
('ANJOU', 'JEANNE');

-- NOMBRES PARA LA DINASTÍA DE ARAGÓN
INSERT INTO DINASTIA_TIENE_NOMBRE (NOMBRE_DINASTIA, NOMBRE_PROPIO) VALUES
('ARAGÓN', 'PEDRO'),
('ARAGÓN', 'ISABEL'),
('ARAGÓN', 'JAIME'),
('ARAGÓN', 'LEONOR'),
('ARAGÓN', 'JUAN'),
('ARAGÓN', 'MARIA'),
('ARAGÓN', 'ALFONSO'),
('ARAGÓN', 'BEATRIZ'),
('ARAGÓN', 'MARTIN'),
('ARAGÓN', 'JUANA');

-- NOMBRES PARA LA DINASTÍA DE BORGOÑA
INSERT INTO DINASTIA_TIENE_NOMBRE (NOMBRE_DINASTIA, NOMBRE_PROPIO) VALUES
('BORGOÑA', 'PHILIPPE'),
('BORGOÑA', 'MARGUERITE'),
('BORGOÑA', 'CHARLES'),
('BORGOÑA', 'CATHERINE'),
('BORGOÑA', 'LOUIS'),
('BORGOÑA', 'ISABELLE'),
('BORGOÑA', 'RENÉ'),
('BORGOÑA', 'MARIE'),
('BORGOÑA', 'HENRI'),
('BORGOÑA', 'JEANNE');

-- NOMBRES PARA LA DINASTÍA DE BRAGANZA
INSERT INTO DINASTIA_TIENE_NOMBRE (NOMBRE_DINASTIA, NOMBRE_PROPIO) VALUES
('BRAGANZA', 'JOÃO'),
('BRAGANZA', 'MARIA'),
('BRAGANZA', 'PEDRO'),
('BRAGANZA', 'ISABEL'),
('BRAGANZA', 'ALFONSO'),
('BRAGANZA', 'BEATRIZ'),
('BRAGANZA', 'CATARINA'),
('BRAGANZA', 'MANUEL'),
('BRAGANZA', 'ANTONIA'),
('BRAGANZA', 'LEONOR');

-- NOMBRES PARA LA DINASTÍA DE CASTILLA
INSERT INTO DINASTIA_TIENE_NOMBRE (NOMBRE_DINASTIA, NOMBRE_PROPIO) VALUES
('CASTILLA', 'FERNANDO'),
('CASTILLA', 'ISABEL'),
('CASTILLA', 'DIEGO'),
('CASTILLA', 'JUANA'),
('CASTILLA', 'GARCÍA'),
('CASTILLA', 'BEATRIZ'),
('CASTILLA', 'JUAN'),
('CASTILLA', 'TERESA'),
('CASTILLA', 'RODRIGO'),
('CASTILLA', 'LEONOR');

-- NOMBRES PARA LA DINASTÍA DE HABSBURGO
INSERT INTO DINASTIA_TIENE_NOMBRE (NOMBRE_DINASTIA, NOMBRE_PROPIO) VALUES
('HABSBURGO', 'MAXIMILIAN'),
('HABSBURGO', 'MARIA'),
('HABSBURGO', 'CHARLES'),
('HABSBURGO', 'ISABELLA'),
('HABSBURGO', 'PHILIP'),
('HABSBURGO', 'MARGARET'),
('HABSBURGO', 'FERDINAND'),
('HABSBURGO', 'JOANNA'),
('HABSBURGO', 'ALBERT'),
('HABSBURGO', 'CATHERINE');

-- NOMBRES PARA LA DINASTÍA DE LANCASTER
INSERT INTO DINASTIA_TIENE_NOMBRE (NOMBRE_DINASTIA, NOMBRE_PROPIO) VALUES
('LANCASTER', 'HENRY'),
('LANCASTER', 'MARGARET'),
('LANCASTER', 'EDWARD'),
('LANCASTER', 'ANNE'),
('LANCASTER', 'RICHARD'),
('LANCASTER', 'ELIZABETH'),
('LANCASTER', 'JOHN'),
('LANCASTER', 'CATHERINE'),
('LANCASTER', 'THOMAS'),
('LANCASTER', 'MARY');

-- NOMBRES PARA LA DINASTÍA DE NORMANDÍA
INSERT INTO DINASTIA_TIENE_NOMBRE (NOMBRE_DINASTIA, NOMBRE_PROPIO) VALUES
('NORMANDÍA', 'WILLIAM'),
('NORMANDÍA', 'MATILDA'),
('NORMANDÍA', 'ROBERT'),
('NORMANDÍA', 'ADELA'),
('NORMANDÍA', 'RICHARD'),
('NORMANDÍA', 'EMMA'),
('NORMANDÍA', 'HENRY'),
('NORMANDÍA', 'CONSTANCE'),
('NORMANDÍA', 'GEOFFREY'),
('NORMANDÍA', 'JOAN');

-- NOMBRES PARA LA DINASTÍA DE TUDOR
INSERT INTO DINASTIA_TIENE_NOMBRE (NOMBRE_DINASTIA, NOMBRE_PROPIO) VALUES
('TUDOR', 'HENRY'),
('TUDOR', 'ELIZABETH'),
('TUDOR', 'EDWARD'),
('TUDOR', 'MARY'),
('TUDOR', 'CATHERINE'),
('TUDOR', 'ANNE'),
('TUDOR', 'ARTHUR'),
('TUDOR', 'MARGARET'),
('TUDOR', 'JANE'),
('TUDOR', 'THOMAS');

-- NOMBRES PARA LA DINASTÍA DE VALOIS
INSERT INTO DINASTIA_TIENE_NOMBRE (NOMBRE_DINASTIA, NOMBRE_PROPIO) VALUES
('VALOIS', 'LOUIS'),
('VALOIS', 'CATHERINE'),
('VALOIS', 'CHARLES'),
('VALOIS', 'MARIE'),
('VALOIS', 'PHILIPPE'),
('VALOIS', 'ISABELLE'),
('VALOIS', 'HENRI'),
('VALOIS', 'JEANNE'),
('VALOIS', 'MARGUERITE'),
('VALOIS', 'RENÉ');

-- NOMBRES PARA LA DINASTÍA DE WITTELSBACH
INSERT INTO DINASTIA_TIENE_NOMBRE (NOMBRE_DINASTIA, NOMBRE_PROPIO) VALUES
('WITTELSBACH', 'LOUIS'),
('WITTELSBACH', 'ISABELLA'),
('WITTELSBACH', 'OTTO'),
('WITTELSBACH', 'ELISABETH'),
('WITTELSBACH', 'RUDOLF'),
('WITTELSBACH', 'MARIA'),
('WITTELSBACH', 'ALBERT'),
('WITTELSBACH', 'MARGARET'),
('WITTELSBACH', 'WILLIAM'),
('WITTELSBACH', 'AGNES');

-- NOMBRES PARA LA DINASTÍA DE YORK
INSERT INTO DINASTIA_TIENE_NOMBRE (NOMBRE_DINASTIA, NOMBRE_PROPIO) VALUES
('YORK', 'EDWARD'),
('YORK', 'MARGARET'),
('YORK', 'RICHARD'),
('YORK', 'ANNE'),
('YORK', 'JOHN'),
('YORK', 'ELIZABETH'),
('YORK', 'THOMAS'),
('YORK', 'CATHERINE'),
('YORK', 'HENRY'),
('YORK', 'MARY');

INSERT INTO MONARCA (NOMBRE_DINASTIA, NOMBRE, ORDINAL_NOMBRE, ESCUDO, DESCRIPCION, NIVEL_ESTRATEGIA, NIVEL_DIPLOMACIA, NIVEL_EXPERIENCIA, VIVO)
VALUES
('ALFONSO', 'ALFONSO', 'I', 'https://upload.wikimedia.org/wikipedia/commons/9/98/Escudo_real_madrid_1941.png', 'Alfonso es un monarca joven y enérgico, de cabello oscuro y ojos penetrantes. Su estatura imponente y su presencia carismática lo hacen destacar en cualquier multitud. Además de sus habilidades militares, es conocido por su inteligencia estratégica y su capacidad para tomar decisiones rápidas en tiempos de crisis.', 5, 5, 25, TRUE),
('ANJOU', 'CATHERINE', 'I', 'https://upload.wikimedia.org/wikipedia/commons/f/f0/Escudo_del_reino_de_Navarra.png', 'Catherine es una líder carismática con cabello rubio y ojos azules, cuya elegancia y gracia son la envidia de la corte. Su tez pálida y sus rasgos delicados esconden una voluntad férrea y una inteligencia aguda. Si bien su apariencia puede engañar a algunos, aquellos que la subestiman lo hacen bajo su propio riesgo.', 4, 6, 28, TRUE),
('ARAGÓN', 'JUAN', 'I','https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Escudo_del_reino_de_Granada.png/800px-Escudo_del_reino_de_Granada.png', 'Juan es un monarca de complexión atlética y porte regio, con cabello castaño y barba recortada. Su mirada franca y su sonrisa amable ocultan un temperamento apasionado y una determinación inquebrantable. Es conocido por su destreza en la equitación y su amor por los torneos y justas.', 3, 7, 20, TRUE),
('BORGOÑA', 'CHARLES', 'I', 'https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Escudo_del_reino_de_Aragon.png/437px-Escudo_del_reino_de_Aragon.png', 'Charles es un monarca de edad madura, con cabello gris plateado y una expresión seria. Su figura imponente y su mirada penetrante inspiran respeto y lealtad entre sus súbditos. Aunque no es dado a las frivolidades de la corte, su presencia tranquila y su firmeza de carácter son su mayor fortaleza.', 2, 8, 30, TRUE),
('BRAGANZA', 'MANUEL', 'I', 'https://upload.wikimedia.org/wikipedia/commons/7/7c/Escudo_del_reino_de_Toledo.png', 'Manuel es un monarca de aspecto refinado y elegante, con cabello oscuro y ojos avellana. Su porte distinguido y su manera educada ocultan un espíritu aventurero y una curiosidad insaciable por el mundo que lo rodea. Es conocido por su amor por el arte y la música, así como por su generosidad hacia los artistas y eruditos de su reino.', 1, 9, 18, TRUE),
('CASTILLA', 'ISABEL', 'I', 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Escudo_de_Arag%C3%B3n.svg/450px-Escudo_de_Arag%C3%B3n.svg.png', 'Isabel es una monarca de belleza serena y majestuosa, con cabello castaño y ojos color miel. Su presencia imponente y su mirada penetrante reflejan su determinación y su voluntad inquebrantable. Es conocida por su habilidad para tomar decisiones difíciles y su dedicación al bienestar de su reino.', 5, 5, 32, TRUE),
('HABSBURGO', 'PHILIP', 'I', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/Coat_of_Arms_of_the_Realm_of_Jaen.svg/427px-Coat_of_Arms_of_the_Realm_of_Jaen.svg.png', 'Philip es un monarca de porte regio y gesto serio, con cabello rubio y ojos azules penetrantes. Su presencia imponente y su voz resonante inspiran respeto y obediencia entre sus súbditos. Aunque es conocido por su diplomacia y su habilidad para mantener la paz, también es un líder decidido que no duda en tomar medidas firmes cuando es necesario.', 4, 6, 22, TRUE),
('LANCASTER', 'HENRY', 'I', 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/UK_Royal_Coat_of_Arms.svg/602px-UK_Royal_Coat_of_Arms.svg.png', 'Henry es un monarca de apariencia noble y serena, con cabello castaño y ojos verdes profundos. Su sonrisa amable y su manera afable lo hacen querido entre sus súbditos, mientras que su determinación y su coraje lo convierten en un líder respetado y admirado. Es conocido por su generosidad hacia los necesitados y su compromiso con la justicia y la equidad.', 3, 7, 29, TRUE),
('NORMANDÍA', 'ROBERT', 'I', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/96/Escudo_de_Asturias.svg/338px-Escudo_de_Asturias.svg.png', 'Robert es un monarca de aspecto fiero y decidido, con cabello oscuro y ojos grises fríos. Su expresión seria y su mirada penetrante reflejan su naturaleza indomable y su espíritu guerrero. Es conocido por su valentía en el campo de batalla y su habilidad para liderar a sus tropas hacia la victoria, así como por su lealtad hacia su pueblo y su tierra.', 2, 8, 15, TRUE),
('TUDOR', 'MARY', 'I', 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/Escudo_de_Pamplona.svg/800px-Escudo_de_Pamplona.svg.png', 'Mary es una monarca de belleza radiante y porte regio, con cabello dorado y ojos azules profundos. Su elegancia natural y su gracia en la corte ocultan una determinación férrea y una voluntad de hierro. Es conocida por su devoción a su fe y su firmeza en la defensa de sus creencias, así como por su compasión hacia los menos afortunados y su deseo de justicia para todos sus súbditos.', 1, 9, 34, TRUE);

INSERT INTO REINO (NOMBRE, DESCRIPCION, ANO_FUNDACION, CAPITAL, ID_MONARCA, SUPERFICIE, POBLACION, BANDERA_URL, PIEDRA, MADERA, ORO)
VALUES
('REINO DE ANJOU', 'Ubicado en el corazón de Francia, el Reino de Anjou se extiende a lo largo de fértiles valles fluviales y colinas ondulantes. Sus ciudades están adornadas con arquitectura medieval, mientras que sus campos están salpicados de viñedos que producen algunos de los vinos más apreciados del país. La capital, Nantes, es un centro bullicioso de comercio y cultura, donde los mercados bulliciosos y los festivales animados dan vida a las calles empedradas. Bajo el reinado de la dinastía de Anjou, el reino ha florecido, convirtiéndose en un bastión de estabilidad y prosperidad en la región.', 1000, 'NANTES', 2, 8000.00, 8765, 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Flag_of_Anjou.svg/1280px-Flag_of_Anjou.svg.png', 2000, 1500, 2200),
('REINO DE ARAGÓN', 'Situado en el noreste de la península ibérica, el Reino de Aragón es un crisol de culturas y tradiciones. Sus ciudades están marcadas por la arquitectura mudéjar, que refleja la influencia islámica en la región, mientras que sus paisajes varían desde las áridas tierras del interior hasta las exuberantes montañas del Pirineo. Zaragoza, la capital, es un centro cosmopolita de comercio y aprendizaje, donde las ideas y las mercancías fluyen libremente. Bajo el liderazgo de la dinastía aragonesa, el reino ha prosperado, convirtiéndose en un faro de conocimiento y progreso en la península.', 950, 'ZARAGOZA', 3, 7505.00, 5648, 'https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/Bandera_de_Arag%C3%B3n.svg/1200px-Bandera_de_Arag%C3%B3n.svg.png', 1800, 2000, 2400),
('REINO DE BORGOÑA', 'Enclavado en el corazón de Europa, el Reino de Borgoña es una tierra de belleza serena y opulencia refinada. Sus campos están salpicados de viñedos y bosques frondosos, mientras que sus ciudades están adornadas con magníficas catedrales y palacios renacentistas. Dijon, la capital, es un tesoro de arte y cultura, donde los artistas y eruditos de todo el mundo se reúnen para intercambiar ideas y inspiración. Bajo el dominio de la dinastía de Borgoña, el reino ha florecido, convirtiéndose en un faro de civilización y sofisticación en el continente.', 1050, 'DIJON', 4, 9007.50, 10320, 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/cd/Bandera_cruz_de_Borgo%C3%B1a_1.svg/1200px-Bandera_cruz_de_Borgo%C3%B1a_1.svg.png', 2500, 2200, 3000),
('REINO DE BRAGANZA', 'Escondido en los rincones más remotos de la península ibérica, el Reino de Braganza es una tierra de misterio y maravilla. Sus paisajes son salvajes y escarpados, con bosques densos y ríos tumultuosos que serpentean entre las montañas. La capital, Braganza, se encuentra en lo alto de una colina, protegida por imponentes murallas y torres de vigilancia. Es un lugar de leyendas y cuentos de hadas, donde la magia y la realidad se entrelazan en cada rincón. Bajo el reinado de la dinastía de Braganza, el reino ha prosperado, manteniendo su independencia y su identidad única en medio de un mundo en constante cambio.', 1100, 'BRAGANZA', 5, 7002.50, 7550, 'https://upload.wikimedia.org/wikipedia/commons/3/3a/Braganza_%28Portugal%29.png', 1500, 1800, 2800),
('REINO DE CASTILLA', 'Dominando la vasta meseta central de la península ibérica, el Reino de Castilla es una tierra de fortaleza y grandeza. Sus ciudades están protegidas por imponentes castillos y murallas, mientras que sus campos son fértiles y prósperos, alimentando a una población vibrante y diversa. Toledo, la capital, es un símbolo de la riqueza cultural y la historia del reino, con sus calles estrechas y sus edificios históricos que cuentan la historia de siglos de gloria y sacrificio. Bajo el liderazgo de la dinastía castellana, el reino ha crecido en poder y prestigio, convirtiéndose en una fuerza dominante en la región.', 1000, 'TOLEDO', 6, 10007.50, 11000, 'https://upload.wikimedia.org/wikipedia/commons/1/10/Bandera_de_Castilla_-_Actual.png', 3000, 2500, 4000);

UPDATE REINO SET ID_MONARCA = 7 WHERE ID_REINO = 1;
UPDATE REINO SET ID_MONARCA = 8 WHERE ID_REINO = 1;
UPDATE REINO SET ID_MONARCA = 9 WHERE ID_REINO = 1;
