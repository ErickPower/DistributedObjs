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
			System.out.println("\n" + tabs + c.getName());
			



/*************** FIELDS ******************************/
			
			Field[] fields = c.getDeclaredFields();
			/*if(fields.length > 0) {
				System.out.println(tabs + "Fields of " + c.getName());
			}*/
			
			for(int i = 0; i < fields.length; i++) {
				
/*************** modifiers ******************************/
				int fieldModifier = fields[i].getModifiers();
				if(Modifier.isFinal(fieldModifier)) {
					continue;
				}
				if(!fields[i].trySetAccessible()) {
					continue;
				}
				System.out.print(tabs + "    " + Modifier.toString(fieldModifier));
				
/*************** type ******************************/
				System.out.print(" " + fields[i].getType().getName());
				
/*************** name ******************************/
				System.out.print(" " + fields[i].getName());
				
/*************** value ******************************/
				if(obj != null) {
					//fields[i].setAccessible(true);
					
					
					Object instance = fields[i].get(obj);
					if(instance != null) {
						
						
						
						//Dealing with array
						if(instance.getClass().isArray()) {
							int length = Array.getLength(instance);
							
							if(instance.getClass().getComponentType().getName().startsWith("assignment")) {
								System.out.print( " = \n" + tabs + "\t[");
							}
							else {
								System.out.print( " = [");
							}
							
							boolean isObj = false;
							for ( int k = 0; k < length; k++ ) {
								isObj = true;
								Object element = Array.get(instance, k);
								if(element == null) {
									continue;
								}
								else if(/*element == null ||*/ element.getClass().isPrimitive()) {
									System.out.print(element);
									isObj = false;
								}
								else {
									if(element.getClass().getName().startsWith("assignment")) {
										inspectClass(element.getClass(), element, depth+1);
									}
									else{
										System.out.print(element.toString());
										isObj = false;
									}
									
								}
								if(k < length-1) {
									if(isObj) {
										System.out.print(tabs + "\t" + ",");
									}
									else {
										System.out.print(",");
									}
									
								}
							}
							if(isObj) {
								System.out.println(tabs + "\t" + "]");
							}
							else {
								System.out.println("]");
							}
							//System.out.println("]");
						}
						
						//dealing with objects
						else if(!fields[i].getType().isPrimitive()) {
							inspectClass(instance.getClass(), instance, depth+1);
							//System.out.println(" = " + instance.getClass().getName()+ "@" + Integer.toHexString(System.identityHashCode(instance)));
							
						}
						
						//dealing with primitives
						else {
							System.out.println(" = " + instance);
						}
						
						/*if(isObj) {
							inspectClass(null, instance, depth+1);
						}*/
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

