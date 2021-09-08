import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class gradebook {

	// Terry Delaney
	// CS 337 Assignment 1
	
	public static File inputFile = new File("grades.txt");
	public static File outputFile = new File("grades2.txt");
	public static Scanner scr = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		
		// Display Menu
		
		menu(scr);
		
		// Close Files
		
		scr.close();

		// End Program
		
		return;
	}
	
	private static void menu(Scanner scr)
	{
		String response = null;
		
		do
		{
		
			System.out.println("Gradebook Main Menu:\n\n");
			System.out.println("To Add a Student, Enter 'addS <StudentName>'");
			System.out.println("To Add an Assignment, Enter 'addA'");
			System.out.println("To Add an Exam, Enter 'addE'");
			System.out.println("To Add All Grades for a Student, Enter 'grade <StudentName>'");
			System.out.println("To Add Grades for an Assignment, Enter 'gradeA <AssignmentNumber>'");
			System.out.println("To Add Grades for an Exam, Enter 'gradeE <ExamNumber>'");
			System.out.println("To Display all Grades, Enter 'display'");
			System.out.println("\nTo Quit, Enter 'q'");
			System.out.println("\n\n");
			System.out.println("Please enter your selection: ");
			
			response = scr.nextLine();
		
		String[] route = response.split(" ");
		
		switch (route[0])
		{
		case "addS":
		{
			if (route.length == 1)
			{
				System.out.println("\nERROR!! You did not enter a student's name!");
				System.out.println("Returning to main menu.\n\n");
				System.out.println("\n\n\n");
				break;
			}
			
			else if (route.length > 2)		// To re-combine student names, if necessary
			{
				for (int i = 2; i < route.length; i++)
					route[1] += " " + route [i];
			}
			
			System.out.println("\n");
			addStudent(route[1]);
			System.out.println("\n\n\n");
			
			break;
		}
		
		case "addA":
		{
			System.out.println("\n");
			addAssignment();
			System.out.println("\n\n\n");
			break;
		}
		
		case "addE":
		{
			System.out.println("\n");
			addExam();
			System.out.println("\n\n\n");
			break;
		}
		
		case "grade":
		{
			if (route.length == 1)
			{
				System.out.println("\nERROR!! You did not enter a student's name!");
				System.out.println("Returning to main menu.\n\n");
				break;
			}
			
			else if (route.length > 2)		// To re-combine student names, if necessary
			{
				for (int i = 2; i < route.length; i++)
					route[1] += " " + route [i];
			}
			
			System.out.println("\n");
			studentGrade(route[1]);
			System.out.println("\n\n\n");
			break;
		}
		
		case "gradeA":
		{
			if (route.length == 1)
			{
				System.out.println("\nERROR!! You did not enter an Assignment Number!");
				System.out.println("Returning to main menu.\n\n");
				break;
			}
			
			System.out.println("\n");
			assignmentGrade(route[1]);
			System.out.println("\n\n\n");
			break;
		}
		
		case "gradeE":
		{
			if (route.length == 1)
			{
				System.out.println("\nERROR!! You did not enter an Exam Number!");
				System.out.println("Returning to main menu.\n\n");
				break;
			}
			
			System.out.println("\n");
			examGrade(route[1]);
			System.out.println("\n\n\n");
			break;
		}
		
		case "display":
		{
			System.out.println("\n");
			displayAllGrades();
			System.out.println("\n\n\n");
			break;
		}
		
		case "q":
		{

			System.out.println("\nExiting the Gradebook, You Are.");
			System.out.println("May the Force be With You!\n\n");
			return;
		}
		
		default:
			System.out.println("\nYou have entered an invalid item.\n\nPlease make another selection.");
			System.out.println("\n\n\n");
			menu(scr);
		}
		
		} while (!response.equals("q"));
	}
	
	/* 
	 * Method addStudent
	 * Receives: Student Name, File Input Scanner, File Output PrintWriter
	 * Validates if student is already in file and if so, display an error message and return
	 * Adds a student to the roster and place zeroes for all existing grades
	 */
	
	private static void addStudent(String name)
	{
		Scanner inFile = openInputFile();
		PrintWriter outFile = openOutputFile();
		
		// Variable Declarations
		
		int exams = 0;
		int assignments = 0;
		String fileLine = null;
		String [] lineItems = null;
		
		// Determine number of Exams and Assignments
		
		if (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			outFile.println(fileLine);
			lineItems = fileLine.split(", ");
			exams = Integer.parseInt(lineItems[0]);
			assignments = Integer.parseInt(lineItems[1]);
		}
		
		// Search if Student Exists
		
		while (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			lineItems = fileLine.split(",");
			
			if (lineItems[0].equals(name))
			{
				System.out.println("The Student " + name + "already exists");
				return;
			}
			
			else
			{
				outFile.println(fileLine);
			}
		}
		
		// Add new Student
		
		fileLine = name + ", ";
		
		// Add zeroes for the existing exam grades
		
		for (int i = 0; i < exams; i++)
		{
			fileLine += "0, ";
		}
		
		fileLine += "|, ";		// Add separator between exams and assignments
		
		// Add zeroes for the existing assignment grades
		
		for (int i = 0; i < assignments; i++)
		{
			fileLine += "0, ";
		}
		
		outFile.println(fileLine.substring(0, (fileLine.length() - 2)));
		
		closeAndRenameFiles(inFile, outFile);
		
		// Return to calling method
		
		return;
	}
	
	/*
	 * Method addAssignment
	 * Receives File Input Scanner and File Output PrintWriter
	 * No validation
	 * Adds an assignment to all students and places a grade of zero for all students
	 */
	
	private static void addAssignment()
	{
		// Variable Declaration
		
		String fileLine = null;
		int assignments = 0;
				
		Scanner inFile = openInputFile();
		PrintWriter outFile = openOutputFile();
		
		// Read in the number of exams and assignments and increment the assignments by one
		
		if (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			assignments = Integer.parseInt(fileLine.substring(fileLine.length() - 1));
			assignments ++;
			
			outFile.println(fileLine.substring(0, (fileLine.length() - 1)) + Integer.toString(assignments));
			
			System.out.println("The new assignment has been created and is assignment number " + assignments + ".");
		}
		
		else	// If the file is empty, set the assignment counter to 1 and write to file
		{
			outFile.println("0, 1");
			return;
		}
		
		// Add the space for the new assignment to the student lines
		
		while (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			outFile.println(fileLine + ", 0");
		}
		
		closeAndRenameFiles(inFile, outFile);
		
		return;
	}
	
	/*
	 * Method addAssignment
	 * Receives File Input Scanner and File Output PrintWriter
	 * No validation
	 * Adds an exam to all students and places a grade of zero for all students
	 */
	
	private static void addExam()
	{
		Scanner inFile = openInputFile();
		PrintWriter outFile = openOutputFile();
		
		// Variable Declarations
		
		String fileLine = null;
		String[] lineItems = null;
		int exams = 0;
		int pipePosition = 0;
		
		// Read in number of exams and increment the exam count by one
		
		if (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			lineItems = fileLine.split(",");
			
			exams = Integer.parseInt(lineItems[0]);
			exams ++;
			
			outFile.println(Integer.toString(exams) + ", " + lineItems[1]);
			
			System.out.println("The new exam has been created and is exam number " + exams + ".");
		}
		
		else	// If input file is empty, set the exam counter to 1 and write to file
		{
			outFile.println("1, 0");
			return;
		}
		
		// Add space for the new exam in the student entries
		
		while (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			
			pipePosition = fileLine.indexOf("|");
			
			outFile.println(fileLine.substring(0, pipePosition) + "0, " + fileLine.substring(pipePosition));	
		}
		
		closeAndRenameFiles(inFile, outFile);
		
		return;
	}
	
	/*
	 * Method studentGrade
	 * Receives: Student's Name, File Reader Scanner, and Output File PrintWriter
	 * Validation: Student's Name is in file.  If student's name is not found, return an error
	 * 
	 * Locate a student in the file and then display each grade entry and provide options to change each grade
	 * 
	 */
	
	private static void studentGrade(String name)
	{
		Scanner inFile = openInputFile();
		PrintWriter outFile = openOutputFile();
		
		// Variable Declaration
		
		String fileLine = null;
		String[] lineItems = null;
		String resultLine = null;
		String newGrade = null;
		int exams = 0;
		boolean exam = true;
		boolean validEntry = true;
		
		if (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			lineItems = fileLine.split(", ");
			
			exams = Integer.parseInt(lineItems[0]);
			
			outFile.println(fileLine);
		}
		
		else	// File is empty
		{
			System.out.println("There are no entries in the gradebook.");
			System.out.println("\nReturning to main menu.");
			
			return;
		}
		
		while (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			
			if (fileLine.startsWith(name))
			{
				lineItems = fileLine.split(", ");
				
				/*
				 * Grades start at lineItems[1]
				 * 
				 * If the element is the pipe character, change from exams to assignments
				 */
				
				System.out.println("Modifying Grades for: " + name);
				System.out.println("\nStarting with Exams:\n");
				
				for (int i = 1; i < lineItems.length; i++)
				{
					if (lineItems[i].equals("|"))
					{
						exam = false;
						continue;
					}
					
					do
					{
						validEntry = true;		// Reset value to true to avoid repetitive looping due to an invalid entry.	
						
						System.out.print("The current grade for ");
						
						if (exam)
							System.out.print("Exam #" + i);
						
						else
							System.out.print("Assignment #" + (i - exams - 1));
						
						System.out.println(" is: " + lineItems[i]);
						System.out.print("\nPlease enter new grade or press 'Enter' to keep the same grade");
						
						newGrade = scr.nextLine();
						
						if (!newGrade.isEmpty())
						{
							if (Integer.parseInt(newGrade) < 0 || Integer.parseInt(newGrade) > 100)
							{
								System.out.println("You have entered an invalid grade.");
								System.out.println("\nPlease re-enter the grade.");
								newGrade = null;
								validEntry = false;
								continue;
							}
							
							lineItems[i] = newGrade;
							
							// Continue to next grade
						}
					}	while (validEntry == false);
				}
				
				for (int i = 0; i < lineItems.length - 1; i++)
				{
					outFile.print(lineItems[i] + ", ");
				}
				
				outFile.println(lineItems[lineItems.length - 1]); 
			}
			
			else
				outFile.println(fileLine);
		}
		
		closeAndRenameFiles(inFile, outFile);
		
		return;
	}
	
	/*
	 * Method assignmentGrade
	 * Receives Assignment Number, File Reading Scanner, Output File PrintWriter
	 * Validation - Determine if the exam number is in the appropriate range
	 * This method will receive a particular assignment number and then cycle through the entire student list
	 * and provide the user the current assignment grade for that student and then give the user the option to
	 * change that particular grade.
	 */
	
	private static void assignmentGrade(String assignment)
	{
		Scanner inFile = openInputFile();
		PrintWriter outFile = openOutputFile();
		
		// Variable Declarations
		
		String fileLine = null;
		String[] lineItems = null;
		String newGrade = null;
		int exams = 0;
		int assignments = 0;
		int assignNum = Integer.parseInt(assignment);
		boolean validEntry = true;
		
		if (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			lineItems = fileLine.split(", ");
			exams = Integer.parseInt(lineItems[0]);
			assignments = Integer.parseInt(lineItems[1]);
			
			// Assignment number validation
			
			if (assignNum <= 0 || assignNum > assignments)
			{
				System.out.println("You have entered an invalid assignment number.");
				System.out.println("\nReturning to the Main Menu.");
				return;
			}
			
			outFile.println(fileLine);
		}
		
		else	// File is empty
		{
			System.out.println("There are no entries in the gradebook.");
			System.out.println("\nReturning to the Main Menu.");
			return;
		}
		
		while (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			lineItems = fileLine.split(", ");
			
			do
			{			
				validEntry = true;		// To prevent infinite loop due to an invalid entry.
				
				System.out.println("For Student: " + lineItems[0]);
				System.out.println("The current grade for Assignment #" + assignNum + " is: " + lineItems[exams + 1 + assignNum]);
				System.out.print("\nPlease enter a new grade or press <enter> to keep the current grade: ");
				newGrade = scr.nextLine();

				// Entry Validation

				if (!newGrade.isEmpty())
				{
					if (Integer.parseInt(newGrade) < 0 || Integer.parseInt(newGrade) > 100)
					{
						System.out.println("You have entered an invalid grade.");
						System.out.println("\nPlease re-enter the grade.");
						newGrade = null;
						validEntry = false;
						continue;
					}
					
					lineItems[exams + 1 + assignNum] = newGrade;
				}
			}	while (validEntry == false); 
						
			newGrade = null;
			
			for (int i = 0; i < lineItems.length - 1; i++)
			{
				outFile.print(lineItems[i] + ", ");
			}
			
			outFile.println(lineItems[lineItems.length - 1]);
		}
		
		closeAndRenameFiles(inFile, outFile);
		
		return;
	}
	
	/*
	 * Method examGrade
	 * Receives Assignment Number, File Reading Scanner, Output File PrintWriter
	 * Validation - Determine if the exam number is in the appropriate range
	 * This method will receive a particular assignment number and then cycle through the entire student list
	 * and provide the user the current assignment grade for that student and then give the user the option to
	 * change that particular grade.
	 */
	
	private static void examGrade(String exam)
	{
		Scanner inFile = openInputFile();
		PrintWriter outFile = openOutputFile();
		
		// Variable Declarations
		
		String fileLine = null;
		String[] lineItems = null;
		String newGrade = null;
		int exams = 0;
		int examNum = Integer.parseInt(exam);
		boolean validEntry = true;

		if (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			lineItems = fileLine.split(", ");
			exams = Integer.parseInt(lineItems[0]);

			// Exam number validation

			if (examNum <= 0 || examNum > exams)
			{
				System.out.println("You have entered an invalid exam number.");
				System.out.println("\nReturning to the Main Menu.");
				return;
			}

			outFile.println(fileLine);
		}

		else	// File is empty
		{
			System.out.println("There are no entries in the gradebook.");
			System.out.println("\nReturning to the Main Menu.");
			return;
		}

		while (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			lineItems = fileLine.split(", ");

			do
			{			
				validEntry = true;
				
				System.out.println("For Student: " + lineItems[0]);
				System.out.println("The current grade for Exam #" + examNum + " is: " + lineItems[examNum]);
				System.out.print("\nPlease enter a new grade or press <enter> to keep the current grade: ");
				newGrade = scr.nextLine();

				// Entry Validation

				if (!newGrade.isEmpty())
				{
					if (Integer.parseInt(newGrade) < 0 || Integer.parseInt(newGrade) > 100)
					{
						System.out.println("You have entered an invalid grade.");
						System.out.println("\nPlease re-enter the grade.");
						newGrade = null;
						validEntry = false;
						continue;
					}
					
					lineItems[examNum] = newGrade;
				}
			}	while (validEntry == false); 

			newGrade = null;

			for (int i = 0; i < lineItems.length - 1; i++)
			{
				outFile.print(lineItems[i] + ", ");
			}
			
			outFile.println(lineItems[lineItems.length - 1]);
		}
		
		closeAndRenameFiles(inFile, outFile);
		
		return;
	}
	
	/*
	 * Method displayAllGrades
	 * Receives Scanner inFile
	 * No Validation
	 * This method will display all the grades in the gradebook along with calculating the overall grade for each student.
	 */
	
	private static void displayAllGrades()
	{
		Scanner inFile = openInputFile();
		
		// Variable Declaration
		
		String fileLine = null;
		String[] lineItems = null;
		int examAverage = 0;
		int assignmentAverage = 0;
		int overallAverage = 0;
		int assignmentCount = 0;
		int examCount = 0;
		
		if (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			lineItems = fileLine.split(", ");
			examCount = Integer.parseInt(lineItems[0]);
			assignmentCount = Integer.parseInt(lineItems[1].strip());
		}
		
		else	// Empty File
		{
			System.out.println("There are no entries in the gradebook.");
			System.out.println("\nReturning to the Main Menu.");
			return;
		}
		
		// Determine if there are no assignments and exams - If so, display message and exit
		
		if (examCount == 0 && assignmentCount == 0)
		{
			System.out.println("There are no grades entered.");
			System.out.println("Returning to Main Menu.");
			return;
		}
		
		// Display Header
		
		System.out.print("Student Name\t");
		
		// Determine if Exam Grades are needed
		
		if (examCount > 0)
			System.out.print("Exam Grades\t\t");
		
		// Determine if Assignment Grades are Needed
		
		if (assignmentCount > 0)
			System.out.print("Assignment Grades\t");
		
		System.out.println("Average Grade");
		
		while (inFile.hasNext())
		{
			fileLine = inFile.nextLine();
			lineItems = fileLine.split(", ");
			examAverage = 0;
			assignmentAverage = 0;
			
			System.out.print(lineItems[0]);
			System.out.print("\t");
			
			// Display Exam Grades
			
			for (int i = 1; i <= examCount; i++)
			{
				System.out.print(lineItems[i]);
				System.out.print("\t");
				examAverage += Integer.parseInt(lineItems[i]);
			}
			
			System.out.print("|\t");
			
			// Display Assignment Grades
			
			for (int i = examCount + 2; i < examCount + assignmentCount + 2; i++)
			{
				System.out.print(lineItems[i]);
				System.out.print("\t");
				assignmentAverage += Integer.parseInt(lineItems[i]);
			}
			
			examAverage = examAverage / examCount;
			assignmentAverage = assignmentAverage / assignmentCount;
			overallAverage = (examAverage + assignmentAverage) / 2;
			
			System.out.println("\t\t" + overallAverage);
		}
		
		inFile.close();
		
		return;
	}
	
	private static void closeAndRenameFiles(Scanner inFile, PrintWriter outFile)
	{
		inFile.close();
		outFile.close();
		inputFile.delete();
		outputFile.renameTo(inputFile);
		return;
	}
	
	private static Scanner openInputFile()
	{
				
		try
		{
			Scanner inFile = new Scanner(inputFile);	
			return inFile;
		}
		
		catch (FileNotFoundException e)
		{
			System.out.println("Input File Not Found!");
			System.out.println("Aborting program.");
			System.out.println(e);
			scr.close();
			return null;
		}
	}
	
	private static PrintWriter openOutputFile()
	{
				
		try
		{
			PrintWriter outFile = new PrintWriter(outputFile);	
			return outFile;
		}
		
		catch (FileNotFoundException e)
		{
			System.out.println("Output File Not Found!");
			System.out.println("Aborting program.");
			System.out.println(e);
			scr.close();
			return null;
		}
	}
}
