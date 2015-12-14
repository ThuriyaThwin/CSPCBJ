package csp_assignmnet1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Problem {
	// Fields
	protected Variable[]			variables;
	protected Constraint[][]	constraintMatrix;
	protected int[][]					constraintVerifier;
	protected static int			constraintCheckCounter;
	protected static int			assignmentCounter;

	public static final int		UNAVAILABLE	= -1;
	public static final int		OCCUR				= 1;
	public static final int		CONSONANT		= 0;
	public static final int		TEMPORARY		= 2;

	// Constructor
	public Problem(int n, int d, double p1, double p2) {
		constraintCheckCounter = 0;
		assignmentCounter = 0;
		this.variables = new Variable[n];
		this.constraintMatrix = new Constraint[n][n];
		this.constraintVerifier = new int[n][n];
		for (int i = 0; i < n; i++) {
			this.variables[i] = new Variable(d);
			for (int j = 0; j < n; j++) {
				this.constraintVerifier[i][j] = Problem.CONSONANT;
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				if ((this.constraintVerifier[i][j] == Problem.CONSONANT)) {
					double generatedRandom = Math.random();
					if (generatedRandom <= p1) {
						this.constraintMatrix[i][j] = new Constraint(d, p2);
						Constraint transposeCon = new Constraint(this.constraintMatrix[i][j]);
						transposeCon.transpose();
						this.constraintMatrix[j][i] = transposeCon;
						this.constraintVerifier[i][j] = Problem.OCCUR;
						this.constraintVerifier[j][i] = Problem.OCCUR;
					} else {

						// No need for [i][j] temporary because we already gone
						// through it
						this.constraintVerifier[j][i] = Problem.TEMPORARY;
					}
				}
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (this.constraintVerifier[i][j] == Problem.TEMPORARY) {
					this.constraintVerifier[i][j] = Problem.CONSONANT;
				}
			}
		}

	}

	public Problem(String fileName) throws IOException {

		Scanner scanner = new Scanner(new File(fileName));
		int n = scanner.nextInt();
		int d = scanner.nextInt();

		// -----------------------------------------------------------------
		constraintCheckCounter = 0;
		assignmentCounter = 0;
		this.variables = new Variable[n];
		this.constraintMatrix = new Constraint[n][n];
		this.constraintVerifier = new int[n][n];
		for (int i = 0; i < n; i++) {
			this.variables[i] = new Variable(d);
			for (int j = 0; j < n; j++) {
				this.constraintVerifier[i][j] = Problem.CONSONANT;
			}
		}
		while (scanner.hasNextInt()) {
			int var1 = scanner.nextInt();
			int var2 = scanner.nextInt();
			int val1 = scanner.nextInt();
			int val2 = scanner.nextInt();

			if (this.constraintVerifier[var1][var2] == Problem.CONSONANT) {
				this.constraintMatrix[var1][var2] = new Constraint(d);
				this.constraintVerifier[var1][var2] = Problem.OCCUR;
				this.constraintVerifier[var2][var1] = Problem.OCCUR;
			}

			this.constraintMatrix[var1][var2].setConflict(val1, val2);
			Constraint transposeCon = new Constraint(this.constraintMatrix[var1][var2]);
			transposeCon.transpose();
			this.constraintMatrix[var2][var1] = transposeCon;
		}

		scanner.close();
	}

	public Problem(Problem firstProblem) {
		assignmentCounter = 0;
		constraintCheckCounter = 0;
		int n = firstProblem.variables.length;
		this.variables = new Variable[n];
		this.constraintMatrix = new Constraint[n][n];
		this.constraintVerifier = new int[n][n];
		for (int i = 0; i < n; i++) {
			this.variables[i] = new Variable(firstProblem.variables[i]);
			for (int j = 0; j < n; j++) {
				this.constraintVerifier[i][j] = firstProblem.constraintVerifier[i][j];
				if (firstProblem.constraintVerifier[i][j] == OCCUR)
					this.constraintMatrix[i][j] = new Constraint(firstProblem.constraintMatrix[i][j]);
			}
		}
	}

	public boolean check(int var1, int val1, int var2, int val2) {
		boolean ans = true;

		if (this.constraintVerifier[var1][var2] == Problem.OCCUR)
			ans = this.constraintMatrix[var1][var2].check(val1, val2);
		return ans;
	}

	public void print() {
		for (int i = 0; i < this.constraintMatrix.length; i++) {
			for (int j = i; j < this.constraintMatrix.length; j++) {
				if (this.constraintVerifier[i][j] == Problem.OCCUR) {
					System.out.println("Constraint (" + i + "," + j + ")");
					this.constraintMatrix[i][j].print();
				}

			}
		}
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX   End XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX \n");
	}

	public void printVar() {
		System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		System.out.println();
		for (int i = 0; i < this.variables.length; i++) {
			System.out.println("X0X0X0X0X0X0 var " + i + "   X0X0X0X0X0X0");
			this.variables[i].printCurrentDomain();
		}
		System.out.println();
		System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
	}

}
