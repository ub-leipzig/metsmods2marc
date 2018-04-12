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

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class ISO639Test {

    @Test
    public void getCodesFromJson() throws IOException, ParseException {
        final URL res = JSONReader.class.getResource("/iso639-2.json");
        final JSONParser parser = new JSONParser();
        final Object obj = parser.parse(new FileReader(res.getPath()));
        final JSONObject jsonObject = (JSONObject) obj;
        final JSONArray codes = (JSONArray) jsonObject.get("codes");
        codes.stream()
                .filter(code -> ((JSONObject) code).containsValue("lo"))
                .forEach(System.out::println);
    }
}
