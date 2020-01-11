package jsonxml.converter.stage2;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlToJsonConverter implements Converter {
    @Override
    public String convert(String text) {

        String tag = "";
        String elementContent = "";
        Map<String, String> attributeMap = new LinkedHashMap<>();

        Matcher matcher = Pattern.compile("<\\W*\\w.*?>", Pattern.CASE_INSENSITIVE).matcher(text);

        if (text.isEmpty() || text.equals(null)) {
            System.exit(0);
        }

        if (!text.contains("/>")) { //ha nem empty xml tag, pl nem <employee/>
            if (!text.contains("\"")) { //ha nincs benne ", akkor nincs attribute
                if (matcher.find()) {
                    elementContent = "\"" + matcher.replaceAll("") + "\"";
                    tag = findTag("(?<=<)[0-9a-z]+", text);
                }
                // pl: <host>127.0.0.1</host> --> {"host":"127.0.0.1"}
                return "{\"" + tag + "\":" + elementContent + "}";
            } else {//van benne attribute
                if (matcher.find()) {
                    elementContent = "\"" + matcher.replaceAll("") + "\"";
                    tag = findTag("(?<=<)[0-9a-z]+", text);
                    attributeMap = populateMap(matcher, text);
                }
                /* pl: <project id = "5" type = "data_processing" level = "beginner"> XML/JSON conversion  </project>
                    --> {"project": { "@id" : "5", "@type" : "data_processing", "@level" : "beginner", "#project" : " XML/JSON conversion  " } }    */
                return "{\"" + tag + "\": { " + printHashMap(attributeMap) + "\"#" + tag + "\" : " + elementContent + " } }";
            }
        } else { //ha empty xml tag, pl <employee/>
            if (!text.contains("\"")){ //ha nincs benne ", akkor nincs attribute
                tag = findTag("(?<=)[0-9a-z]+", text);
                // pl: <success/> --> {"success": null }
                return "{\"" + tag + "\":" + null + "}";
            } else { // van benne attribute
                tag = findTag("(?<=)[0-9a-z]+", text);
                attributeMap = populateMap(matcher, text);
                /* pl: <person rate = "1" name = "Torvalds" />
                    --> {"person": { "@rate" : "1", "@name" : "Torvalds", "#person" : null } }  */
                return "{\"" + tag + "\": { " + printHashMap(attributeMap) + "\"#" + tag + "\" : " + null + " } }";
            }
        }
    }

    private String findTag(String regex, String text){
        Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text);
        return matcher.find() ? matcher.group() : "";
    }

    private Map<String, String> populateMap(Matcher matcher, String text) {
        String attribute;
        String attributeValue;
        Map<String, String> map = new LinkedHashMap<>();

        if (!text.contains("/>")) {
            //<.*?(?<=\s)[0-9a-z]+.*?> replace a text-et csak a < > koze esore
            matcher = Pattern.compile("<.*?(?<=\\s)[0-9a-z]+.*?>", Pattern.CASE_INSENSITIVE).matcher(text);
            if (matcher.find()) {
                text = matcher.group();
            }
        }
        //splittelni, hogy key-value parokat mapba rakjam ((?<=\\s)[0-9a-z]+|(?<=\\s)\".*?\")
        matcher = Pattern.compile("((?<=\\s)[0-9a-z]+|(?<=\\s)\".*?\")", Pattern.CASE_INSENSITIVE).matcher(text);
        while (matcher.find()) {
            attribute = matcher.group();
            matcher.find();
            attributeValue = matcher.group();
            map.put(attribute, attributeValue);
        }
        return map;
    }

    private String printHashMap(Map<String, String> map) {
        String returnVal = "";
        for (String key : map.keySet()) {
            returnVal += "\"@" + key + "\" : " + map.get(key) + ", ";
        }
        return returnVal;
    }
}
