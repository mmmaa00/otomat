public class DFA {
	public String[] state;
	public String[] alphabet;
	public int[][] transTable;
	public int qo;
	public int[] finalState;

	public DFA() {

	}

	public void initState(String[] states) {
		state = new String[states.length];
		for (int i = 0; i < states.length; i++) {
			state[i] = new String(states[i]);
		}
	}

	public void initAlphabet(String[] alpha) {
		alphabet = new String[alpha.length];
		for (int i = 0; i < alpha.length; i++) {
			alphabet[i] = new String(alpha[i]);
		}
	}

	public int getIndexOf(String st) {
		for (int i = 0; i < state.length; i++) {
			if (st.equals(state[i]))
				return i;
		}
		return -1;
	}

	public void setInitial(String st) {
		qo = this.getIndexOf(st);
	}

	public String getInitial() {
		return state[qo];
	}

	public void setFinal(String[] st) {
		finalState = new int[st.length];
		for (int i = 0; i < st.length; i++) {
			finalState[i] = getIndexOf(st[i]);
		}
	}

	public int getNoStates() {
		return state.length;
	}

	public int getAlphabetSize() {
		return alphabet.length;
	}

	public int getFinalSize() {
		return finalState.length;
	}

	public String getFinal(int index) {
		return state[finalState[index]];
	}

	public boolean checkFinal(String st) {
		for (int i = 0; i < finalState.length; i++) {
			if (this.getIndexOf(st) == finalState[i])
				return true;
		}
		return false;
	}

	public String getState(int index) {
		return state[index];
	}

	public String getSymbol(int index) {
		return alphabet[index];
	}

	public String[] getAlphabet() {
		String[] alpha = new String[alphabet.length];
		System.arraycopy(alphabet, 0, alpha, 0, alphabet.length);
		return alpha;
	}

	public String[] getStates() {
		String[] st = new String[state.length];
		System.arraycopy(state, 0, st, 0, state.length);
		return st;
	}

	public void createTransTable() {
		transTable = new int[state.length][alphabet.length];
	}

	public void setRowTT(String st[]) {
		int index = this.getIndexOf(st[0]);
		for (int i = 1; i < st.length; i++) {
			transTable[index][i - 1] = this.getIndexOf(st[i]);
		}
	}

	public int getSymbolIndex(String alpha) {
		for (int i = 0; i < alphabet.length; i++) {
			if (alpha.equals(alphabet[i]))
				return i;
		}
		return -1;
	}

	public int[][] getTransTable() {
		int[][] ttCopy;
		ttCopy = new int[transTable.length][transTable[0].length];
		for (int i = 0; i < transTable.length; i++) {
			System.arraycopy(transTable[i], 0, ttCopy[i], 0, transTable[i].length);
		}
		return ttCopy;
	}

	public String getTransition(String from, String symbol) {
		int fromIndex = this.getIndexOf(from);
		int symbolIndex = this.getSymbolIndex(symbol);
		return state[transTable[fromIndex][symbolIndex]];
	}

	public void display() {
		System.out.println("Q");
		for (int i = 0; i < state.length; i++) {
			System.out.print(state[i] + "	");
		}
		System.out.println();
		System.out.println("*****************************************************************************");
		System.out.println("Sigma");
		for (int i = 0; i < alphabet.length; i++) {
			System.out.print(alphabet[i] + "   ");
		}
		System.out.println();
		System.out.println("*****************************************************************************");
		System.out.println("qo");
		System.out.println(this.getInitial());
		System.out.println("*****************************************************************************");
		System.out.println("Final State");
		for (int i = 0; i < finalState.length; i++) {
			System.out.print(this.getFinal(i) + "   ");
		}
		System.out.println();
		System.out.println("*****************************************************************************");
		int largestLen = 0;
		for (int i = 0; i < state.length; i++) {
			if (state[i].length() > largestLen)
				largestLen = state[i].length();
		}
		largestLen = largestLen * -1;
		System.out.printf("%" + largestLen + "s ", "T");
		for (int i = 0; i < alphabet.length; i++) {
			System.out.printf("%" + largestLen + "s ", alphabet[i]);
		}
		System.out.println();
		for (int i = 0; i < transTable.length; i++) {
			System.out.printf("%" + largestLen + "s ", state[i]);
			for (int j = 0; j < transTable[i].length; j++) {
				System.out.printf("%" + largestLen + "s ", state[transTable[i][j]]);
			}
			System.out.println();
		}
	}
}
