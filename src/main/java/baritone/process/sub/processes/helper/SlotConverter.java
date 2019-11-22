
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


public class SlotConverter {

    protected int slot;
    protected ContainerType as;

    public SlotConverter(int slot, ContainerType as) {
        this.slot = slot;
        this.as = as;
    }

    public int getSlotNow() {
        return getSlotIn(ChestHelper.getContainer());
    }

    public int getSlotIn(ContainerType in) {
        return in.getSlots() - as.getSlots() + this.slot;
    }

    @Override
    public String toString() {
        return "        slot: " + this.slot + " (" + this.getSlotNow() + ")";
    }

}
