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

package baritone.process.sub.processes;

import baritone.Baritone;
import baritone.api.process.PathingCommand;
import baritone.api.utils.IPlayerContext;
import baritone.process.ChestSortProcess;
import net.minecraft.client.entity.EntityPlayerSP;

public abstract class SubProcess {

    protected SubProcess nextProcess;

    protected ChestSortProcess parent;
    protected Baritone baritone;
    protected IPlayerContext ctx;
    protected EntityPlayerSP player;

    public SubProcess(SubProcess nextProcess){
        this.nextProcess = nextProcess;

        this.parent = ChestSortProcess.INSTANCE;
        this.baritone = this.parent.baritone;
        this.ctx = this.parent.ctx;
        this.player = this.ctx.player();
    }

    public abstract boolean finished();

    public boolean superFinished(){
        return finished() && nextProcess.superFinished();
    }


    public abstract void tick();

    public void superTick(){
        if(finished())
            nextProcess.superTick();
        else
            tick();
    }

    public PathingCommand getReturn(){
        return nextProcess.getReturn();
    }

}
