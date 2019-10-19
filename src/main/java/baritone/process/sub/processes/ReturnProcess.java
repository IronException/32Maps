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

public abstract class ReturnProcess extends SubProcess {

    public ReturnProcess(SubProcess nextProcess) {
        super(nextProcess);
    }


    @Override
    public boolean isFinished() {
        return true; // TODO is this right?
    }

    @Override
    public void doTick() {
        // returning is my childrens job
    }

    @Override
    public PathingCommand getReturn(){
        PathingCommand rV = super.getReturn();
        return (rV != null) ? rV : generateReturn();
    }


    /*

    there might be a problem actually?
     -> where the fuck is the PathingCommand?

    it has to be defined where. rn it could be anywhere and the one that is on the lowest will be taken...

    okay so we have:
    - we need to return one for sure. no matter from where...
    - it could be the first one encountered in the series
    - it could be the last one?
    - maybe best is like this:


    - = command
    | = a return
    = = already executed

    =-|---|---
      ^ is first...

    ==|=--|---
      ^ this one should be taken

    ==|===|=--
          ^ that one

    how to do that?

    so getReturn() should give it when it has one
    when not it should get one from next
    -> that way it will always return the first one... so:


       normal: return null if not finished
       return: generate if null

     */

    public abstract PathingCommand generateReturn();

}
