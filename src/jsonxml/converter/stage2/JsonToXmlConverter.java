package jsonxml.converter.stage2;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonToXmlConverter implements Converter {
    @Override
    public String convert(String text) {

        String value = "";
        String openingTag = "";
        String closingTag = "";
        Map<String, String> attributeMap = new LinkedHashMap<>();

        Matcher matcher = Pattern.compile("(?<=\")[0-9a-z]+", Pattern.CASE_INSENSITIVE).matcher(text);

        if (text.isEmpty() || text.equals(null)) {
            System.exit(0);
        }

        if (!text.contains("null")) {
            if (!text.contains("@")){ // ha nincs benne @, akkor nincs attribute
                if (matcher.find()) {
                    openingTag = "<" + matcher.group() + ">";
                    closingTag = "</" + matcher.group() + ">";
                    value = text.replaceAll("\\{\"\\w*\\W.*?\"", "").replaceAll("\"}", "");
                }
                // pl: {"jdk" : "1.8.9"} --> <jdk>1.8.9</jdk>
                return openingTag + value + closingTag;
            } else {
                if (matcher.find()) {
                    openingTag = "<" + matcher.group();
                    closingTag = "</" + matcher.group() + ">";
                    attributeMap = populateMap(matcher, text);
                    //{.*?"\w*\W.*?#\w*"\W* regex levagni a value reszt aztan levagni a veget \"*}*
                    value = text.replaceAll("\\{.*?\"\\w*\\W.*?#\\w*\"\\W*", "").replaceAll("\"*}*", "").trim();
                }
                /* pl: {"employee" : { "@department" : "manager", "#employee" : "Garry Smith" } }
                    --> <employee department = "manager">Garry Smith</employee> */
                return openingTag + printHashMap(attributeMap) + ">" + value + closingTag;
            }
        } else {
            if (!text.contains("@")){// ha nincs benne @, akkor nincs attribute
                if (matcher.find()) {
                    openingTag = "<" + matcher.group() + "/>";
                }
                // pl: { "storage" : null } --> <storage/>
                return openingTag + value + closingTag;
            } else {
                if (matcher.find()) {
                    openingTag = "<" + matcher.group();
                    attributeMap = populateMap(matcher, text);
                }
                /* pl: { "person" : { "@rate" : 1, "@name" : "Torvalds", "#person" : null } }
                    --> <person rate = "1" name = "Torvalds" /> */
                return openingTag + printHashMap(attributeMap) + "/>";
            }
        }

    }

    private Map<String, String> populateMap(Matcher matcher, String text) {
        String attribute = "";
        String attributeValue = "";
        Map<String, String> map = new LinkedHashMap<>();
        //regex levagni az attribute reszt
        matcher = Pattern.compile(":\\s*\\{.*\"#", Pattern.CASE_INSENSITIVE).matcher(text);
        String tagWithAttributes = "";
        if (matcher.find()) {
            tagWithAttributes = matcher.group();
        }
        //splittelni, hogy key-value parokat mapba rakjam ((?<=)[0-9a-z]+|(?<=\\s)\".*?\")
        matcher = Pattern.compile("((?<=)[0-9a-z]+|(?<=\\s)\".*?\")", Pattern.CASE_INSENSITIVE).matcher(tagWithAttributes);
        while (matcher.find()) {
            attribute = matcher.group().replaceAll("@", "").replaceAll("\"", "");
            matcher.find();
            attributeValue = matcher.group();
            if (attributeValue.matches("\\d*")) {
                attributeValue = "\"" + attributeValue + "\"";
            }
            map.put(attribute, attributeValue);
        }
        return map;
    }

    private String printHashMap(Map<String, String> map) {
        String returnVal = "";
        for (String key : map.keySet()) {
            returnVal += " " + key + " = " + map.get(key);
        }
        return returnVal;
    }
}
