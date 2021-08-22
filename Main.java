import java.util.Scanner;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		while (true) {
			System.out.println("Enter the name of the input file");
			Scanner scan = new Scanner(System.in);
			String fileName = scan.nextLine();
			DFA automata = new DFA();
			String[] tokens, stokens;
			try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					line = line.replaceAll("\\s+", " ");
					stokens = line.replaceFirst("^ ", "").split(" ");
					if (stokens[0].equals("Q")) {
						line = reader.readLine();
						line = line.replaceAll("\\s+", " ");
						tokens = line.replaceFirst("^ ", "").split(" ");
						automata.initState(tokens);
					} else if (stokens[0].equals("S")) {
						line = reader.readLine();
						line = line.replaceAll("\\s+", " ");
						tokens = line.replaceFirst("^ ", "").split(" ");
						automata.initAlphabet(tokens);
					} else if (stokens[0].equals("qo")) {
						line = reader.readLine();
						line = line.replaceAll("\\s+", " ");
						tokens = line.replaceFirst("^ ", "").split(" ");
						automata.setInitial(tokens[0]);
					} else if (stokens[0].equals("F")) {
						line = reader.readLine();
						line = line.replaceAll("\\s+", " ");
						tokens = line.replaceFirst("^ ", "").split(" ");
						automata.setFinal(tokens);
					} else if (stokens[0].equals("T")) {
						automata.createTransTable();
						for (int i = 0; i < automata.getNoStates(); i++) {
							line = reader.readLine();
							line = line.replaceAll("\\s+", " ");
							tokens = line.replaceFirst("^ ", "").split(" ");
							automata.setRowTT(tokens);
						}
					}
				}
			} catch (IOException ex) {
				System.err.format("IOException: " + ex.getMessage());
				System.out.println("\n\nPress enter to continue");
				scan.nextLine();
				continue;
			}
			System.out.println("The entered DFA is");
			automata.display();

			System.out.println("\n\nEnter 1 to display intermediate steps, else enter any other number");
			boolean debug = false;
			int inStep = scan.nextInt();
			if (inStep == 1)
				debug = true;
			DFAMinimizer minimizer = new DFAMinimizer(automata, debug);
			DFA min = minimizer.minimize(automata);
			System.out.println("The minimized DFA formed is ");
			System.out.println("-------------------------------------------------------------------------------");
			min.display();
			PrintStream stdout = System.out;
			try {
				System.setOut(
						new PrintStream(new BufferedOutputStream(new FileOutputStream("result_" + fileName)), true));
				min.display();
			} catch (FileNotFoundException e) {
				System.out.println("Can not create a file");
			}
			System.setOut(stdout);
			break;
		}
	}
}