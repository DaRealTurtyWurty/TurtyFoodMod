package dev.turtywurty.turtyfoodmod.menu.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.Predicate;

public class PredicateSlotItemHandler extends SlotItemHandler {
    private final Predicate<ItemStack> placePredicate;

    public PredicateSlotItemHandler(IItemHandler handler, int index, int xPosition, int yPosition, Predicate<ItemStack> placePredicate) {
        super(handler, index, xPosition, yPosition);
        this.placePredicate = placePredicate;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return super.mayPlace(stack) && this.placePredicate.test(stack);
    }
}
