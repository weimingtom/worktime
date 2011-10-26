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
 * User: DIRK VRANCKAERT
 * Date: 19/02/11
 * Time: 17:35
 */
public enum CsvSeparator {
    COMMA(','),
    SEMICOLON(';');

    private char seperator;

    private CsvSeparator(char seperator) {
        this.seperator = seperator;
    }

    public char getSeperator() {
        return seperator;
    }

    public void setSeperator(char seperator) {
        this.seperator = seperator;
    }

    public static CsvSeparator matchFileType(String seperator) {
        for(CsvSeparator s : CsvSeparator.values()) {
            if (String.valueOf(s.getSeperator()).equals(seperator)) {
                return s;
            }
        }
        return null;
    }
}
