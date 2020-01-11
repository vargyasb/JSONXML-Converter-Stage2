package jsonxml.converter.stage2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JSONXMLConverterStage2 {

    public static void main(String[] args) {

        String input = "";
        File file = new File("./test.txt");
        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNextLine()){
                input += scanner.nextLine().trim();
            }
            //System.out.println(input);
            conversionDeterm(input);
        } catch (FileNotFoundException e) {
            System.out.println("Error " + e.getMessage());
        } catch (Exception e) {
            System.out.println("More general error :v " + e.getMessage());
        }

    }

    public static void conversionDeterm(String text) {
        Converter converter;
        if (text.charAt(0) == '<') {
            converter = new XmlToJsonConverter();
            System.out.println(converter.convert(text));
        } else if (text.charAt(0) == '{') {
            converter = new JsonToXmlConverter();
            System.out.println(converter.convert(text));
        }
    }
    
}
