TRUNCATE TABLE listas CASCADE;

ALTER TABLE listas 
ADD COLUMN usuario_id INTEGER NOT NULL;


ALTER TABLE listas 
ADD CONSTRAINT fk_listas_usuarios 
FOREIGN KEY (usuario_id) REFERENCES usuarios(id);