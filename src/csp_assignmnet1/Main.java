package csp_assignmnet1;

import java.io.File;
import java.io.IOException;

public class Main {
	private static final String	usage		= "usage: #variables #values #probability_for_a_constraint #probability_for_a_conflict";
	private static final String	path		= System.getProperty("user.dir") + "/problems/";
	private static final String	problem	= ".problem";

	public static void main(String[] args) throws IOException {

		/* TODO Check where in ac4 there are more CCs ?@?!?!$?!@$!@ */
		File mySolutions = new File(System.getProperty("user.dir") + "/mySolution.txt");
		mySolutions.delete();
		for (int i = 0; i < 1000; i++) {
			String fileName = path + i + problem;
			Problem newProblem = new Problem(fileName);
			// newProblem.printVar();
			BM_CBJ newMAC = new BM_CBJ(newProblem.variables.length, newProblem.variables[0].domain.length);
			newMAC.solve(newProblem);
		}

	}
}
