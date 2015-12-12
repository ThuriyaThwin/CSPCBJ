package csp_assignmnet1;

import java.io.IOException;

public class Main {
	private static final String usage = "usage: #variables #values #probability_for_a_constraint #probability_for_a_conflict";
	private static final String path = System.getProperty("user.dir")
			+ "/problems/";
	private static final String problem = ".problem";

	public static void main(String[] args) throws IOException {
		if (args.length != 4) {
			System.out.println(usage);
			System.exit(1);

		}
		int n = Integer.parseInt(args[0]);
		int d = Integer.parseInt(args[1]);
		double p1 = Double.parseDouble(args[2]);
		double p2 = Double.parseDouble(args[3]);

		for (int i = 0; i < 1; i++) {
			String fileName = path + i + problem;
			Problem newProblem = new Problem(fileName);
			FC fcSolver = new FC(newProblem.variables.length);
			fcSolver.solve(newProblem);
		}

	}
}
