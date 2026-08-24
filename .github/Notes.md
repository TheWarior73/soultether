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
- Dying twice (with soul tether in inventory but nothing else) drops chest content on ground.  
  This behaviour is expected and contributes to the fairness of the mod overall.
- If a player has the tether of another player, and tries to unlink from a secure chest, the chest won't allow it based on the fact that is it not the same player.

## TODO
Chest
- FIX SoulChest GUI
- Secure SoulChest block & logic
- Secure SoulChest Crafting Recipe

Tether
- Send a message about dropped items depending on tier
- Keep item with inventory on death ? (remove item loss chance)
- Add enchants - [Unbreaking, mending]

Compatibility
- [x] Adapt to ModMenu Config Type

Misc
- Better DEBUG Logs
- TESTS
- [x] Config Screen
    - [x] Toggle Item Loss probability on death
    - [x] Toggle Debug stuff (idk if usefull in this case with how we implemented logs...)
    - [x] Tweak Item Loss probability (subscreen with each tether displayed, on the right a slider that goes from 0 to 100%)

# Default Behaviour

Durability loss is affected by dimension (deathDim != chestDim -> durabilityLoss * dimMult : durabilityLoss)

Item loss will occur but won't be affected by dimensional cost
