package csp_assignmnet1;

public class BackTracking extends Algorithm {

	@Override
	protected int label(int index, Problem problem) {
		consistent = false;
		Variable currentVar = problem.variables[index];
		int i;
		for (i = 0; (i < currentVar.domain.length) && (!consistent); i++) {
			if (currentVar.domain[i] == Problem.CONSONANT) {
				consistent = true;
				for (int j = 0; (j < index) && (consistent); j++) {
					Problem.constraintCheckCounter++;
					consistent = problem.check(index, i, j,
							problem.variables[j].currentValue);
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
		if (index == Problem.CONSONANT) {
			return (index - 1);
		} else {
			int h = index - 1;
			problem.variables[index].restoreDomain();
			int hIndex = problem.variables[h].currentValue;
			problem.variables[h].removeValue(hIndex);
			consistent = !problem.variables[h].isEmpty();
			return h;
		}
	}
}
