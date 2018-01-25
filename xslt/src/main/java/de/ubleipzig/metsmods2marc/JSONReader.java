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

package de.ubleipzig.metsmods2marc;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * JSONReader.
 *
 * @author christopher-johnson
 */
public class JSONReader {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final JsonNode data;
    private final String filePath;

    /**
     * Create a JSON-based Namespace service.
     *
     * @param filePath the file path
     */
    public JSONReader(final String filePath) {
        requireNonNull(filePath, "The filePath may not be null!");

        this.filePath = filePath;
        this.data = read(filePath);
    }

    public static JsonNode read(final String filePath) {
        final File file = new File(filePath);
        JsonNode rootNode = null;
        if (file.exists()) {
            try {
                rootNode = MAPPER.readTree(file);
            } catch (final IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
        return rootNode;
    }

    public JsonNode getRoot() {
        return data;
    }
}

