INSERT INTO %table_prefix%spawner_blocks (
    world, x, y, z, chunk_x, chunk_z, config
) VALUES (?, ?, ?, ?, ?, ?, ?)
ON CONFLICT (world, x, y, z) DO UPDATE SET
    config = excluded.config;