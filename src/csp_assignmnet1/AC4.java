package csp_assignmnet1;

import java.util.ArrayList;
import java.util.LinkedList;

public class AC4 {
	protected ArrayList<ArrayList<LinkedList<Pair>>>	supportList;
	protected int[][][]																supportCounters;
	protected LinkedList<Pair>												unsupportedPairs;

	public AC4(int numOfVariables, int sizeOfDomain) {
		this.unsupportedPairs = new LinkedList<Pair>();
		this.supportList = new ArrayList<ArrayList<LinkedList<Pair>>>();
		this.supportCounters = new int[numOfVariables][sizeOfDomain][numOfVariables];
		for (int i = 0; i < numOfVariables; i++) {
			this.supportList.add(new ArrayList<LinkedList<Pair>>());

			for (int j = 0; j < sizeOfDomain; j++) {
				this.supportList.get(i).add(new LinkedList<Pair>());
				for (int k = 0; k < numOfVariables; k++) {
					this.supportCounters[i][j][k] = -1; // fixme temporary
				}
			}
		}
	}

	public void enforceConsistency(Problem problem) {
		int numOfVariables = problem.variables.length;
		int domainSize = problem.variables[0].domain.length;

		for (int i = 0; i < numOfVariables; i++) {
			for (int j = i + 1; j < numOfVariables; j++) {
				if (problem.constraintVerifier[i][j] == Problem.OCCUR) {
					Variable var1 = problem.variables[i];
					Variable var2 = problem.variables[j];

					for (int k = 0; k < domainSize; k++) {
						if (var1.domain[k] == Problem.CONSONANT) {
							int total = 0;

							for (int l = 0; l < domainSize; l++) {
								if (var2.domain[l] == Problem.CONSONANT) {
									if (problem.constraintMatrix[i][j].check(k, l)) { // no conflict
										total++;
										this.supportList.get(j).get(l).add(new Pair(i, k));
									}
								}
							} // end for of l

							this.supportCounters[i][k][j] = total;
							System.out.println("[(" + i + ", " + j + "), " + k + "] = " + total);
						}
					}
				}// end if
			}
		}

		// get all unsupported variables
		for (int i = 0; i < numOfVariables; i++) {
			for (int j = 0; j < domainSize; j++) {
				for (int k = 0; k < numOfVariables; k++) {
					if (this.supportCounters[i][j][k] == 0) {
						this.unsupportedPairs.add(new Pair(i, j));
					}
				}
			}
		}

		System.out.println(this.unsupportedPairs);

		// empty unsupported variable list and update counter (possibly deleting
		// values)
		while (!this.unsupportedPairs.isEmpty()) {
			Pair currentPair = this.unsupportedPairs.remove();

			problem.variables[currentPair.var].removeValue(currentPair.val);

			for (Pair pair : this.supportList.get(currentPair.var).get(currentPair.val)) {
				this.supportCounters[pair.var][pair.val][currentPair.var]--;
				if (this.supportCounters[pair.var][pair.val][currentPair.var] == 0) {
					this.unsupportedPairs.add(pair);
				}
			}
		}
	}

	public void updateConsistency(Problem problem, Pair problemPair) {
		this.unsupportedPairs.add(problemPair);

		while (!this.unsupportedPairs.isEmpty()) {
			Pair currentPair = this.unsupportedPairs.remove();

			problem.variables[currentPair.var].removeValue(currentPair.val);
			for (Pair pair : this.supportList.get(currentPair.var).get(currentPair.val)) {
				this.supportCounters[pair.var][pair.val][currentPair.var]--;
				if (this.supportCounters[pair.var][pair.val][currentPair.var] == 0) {
					this.unsupportedPairs.add(pair);
				}
			}
		}
	}
}
