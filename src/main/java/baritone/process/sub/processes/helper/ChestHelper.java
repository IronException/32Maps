/*
 * This file is part of Baritone.
 *
 * Baritone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Baritone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Baritone.  If not, see <https://www.gnu.org/licenses/>.
 */

package baritone.process.sub.processes.helper;

import baritone.process.ChestSortProcess;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class ChestHelper {

    public static Container getCurrentContainer(){
        return ChestSortProcess.INSTANCE.ctx.player().openContainer;
    }

    public static boolean isChestOpen() {
        // TODO add all other container types
        return getCurrentContainer() instanceof ContainerChest || getCurrentContainer() instanceof ContainerShulkerBox;
    }

    public static ContainerType getContainer() {
        // TODO when no container open.... ? + many other types ...
        if (getCurrentContainer() instanceof ContainerChest || getCurrentContainer() instanceof ContainerShulkerBox)
            return ContainerType.NORMAL_CHEST;
        else if (getCurrentContainer() instanceof ContainerPlayer)
            return ContainerType.INVENTORY;

        return null;
    }

    public static int itemsInInv(Item item) {
        return ChestSortProcess.INSTANCE.ctx.player().inventory.mainInventory.stream().filter(stack -> item.equals(stack.getItem())).mapToInt(ItemStack::getCount).sum();
    }


    public static ItemStack getItemStackAt(SlotConverter slot){
        return ChestSortProcess.INSTANCE.ctx.player().openContainer.getSlot(slot.getSlotNow()).getStack();
    }

    public static boolean hasSlotItem(SlotConverter slot, Item item){
        return getItemStackAt(slot).getItem().equals(item);
    }

    public static int convertMapId(SlotConverter mapSlot) {
        return getItemStackAt(mapSlot).getMetadata();
    }
}
