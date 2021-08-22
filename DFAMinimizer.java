import java.util.ArrayList;

public class DFAMinimizer {
	public String[][] table;
	boolean intStep;

	public DFAMinimizer(DFA automata, boolean in) {
		table = new String[automata.getNoStates() - 1][];
		for (int i = 0; i < automata.getNoStates() - 1; i++) {
			table[i] = new String[i + 1];
			for (int k = 0; k < i + 1; k++) {
				table[i][k] = new String("E");
			}
		}
		intStep = in;
	}

	public DFA minimize(DFA automata) {
		// mark each final and non final state, first mark the row corresponding to the
		// final state
		if (intStep)
			System.out.println("Marking pairs of final and non final states as distinguishable");
		for (int i = 0; i < automata.getFinalSize(); i++) {
			int row = automata.getIndexOf(automata.getFinal(i)) - 1;
			for (int col = 0; col < row + 1; col++) {
				if (!automata.checkFinal(automata.getState(col))) {
					table[row][col] = new String("X");
				}
			}
			// then mark the columns corresponding to the final state
			int col = automata.getIndexOf(automata.getFinal(i));
			for (row = col; row < table.length; row++) {
				if (!automata.checkFinal(automata.getState(row + 1))) {
					table[row][col] = new String("X");
				}
			}
		}
		// Start checking for states which are not in F or which are both in F
		if (intStep) {
			this.displayTable(automata);
			System.out.println("Now comparing the remaining pairs");
		}
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				// Process each non marked box
				if (!table[i][j].equals("X")) {
					// int flag = 0;
					if (intStep)
						System.out.println(
								"--------------------------------------------------------------------------------");
					for (int k = 0; k < automata.getAlphabetSize(); k++) {
						// Determine the transition of the both states (i+1 and j) on the input symbol k
						int trans1 = automata
								.getIndexOf(automata.getTransition(automata.getState(i + 1), automata.getSymbol(k)));
						int trans2 = automata
								.getIndexOf(automata.getTransition(automata.getState(j), automata.getSymbol(k)));
						if (intStep) {

							System.out.println("(" + automata.getState(i + 1) + "," + automata.getState(j)
									+ ") goes to (" + automata.getState(trans1) + "," + automata.getState(trans2)
									+ ") on symbol " + automata.getSymbol(k));
						}
						// if the output states are distinguishable, mark both the states as
						// distinguishable. Also mark all the states as distinguishable which are
						// associated with this state
						if (((trans1 < trans2) && (table[trans2 - 1][trans1].equals("X")))
								|| ((trans2 < trans1) && (table[trans1 - 1][trans2].equals("X")))) {
							String[] token = table[i][j].split(" ");
							if (intStep)
								System.out.println(
										"Output states are distinguishable, Marking (" + automata.getState(i + 1) + ","
												+ automata.getState(j) + ") as distinguishable");
							table[i][j] = new String("X");
							for (int p = 1; p < token.length; p++) {
								String[] coords = token[p].split(",");
								if (intStep)
									System.out.println("Also marking states "
											+ automata.getState(Integer.parseInt(coords[0]) + 1) + " and "
											+ automata.getState(Integer.parseInt(coords[1])) + " as distinguishable");
								table[Integer.parseInt(coords[0])][Integer.parseInt(coords[1])] = new String("X");
							}
						}
						// else add the list associated with the current pair and the current pair
						// itself to the list of output pair
						else if (trans1 < trans2) {
							String[] token = table[i][j].split(" ");
							table[trans2 - 1][trans1] = table[trans2 - 1][trans1] + " " + String.valueOf(i) + ","
									+ String.valueOf(j);
							if (intStep)
								System.out.println("The state pair (" + automata.getState(i + 1) + ","
										+ automata.getState(j) + ") depends on the state pair ("
										+ automata.getState(trans2) + "," + automata.getState(trans1) + ")");
							for (int p = 1; p < token.length; p++) {
								table[trans2 - 1][trans1] = table[trans2 - 1][trans1] + " " + token[p];
								if (intStep)
									System.out.println("Also added " + token[p]);
							}
						} else if (trans1 > trans2) {
							String[] token = table[i][j].split(" ");
							table[trans1 - 1][trans2] = table[trans1 - 1][trans2] + " " + String.valueOf(i) + ","
									+ String.valueOf(j);
							if (intStep)
								System.out.println("The state pair (" + automata.getState(i + 1) + ","
										+ automata.getState(j) + ") depends on the state pair ("
										+ automata.getState(trans1) + "," + automata.getState(trans2) + ")");
							for (int p = 1; p < token.length; p++) {
								table[trans1 - 1][trans2] = table[trans1 - 1][trans2] + " " + token[p];
								if (intStep)
									System.out.println("Also added " + token[p]);
							}
						}
					}
				}
			}
		}
		// Mark with O, all those states which don't have an X associated with them.
		if (intStep) {
			System.out.println();
			System.out.println("--------------------------------------------------------------------------------");
			System.out.println("Marking O those states which are not marked X");
		}
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (!table[i][j].equals("X"))
					table[i][j] = new String("O");
			}
		}
		this.displayTable(automata);
		// Enter the sum of index of second state and automata.number of states to the
		// state with higher index in the transition table just to act as a pointer to
		// the first stating that the second state is equivalent to the first one
		int[][] tTable = automata.getTransTable();
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j].equals("O")) {
					for (int k = 0; k < tTable[i + 1].length; k++) {
						tTable[i + 1][k] = automata.getNoStates() + j;
					}
				}
			}
		}

		DFA mautomata = new DFA();
		// Initialize the alphabet of the minimized automata
		mautomata.initAlphabet(automata.getAlphabet());
		String[] st = new String[automata.getNoStates()];
		st = automata.getStates();
		int rmvState = 0;// counter for counting the number of states to be removed
		// loop to remove the states and make corresponding pairs
		for (int i = st.length - 1; i >= 0; i--) {
			if ((tTable[i][0] - automata.getNoStates()) >= 0) {
				st[tTable[i][0] - automata.getNoStates()] = st[tTable[i][0] - automata.getNoStates()] + "," + st[i];
				st[i] = "-";
				rmvState++;
			}
		}
		// Create an array of the new states
		String[] mst = new String[automata.getNoStates() - rmvState];
		for (int i = 0, k = 0; i < st.length; i++) {
			if (!st[i].equals("-")) {
				mst[k] = st[i];
				k++;
			}
		}
		// Initialize the states of the minimized automata
		mautomata.initState(mst);
		int mqo = 0;// =automata.getIndexOf(automata.getInitial());
		// Loop to determine the initial state of the minimized automata
		for (int i = 0; i < mautomata.getNoStates(); i++) {
			int flag = 0;
			String[] stt = mautomata.getState(i).split(",");
			for (int j = 0; j < stt.length; j++) {
				if (automata.getInitial().equals(stt[j])) {
					mqo = i;
					flag = 1;
					break;
				}
			}
			if (flag == 1)
				break;
		}
		// Initialize the initial state of the minimized automata
		mautomata.setInitial(mautomata.getState(mqo));
		ArrayList<String> finalS = new ArrayList<String>();
		for (int i = 0; i < automata.getFinalSize(); i++) {
			for (int j = 0; j < mautomata.getNoStates(); j++) {
				int flag = 0;
				String[] stt = mautomata.getState(j).split(",");
				for (int k = 0; k < stt.length; k++) {
					if (automata.getFinal(i).equals(stt[k])) {
						finalS.add(mautomata.getState(j));
						flag = 1;
						break;
					}
				}
				if (flag == 1)
					break;
			}
		}
		String[] fState = finalS.toArray(new String[finalS.size()]);
		// Initialize the final states of the new automata
		mautomata.setFinal(fState);
		// Create the new Transition table
		mautomata.createTransTable();
		for (int i = 0, m = 0; i < tTable.length; i++) {
			String[] stt = new String[tTable[0].length + 1];
			if (tTable[i][0] - automata.getNoStates() < 0) {
				stt[0] = mautomata.getState(m);
				m++;
				for (int j = 0; j < tTable[i].length; j++) {
					String st2 = automata.getState(tTable[i][j]);
					for (int k = 0; k < mautomata.getNoStates(); k++) {
						int flag = 0;
						String[] stt2 = mautomata.getState(k).split(",");
						for (int l = 0; l < stt2.length; l++) {
							if (stt2[l].equals(st2)) {
								stt[j + 1] = mautomata.getState(k);
								flag = 1;
								break;
							}
						}
						if (flag == 1)
							break;
					}
				}
				mautomata.setRowTT(stt);
			}
		}
		System.out.println();
		return mautomata;
	}

	public void displayTable(DFA automata) {
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("The table formed is ");
		System.out.println();
		int largestLen = 0;
		for (int i = 0; i < automata.getNoStates(); i++) {
			if (automata.getState(i).length() > largestLen)
				largestLen = automata.getState(i).length();
		}
		largestLen = -1 * largestLen;
		for (int i = 0; i < table.length; i++) {
			System.out.printf("%" + largestLen + "s ", automata.getState(i + 1));
			for (int j = 0; j < table[i].length; j++) {
				System.out.printf("%" + largestLen + "s ", table[i][j]);
			}
			System.out.println();
		}
		System.out.printf("%" + largestLen + "s ", " ");
		for (int j = 0; j < automata.getNoStates() - 1; j++) {
			System.out.printf("%" + largestLen + "s ", automata.getState(j));
		}
		System.out.println();
		System.out.println("--------------------------------------------------------------------------------");
	}
}
