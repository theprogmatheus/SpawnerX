CREATE TABLE %prefix%_spawner_blocks (
    world VARCHAR(32) NOT NULL,
    x INT NOT NULL,
    y SMALLINT NOT NULL,
    z INT NOT NULL,
    chunk_x INT NOT NULL,
    chunk_z INT NOT NULL,
    config VARCHAR(64) NOT NULL,
    amount INT NOT NULL DEFAULT 1,
    PRIMARY KEY (world, x, y, z)
);

CREATE INDEX idx_chunk ON %prefix%_spawner_blocks (world, chunk_x, chunk_z);
CREATE INDEX idx_config ON %prefix%_spawner_blocks (config, world);