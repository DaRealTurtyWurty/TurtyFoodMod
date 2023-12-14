package dev.turtywurty.turtyfoodmod.menu.util;

import net.minecraftforge.items.IItemHandler;

public class OutputSlotItemHandler extends PredicateSlotItemHandler {
    public OutputSlotItemHandler(IItemHandler handler, int index, int xPosition, int yPosition) {
        super(handler, index, xPosition, yPosition, stack -> false);
    }
}
