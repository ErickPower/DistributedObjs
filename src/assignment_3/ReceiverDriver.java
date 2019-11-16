package assignment_3;

import java.lang.reflect.Array;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ReceiverDriver {

	public static void main(String[] args) {
		Document doc = Receiver.receiveDoc();
		
		
		System.out.println("Document received: ");
		XMLOutputter myXMLOutput = new XMLOutputter();
		
		myXMLOutput.setFormat(Format.getPrettyFormat());
		System.out.println(myXMLOutput.outputString(doc));
		
		
		Object deserializedObjs = Deserializer.deserialize(doc);
		
		if(deserializedObjs.getClass().isArray()) {
			int len = Array.getLength(deserializedObjs);
			for(int i = 0; i < len; ++i) {
				Object toDisplay = Array.get(deserializedObjs, i);
				if(toDisplay == null) {
					continue;
				}
				Visualizer.inspect( toDisplay );
				System.out.println("");
			}
		}
		else {
			Visualizer.inspect(deserializedObjs);
		}

	}

}
