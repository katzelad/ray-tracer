import java.io.*;

public class Parser {
	@SuppressWarnings("serial")
	public static class ParseException extends Exception {
		public ParseException(String msg) {
			super(msg);
		}
	}

	public Parser() {
	}

	public final void parse(Reader in) throws IOException, ParseException {
		BufferedReader r = new BufferedReader(in);
		String line, curobj = null;
		int lineNum = 0;
		startFile();

		while ((line = r.readLine()) != null) {
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#')) { // comment
				continue;
			} else if (line.charAt(line.length() - 1) == ':') { // new object;
				if (curobj != null)
					commit();
				curobj = line.substring(0, line.length() - 1).trim().toLowerCase();
				if (!addObject(curobj))
					reportError(String.format("Did not recognize object: %s (line %d)", curobj, lineNum));
			} else {
				int eqIndex = line.indexOf('=');
				if (eqIndex == -1) {
					reportError(String.format("Syntax error line %d: %s", lineNum, line));
					continue;
				}
				String name = line.substring(0, eqIndex).trim().toLowerCase();
				String[] args = line.substring(eqIndex + 1).trim().toLowerCase().split("\\s+");

				if (curobj == null) {
					reportError(String.format("parameter with no object %s (line %d)", name, lineNum));
					continue;
				}

				if (!setParameter(name, args))
					reportError(String.format("Did not recognize parameter: %s of object %s (line %d)", name, curobj,
							lineNum));
			}
		}
		if (curobj != null)
			commit();

		endFile();
	}

	// utility assertion.
	// use this for parameter validity checks.
	static void pAssert(Boolean v, String msg) throws ParseException {
		if (!v)
			throw new ParseException(msg);
	}

	///////////////////// override these methods in your implementation
	///////////////////// //////////////////

	public void startFile() {
		System.out.println("----------------");
	}

	public void endFile() throws ParseException {
		System.out.println("================");
	}

	// start a new object definition
	// return true if recognized
	public boolean addObject(String name) throws ParseException {
		System.out.println("OBJECT: " + name);
		return true;
	}

	// set a specific parameter for the current object
	// return true if recognized
	public boolean setParameter(String name, String[] args) throws ParseException {
		System.out.print("PARAM: " + name);
		for (String s : args)
			System.out.print(", " + s);
		System.out.println();
		return true;
	}

	// finish the parsing of the current object
	// here is the place to perform any validations over the parameters and
	// final initializations.
	public void commit() throws ParseException {

	}

	public void reportError(String err) {
		System.out.println("ERROR: " + err);
	}

}
