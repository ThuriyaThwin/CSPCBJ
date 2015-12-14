package csp_assignmnet1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class Algorithm {

	private final int	unkown			= 5;
	private final int	solution		= 4;
	private final int	impossible	= 3;
	protected boolean	consistent;

	public void solve(Problem problem) throws IOException {
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
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX Status :" + status + " CCs:" + Problem.constraintCheckCounter + "  Assignmnets :"
				+ Problem.assignmentCounter + " XXXXXXXXXXXXXXXXXXXXXXXX");

		if (status == this.solution) {
			this.writeAnswerToFile(problem);

			Problem.assignmentCounter = 0;
			Problem.constraintCheckCounter = 0;

		} else {
			String filePathAndName = System.getProperty("user.dir") + "/mySolution.txt";
			FileWriter fileWriter = new FileWriter(filePathAndName, true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println("null");

			printWriter.close();

		}
		System.out.println();

	}

	private void writeAnswerToFile(Problem problem) throws IOException {
		String filePathAndName = System.getProperty("user.dir") + "/mySolution.txt";
		FileWriter fileWriter = new FileWriter(filePathAndName, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);

		printWriter.print("[");

		for (int i = 0; i < problem.variables.length; i++) {
			printWriter.print(problem.variables[i].currentValue);
			if (i + 1 != problem.variables.length) {
				printWriter.print(", ");

			}
		}
		printWriter.println("]");

		printWriter.close();

	}

	protected abstract int label(int index, Problem problem);

	protected abstract int unlabel(int index, Problem problem);
}
