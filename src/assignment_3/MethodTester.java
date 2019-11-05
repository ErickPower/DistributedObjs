package assignment_3;

import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.output.*;

public class MethodTester {

	public static void main(String[] args) {
		ArrayList<Object> createdObjects = ObjectCreator.createObjects();
		System.out.println("Created object size: " + createdObjects.size());

		System.out.println("Serializing objects...\n\n");
		Document doc = Serializer.serialize(createdObjects);
		System.out.println("Document: ");
		System.out.println(doc.toString());
		XMLOutputter myXMLOutput = new XMLOutputter();
		myXMLOutput.setFormat(Format.getPrettyFormat());
		System.out.println(myXMLOutput.outputString(doc));
		
	}

}
