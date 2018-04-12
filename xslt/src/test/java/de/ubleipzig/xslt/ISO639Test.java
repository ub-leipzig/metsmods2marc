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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Test;

public class ISO639Test {

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void getCodesFromJson() throws IOException {
        final URL res = JSONReader.class.getResource("/iso639-2.json");
        final ISO639CodeList codelist = MAPPER.readValue(res, new TypeReference<ISO639CodeList>() {
        });
        final List<ISO639Code> codes = codelist.getCodes();
        codes.stream()
             .filter(c -> c.getEnglishLabel()
                           .contains("lo"))
             .forEach(c -> System.out.println(c.getEnglishLabel()));
    }
}
