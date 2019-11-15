package assignment_3;

import java.util.ArrayList;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.output.*;


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
				
				Sender.sendDoc(doc);
				
				//in.close();
				
				break;
			}
			else if(resp.startsWith("d")) {
				
				doc = Receiver.receiveDoc();
				
				System.out.println("Document received: ");
				XMLOutputter myXMLOutput = new XMLOutputter();
				
				myXMLOutput.setFormat(Format.getPrettyFormat());
				System.out.println(myXMLOutput.outputString(doc));
				
				//in.close();
				
				break;
			}
			else {
				System.out.println(resp + " is an invalid choice!\nPlease try again...\n");
			}
		}
		
	}

}
