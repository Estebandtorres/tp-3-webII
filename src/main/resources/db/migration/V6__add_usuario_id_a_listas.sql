TRUNCATE TABLE listas CASCADE;

-- 2. Agregamos la columna directamente como obligatoria
ALTER TABLE listas 
ADD COLUMN usuario_id INTEGER NOT NULL;

-- 3. Creamos la relación de clave foránea
ALTER TABLE listas 
ADD CONSTRAINT fk_listas_usuarios 
FOREIGN KEY (usuario_id) REFERENCES usuarios(id);