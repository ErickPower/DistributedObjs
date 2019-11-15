package assignment_3;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class Deserializer {

	public static Object deserialize(org.jdom2.Document doc) {
		Element rootNode = doc.getRootElement();
		List<Element> objElems = rootNode.getChildren();
		Object[] objs = new Object[objElems.size()];
		
		createObjects(objElems, objs);
		
		populateFields(objElems, objs);
		
		/*
		for(Object obj : objs) {
			System.out.println("Object: " + obj.toString());
		}*/
		
		return objs;
	}
	
	private static void createObjects(List<Element> objElems, Object[] objs) {
		try {
			int id;
			for(Element elem : objElems) {
				Object currObj;
				Class currClass = Class.forName( elem.getAttributeValue("class") );
				id = elem.getAttribute("id").getIntValue();
				
				if(currClass.isArray()) {
					//object is array. get length from attributes, and type
					int arrayLen = elem.getAttribute("length").getIntValue();
					Class arrayType = currClass.getComponentType();
					
					currObj = Array.newInstance(arrayType, arrayLen);
					
					
				}
				else {
					//object is not array. get constructor, instantiate
					Constructor objCreator = currClass.getDeclaredConstructor(null);
					
					if(!Modifier.isPublic(objCreator.getModifiers())) {
						objCreator.setAccessible(true);
					}
					
					currObj = objCreator.newInstance((Object[])null);
					
				}
				
				objs[id] = currObj;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private static void populateFields(List<Element> objElems, Object[] objs) {
		try {
			int id;
			for(Element currElem : objElems) {
				id = currElem.getAttribute("id").getIntValue();
				Object currObj = objs[ id ];
				
				//objFields either value, field, or reference
				List<Element> objFields = currElem.getChildren();
				
				Class currClass = currObj.getClass();
				
				if(currClass.isArray()) {
					//set each element's value
					Class arrayType = currClass.getComponentType();
					
					for(int i = 0; i < objFields.size(); ++i) {
						Element arrayElemVal = objFields.get(i);
						
						Object arrayElemObj = deserializeElement(arrayElemVal, arrayType, objs);
						
						Array.set(currObj, i, arrayElemObj);
						
					}
					
					
				}
				else if(currClass == java.util.ArrayList.class) {
					//children are references
					Method listAdd = currClass.getMethod("add", Object.class);
					listAdd.trySetAccessible();
					
					//Add each reference element to the arrayList object, in order they are listed.
					for(Element refEl : objFields) {
						Object refObj = deserializeElement(refEl, currClass, objs);
						
						listAdd.invoke(currObj, refObj);
					}
					
					
					
				}
				else {
					//children are fields. fields can be reference or value
					for(Element fieldEl : objFields) {
						Class declaringClass = Class.forName( fieldEl.getAttributeValue("declaringclass") );
						String fieldName = fieldEl.getAttributeValue("name");
						
						Field field = declaringClass.getDeclaredField(fieldName);
						field.trySetAccessible();
						
						Class fieldType = field.getType();
						//Get the <value> or <reference> element of the field.
						Element fieldChild = fieldEl.getChildren().get(0);
						
						//deserialize the <value> or <reference> element.
						
						Object fieldObject = deserializeElement(fieldChild, fieldType, objs);
						
						
						field.set(currObj, fieldObject);
					}
					
				}
				objs[id] = currObj;
			}
			
		} catch (Exception e) {
			System.err.println("Error somewhere.\nQuitting!");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private static Object deserializeElement(Element el, Class elType, Object[] objs) {
		Object elementObject = null;
		
		String elementType = el.getName();
		
		if(elementType.equals("reference")) {
			elementObject = objs[Integer.parseInt(el.getText())];
		}
		else if(elementType.equals("value")) {
			elementObject = deserializePrim(el, elType);
		}
		else {
			//"field". handle somehow TODO
			
		}
		
		return elementObject;
	}
	
	/**
	 * 
	 * @param el The element to be deserialized.
	 * @param elType The type of el. Currently only handles int or double
	 * @return The value that el represents, as a elType wrapper object.
	 */
	private static Object deserializePrim(Element el, Class elType) {
		Object primitiveObject = null;
		
		if(elType.equals(int.class)) {
			primitiveObject = Integer.valueOf(el.getText());
		}
		else if (elType.equals(double.class)) {
			primitiveObject = Double.valueOf(el.getText());
		}
		
		return primitiveObject;
	}
	
}
