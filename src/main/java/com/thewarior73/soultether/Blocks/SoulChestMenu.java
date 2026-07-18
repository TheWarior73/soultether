package com.thewarior73.soultether.Blocks;

import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SoulChestMenu extends AbstractContainerMenu {
    private final Container container;

    // Client-side constructor
    public SoulChestMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(54));
    }

    // Server-side constructor
    public SoulChestMenu(int containerId, Inventory playerInventory, Container container) {
        super(ModMenuTypes.SOUL_CHEST_MENU_TYPE, containerId);
        checkContainerSize(container, 54);
        this.container = container;
        container.startOpen(playerInventory.player);

        // Soul Chest Main Inventory slots (slots 0-35 in 9x4 grid)
        int chestSlotIndex = 0;
        for (int row = 0; row < 4; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(container, chestSlotIndex++, 8 + col * 18, 18 + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                });
            }
        }

        // Soul Chest Armor slots (slots 36-39 on Row 4, Columns 0-3)
        for (int col = 0; col < 4; ++col) {
            final int armorSlotIndex = 36 + col;
            this.addSlot(new Slot(container, armorSlotIndex, 8 + col * 18, 18 + 4 * 18) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }

                @Override
                public Identifier getNoItemIcon() {
                    switch (armorSlotIndex) {
                        case 36: return InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS;
                        case 37: return InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS;
                        case 38: return InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE;
                        case 39: return InventoryMenu.EMPTY_ARMOR_SLOT_HELMET;
                        default: return null;
                    }
                }
            });
        }

        // Soul Chest Offhand slot (slot 40 on Row 4, Column 4)
        this.addSlot(new Slot(container, 40, 8 + 4 * 18, 18 + 4 * 18) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public Identifier getNoItemIcon() {
                return InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD;
            }
        });

        // Player Inventory slots (slots 9-35 starting at y = 140)
        int playerInvY = 140;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, playerInvY + row * 18));
            }
        }

        // Player Hotbar slots (slots 0-8 starting at y = 198)
        int hotbarY = 198;
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, hotbarY));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemStack = stackInSlot.copy();
            
            // Chest slots are indices 0 to 40 (41 slots total)
            if (index < 41) {
                // Chest -> Player Inventory/Hotbar (indices 41 to 76)
                if (!this.moveItemStackTo(stackInSlot, 41, 77, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Player -> Chest: Disabled (Take-only!)
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }
        return itemStack;
    }
}
