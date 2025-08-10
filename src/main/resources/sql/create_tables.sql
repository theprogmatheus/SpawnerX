CREATE TABLE IF NOT EXISTS %prefix%_spawner_blocks (
    world VARCHAR(32) NOT NULL,
    x INT NOT NULL,
    y SMALLINT NOT NULL,
    z INT NOT NULL,
    chunk_x INT NOT NULL,
    chunk_z INT NOT NULL,
    config VARCHAR(64) NOT NULL,
    amount INT NOT NULL DEFAULT 1,
    PRIMARY KEY (world, x, y, z),
    INDEX idx_chunk (world, chunk_x, chunk_z),
    INDEX idx_config (config, world)
);

CREATE TABLE IF NOT EXISTS %prefix%_player_profiles (
    uuid CHAR(36) NOT NULL,
    data BLOB,
    PRIMARY KEY (uuid)
);