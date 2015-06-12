package com.pms.webservice.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.OutputStreamWriter;  
  
import org.jdom2.Attribute;  
import org.jdom2.Document;  
import org.jdom2.Element;  
import org.jdom2.output.Format;  
import org.jdom2.output.XMLOutputter;  
public class xmltest {  
    public void create(){  
        Element root=new Element("PEOPLE");  
        Document doc=new Document(root);  
        Element person=new Element("PERSON");  
        root.addContent(person);  
        Attribute attribute=new Attribute("title","这是一个测试的文字");  
        person.setAttribute(attribute);  
        XMLOutputter output=new XMLOutputter(Format.getPrettyFormat().setEncoding("utf-8"));  
        try {  
            //FileWriter writer=new FileWriter("src/people.xml");  
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("d:/people.xml"), "UTF-8");  
            output.output(doc, writer);  
            writer.close();  
            
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			OutputStreamWriter writer11 = new OutputStreamWriter(baos, "utf-8"); 
			output.output(doc, writer11);
			writer11.close();
			String result = baos.toString();
			System.out.println(result);
               
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    public static void main(String[] args) {  
        new xmltest().create();  
    }  
} 