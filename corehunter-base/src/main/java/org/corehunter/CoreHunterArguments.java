/*--------------------------------------------------------------*/
/* Licensed to the Apache Software Foundation (ASF) under one   */
/* or more contributor license agreements.  See the NOTICE file */
/* distributed with this work for additional information        */
/* regarding copyright ownership.  The ASF licenses this file   */
/* to you under the Apache License, Version 2.0 (the            */
/* "License"); you may not use this file except in compliance   */
/* with the License.  You may obtain a copy of the License at   */
/*                                                              */
/*   http://www.apache.org/licenses/LICENSE-2.0                 */
/*                                                              */
/* Unless required by applicable law or agreed to in writing,   */
/* software distributed under the License is distributed on an  */
/* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       */
/* KIND, either express or implied.  See the License for the    */
/* specific language governing permissions and limitations      */
/* under the License.                                           */
/*--------------------------------------------------------------*/

package org.corehunter;

import org.corehunter.data.CoreHunterData;

public class CoreHunterArguments {

    private int minimumSubsetSize;

    private int maximumSubsetSize;

    private CoreHunterObjective objective;

    private CoreHunterData data;

    public CoreHunterArguments(CoreHunterData data, int subsetSize) {
        this(data, subsetSize, subsetSize);
    }

    public CoreHunterArguments(CoreHunterData data, int minimumSubsetSize, int maximumSubsetSize) {
        super();
        this.minimumSubsetSize = minimumSubsetSize;
        this.maximumSubsetSize = maximumSubsetSize;
        
        setData(data) ;
    }

    public final int getMinimumSubsetSize() {
        return minimumSubsetSize;
    }

    public final void setMinimumSubsetSize(int minimumSubsetSize) {
        this.minimumSubsetSize = minimumSubsetSize;
    }

    public final int getMaximumSubsetSize() {
        return maximumSubsetSize;
    }

    public final void setMaximumSubsetSize(int maximumSubsetSize) {
        this.maximumSubsetSize = maximumSubsetSize;
    }

    public final CoreHunterData getData() {
        return data;
    }

    public final void setData(CoreHunterData data) {
        this.data = data;
    }

    public final CoreHunterObjective getObjective() {
        return objective;
    }

    public final void setObjective(CoreHunterObjective objective) {
        this.objective = objective;
    }
}
