package assignment_3;

import java.util.ArrayList;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;


public class MethodTester {

	public static void main(String[] args) {
		Scanner in = new java.util.Scanner(System.in);
		String resp;
		
		Document doc = null;
		while(true) {
			System.out.print("Is this the (s)erializer or (d)eserializer? [s/d]  ");
			resp = in.nextLine();
			if(resp.startsWith("s")) {
				
				ArrayList<Object> createdObjects = ObjectCreator.createObjects();
				System.out.println("Created object size: " + createdObjects.size());

				System.out.println("Serializing objects...\n\n");
				doc = Serializer.serialize(createdObjects);
				
				System.out.println("Document to be sent: ");
				XMLOutputter myXMLOutput = new XMLOutputter();
				
				myXMLOutput.setFormat(Format.getPrettyFormat());
				System.out.println(myXMLOutput.outputString(doc));
				
				//Sender.sendDoc(doc);
				
				
				break;
			}
			else if(resp.startsWith("d")) {
				
				boolean fromFile = true;
				if(fromFile) {
					
					File file = new java.io.File("D:\\Desktop\\xmlFile.txt");
					//Scanner fScan = null;
					
					try {
						SAXBuilder docBuilder = new SAXBuilder();
						doc = docBuilder.build(file);
						//fScan = new Scanner(file);
						
					} catch (FileNotFoundException e) {
						System.err.println(file.getAbsolutePath() + " not found");
						e.printStackTrace();
						System.exit(-1);
					} catch (Exception e) {
						System.err.println("Error parsing file.\nQuitting!");
						e.printStackTrace();
						System.exit(-1);
					}
					/*StringBuilder stringDoc = new StringBuilder();
					while(fScan.hasNextLine()) {
						stringDoc.append(fScan.nextLine());
					}*/
					
				}
				else {
					doc = Receiver.receiveDoc();
				}
				
				System.out.println("Document received: ");
				XMLOutputter myXMLOutput = new XMLOutputter();
				
				myXMLOutput.setFormat(Format.getPrettyFormat());
				System.out.println(myXMLOutput.outputString(doc));
				
				
				Object deserializedObjs = Deserializer.deserialize(doc);
				
				if(deserializedObjs.getClass().isArray()) {
					int len = Array.getLength(deserializedObjs);
					for(int i = 0; i < len; ++i) {
						Visualizer.inspect( Array.get(deserializedObjs, i) );
						System.out.println("");
					}
				}
				else {
					Visualizer.inspect(deserializedObjs);
				}
				
				
				break;
			}
			else {
				
				System.out.println(resp + " is an invalid choice!\nPlease try again...\n");
			}
		}
		in.close();
		
	}

}
