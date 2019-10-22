import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

/**
 * this class realize a keyboard reading and command processing
 * @author whatislove118
 */
public class CommandReader {
	Olders arg;
	/**
	 * object of the class Collection
	 */
	private  CollectionsOlders collections;

	public CommandReader() {
		CollectionFileScanner s = new CollectionFileScanner();
		this.collections = s.readFile(CollectionFileScanner.getFilePath());
		MyShutDownHook mshtd = new MyShutDownHook();
		Runtime.getRuntime().addShutdownHook(mshtd);
	}
	/**
	 * this method perfom reading and parsing a command
	 * @return String[] fullcommand - array of the char of the command
	 * @throws NoSuchElementException - this method can throw this exception
	 */
	/**
	 * this method read and perfom command
	 */
	public void read(Messages m) {

			DatabaseConnection.connectionToDatabase(m);

	}
	}

	/**
	 * This class realize a trap that, when the program is interrupted, writes the collection objects to a file.
	 *
	 * @author whatislove118
	 */
	class MyShutDownHook extends Thread {
		@Override
		/**
		 * This method is executed after the completion of the main thread.
		 */
		public void run() {
			try {
				shutdown();
			} catch (IOException e) {
				System.exit(0);
			} catch (ParserConfigurationException e) {
				System.exit(0);
			}
		}

		/**
		 * This method writes collection objects to a file when an emergency program ends.
		 *
		 * @throws IOException                  - this method throws IOException if programm can't write objects into the file
		 * @throws ParserConfigurationException if programm had problem in parsing from json
		 */
		public void shutdown() throws IOException, ParserConfigurationException {
			System.out.println("Программа завершила работу !");
		}

	}

