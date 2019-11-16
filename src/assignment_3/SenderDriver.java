package assignment_3;

import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class SenderDriver {

	public static void main(String[] args) {
		Document doc;
		ArrayList<Object> createdObjects = ObjectCreator.createObjects();
		System.out.println("Created object size: " + createdObjects.size());

		System.out.println("Serializing objects...\n\n");
		doc = Serializer.serialize(createdObjects);
		
		System.out.println("Document to be sent: ");
		XMLOutputter myXMLOutput = new XMLOutputter();
		
		myXMLOutput.setFormat(Format.getPrettyFormat());
		System.out.println(myXMLOutput.outputString(doc));
		
		Sender.sendDoc(doc);

	}

}
