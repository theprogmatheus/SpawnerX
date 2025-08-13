CREATE TABLE IF NOT EXISTS %table_prefix%spawner_blocks (
    world TEXT NOT NULL,
    x INTEGER NOT NULL,
    y INTEGER NOT NULL,
    z INTEGER NOT NULL,
    chunk_x INTEGER NOT NULL,
    chunk_z INTEGER NOT NULL,
    config TEXT NOT NULL,
    PRIMARY KEY (world, x, y, z)
);

CREATE INDEX IF NOT EXISTS idx_chunk ON %table_prefix%spawner_blocks (world, chunk_x, chunk_z);
CREATE INDEX IF NOT EXISTS idx_config ON %table_prefix%spawner_blocks (config, world);

CREATE TABLE IF NOT EXISTS %table_prefix%player_profiles (
    uuid TEXT NOT NULL,
    data BLOB,
    PRIMARY KEY (uuid)
);