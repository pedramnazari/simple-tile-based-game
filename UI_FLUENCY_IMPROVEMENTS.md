# UI Fluency Improvements

## Diagonal Movement Implementation

### Approach Chosen: Option B - Keep 4-Direction Gameplay with Better Input Handling

**Why Option B instead of Option A (true diagonal support)?**

1. **Minimal Changes**: The existing game logic, movement services, and collision detection are all built around the `MoveDirection` enum which only supports 4 directions (UP, DOWN, LEFT, RIGHT). Extending this to support 8 directions (including diagonals) would require:
   - Updating the `MoveDirection` enum
   - Modifying movement calculation logic in `MovementService`
   - Updating collision detection for diagonal moves
   - Potentially affecting enemy AI and pathfinding
   - Revising multiple test cases

2. **Game Design Consistency**: The game is fundamentally a tile-based game with orthogonal movement. Adding true diagonal movement changes the core gameplay mechanics and could affect game balance (diagonal tiles would be more valuable for escape/chase).

3. **Effective Solution**: Option B still achieves the fluency goals:
   - **No overlapping transitions**: Only one move executes per tick (90ms), preventing the jarring snap from cancelling mid-flight animations
   - **Predictable diagonal feel**: When both vertical and horizontal keys are pressed, we prioritize vertical movement. This creates a consistent zigzag pattern that feels natural
   - **Consistent speed**: Each move takes the same 90ms regardless of direction

### Implementation Details

**Input Aggregation (`InputState` class)**:
- Aggregates all key states in one place
- Provides `getDiscreteMovement()` to return at most one movement command per tick
- Vertical priority ensures diagonal movement alternates predictably (e.g., up-right becomes: up, right, up, right...)

**Smooth Camera Tracking (`CameraController` class)**:
- Updates at 60fps (16ms intervals) instead of only when hero reaches a tile
- Tracks the hero's actual rendered position including `translateX/Y` during animation
- Uses lerp (LERP_FACTOR = 0.25) for smooth easing
- Camera no longer "snaps" - it smoothly follows throughout the movement

**Node Reuse**:
- Items: Differential update - only add/remove items that changed positions
- Bombs: Only recreate if not already in the view list
- Image caching: All images loaded once and cached in `imageCache` HashMap
- Result: Reduced DOM churn, fewer image loads, smoother rendering

## Performance Improvements

- **Before**: Items/bombs recreated on every update → layout thrashing
- **After**: Incremental updates, reuse existing nodes
- **Before**: Images loaded repeatedly from resources
- **After**: Single load per unique image path, cached for reuse
- **Before**: Camera updated only when hero reaches destination tile
- **After**: Camera updates continuously at 60fps, tracking mid-animation position

## Testing Notes

Manual testing recommended:
1. Hold diagonal keys (e.g., UP+RIGHT) - should see smooth alternating movement
2. Change direction quickly - no visible snapping or animation cancellation
3. Move around map - camera should smoothly track hero without jumpiness
4. Verify items, bombs, enemies still render correctly
