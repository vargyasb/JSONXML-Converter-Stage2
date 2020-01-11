# JSONXML-Converter-Stage2
Hyperskill project Stage 2 implementation
Description
XML elements can have attributes. Attributes are designed to contain data related to a specific element or metadata.

Attribute values must always be in quotes.

The structure of an XML attribute is shown below:

<element attribute = "value">element_content</element>
<element attribute_1 = "value" … attribute_N = "value"/>
Here are some examples of XML attributes:

<request id="45692334" date = "2018-12-13"/>
<auth login = "auth_login" sign = "auth_sign" />
<project id = "5" type = "data_processing"> XML/JSON Converter </project>
 

There aren’t attributes in JSON, but as we write a converter, we need a way to interpret XML attributes. Let's say that an attribute in JSON has "@attribute_name": "attribute_value" form. If the element has attributes and has a value inside, you should add #element to element content as the key. See the examples below:

<element attribute1 = "attribute1_value" … attributeN= "attributeN_value">content</element>
This XML objects would be mapped to this JSON object:

{
    "element_name" :
    {
        "@attribute1" : "attribute1_value",
        ……………………………………,
        "@attributeN" : "attributeN_value",
        "#element" : "content"
    }
}
But if the XML or JSON element does not contain any attributes, you should use a simple conversion like in the previous stage.

In this project you should write a program that will read an XML/JSON file from disk and convert it to a JSON/XML file.

Note that you do not need to output JSON or XML in a formatted form. You can print them in one line. Because grader deletes all unnecessary whitespace characters before checking.

You should read from the file named test.txt. If you want to test your program, you should check it on some other file, because the contents of this file will be overwritten during testing and after testing the file will be deleted.

Examples
Example 1:

Sample input #1 (file test.txt)

<employee department = "manager">Garry Smith</employee>
Sample output #1

{
    "employee" : {
        "@department" : "manager",
        "#employee" : "Garry Smith"
    }
}
Example 2:

Sample input #2 (file test.txt)

<person rate = "1" name = "Torvalds" />
Sample output #2

{
    "person" : {
        "@rate" : "1",
        "@name" : "Torvalds",
        "#person" : null
    }
}
Example 3:

Sample input #3 (file test.txt)

{
    "employee" : {
        "@department" : "manager",
        "#employee" : "Garry Smith"
    }
}
Sample output #3

<employee department = "manager">Garry Smith</employee>
Example 4:

Sample input #4 (file test.txt)

{
    "person" : {
        "@rate" : 1,
        "@name" : "Torvalds",
        "#person" : null
    }
}
Sample output #4

<person rate = "1" name = "Torvalds" />
Example 5: 

Sample input #5 (file test.txt)

<host>127.0.0.1</host>
Sample output #5

{"host":"127.0.0.1"}
Example 6:

Sample input #6 (file test.txt)

{"jdk" : "1.8.9"}
Sample output #6

<jdk>1.8.9</jdk>
