package org.example;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class ReadXmlJDomParser {

    private static final String FILENAME = "src/main/resources/staff.xml";
    private static final String RESULT_FILENAME = "src/main/resources/result.xml";

    private static String result = "<Company>\n";

    static class StaffValidator {

        private final Long id;
        private final Element element;
        private String output = "";

        public StaffValidator(Long id, Element element) {
            this.id = id;
            this.element = element;
        }

        protected void validateName() {
            String res = element.getChildText("name");
            if (res == null) {
                output += "<name>IS NULL</name>";
            }
        }

        protected void validateRole() {
            String res = element.getChildText("role");
            if (res == null) {
                output += "<role>IS NULL</role>";
            }
        }

        protected void validateSalary() {
            String res = element.getChildText("salary");
            if (res == null) {
                output += "<salary>IS NULL</salary>";
            }
        }

        protected void validateBio() {
            String res = element.getChildText("bio");
            if (res == null) {
                output = "<bio>IS NULL</bio>";
            }
        }

        public String execute() {
            validateName();
            validateRole();
            validateSalary();
            validateBio();
            return output;
        }

    }

    public static String getChild(Element target, String name) {
        String res = null;
        res = target.getChildText(name);
        if (res == null) {
            result += String.format("<%s>Not found</%s>", name, name);
        }
        return res;
    }

    public static void main(String[] args) {
        try {
            SAXBuilder sax = new SAXBuilder();
            Document doc = sax.build(new File(FILENAME));
            Element rootNode = doc.getRootElement();
            List<Element> list = rootNode.getChildren("staff");
            for (Element element : list) {
                String id = element.getAttributeValue("id");
                StaffValidator staffValidator = new StaffValidator(Long.valueOf(id), element);
                String res = staffValidator.execute();
                if (res != null || !res.equals("")) {
                    result += String.format("<staff id=\"%s\">\n%s\n</staff>\n", id, res);
                }
            }

        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
        result += "</Company>";

        // write the final result
        XMLOutputter xmlOutputter = new XMLOutputter();

        // output to any OutputStream
        try (FileOutputStream fileOutputStream = new FileOutputStream(RESULT_FILENAME)) {
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(new StringReader(result));
            xmlOutputter.output(doc, fileOutputStream);
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }

    }
}
