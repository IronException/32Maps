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

import baritone.api.process.PathingCommand;

public class MultiProcess extends SubProcess {

    protected SubProcess[] processes;
    protected int i;

    public MultiProcess(SubProcess[] processes) {
        super(new Epsilon());

        this.processes = processes;
        i = 0;
    }

    @Override
    public boolean finished() {
        if(this.processes[i].finished())
            i ++;
        return i >= processes.length;
    }

    @Override
    public boolean superFinished(){
        return finished() && nextProcess.superFinished();
    }

    @Override
    public void tick() {
        this.processes[i].tick();
    }

    @Override
    public void superTick(){
        if(finished())
            nextProcess.superTick();
        else
            tick();
    }

    @Override
    public PathingCommand getReturn(){
        return this.processes[i].getReturn();
    }

}
