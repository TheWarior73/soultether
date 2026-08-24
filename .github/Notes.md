## IMPORTANT

src/main/resources contains all the ressources. relative paths from there are :

- assets/MODID/
    - items
    - lang
    - models/item
    - textures/item

Notice that `models/item` and `texture/item` do not pluralize 'item' whereas `items` does

AABB - Axis Aligned Bounding Box

## Idea.s

> Soul tether item
> - Links to a container (chest, custom-made "chest (with armor and off-hand slots and auto place stuff in the right place capabilities)" ?)
> - Can lose some items (up to 20%) or something similar (to be tested)
> - Dimensional cost (durability use multiplier)
> - Multiple tiers could modify
    >   - Number of uses before the item breaks
>   - The probability to lose stuff could diminish the higher the tier is.
>   - Reduces dimensional cost

> Soul Chest
> - Links to a tether and stores dead player's inventory
> - "take only" - cannot put items in it, only retrieve
> - Empties content on ground if player's inventory could not hold all the items

## Known Stuff

- Linking to a chest, breaking it and placing another one keeps the link alive - WONTFIX ?
- Dying with soul tether in inventory drops chest content on the ground if any items were left in it.  
  This behaviour is expected and contributes to the fairness of the mod overall.
- If a player has the tether of another player, and tries to unlink from a secure chest, the chest won't allow it based on the fact that is it not the same player.

## TODO
Chest
- FIX SoulChest GUI
- Secure SoulChest Crafting Recipe
- SoulChest & Secure variant Textures (refactor)

Tether
- Send a message about dropped items depending on tier
- Add enchants - [Unbreaking, mending]
- Include tether in drop chance instead of guaranted drop ?

Compatibility  
N/A

Misc
- Better DEBUG Logs
- TESTS

# Default Behaviour

By default, Durability cost for each tier of tether is `1`.  
If the soul chest (or Secure soul chest) is in a different dimension as the player on death,
the following durability costs take effect :
```
Basic   -> 1 (unchanged)
Iron    -> 2
Gold    -> 3
Diamond -> 5
```

Durability of tethers by tier is :
```
Basic   -> 1
Iron    -> 10
Gold    -> 5
Diamond -> 25
```

> [!NOTE]
> Item loss will occur but won't be affected by dimensional cost
