package csp_assignmnet1;

public class Variable {

	protected int[] domain;
	protected int currentValue;
	protected int numberOfValues;

	/**
	 * 
	 * @param domain
	 *            - the number of values in var
	 */
	public Variable(int domain) {
		this.numberOfValues = domain;
		this.currentValue = -1;
		this.domain = new int[domain];
		for (int i = 0; i < domain; i++) {
			this.domain[i] = Problem.CONSONANT;
		}
	}

	public Variable(Variable var) {
		this.numberOfValues = var.domain.length;
		this.currentValue = var.currentValue;
		this.domain = new int[var.domain.length];
		for (int i = 0; i < domain.length; i++) {
			this.domain[i] = var.domain[i];
		}
	}

	public void restoreDomain() {
		for (int i = 0; i < domain.length; i++) {
			this.domain[i] = Problem.CONSONANT;
		}
		this.numberOfValues = domain.length;
		this.currentValue = -1;
	}

	public void removeValue(int i) {
		if (this.domain[i] == Problem.CONSONANT) {
			this.domain[i] = Problem.UNAVAILABLE;
			this.numberOfValues--;
		}
	}

	public void restoreValue(int i) {
		if (this.domain[i] == Problem.UNAVAILABLE) {
			this.domain[i] = Problem.CONSONANT;
			this.numberOfValues++;
		}
	}

	public boolean isEmpty() {
		return (this.numberOfValues == 0);
	}

	public void printCurrentDomain() {
		System.out.print("[ ");
		for (int i = 0; i < this.domain.length; i++) {
			if (this.domain[i] == Problem.CONSONANT)
				System.out.print(i + ",");
		}
		System.out.println("]");

	}

}
