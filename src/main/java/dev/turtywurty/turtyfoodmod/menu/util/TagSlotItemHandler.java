package dev.turtywurty.turtyfoodmod.menu.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.items.IItemHandler;

public class TagSlotItemHandler extends PredicateSlotItemHandler {
    public TagSlotItemHandler(IItemHandler handler, int index, int xPosition, int yPosition, TagKey<Item> tag) {
        super(handler, index, xPosition, yPosition, stack -> stack.is(tag));
    }
}
