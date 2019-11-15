package assignment_3;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.lang.reflect.*;

import java.util.List;

public class Deserializer {

	public Object deserialize(org.jdom2.Document doc) {
		Element rootNode = doc.getRootElement();
		List<Element> objElems = rootNode.getChildren();
		Object[] objs = new Object[objElems.size()];
		
		createObjects(objElems, objs);
		
		populateFields(objElems, objs);
		
		for(Object obj : objs) {
			System.out.println("Object: " + obj.toString());
		}
		
		/*
		try {
			
				
				
				
				
				Field[] currFields = currClass.getDeclaredFields();
				
				for(Field f : currFields) {
					f.trySetAccessible();
					if(f.getClass().isArray()) {
						//f is array
					}else if(!f.getClass().isPrimitive()) {
						//f is not array, but is object
					}
					else {
						//f is primitive
						
						
					}
				}
				
				
				
				
			
		} catch (Exception e) {
			System.err.println("Error in getting objects from document.\nQuitting!");
			e.printStackTrace();
			System.exit(-1);
		}*/
		
		
		
		
		return objs;
	}
	
	private void createObjects(List<Element> objElems, Object[] objs) {
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
	
	private void populateFields(List<Element> objElems, Object[] objs) {
		try {
			for(Element elem : objElems) {
				Object currObj = objs[ elem.getAttribute("id").getIntValue() ];
				
				//objFields either value
				List<Element> objFields = elem.getChildren();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
