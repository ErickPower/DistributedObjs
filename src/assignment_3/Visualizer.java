package assignment_3;

import java.lang.reflect.*;
import java.io.*;

public class Visualizer {
	
	public static void inspect(Object obj) {
		Class c = null /* obj.getClass() */;
		inspectClass(c, obj, 0);
	}
	
	private static void inspectClass(Class c, Object obj, int depth) {
		try {
			//pass c as null when inspecting field objects
			if(c == null){
				c = obj.getClass();
			}
			
			//If the object is a class, then it is an interface, and no instance of the object
			if(obj.getClass().isInstance(Class.class)) {
				obj = null;
			}
			
			String tabs = new String(new char[depth]).replace("\0", "\t"); //Code from https://stackoverflow.com/a/4903603
			
/*************** DECLARING CLASS ******************************/
			System.out.println(tabs + c.getName());
			



/*************** FIELDS ******************************/
			
			Field[] fields = c.getDeclaredFields();
			if(fields.length > 0) {
				System.out.println(tabs + "Fields of " + c.getName());
			}
			
			for(int i = 0; i < fields.length; i++) {
				
/*************** modifiers ******************************/
				int fieldModifier = fields[i].getModifiers();
				System.out.print(tabs + " " + Modifier.toString(fieldModifier));
				
/*************** type ******************************/
				System.out.print(" " + fields[i].getType().getName());
				
/*************** name ******************************/
				System.out.print(" " + fields[i].getName());
				
/*************** value ******************************/
				if(obj != null) {
					fields[i].setAccessible(true);
					
					Object instance = fields[i].get(obj);
					if(instance != null) {
						
						boolean isObj = true;
						
						//Dealing with array
						if(instance.getClass().isArray()) {
							int length = Array.getLength(instance);
							System.out.print( " = [");
							for ( int k = 0; k < length; k++ ) {
								Object element = Array.get(instance, k);
								if(element == null || element.getClass().isPrimitive()) {
									System.out.print(element);
								}
								else {
									System.out.print(element.toString());
								}
								if(k < length-1) {
									System.out.print(",");
								}
							}
							System.out.println("]");
						}
						
						//dealing with objects
						else if(!fields[i].getType().isPrimitive()) {
							System.out.println(" = " + instance.getClass().getName()+ "@" + Integer.toHexString(System.identityHashCode(instance)));
							
						}
						
						//dealing with primitives
						else {
							System.out.println(" = " + instance);
							isObj = false;
						}
						
						if(isObj) {
							inspectClass(null, instance, depth+1);
						}
					}
					else {
						System.out.println(" = " + instance);
					}
				}
			}
		}
		catch (Exception e){
			System.out.println("Exception: " + e);
			e.printStackTrace();
		}
	}
}

