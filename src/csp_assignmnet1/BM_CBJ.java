package csp_assignmnet1;

import java.util.ArrayList;
import java.util.TreeSet;

public class BM_CBJ extends Algorithm {
	protected ArrayList<TreeSet<Integer>> conflictSet;
	protected int[][] mcl;
	protected int[] mbl;

	public BM_CBJ(int numberOfVariables, int domainSize) {
		// Initialize data storages for conflictSet, mcl and mbl
		conflictSet = new ArrayList<TreeSet<Integer>>(numberOfVariables);
		this.mcl = new int[numberOfVariables][domainSize];
		this.mbl = new int[numberOfVariables];

		for (int i = 0; i < numberOfVariables; i++) {
			this.mbl[i] = Problem.CONSONANT;
			TreeSet<Integer> newTreeSet = new TreeSet<Integer>();
			conflictSet.add(newTreeSet);
			for (int j = 0; j < domainSize; j++) {
				this.mcl[i][j] = Problem.CONSONANT;
			}
		}
	}

	@Override
	protected int label(int index, Problem problem) {
		consistent = false;
		Variable currentVar = problem.variables[index];
		int i;
		for (i = 0; (i < currentVar.domain.length) && (!consistent); i++) {
			if (currentVar.domain[i] == Problem.CONSONANT) {
				consistent = (this.mcl[index][i] >= this.mbl[index]);

				for (int j = this.mbl[index]; (j < index) && (consistent); j++) {
					Problem.constraintCheckCounter++;
					consistent = problem.check(index, i, j,
							problem.variables[j].currentValue);
					this.mcl[index][i] = j;
				}
				if (!consistent) {
					TreeSet<Integer> currentTreeSet = this.conflictSet
							.get(index);
					currentTreeSet.add(mcl[index][i]);
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
			// CBJ get maximum from the conflict set and merge the conflict set
			// of h
			// with the conflict set for index
			TreeSet<Integer> currentTreeSet = this.conflictSet.get(index);
			// In case there is nothing in the conflict set, back track to the
			// previous variable
			Integer helper = currentTreeSet.pollLast();
			int h;
			if (helper == null)
				h = index - 1;
			else
				h = helper;
			this.conflictSet.get(h).addAll(this.conflictSet.get(index));

			// BM
			this.mbl[index] = h;
			for (int i = h + 1; i < problem.variables.length; i++)
				this.mbl[i] = Math.min(this.mbl[i], h);
			for (int i = h + 1; i <= index; i++) {
				this.conflictSet.get(i).clear();
				problem.variables[i].restoreDomain();
			}

			// Remove value from domain
			int hIndex = problem.variables[h].currentValue;
			problem.variables[h].removeValue(hIndex);
			consistent = !problem.variables[h].isEmpty();
			return h;
		}
	}
}
