package assignment_3;
import java.io.*;
import java.util.*;

import org.jdom2.*;

import java.lang.reflect.*;

public class Serializer {
	
	private static Map<String, Object> serialized = new IdentityHashMap<String, Object>();
	private static Document doc;
	
	public static org.jdom2.Document serialize(Object obj){
		try {
			
			Element serialized = new Element("serialized");
			doc = new Document(serialized);
			
			if(obj.getClass().isArray()) {
				for(int i = 0; i < Array.getLength(obj); i++) {
					Object objToSerialize = Array.get(obj, i);
					serializeObject(objToSerialize);
				}
			}
			else if(obj instanceof Collection<?>) {
				Iterator<Object> objIter = ((Collection) obj).iterator();
				while(objIter.hasNext()) {
					serializeObject(objIter.next());
				}
			}
			else {
				serializeObject(obj);
			}
			
		}
		catch (Exception e) {
			System.err.println("Error in serializer...");
			e.printStackTrace();
			System.exit(-1);
		}
		
		return doc;

	}
	
	/**
	 * 
	 * @param obj The object to be serialized
	 * @return The element that represents the Object obj, serialized. Returns null if obj already serialized.
	 */
	private static Element serializeObject(Object obj) throws Exception {
		if(serialized.containsValue(obj)) {
			return null;
		}
		
		else {
			String id = Integer.toString(serialized.size());
			serialized.put(id, obj);
			
			Element objElement = new Element("object");
			objElement.setAttribute("class", obj.getClass().getName());
			objElement.setAttribute("id", id);
			
			//Handle if object is array
			if(obj.getClass().isArray()) {
				objElement.setAttribute("length", Integer.toString(Array.getLength(obj)) );
				
				//If the array is of primitive objects
				if(obj.getClass().getComponentType().isPrimitive()) {
					for(int i = 0; i < Array.getLength(obj); i++) {
						Element val = new Element("value");
						val.setText(Array.get(obj, i).toString() );
						objElement.addContent(val);
					}
		//			doc.getRootElement().addContent(objElement);///////////////////////////////////////////////////////////////
				}
				//Array is of non primitive objects
				else {
					for(int i = 0; i < Array.getLength(obj); i++) {
						Element ref = new Element("reference");
						Object referencedObject = Array.get(obj, i);
						if(!serialized.containsValue(referencedObject)) {
							String refID = Integer.toString(serialized.size());
							ref.setText(refID);
							objElement.addContent(ref);
					//		doc.getRootElement().addContent(objElement);///////////////////////////////////////////////////////////////
							serializeObject(referencedObject);
						}
						else {
							ref.setText(getObjID(referencedObject));
							objElement.addContent(ref);
							//doc.getRootElement().addContent(objElement);///////////////////////////////////////////////////////////////
						}
					}
				}
			}
			
			else if(obj instanceof Collection<?>) {
				Iterator<Object> objIter = ((Collection) obj).iterator();
				objElement.setAttribute("length", Integer.toString(((Collection) obj).size()));
				
				Object[] toSerialize = new Object [((Collection) obj).size()];
				int totalNonPrim = 0;
				
				while(objIter.hasNext()) {
					Object nextObj = objIter.next();
					if(nextObj.getClass().isPrimitive()) {
						Element val = new Element("value");
						val.setText(nextObj.toString());
						objElement.addContent(val);
					}
					else {
						Element ref = new Element("reference");
						if(!serialized.containsValue(nextObj)) {
							String refID = Integer.toString(serialized.size() + totalNonPrim);
							ref.setText(refID);
							objElement.addContent(ref);
							toSerialize[totalNonPrim] = nextObj;
							totalNonPrim++;
						
						}
						else {
							ref.setText(getObjID(nextObj));
							objElement.addContent(ref);
						}
					}
					//serializeObject(objIter.next());
				}
				
				//doc.getRootElement().addContent(objElement);///////////////////////////////////////////////////////////////
				for(Object objBeingSerialized : toSerialize) {
					serializeObject(objBeingSerialized);
				}
				
				//Serialize all the non primitives of the collection
			}
			
			//serialize fields of the object
			else {
				
				Field[] fields = obj.getClass().getDeclaredFields();
				for(Field f : fields) {
					objElement.addContent(serializeField(f, obj));
				}
				//doc.getRootElement().addContent(objElement);///////////////////////////////////////////////////////////////
			}
			
			doc.getRootElement().addContent(objElement);
			return objElement;
		}
		
	}
	
	/**
	 * 
	 * @param field The field to be serialized.
	 * @return The element that represents the field, serialized.
	 */
	private static Element serializeField(Field field, Object obj) throws Exception {
		Element fieldElement = new Element("field");
		Element valueElement;

		//field.setAccessible(true);
		if( !field.isAccessible() ) {
			field.setAccessible(true);
		}
		
		fieldElement.setAttribute("name", field.getName());
		fieldElement.setAttribute("declaringclass", field.getDeclaringClass().getName());
		
		//handling primitive field values
		if( field.getType().isPrimitive()) {
			valueElement = new Element("value");
			String valueString = field.get(obj).toString();
			valueElement.setText(valueString);
		}
		//handling object reference field values
		else /*if(!field.getClass().isArray())*/ {
			valueElement = new Element("reference");
			Object referencedObject = field.get(obj);
			if(!serialized.containsValue(referencedObject)) {
				valueElement.setText(Integer.toString(serialized.size()));
				serializeObject(referencedObject);
			}
			else {
				;//TODO object already serialized. get the ID of object.
			}
		}
		
		fieldElement.addContent(valueElement);
		return fieldElement;
	}
	
	private static String getObjID(Object obj) {
		if(!serialized.containsValue(obj)) {
			return "err";
		}
		else {
			return "xx";
		}
	}
}
