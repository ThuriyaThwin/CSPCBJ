package csp_assignmnet1;

public class BM extends Algorithm {
	protected int[][] mcl;
	protected int[] mbl;

	public BM(int numberOfVariables, int domainSize) {

		this.mcl = new int[numberOfVariables][domainSize];
		this.mbl = new int[numberOfVariables];

		for (int i = 0; i < numberOfVariables; i++) {
			this.mbl[i] = Problem.CONSONANT;
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
				if (!consistent)
					currentVar.removeValue(i);
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
		int h = index - 1;
		problem.variables[index].restoreDomain();
		this.mbl[index] = h;
		for (int i = h + 1; i < problem.variables.length; i++)
			this.mbl[i] = Math.min(this.mbl[i], h);
		// Remove value from domain
		int hIndex = problem.variables[h].currentValue;
		problem.variables[h].removeValue(hIndex);
		consistent = !problem.variables[h].isEmpty();
		return h;
	}
}
