/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.*;
import org.jgap.gp.terminal.*;


public class BinaryClassification
extends GPProblem {

	//9 Attributes from the file - act as inputs 
	// 	-------KEY------------------------------------
	//	   a1. Clump Thickness               1 - 10
	//	   a2. Uniformity of Cell Size       1 - 10
	//	   a3. Uniformity of Cell Shape      1 - 10
	//	   a4. Marginal Adhesion             1 - 10
	//	   a5. Single Epithelial Cell Size   1 - 10
	//	   a6. Bare Nuclei                   1 - 10
	//	   a7. Bland Chromatin               1 - 10
	//	   a8. Normal Nucleoli               1 - 10
	//	   a9. Mitoses                       1 - 10
	public static Variable a1;
	public static Variable a2;
	public static Variable a3;
	public static Variable a4;
	public static Variable a5;
	public static Variable a6;
	public static Variable a7;
	public static Variable a8;
	public static Variable a9;
	//Inputs are the attributes from the file. 
	private static ArrayList<CancerPatient> inputs;
	//Expected classes: 2 or 4 from the instances
	private static ArrayList<Integer> correctOutputs;
	//Stores best program
	//Note: This is computed from the training set
	private static IGPProgram bestSolution; 
	/**
	 * Constructer
	 * @param a_conf - configuration of the GP
	 * @param filename - should take in breast-cancer-wisconsin.data file
	 * @throws InvalidConfigurationException
	 */
	public BinaryClassification(GPConfiguration a_conf, String trainingSet)
			throws InvalidConfigurationException {
		super(a_conf);
		//Read training set: to produce formula
		readData(trainingSet);
	}	

	/**
	 * Reads in the data from the file and stores them in fields
	 * - stores the attributes in the inputs arraylist
	 * - stores the outputs (Correct classes) in the outputs arraylist
	 * @param filename
	 */
	public static void readData(String filename){
		//Read in data and store it
		try {
			File file = new File(filename);
			Scanner scan = new Scanner(file);
			//create fields
			inputs = new ArrayList<CancerPatient>();
			correctOutputs = new ArrayList<Integer>();
			while (scan.hasNextLine()){
				Scanner line = new Scanner(scan.nextLine());
				line.useDelimiter(",");
				//Add 10 attributes to the array
				inputs.add(new CancerPatient(line.nextInt(), line.nextInt(), line.nextInt(), 
						line.nextInt(), line.nextInt(), line.nextInt(),line.nextInt(), 
						line.nextInt(), line.nextInt(), line.nextInt()));
				//The correct classes
				correctOutputs.add(line.nextInt());
			}
			scan.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * This method is used for setting up the commands and terminals that can be
	 * used to solve the problem.
	 * In this example an ADF (an automatically defined function) is used for
	 * demonstration purpuses. Using an ADF is optional. If you want to use one,
	 * care about the places marked with "ADF-relevant:" below. If you do not want
	 * to use an ADF, please remove the below places (and reduce the outer size of
	 * the arrays "types", "argTypes" and "nodeSets" to one).
	 * Please notice, that the variables types, argTypes and nodeSets correspond
	 * to each other: they have the same number of elements and the element at
	 * the i'th index of each variable corresponds to the i'th index of the other
	 * variables!
	 *
	 * @return GPGenotype
	 * @throws InvalidConfigurationException
	 */
	public GPGenotype create()
			throws InvalidConfigurationException {
		GPConfiguration conf = getGPConfiguration();
		// At first, we define the return type of the GP program.
		// ------------------------------------------------------
		Class[] types = {
				// Return type of result-producing chromosome
				CommandGene.IntegerClass,
				// ADF-relevant:
				// Return type of ADF 1
				CommandGene.IntegerClass};
		// Then, we define the arguments of the GP parts. Normally, only for ADF's
		// there is a specification here, otherwise it is empty as in first case.
		// -----------------------------------------------------------------------
		Class[][] argTypes = {
				// Arguments of result-producing chromosome: none
				{},
				// ADF-relevant:
				// Arguments of ADF1: all 3 are float
				{CommandGene.IntegerClass, CommandGene.IntegerClass, CommandGene.IntegerClass}
		};
		// Next, we define the set of available GP commands and terminals to use.
		// Please see package org.jgap.gp.function and org.jgap.gp.terminal
		// You can easily add commands and terminals of your own.
		// ----------------------------------------------------------------------
		CommandGene[][] nodeSets = { {
			// We use a variables that can be set in the fitness function.
			// These are the 9 attributes in the file, and are the inputs
			// Please refer to key that is near the fields at top of document
			// ----------------------------------------------------------
			a1 = Variable.create(conf, "A1", CommandGene.IntegerClass),
					a2 = Variable.create(conf, "A2", CommandGene.IntegerClass),
					a3 = Variable.create(conf, "A3", CommandGene.IntegerClass),
					a4 = Variable.create(conf, "A4", CommandGene.IntegerClass),
					a5 = Variable.create(conf, "A5", CommandGene.IntegerClass),
					a6 = Variable.create(conf, "A6", CommandGene.IntegerClass),
					a7 = Variable.create(conf, "A7", CommandGene.IntegerClass),
					a8 = Variable.create(conf, "A8", CommandGene.IntegerClass),
					a9 = Variable.create(conf, "A9", CommandGene.IntegerClass),
					new Multiply(conf, CommandGene.IntegerClass),
					//new Multiply3(conf, CommandGene.FloatClass),
					new Divide(conf, CommandGene.IntegerClass),
					//new Sine(conf, CommandGene.FloatClass),
					//new Exp(conf, CommandGene.FloatClass),
					new Subtract(conf, CommandGene.IntegerClass),
					new Add(conf, CommandGene.IntegerClass),
					//new Pow(conf, CommandGene.FloatClass),
					//new Terminal(conf, CommandGene.FloatClass, 2.0d, 10.0d, true),
					new Terminal(conf, CommandGene.IntegerClass,-10, 10, true),
		},
		// ADF-relevant:
		// and now the definition of ADF(1)
		{
			new Add3(conf, CommandGene.IntegerClass),
		}
		};

		// Create genotype with initial population. Here, we use the declarations
		// made above:
		// Use one result-producing chromosome (index 0) with return type float
		// (see types[0]), no argument (argTypes[0]) and several valid commands and
		// terminals (nodeSets[0]). Contained in the node set is an ADF at index 1
		// in the node set (as declared with the second parameter during
		// ADF-construction: new ADF(..,1,..)).
		// The ADF has return type float (types[1]), three input parameters of type
		// float (argTypes[1]) and exactly one function: Add3 (nodeSets[1]).
		// ------------------------------------------------------------------------
		return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodeSets,
				20, true);
	}

	/**
	 * Starts the example.
	 *
	 * @param args ignored
	 * @throws Exception
	 *
	 * @author Klaus Meffert
	 * @since 3.0
	 */
	public static void main(String[] args)
			throws Exception {
		//Reads in file 
		if (args.length > 2){
			System.out.println("Need to pass in training set  arguments");
			return;
		}
		// Setup the algorithm's parameters.
		// ---------------------------------
		GPConfiguration config = new GPConfiguration();
		config.setGPFitnessEvaluator(new DefaultGPFitnessEvaluator());
		config.setMaxInitDepth(6);
		config.setPopulationSize(200);
		config.setMaxCrossoverDepth(6); //diversity
		config.setFitnessFunction(new BinaryClassification.FormulaFitnessFunction());
		config.setStrictProgramCreation(true);
		//Print statements
		System.out.println("\nFormula to discover: X^4 + X^3 + X^2 - X");
		System.out.println("FINDING FORMULA FROM TRAINING SET:");
		//Training and test classification
		GPProblem problem = new BinaryClassification(config, args[0]);
		// Create the genotype of the problem, i.e., define the GP commands and
		// terminals that can be used, and constrain the structure of the GP
		// program.
		// --------------------------------------------------------------------
		GPGenotype gp = problem.create();
		gp.setVerboseOutput(true);
		// Start the computation with maximum 400 evolutions 
		// Termination criteria: 400 evolutions
		// --------------------------------------------------------------------
		gp.evolve(400);
		// Print the best solution so far to the console.
		// ----------------------------------------------
		gp.outputSolution(gp.getAllTimeBest());
		//Set the BEST solution so it can be run on the TEST solution
		bestSolution = gp.getAllTimeBest();
		//Read in test set: to see classification accuracy
		if (args.length != 2){
			System.out.println("Need to pass in test set  arguments for test classification accuracy");
			return;
		}
		//FIND TEST SET CLASSIFICATOIN ACCURACY
		classifyTestSet(args[1]);
	}

	/**
	 * Prints the classification accuracy of the best program on the test set
	 */
	public static void classifyTestSet(String filename){
		//No Solution equation found from the training set
		if (bestSolution == null){
			System.out.println("No solution has been found from the training set.");
			return;
		}
		//Read in test data
		readData(filename);
		System.out.println("\nFINDING TEST SET CLASSIFICATION ACCURACY:");

		//Input the 9 attributes into the BEST program/formula
		//Find the classification accuracy on the test set
		System.out.printf("classification accuracy: %.2f %% (2dp)",new BinaryClassification.FormulaFitnessFunction().computeRawFitness(bestSolution));
	}

	/**
	 * Fitness function for evaluating the produced fomulas, represented as GP
	 * programs. The fitness is computed by calculating the classfication accuracy. 
	 * Therefore the fitness value of 100 would mean a perfect 100% classification accuracy rate and 
	 * a prefect fitness value. 
	 */
	public static class FormulaFitnessFunction
	extends GPFitnessFunction {
		protected double evaluate(final IGPProgram a_subject) {
			return computeRawFitness(a_subject);
		}
		public double computeRawFitness(final IGPProgram ind) {
			double totalCorrect = 0.0f;
			Object[] noargs = new Object[0];
			for (int i = 0; i < inputs.size(); i++) {
				// Put in the inputs which are the 9 attributes
				// -------------------------------------------------------------
				a1.set(inputs.get(i).getA1());
				a2.set(inputs.get(i).getA2());
				a3.set(inputs.get(i).getA3());
				a4.set(inputs.get(i).getA4());
				a5.set(inputs.get(i).getA5());
				a6.set(inputs.get(i).getA6());
				a7.set(inputs.get(i).getA7());
				a8.set(inputs.get(i).getA8());
				a9.set(inputs.get(i).getA9());
				try {
					// Execute the GP program representing the function to be evolved.
					// As in method create(), the return type is declared as int (see
					// declaration of array "types").
					// ----------------------------------------------------------------
					int result = ind.execute_int(0, noargs);
					//Do the classification: 2 for benign, 4 for malignant)
					if (result >= 0){		
						//Benign
						result = 2; 
					} else if (result < 0){	
						//Malignant
						result = 4;
					}
					//Check if correct classification
					if (result == correctOutputs.get(i)){
						totalCorrect++;
					} 
				} catch (ArithmeticException ex) {
					// This should not happen, some illegal operation was executed.
					// ------------------------------------------------------------
					System.out.println("x = " + inputs.get(i).toString());
					System.out.println(ind);
					throw ex;
				}
			}
			//Return the classification percentage
			return (totalCorrect/(inputs.size()) * 100);
		}
	}
}
