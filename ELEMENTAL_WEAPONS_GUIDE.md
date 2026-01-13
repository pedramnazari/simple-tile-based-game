# Elemental Weapons Guide

## Overview

This guide explains how to use the new elemental weapons: **Ice Wand** and **Lightning Rod**. These magical ranged weapons offer unique tactical advantages beyond the classic Fire Staff.

## The Elemental Arena Map

A special map called **"Elemental Arena"** has been created to showcase these new weapons. To play this map:

1. Run the game: `./gradlew run` (Linux/macOS) or `.\gradlew run` (Windows)
2. Select **"Elemental Arena"** from the map selection menu
3. The map is specifically designed with enemy formations that demonstrate the unique effects of each weapon

## New Weapons

### Ice Wand 🧊

**Location in Elemental Arena:** Upper left area (row 2, column 2)

**Stats:**
- Damage: 12
- Range: 5 tiles
- Effect: **Freeze**

**How the Freeze Effect Works:**
- When an ice projectile hits an enemy, that enemy becomes **frozen**
- Frozen enemies **skip their next turn** completely (no movement, no attacks)
- After skipping one turn, the enemy returns to normal behavior
- The freeze effect is visible through the **blue glowing projectile trail** and **frost burst impact**

**Tactical Use:**
- Use against aggressive enemies that move toward you (e.g., "Follow Hero" type enemies)
- Freeze dangerous enemies to create safe passage
- Control enemy movement in tight corridors
- Buy time to reposition or heal

**Visual Effects:**
- Projectile: Blue glow with cyan frost particle trail
- Impact: Expanding frost burst that fades out

### Lightning Rod ⚡

**Location in Elemental Arena:** Upper right area (row 2, column 15)

**Stats:**
- Damage: 10 (to primary target and chain target)
- Range: 5 tiles  
- Effect: **Chain Lightning**

**How Chain Lightning Works:**
- The primary target takes full damage (10 HP)
- Lightning **automatically chains** to **one adjacent enemy**
- Adjacent means enemies in the 4-neighborhood: up, down, left, or right (NOT diagonal)
- If multiple enemies are adjacent, the chain target is selected **deterministically**:
  - **Priority 1:** Topmost enemy (lowest Y coordinate)
  - **Priority 2:** If same row, leftmost enemy (lowest X coordinate)
- The chained enemy also takes full damage (10 HP)
- If no adjacent enemies exist, no chain occurs

**Tactical Use:**
- Most effective against **clustered enemies** (see row 9 in Elemental Arena)
- Position yourself so enemies align in cardinal directions
- Double the damage output compared to single-target weapons
- Break through enemy groups efficiently

**Visual Effects:**
- Projectile: Yellow flickering electric effect with pulsing sparks
- Impact: Quick yellow spark flash
- Chain: Animated yellow lightning arc connecting the two targets (flickers 3 times)

### Fire Staff 🔥 (For Comparison)

**Location in Elemental Arena:** Center area (row 7, column 8)

**Stats:**
- Damage: 15
- Range: 5 tiles
- Effect: None (pure damage)

**Visual Effects:**
- Projectile: Orange/red glow
- Impact: Standard impact animation

## Enemy Formations in Elemental Arena

The map includes strategic enemy placements to help you test each weapon:

- **Row 4:** Two horizontal-moving enemies (ELR) - Perfect for testing Ice Wand freeze
- **Row 7:** Two vertical-moving enemies (ETD) on the sides
- **Row 9:** Cluster of four 2D-moving enemies (E2D) - Ideal for Lightning Rod chain testing
- **Row 12:** Two follow-hero enemies (EFH) - Aggressive targets for freeze testing

## Combat Tips

### Using Ice Wand Effectively:
1. Identify the most dangerous enemy (usually "Follow Hero" types)
2. Freeze them to prevent attacks
3. Deal with other enemies while the frozen one is disabled
4. Remember: freeze only lasts one turn, so plan accordingly

### Using Lightning Rod Effectively:
1. Look for enemies standing adjacent to each other
2. Target the one that will chain to the most dangerous enemy
3. Watch the lightning arc to see which enemy gets chained
4. Use against enemy clusters for maximum efficiency (2x damage output)

### General Strategy:
- Collect **health potions** (marked HP on the map) when health is low
- All three ranged weapons share the same 5-tile range
- Experiment with different weapons against different enemy types
- The exit is located in the lower right (row 13, column 16)

## Controls

- **Arrow Keys / WASD:** Move hero
- **Spacebar:** Attack with equipped weapon
- **E:** Pick up items / equip weapons
- **ESC:** Pause menu

## Testing the Effects

### To Test Freeze:
1. Equip the Ice Wand
2. Stand at a safe distance from a "Follow Hero" enemy
3. Shoot the ice projectile at the enemy
4. Observe the blue frost effect on hit
5. Watch the enemy skip its next turn (it won't move)
6. On the turn after that, the enemy will move normally again

### To Test Chain Lightning:
1. Equip the Lightning Rod
2. Position yourself so at least two enemies are adjacent (horizontally or vertically aligned)
3. Shoot at one enemy
4. Watch for the yellow lightning arc connecting to the adjacent enemy
5. Both enemies should take damage

### To Compare All Three:
Use the Elemental Arena map which provides all three weapons. Try each against the same enemy types and compare:
- Fire Staff: Highest single-target damage (15)
- Ice Wand: Medium damage (12) but provides crowd control
- Lightning Rod: Lower damage (10) but hits two enemies for 20 total damage

## Advanced Techniques

### Freeze Chaining:
Against multiple enemies, freeze one, deal with others, then freeze the next threat.

### Strategic Positioning for Lightning:
- Enemies naturally move in patterns
- Position yourself so their paths bring them adjacent to each other
- Time your shots when multiple enemies align

### Weapon Switching:
- Use Ice Wand for crowd control
- Switch to Lightning Rod for clustered enemies
- Use Fire Staff for maximum single-target damage

## Troubleshooting

**Q: The chain lightning didn't chain to an enemy I expected?**  
A: Chain lightning only works on the 4-neighborhood (up/down/left/right), not diagonals. Also, it follows deterministic priority (top-left first).

**Q: The freeze effect didn't seem to work?**  
A: Make sure you're observing the enemy's next turn. The enemy will move normally on turns after the freeze expires.

**Q: Where can I find these weapons in other maps?**  
A: The Ice Wand and Lightning Rod are currently featured in the Elemental Arena map. Future map updates may include them in other locations.

## Visual Effect Details

All projectile effects are rendered using modern JavaFX effects:
- **Glow effects** for magical appearance
- **Particle trails** for movement visualization  
- **Impact bursts** for hit feedback
- **Chain arcs** for lightning effect clarity
- All effects are **time-bounded** and clean up automatically

## Map Design Philosophy

The Elemental Arena was designed with these principles:
- **Open spaces** for testing long-range weapons
- **Strategic walls** to create positioning challenges
- **Enemy variety** to test different tactical scenarios
- **Health potions** for sustained testing
- **Clear weapon locations** for easy access

Enjoy experimenting with the new elemental weapons! 🎮✨
