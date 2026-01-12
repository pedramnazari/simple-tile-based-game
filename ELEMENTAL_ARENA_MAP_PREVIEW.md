# Elemental Arena Map Layout

```
Legend:
W = Wall           F = Floor          G = Grass          D = Destructible Wall
X = Exit           HP = Health Potion IW = Ice Wand     LR = Lightning Rod
FS = Fire Staff    ELR = Enemy (L-R)  ETD = Enemy (T-D) E2D = Enemy (2D)
EFH = Enemy (Follow Hero)

Map View (18x15):
╔════════════════════════════════════╗
║W  W  W  W  W  W  W  W  W  W  W  W  W  W  W  W  W  W ║
║W  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  W ║
║W  . IW  .  .  .  .  .  .  .  .  .  .  . LR  .  .  W ║  <- Ice Wand (left) & Lightning Rod (right)
║W  .  . W  W  W  .  .  .  .  . W  W  W  .  .  .  W ║
║W  .  . W HP W  . ELR .  . ELR W HP W  .  .  .  W ║  <- Horizontal enemies for freeze testing
║W  .  . W  W  W  .  . D  D  . W  W  W  .  .  .  W ║
║W  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  W ║
║W  .  . ETD .  .  .  . FS  .  .  .  . ETD .  .  .  W ║  <- Fire Staff (center) & vertical enemies
║W  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  W ║
║W  .  .  .  .  . E2D E2D .  . E2D E2D .  .  .  .  W ║  <- Enemy cluster for chain testing
║W  .  . W  W  W  .  .  .  .  . W  W  W  .  .  .  W ║
║W  .  . W HP W  .  .  .  .  . W HP W  .  .  .  W ║
║W  .  . W  W  W  .  . EFH .  . EFH . W  W  W  .  W ║  <- Follow-hero enemies
║W  .  .  .  .  .  .  .  .  .  .  .  .  .  . X  .  W ║  <- Exit (bottom right)
║W  W  W  W  W  W  W  W  W  W  W  W  W  W  W  W  W  W ║
╚════════════════════════════════════╝

Key Features:
1. START POSITION: Top-left corner (1,1)
2. ICE WAND: Row 2, Column 2 - Pickup immediately and test freeze effect
3. LIGHTNING ROD: Row 2, Column 15 - Long walk but worth it for chain testing
4. FIRE STAFF: Row 7, Column 8 - Center position for comparison
5. ENEMY CLUSTER (Row 9): Four 2D-moving enemies close together - perfect for chain lightning
6. HEALTH POTIONS: Four potions strategically placed (rows 4 and 11)
7. EXIT: Bottom right (13, 16)

Tactical Setup:
- Upper area: Test Ice Wand on horizontal enemies (they move left-right)
- Middle area: Get Fire Staff for comparison
- Center cluster: Best spot to test Lightning Rod's chain effect
- Lower area: Test freeze on aggressive follow-hero enemies
```

## Visual Effect Preview

When you run the game and use these weapons, you'll see:

### Ice Wand Effect:
```
   Enemy
     ↓
  ❄️ 💙 ❄️  ← Blue glowing projectile with frost particles
     ↓
   💥❄️💥  ← Frost burst on impact
   [Enemy frozen - skips next turn]
```

### Lightning Rod Effect:
```
   Enemy 1    Enemy 2
      ↓         ↓
   ⚡ 💛 ⚡   ← Yellow flickering projectile
      ↓         
    💥⚡💥     ← Spark burst on first enemy
      ⚡⚡⚡⚡→ Enemy 2  ← Lightning arc chains to adjacent enemy
      💥⚡💥     ← Second enemy also damaged
```

## How to Play

1. Run: `./gradlew run`
2. Select "Elemental Arena" from the map menu
3. Navigate with arrow keys/WASD
4. Pick up Ice Wand (just move right to position 2,2)
5. Press Space to shoot
6. Watch the blue frost effects!
7. Later, try Lightning Rod against the enemy cluster

See ELEMENTAL_WEAPONS_GUIDE.md for complete instructions!
