# SpawnerX

**SpawnerX** is a powerful plugin for **Paper 1.20+** servers that introduces advanced spawner mechanics, mob stacking, and deep customization.

> **Main command:** `/spawnerx`

---

## Features

### 1. Spawner Collection with Silk Touch
- Players can collect spawners using a Silk Touch pickaxe.

### 2. Spawner Personalization
- Collected spawners have custom names, e.g. `Spawner [Zombie]`.
- Holograms display spawner info above the block, e.g. `x5 Spawner [Zombie]`.

### 3. Spawner Configuration Menu (Right-Click)
When right-clicking a placed spawner, a configuration menu appears with:
- **Knockback:** Toggle ON/OFF for mob knockback
- **Attack Players:** OFF = mobs are passive, ON = normal behavior
- **Allow Attack in Claims:** If ON, others can attack mobs inside your claim; if OFF, only the owner can interact
- **Save Settings** button

### 4. Mob Stacking System
- Identical mobs spawned from the same spawner are automatically stacked
- Custom name shows stack size, e.g. `x12 Zombie`
- Configurable stacking **radius**
- Configurable **maximum stack size**, e.g. spawn a new stack after 250 mobs

### 5. Configuration File (`config.yml`)
Allows full control over:
- Enabling/disabling the plugin
- Stacking radius
- Maximum mobs per stack

### 6. Commands & Permissions

| Command                                | Permission           | Description                            |
|----------------------------------------|-----------------------|----------------------------------------|
| `/spawnerx reload`                    | `spawnerx.cmd.reload`     | Reloads the config                     |
| `/spawnerx give <player> <mob> [amount]` | `spawnerx.cmd.give`    | Gives a custom spawner to a player     |

---

## Notes

- **Naturally-generated spawners** (e.g. dungeon spawners) are **not affected** by this plugin.
- All modifications apply **only to spawners placed by players** using SpawnerX.

---

## Requirements

- Java 17+
- Minecraft Server **Paper** 1.20+

---

## Roadmap

- NBT-based custom mob support
- Hooks for WorldGuard, Lands, and other protection plugins
- GUI-based spawner upgrades
- Public API for developers

---

## Contributions

Pull requests are welcome!  
Got a feature idea or bug report? Open an [issue](https://github.com/theprogmatheus/SpawnerX/issues) or start a [discussion](https://github.com/theprogmatheus/SpawnerX/discussions).

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

Built with ☕ by [@theprogmatheus](https://github.com/theprogmatheus)
