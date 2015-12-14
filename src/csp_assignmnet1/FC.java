package csp_assignmnet1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class FC extends Algorithm {

	protected ArrayList<Stack<HashSet<Integer>>> reductions;

	protected ArrayList<Stack<Integer>> futureFC;
	protected ArrayList<Stack<Integer>> pastFC;

	// Constructor
	public FC(int numberOfVariables) {
		this.reductions = new ArrayList<Stack<HashSet<Integer>>>(
				numberOfVariables);
		this.futureFC = new ArrayList<Stack<Integer>>(numberOfVariables);
		this.pastFC = new ArrayList<Stack<Integer>>(numberOfVariables);
		for (int i = 0; i < numberOfVariables; i++) {
			this.futureFC.add(new Stack<Integer>());
			this.pastFC.add(new Stack<Integer>());
			Stack<HashSet<Integer>> newStack = new Stack<HashSet<Integer>>();
			this.reductions.add(newStack);
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
			for (int value : reduction)
				problem.variables[j].restoreValue(value);
			this.pastFC.get(j).pop();
		}
		// empty futereFC for index
		while (!this.futureFC.get(index).isEmpty())
			this.futureFC.get(index).pop();
	}

	private boolean checkForward(int var1Index, int var1Value, int var2Index,
			Problem problem) {
		HashSet<Integer> newReduction = new HashSet<Integer>();
		Variable var2 = problem.variables[var2Index];
		int j;
		// fill the reduction set ,with values conflicting with the current
		// assignment of var1
		for (j = 0; j < var2.domain.length; j++) {
			if (var2.domain[j] == Problem.CONSONANT) {
				boolean conflict = problem.check(var1Index, var1Value,
						var2Index, j);
				if (!conflict)
					newReduction.add(j);
			}
		}// end for

		// remove the conflicting value in var2
		if (newReduction.size() != 0) {
			for (int value : newReduction)
				var2.removeValue(value);

			this.reductions.get(var2Index).push(newReduction);
			this.futureFC.get(var1Index).add(var2Index);
			this.pastFC.get(var2Index).add(var1Index);
		}
		return (!var2.isEmpty());
	}
}
