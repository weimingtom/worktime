/*
 *  Copyright 2011 Dirk Vranckaert
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package eu.vranckaert.worktime.enums.export;

/**
 * All possible file types with their extensions.
 * User: DIRK VRANCKAERT
 * Date: 19/02/11
 * Time: 14:42
 */
public enum FileType {
    COMMA_SERPERATED_VALUES("CSV"),
    ;

    private String extension;

    /**
     * Constructor.
     * @param extension The extension.
     */
    private FileType(String extension) {
        this.extension = extension;
    }

    /*
     * Getters and setters
     */

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Match an extension on one of the possible export formats!
     * @param extension The extensions to match.
     * @return The type of file to export to. If no math is found default choice is
     * {@link FileType#COMMA_SERPERATED_VALUES}
     */
    public static FileType matchFileType(String extension) {
        for (FileType ft : FileType.values()) {
            if (ft.getExtension().equals(extension)) {
                return ft;
            }
        }

        return FileType.COMMA_SERPERATED_VALUES;
    }
}
