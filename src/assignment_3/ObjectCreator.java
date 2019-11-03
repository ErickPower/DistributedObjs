package assignment_3;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ObjectCreator {
	
	private static Scanner in = new java.util.Scanner(System.in);
	
	public static ArrayList<Object> createObjects() {
		
		ArrayList<Object> myObjs = new ArrayList<Object>();
		
		int selection;
		printObjectMenu();
		
			
		//Get first object
		while(true) {
			try {
				selection = in.nextInt();
				
				if(selection > 0 && selection < 6) {
					break;
				} else if (selection == 0) {
					System.out.println("Need at least one object created!\nEnter a number between 1 and 5");
				} else {
					System.out.println("Enter a valid selection (0-5):");
				}
				
			}
			catch (InputMismatchException e) {
				System.out.println("Please enter an integer between 0 and 5:");
			}
			catch (Exception e) {
				System.err.println("Error with input.");
				e.printStackTrace();
				in.close();
				return null;
			}
		} 
		
		while(selection != 0) {
			if(selection == 1) {
				//create simplePrim
				myObjs.add(createSimplePrim());
			}
			else if (selection == 2) {
				//create arrayPrim
				myObjs.add(createArrayPrim());
			}
			else if (selection == 3) {
				//create simpleRef
				myObjs.add(createSimpleRef());
			}
			else if (selection == 4) {
				//create arrayRef
				myObjs.add(createArrayRef());
			}
			else if (selection == 5) {
				//create collectionRef
				myObjs.add(createCollectionRef());
			}
			else {
				//enter num between 0-5
				System.out.println("Enter a number between 0 and 5!");
			}
			
			try {
				//get next obj selection
				printObjectMenu();
				selection = in.nextInt();
				
			} catch (InputMismatchException e) {
				System.out.println("Please enter an integer between 0 and 5:");
			}
			catch (Exception e) {
				System.err.println("Error with input.");
				e.printStackTrace();
				in.close();
				return null;
			}
				
		}
		
		System.out.println("0 Chosen. Done creating objects.");
			
		in.close();
		return myObjs;
	}
	
	private static void printObjectMenu() {
		System.out.println("\nEnter a number for which type of object to create:\n");
		System.out.println("  0. Finish creating objects.");
		System.out.println("  1. int & double field.");
		System.out.println("  2. Array of ints.");
		System.out.println("  3. Reference to an object.");
		System.out.println("  4. Array of references to objects.");
		System.out.println("  5. ArrayList of object references.");
	}
	
	private static int getArrayOrListLength(String formatWord) {
		int len;
		
		System.out.println("Enter desired length for the " + formatWord + ":");
		while(true) {
			try {
				
				len = in.nextInt();
				while(len < 1) {
					System.out.println("Length must be minimum of 1 element!\nPlease enter a new length: ");
					len = in.nextInt();
				}
				break;
				
			} catch (InputMismatchException e) {
				System.out.println("Enter valid numbers!");
			} catch (Exception e) {
				System.err.println("Error in input! exiting...");
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		return len;
		
	}
	
	private static SimplePrimitive createSimplePrim() {
		System.out.println("\nCreating an object with an int field and double field...");
		
		int intParm;
		double doubParm;
		while(true) {
			try{
				
				System.out.println("Enter a whole number for the int field of the object: ");
				intParm = in.nextInt();
				
				System.out.println("Enter a floating point number for the double field of the object: ");
				doubParm = in.nextDouble();
				break;
				
			}catch (InputMismatchException e) {
				System.out.println("Enter valid numbers!");
			}catch (Exception e) {
				System.err.println("Error in input! exiting...");
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		
		return new SimplePrimitive(intParm, doubParm);
	}
	
	private static ArrayPrimitive createArrayPrim() {
		System.out.println("\nCreating an object with array of ints...");
		
		int len;
		int[] arrayInts;
		
		len = getArrayOrListLength("array");
		
		arrayInts = new int [len];
		
		for(int i = 0; i < len; i++) {
			try {
				
				System.out.println("Enter an integer for position " +(i+1) + " of the array: ");
				int nextVal = in.nextInt();
				arrayInts[i] = nextVal; 
				
			} catch (InputMismatchException e) {
				System.out.println("Enter a valid number!");
				i--;
			} catch (Exception e) {
				System.err.println("Error in input! exiting...");
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		return new ArrayPrimitive(arrayInts);
	}
	
	private static SimpleReference createSimpleRef() {
		System.out.println("\nCreating an object with an object reference field...");
		
		return new SimpleReference(createSimplePrim());
	}
	
	private static ArrayReference createArrayRef() {
		System.out.println("\nCreating an object with an array of object references...");
		int len;
		SimplePrimitive[] arrayOfObjs;
		
		len = getArrayOrListLength("array");
		
		arrayOfObjs = new SimplePrimitive[len];
		
		for(int i = 0; i < len; i++) {
			System.out.print("Object " + (i+1) + ": ");
			arrayOfObjs[i] = createSimplePrim();
		}
		
		return new ArrayReference(arrayOfObjs);
	}
	
	private static CollectionReference createCollectionRef() {
		System.out.println("\nCreating an object with an ArrayList of object references...");
		int len;
		int selection;
		ArrayList<MySuper> collectionOfObjs;
		
		len = getArrayOrListLength("ArrayList");
		
		collectionOfObjs = new ArrayList<MySuper>(len);
		
		for(int i = 0; i < len; i++) {
			try {
				System.out.println("\nWhat would you like object " + (i+1) + " to be?");
				System.out.println("  Enter 1 for int and double fields.\n  Enter 2 for array of ints field.");
				selection = in.nextInt();
				while(selection > 2 || selection < 1) {
					System.out.println("Invalid selection!\nPlease try again.\n");
					System.out.println("Enter 1 for int and double fields.\nEnter 2 for array of ints field.");
					selection = in.nextInt();
				}
				
				if(selection == 1) {
					//create simple primitive obj
					collectionOfObjs.add(createSimplePrim());
				}
				else {
					//create array primitive obj
					collectionOfObjs.add(createArrayPrim());
				}
				
			} catch (InputMismatchException e) {
				System.out.println("Enter a valid number!");
				i--;
			} catch (Exception e) {
				System.err.println("Error in input! exiting...");
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		return new CollectionReference(collectionOfObjs);
		
		
	}
}
