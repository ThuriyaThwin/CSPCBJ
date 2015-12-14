package csp_assignmnet1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

public class MAC extends Algorithm {

	protected ArrayList<ArrayList<LinkedList<Pair>>>	supportList;
	protected int[][][]																supportCounters;
	protected LinkedList<Pair>												unsupportedPairs;
	protected ArrayList<Stack<HashSet<Integer>>>			reductions;
	protected ArrayList<Stack<Integer>>								futureFC;
	protected ArrayList<LinkedList<Pair>>							acChangeList;

	public MAC(int numOfVariables, int sizeOfDomain, Problem problem) {
		// initialize AC4
		this.unsupportedPairs = new LinkedList<Pair>();
		this.supportList = new ArrayList<ArrayList<LinkedList<Pair>>>();
		this.acChangeList = new ArrayList<LinkedList<Pair>>();
		this.supportCounters = new int[numOfVariables][sizeOfDomain][numOfVariables];
		for (int i = 0; i < numOfVariables; i++) {
			this.acChangeList.add(new LinkedList<Pair>());
			this.supportList.add(new ArrayList<LinkedList<Pair>>());

			for (int j = 0; j < sizeOfDomain; j++) {
				this.supportList.get(i).add(new LinkedList<Pair>());
				for (int k = 0; k < numOfVariables; k++) {
					this.supportCounters[i][j][k] = -1;
				}
			}
		}
		// initialize FC
		this.reductions = new ArrayList<Stack<HashSet<Integer>>>(numOfVariables);
		this.futureFC = new ArrayList<Stack<Integer>>(numOfVariables);
		for (int i = 0; i < numOfVariables; i++) {
			this.futureFC.add(new Stack<Integer>());
			Stack<HashSet<Integer>> newStack = new Stack<HashSet<Integer>>();
			this.reductions.add(newStack);
		}
		// run one cycle of AC4
		this.enforceConsistency(problem);
	}

	public void enforceConsistency(Problem problem) {
		int numOfVariables = problem.variables.length;
		int domainSize = problem.variables[0].domain.length;

		for (int i = 0; i < numOfVariables; i++) {
			for (int j = i + 1; j < numOfVariables; j++) {
				if (problem.constraintVerifier[i][j] == Problem.OCCUR) {
					Variable var1 = problem.variables[i];
					Variable var2 = problem.variables[j];

					for (int b = 0; b < domainSize; b++) {
						if (var1.domain[b] == Problem.CONSONANT) {
							int total = 0;

							for (int c = 0; c < domainSize; c++) {

								if (var2.domain[c] == Problem.CONSONANT) {
									Problem.constraintCheckCounter++;
									if (problem.constraintMatrix[i][j].check(b, c)) { // no conflict
										total++;
										this.supportList.get(j).get(c).add(new Pair(i, b));
									}
								}
							} // end for of l

							this.supportCounters[i][b][j] = total;

						}
					}
				}// end if
			}
		}
		// get all unsupported variables
		for (int i = 0; i < numOfVariables; i++) {
			for (int b = 0; b < domainSize; b++) {
				for (int j = i + 1; j < numOfVariables; j++) {
					if (this.supportCounters[i][b][j] == 0) {
						this.unsupportedPairs.add(new Pair(i, b));
					}
				}
			}
		}

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

	@Override
	protected int label(int index, Problem problem) {

		consistent = false;
		Variable currentVar = problem.variables[index];
		int numberOfVariables = problem.variables.length;
		int i;
		for (i = 0; (i < currentVar.domain.length) && (!consistent); i++) {
			if (currentVar.domain[i] == Problem.CONSONANT) {

				consistent = true;
				// Go forward
				for (int j = index + 1; (j < numberOfVariables) && (consistent); j++) {
					Problem.constraintCheckCounter++;
					consistent = checkForward(index, i, j, problem);
				}
				if (!consistent) {
					undoReductions(index, problem);
					currentVar.removeValue(i);
				} else {
					// if we think the value is consistent, we MAC the index+1 problem.

					// initialize reductionList with an empty HashList
					ArrayList<HashSet<Integer>> reductionList = new ArrayList<HashSet<Integer>>();
					for (int j = 0; j < numberOfVariables; j++) {
						reductionList.add(new HashSet<Integer>());
					}
					// pour all reductions to the appropriate cell in reductionList
					while (!this.futureFC.get(index).isEmpty()) {
						int variableIndex = this.futureFC.get(index).pop();
						reductionList.get(variableIndex).addAll(this.reductions.get(variableIndex).pop());
						// var, val pair that was deleted , is added to unsapported pairs for AC
						for (int value : reductionList.get(variableIndex)) {
							this.unsupportedPairs.add(new Pair(variableIndex, value));
						}
					}
					// empty unsupported variable list and update counter
					// (possibly deleting values)

					while (!this.unsupportedPairs.isEmpty()) {
						Pair currentPair = this.unsupportedPairs.remove();
						if ((currentPair.var > index) && (problem.variables[currentPair.var].domain[currentPair.val] == Problem.CONSONANT)) {
							problem.variables[currentPair.var].removeValue(currentPair.val);

							for (Pair pair : this.supportList.get(currentPair.var).get(currentPair.val)) {

								this.supportCounters[pair.var][pair.val][currentPair.var]--;
								if (this.supportCounters[pair.var][pair.val][currentPair.var] == 0) {

									this.unsupportedPairs.add(pair);
									reductionList.get(pair.var).add(pair.val);
								}
							}

						}
					}

					for (int j = index + 1; j < numberOfVariables; j++) {
						if (!reductionList.get(j).isEmpty()) {
							consistent = (consistent && !problem.variables[j].isEmpty());
							this.futureFC.get(index).add(j);
							this.reductions.get(j).add(reductionList.get(j));
						}
					}

					if (!consistent) {
						undoReductions(index, problem);
						currentVar.removeValue(i);

					}

				}
			}

		}
		if (consistent) {
			// i-1 because when the loop exits it increments i again, giving it
			// an out of bounds value
			Problem.assignmentCounter++;
			currentVar.currentValue = i - 1;
			return index + 1;
		} else {

			return index;
		}

	}

	@Override
	protected int unlabel(int index, Problem problem) {
		// if we are un-labeling variable #0 the problem is not solvable !
		if (index == Problem.CONSONANT) {
			return (index - 1);
		} else {
			int h = index - 1;

			undoReductions(index, problem);
			updateCurrentDomain(index, problem);

			undoReductions(h, problem);

			// Remove value from domain
			int hIndex = problem.variables[h].currentValue;
			problem.variables[h].removeValue(hIndex);
			consistent = !problem.variables[h].isEmpty();
			return h;
		}
	}

	private void updateCurrentDomain(int index, Problem problem) {

		Variable var = problem.variables[index];
		var.restoreDomain();

		for (HashSet<Integer> reduction : this.reductions.get(index))
			for (int value : reduction)
				var.removeValue(value);

	}

	private void undoReductions(int index, Problem problem) {
		// restore the domain of all the future variables index ruined
		for (int j : this.futureFC.get(index)) {
			HashSet<Integer> reduction = this.reductions.get(j).pop();

			for (int value : reduction) {
				problem.variables[j].restoreValue(value);
				for (Pair pair : this.supportList.get(j).get(value)) {
					this.supportCounters[pair.var][pair.val][j]++;
				}
			}

		}
		// empty futereFC for index
		while (!this.futureFC.get(index).isEmpty())
			this.futureFC.get(index).pop();
	}

	private boolean checkForward(int var1Index, int var1Value, int var2Index, Problem problem) {
		HashSet<Integer> newReduction = new HashSet<Integer>();
		Variable var2 = problem.variables[var2Index];
		int j;
		// fill the reduction set ,with values conflicting with the current
		// assignment of var1
		for (j = 0; j < var2.domain.length; j++) {
			if (var2.domain[j] == Problem.CONSONANT) {
				Problem.constraintCheckCounter++;
				boolean conflict = problem.check(var1Index, var1Value, var2Index, j);
				if (!conflict) {
					newReduction.add(j);
				}
			}
		}// end for

		// remove the conflicting value in var2
		if (newReduction.size() != 0) {
			for (int value : newReduction)
				var2.removeValue(value);

			this.reductions.get(var2Index).push(newReduction);
			this.futureFC.get(var1Index).add(var2Index);
		}
		return (!var2.isEmpty());
	}
}
