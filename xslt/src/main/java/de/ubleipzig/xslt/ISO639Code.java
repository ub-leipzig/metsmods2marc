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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

class ISO639Code {

    @JsonProperty("iso6392")
    private String iso6392;
    @JsonProperty("iso6391")
    private String iso6391;
    @JsonProperty("englishLabel")
    private String englishLabel;
    @JsonProperty("frenchLabel")
    private String frenchLabel;
    @JsonProperty("germanLabel")
    private String germanLabel;

    @JsonIgnore
    public String getIso6392() {
        return iso6392;
    }

    @JsonIgnore
    public String getIso6391() {
        return iso6391;
    }

    @JsonIgnore
    public String getEnglishLabel() {
        return englishLabel;
    }

    @JsonIgnore
    public String getFrenchLabel() {
        return frenchLabel;
    }

    @JsonIgnore
    public String getGermanLabel() {
        return germanLabel;
    }
}
