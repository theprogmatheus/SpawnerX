SELECT world, x, y, z, chunk_x, chunk_z, config, amount
FROM %prefix%_spawner_blocks
WHERE world = ?
AND x = ?
AND y = ?
AND z = ?