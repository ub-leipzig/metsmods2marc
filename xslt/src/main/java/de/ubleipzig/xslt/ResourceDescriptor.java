/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.ubleipzig.xslt;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ResourceDescriptor.
 *
 * @author Christopher Johnson
 */
@XmlRootElement
public class ResourceDescriptor {

    private String body;

    /**
     * getBody.
     *
     * @return String
     */
    public String getBody() {
        return body;
    }

    /**
     * setBody.
     *
     * @param body String
     */
    @XmlElement
    void setBody(final String body) {
        this.body = body;
    }

}