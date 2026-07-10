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
- FIX SoulChestBlock & SoulChestBlockEntity not working
- SoulTether.s Texture.s
- SoulChest Texture
- TEST SoulTether : chests link, item recovery on death, tiers, chest unlink
- Better DEBUG Logs