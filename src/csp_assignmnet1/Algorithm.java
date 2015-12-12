package csp_assignmnet1;

public abstract class Algorithm {

	private final int unkown = 5;
	private final int solution = 4;
	private final int impossible = 3;
	protected static boolean consistent;

	public void solve(Problem problem) {
		consistent = true;
		int status = this.unkown;
		int i = 0;
		while (status == this.unkown) {
			// label and un-label change the global variable, consistent
			if (consistent)
				i = label(i, problem);
			else
				i = unlabel(i, problem);
			// If we passed the last variable then we succeeded !
			if (i == problem.variables.length)
				status = this.solution;
			else if (i == -1)
				status = this.impossible;
		}
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX Status :" + status
				+ " CCs:" + Problem.constraintCheckCounter
				+ " XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		if (status == this.solution) {
			for (int j = 0; j < problem.variables.length; j++) {
				System.out.print("(" + j + "->"
						+ problem.variables[j].currentValue + ")");
			}

			Problem.assignmnetCounter = 0;
			Problem.constraintCheckCounter = 0;

		}
		System.out.println();

	}

	protected abstract int label(int index, Problem problem);

	protected abstract int unlabel(int index, Problem problem);
}
