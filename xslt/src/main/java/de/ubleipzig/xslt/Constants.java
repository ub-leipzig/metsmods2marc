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

/**
 * Constants.
 *
 * @author christopher-johnson
 */
final class Constants {

    static final String OUTPUT_DIR =
            "/tmp/output";

    static final String METS_MODS_STYLESHEET = "extract-mods.xsl";

    static final String MODS_MARC_STYLESHEET = "MODS3-4_MARC21slim_XSLT1-0.xsl";

    static final String MARC_MODS_STYLESHEET = "MARC21slim_MODS3-6_XSLT2-0.xsl";

    static final String MODS_BIBFRAME_STYLESHEET = "mods2bibframe.xsl";

    static final String REPOSITORY_BASE_URI = "http://localhost:8080/repository";

    private Constants() {
    }
}
