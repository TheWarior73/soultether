# Soul Tether

> MC 26.2 Fabric Documentation   
> Pull request status : https://github.com/FabricMC/fabric-docs/pull/645  
> Temporary link : https://deploy-preview-645--nimble-elf-d9d491.netlify.app/develop/items/custom-tools

## IMPORTANT

src/main/resources contains all the ressources. relative paths from there are :

- assets/MODID/
  - items
  - lang
  - models/item
  - textures/item

Notice that `models/item` and `texture/item` do not pluralize 'item' whereas `items` does

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

## TODO
- SoulTether.s Texture.s (gold, diamond)
- SoulChest Texture
- Better DEBUG Logs
- Send a message about dropped items depending on tier
- SoulChest GUI
- TEST: Dimensional Costs

## TESTS

- Soul Tether (BASIC)

| ITEM                             | STATUS  | COMMENT                                |
|----------------------------------|---------|----------------------------------------|
| Link to chest                    | PASSED  |                                        |
| Unlink from chest                | FAILED  | FIXME                                  |
| Link to new chest                | PASSED  |                                        |
| consume durability on death      | PASSED  | Consumes the item (1 durability total) |
| Drop item on death               | N/A     | Should be 15% of inventory             |
| Dimentional cost drop multiplier | N/A     | Should be x2.5                         |

- Soul Tether (IRON)

| ITEM                              | STATUS  | COMMENT                    |
|-----------------------------------|---------|----------------------------|
| Link to chest                     | PASSED  |                            |
| Unlink from chest                 | FAILED  |                            |
| Link to new chest                 | PASSED  |                            |
| consume durability on death       | UNSURE  |                            |
| Drop item on death                | N/A     | Should be 10% of inventory |
| Dimentional cost drop multiplier  | N/A     | Should be x1.75            |

- Soul Tether (GOLD)

| ITEM                             | STATUS  | COMMENT                   |
|----------------------------------|---------|---------------------------|
| Link to chest                    | N/A     |                           |
| Unlink from chest                | N/A     |                           |
| Link to new chest                | N/A     |                           |
| consume durability on death      | N/A     |                           |
| Drop item on death               | N/A     | Should be 0% of inventory |
| Dimentional cost drop multiplier | N/A     | Should be x1.25           |

- Soul Tether (DIAMOND)

| ITEM                             | STATUS  | COMMENT                   |
|----------------------------------|---------|---------------------------|
| Link to chest                    | N/A     |                           |
| Unlink from chest                | N/A     |                           |
| Link to new chest                | N/A     |                           |
| consume durability on death      | N/A     |                           |
| Drop item on death               | N/A     | Should be 0% of inventory |
| Dimentional cost drop multiplier | N/A     | Should be x1              |

- Soul Chest

| ITEM                                                               | STATUS  | COMMENT                   |
|--------------------------------------------------------------------|---------|---------------------------|
| Store inventory on death                                           | N/A     |                           |
| Cannot put stuff inside                                            | N/A     |                           |
| Drops content on ground if player inventory cannot hold everything | N/A     |                           |

- Soul Chest (Secure)

| ITEM                                                               | STATUS  | COMMENT                   |
|--------------------------------------------------------------------|---------|---------------------------|
| Can only be accessed by owning player                              | N/A     |                           |
| Store inventory on death                                           | N/A     |                           |
| Cannot put stuff inside                                            | N/A     |                           |
| Drops content on ground if player inventory cannot hold everything | N/A     |                           |
