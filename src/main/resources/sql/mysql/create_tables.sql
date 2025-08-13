CREATE TABLE IF NOT EXISTS %table_prefix%spawner_blocks (
    world VARCHAR(32) NOT NULL,
    x INT NOT NULL,
    y SMALLINT NOT NULL,
    z INT NOT NULL,
    chunk_x INT NOT NULL,
    chunk_z INT NOT NULL,
    config VARCHAR(64) NOT NULL,
    PRIMARY KEY (world, x, y, z),
    INDEX idx_chunk (world, chunk_x, chunk_z),
    INDEX idx_config (config, world)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS %table_prefix%player_profiles (
    uuid CHAR(36) NOT NULL,
    data BLOB,
    PRIMARY KEY (uuid)
) ENGINE=InnoDB;