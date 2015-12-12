package csp_assignmnet1;

public class Constraint {

	protected int[][] constraintDomain;

	public Constraint(int domain, double conflictProbability) {
		// initialize CC matrix
		this.constraintDomain = new int[domain][domain];
		for (int i = 0; i < domain; i++) {
			for (int j = 0; j < domain; j++) {
				this.constraintDomain[i][j] = Problem.CONSONANT;
			}
		}
		// iterate through the CC matrix and create conflicts between values
		// according to the conflict probability
		for (int i = 0; i < domain; i++) {
			for (int j = 0; j < domain; j++) {
				if (this.constraintDomain[i][j] != Problem.OCCUR) {
					double generatedProb = Math.random();
					if (generatedProb <= conflictProbability) {
						this.constraintDomain[i][j] = Problem.OCCUR;
					}
				}

			}
		}

	}

	// Empty Constructor , for reading from a file
	public Constraint(int domain) {
		// initialize CC matrix
		this.constraintDomain = new int[domain][domain];
		for (int i = 0; i < domain; i++) {
			for (int j = 0; j < domain; j++) {
				this.constraintDomain[i][j] = Problem.CONSONANT;
			}
		}
	}

	public void setConflict(int value1, int value2) {
		this.constraintDomain[value1][value2] = Problem.OCCUR;

	}

	public Constraint(Constraint con) {
		this.constraintDomain = new int[con.constraintDomain.length][con.constraintDomain.length];
		for (int i = 0; i < this.constraintDomain.length; i++) {
			for (int j = 0; j < this.constraintDomain.length; j++) {
				this.constraintDomain[i][j] = con.constraintDomain[i][j];
			}
		}
	}

	public boolean check(int value1, int value2) {

		return (constraintDomain[value1][value2] == Problem.CONSONANT);
	}

	public void print() {
		for (int i = 0; i < this.constraintDomain.length; i++) {
			for (int j = i; j < this.constraintDomain.length; j++) {
				if (this.constraintDomain[i][j] == Problem.OCCUR)
					System.out.print("(" + i + "," + j + ")");
			}
		}
		System.out.println();
	}

	public void transpose() {
		int domain = this.constraintDomain.length;
		int[][] newMatrix = new int[domain][domain];
		for (int i = 0; i < domain; i++)
			for (int j = 0; j < domain; j++)
				newMatrix[j][i] = this.constraintDomain[i][j];
		this.constraintDomain = newMatrix;
	}
}
