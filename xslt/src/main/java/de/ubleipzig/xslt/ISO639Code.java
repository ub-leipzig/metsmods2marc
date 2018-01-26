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

import com.fasterxml.jackson.annotation.JsonProperty;

class ISO639Code {

    @JsonProperty("ISO_639_2")
    private String ISO_639_2;
    @JsonProperty("ISO_639_1")
    private String ISO_639_1;
    @JsonProperty("English_name_of_Language")
    private String English_name_of_Language;
    @JsonProperty("French_name_of_Language")
    private String French_name_of_Language;
    @JsonProperty("German_name_of_Language")
    private String German_name_of_Language;

    public String getISO_639_2() {
        return ISO_639_2;
    }

    public String getISO_639_1() {
        return ISO_639_1;
    }

    public String getEnglishName() {
        return English_name_of_Language;
    }

    public String getFrenchName() {
        return French_name_of_Language;
    }

    public String getGermanName() {
        return German_name_of_Language;
    }
}
