package csp_assignmnet1;

import java.util.ArrayList;
import java.util.TreeSet;

public class CBJ extends Algorithm {
	protected ArrayList<TreeSet<Integer>> conflictSet;

	public CBJ(int numberOfVariables) {

		conflictSet = new ArrayList<TreeSet<Integer>>(numberOfVariables);
		for (int i = 0; i < numberOfVariables; i++) {
			TreeSet<Integer> newTreeSet = new TreeSet<Integer>();
			conflictSet.add(newTreeSet);

		}
	}

	@Override
	protected int label(int index, Problem problem) {
		consistent = false;
		Variable currentVar = problem.variables[index];
		int i;
		for (i = 0; (i < currentVar.domain.length) && (!consistent); i++) {
			if (currentVar.domain[i] == Problem.CONSONANT) {
				consistent = true;
				int j;
				for (j = 0; (j < index) && (consistent); j++) {
					Problem.constraintCheckCounter++;
					consistent = problem.check(index, i, j,
							problem.variables[j].currentValue);
				}
				if (!consistent) {
					TreeSet<Integer> currentTreeSet = this.conflictSet
							.get(index);
					currentTreeSet.add(j - 1);
					currentVar.removeValue(i);
				}

			}
		}
		if (consistent) {
			// i-1 because when the loop exits it increments i again, giving it
			// an out of bounds value
			currentVar.currentValue = i - 1;
			return index + 1;
		} else {
			return index;
		}

	}

	@Override
	protected int unlabel(int index, Problem problem) {
		if (index == Problem.CONSONANT) {
			return (index - 1);
		} else {
			TreeSet<Integer> currentTreeSet = this.conflictSet.get(index);
			int h = currentTreeSet.pollLast();
			this.conflictSet.get(h).addAll(this.conflictSet.get(index));

			for (int i = h + 1; i <= index; i++) {
				this.conflictSet.get(i).clear();
				problem.variables[i].restoreDomain();
			}
			int hIndex = problem.variables[h].currentValue;
			problem.variables[h].removeValue(hIndex);

			consistent = !problem.variables[h].isEmpty();
			return h;
		}
	}
}
