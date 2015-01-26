/* 
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 *  Copyright 2015 czcaspercz. All rights reserved.
 *  
 *  The contents of this file are subject to the terms of either the the Common Development and Distribution License 1.0 ("CDDL 1.0")
 *  You may not use this file except in compliance with the License. You can obtain a copy of the License at 
 *  
 *  http://opensource.org/licenses/CDDL-1.0
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 *  When distributing the software, include this License Header
 *  
 */
package cz.a_d.automation.golem.io;

import cz.a_d.automation.golem.protocol.file.FileURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This enum is representing option for access rights to file object used in
 * connections. Primary developed for
 *
 * @see FileURLConnection to recognize which type of file stream should use for
 * internal channel representation.
 *
 * @author casper
 */
public enum FileAccessModificator {

    /**
     * READ - represent read access to file resource.
     */
    READ('r'),
    /**
     * WRITE - represent write acces to file resource, doesn't implies create.
     */
    WRITE('w'),
    /**
     * CREATE - represent rights to create file resource by using URL.
     */
    CREATE('c'),
    /**
     * EXECUTE -represent access rights execute file resource.
     */
    EXECUTE('x');

    /**
     * This patter represents pattern of regular exppression which is able
     * extract from URL query string access words defined in format:
     * access={...,....,..} with validating of used characters inside squares.
     */
    private final static Pattern access = Pattern.compile(".*[\\&,\\;]*access=\\{([a-zA-Z,\\,]*)\\}[\\&,\\;]*.*");

    /**
     * Internal enum storage for value represented by specific enum instance.
     */
    private final char value;

    /**
     * Internal enum constructor required by defined conversion between enum and
     * stadart Java type.
     *
     * @param value this value come from definition of enum values.
     */
    private FileAccessModificator(char value) {
        this.value = value;
    }

    /**
     * Method initialize array of enum instaces from short character
     * representation inside Character sequence (One character one enum in best
     * case).
     *
     * @param modificators -it must contains at least one character shortcut
     * which representing defined enum value.
     * @return null in case when sequence doesn't contains valid character,
     * otherwise array with enum values representing rights in order from
     * beginig sequence to end.
     */
    public static FileAccessModificator[] fromCharStream(CharSequence modificators) {
        FileAccessModificator[] retValue = null;
        if ((modificators != null) && (modificators.length() > 0)) {
            List<FileAccessModificator> tmpList = new ArrayList<>(modificators.length());
            for (int i = 0; i < modificators.length(); i++) {
                FileAccessModificator fromChar = fromChar(modificators.charAt(i));
                if (fromChar != null) {
                    tmpList.add(fromChar);
                }
            }
            retValue = new FileAccessModificator[tmpList.size()];
            tmpList.toArray(retValue);
        }
        return retValue;
    }

    /**
     * Method initialize array of enum instaces from String representation
     * inside URL query parameters.
     *
     * example of valid query:
     * ...someParameret&access={read,write};otherParmers=.. where other
     * parameters before and after are optional.
     *
     * @param query properly decoded string from URL query part.
     * @return return null in case when query doesn't contains valid string
     * representation of comma separated enums.
     */
    public static FileAccessModificator[] fromQuery(String query) {
        FileAccessModificator[] retValue = null;
        if ((query != null) && (!query.isEmpty())) {
            Matcher matcher;
            synchronized (access) {
                matcher = access.matcher(query);
            }
            if ((matcher.matches()) && (matcher.groupCount() == 1)) {
                String group = matcher.group(1);
                if ((group != null) && (!group.isEmpty())) {
                    String[] splitedRights = group.split(",");
                    List<FileAccessModificator> tmpList = new ArrayList<>(splitedRights.length);
                    for (String singleRight : splitedRights) {
                        try {
                            FileAccessModificator fromString = valueOf(singleRight.toUpperCase());
                            tmpList.add(fromString);
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(FileAccessModificator.class.getName()).log(Level.WARNING, "Invalid access rights identificator:{0} .Has been used.", singleRight);
                        }
                    }
                    retValue = new FileAccessModificator[tmpList.size()];
                    tmpList.toArray(retValue);
                }
            }
        }
        return retValue;
    }

    /**
     * Method is override to return simple character representation of enum.
     *
     * @return string with one character which representing enum value.
     */
    @Override
    public String toString() {
        return Character.toString(value);
    }

    /**
     * Method creates instance of enum from valid character representation.
     *
     * @param ch - character which should represent valid character
     * representation of enum.
     * @return - enum instance in case of valid input, otherwise null.
     */
    public static FileAccessModificator fromChar(Character ch) {
        FileAccessModificator retValue = null;
        if (ch != null) {
            for (FileAccessModificator search : values()) {
                if (Character.toLowerCase(ch) == search.value) {
                    retValue = search;
                    break;
                }
            }
        }
        return retValue;
    }
}
