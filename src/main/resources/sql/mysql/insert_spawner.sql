INSERT INTO %table_prefix%spawner_blocks (
    world, x, y, z, chunk_x, chunk_z, config
) VALUES (?, ?, ?, ?, ?, ?, ?)
ON DUPLICATE KEY UPDATE
    config = VALUES(config);