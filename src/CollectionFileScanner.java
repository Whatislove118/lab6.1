import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class CollectionFileScanner {
	/** The class that parses the original file */
	private static final Pattern patternName = Pattern.compile("<Name>(.+?)</Name>");
	private static final Pattern patternNameBuilder = Pattern.compile("<NameBuilder>(.+?)</NameBuilder>");
	private static final Pattern patternID = Pattern.compile("<ID>(.+?)</ID>");
	private static final Pattern patternInstrumentValue = Pattern.compile("<InstrumentValue>(.+?)</InstrumentValue>");
	private static final Pattern patternDimension = Pattern.compile("<Dimension>(.+?)</Dimension>");
	private static String lastName=null;

	public static String getFilePath() {
		return filePath;
	}
	private static String filePath ="/Users/whatislove118/desktop/программирование/lab6.1/lab5.xml";
	//private static String filePath =System.getenv("FILE_PATH");
	private int countCheck = 0;
	/**
	 * This method searches for a substring by pattern
	 * @param str - substring
	 * @param p  - pattern
	 * @return true,false -depending on the search result
	 */
	public  static boolean getName(final String str,Pattern p) {
	   final Matcher matcher = p.matcher(str);
	    if (matcher.find()) {
	    	lastName=matcher.group(1);
	        return true;
	    }else {
	       return false;
	}
}

	/**
	 * This method parses the file and returns the object of the CollectionsOlders class, which already has a collection filled with information from the file.
	 * @return  CollectionsOlders - class CollectionsOlders object
	 * checkTAGOlders -check on the location of the program in the Olders class object
	 * checkName -  tag check (object name)
	 * checkTAGBuilder- checking for a program in the Builders class object
	 * checkID -  tag verification (object ID)
	 * checkInstruments - checking for a program in Instruments of the Builders class object
	 * Olders o -auxiliary object
	 * Builders b -auxiliary object
	 * CollectionsOlders c -the object of the class in which the collection is located
	 * fileName -file name
	 */
	 private File checkFile(String s) throws IOException  {
		 if( new File(s).exists()) {
			 return new  File(s);
		 }else {
			 File newFile = new File(s);
			 boolean created = newFile.createNewFile();
			 if(created) {
				 System.out.println("Файл не найден! Новый файл будет создан по указанному пути");
			 }
				 return newFile;
			 }
}
	public CollectionsOlders readFile(String filePath) {
		if(filePath ==null) {
			System.exit(0);
		}
		try {
			File file = checkFile(filePath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
		boolean checkTAGOlder = false;
		boolean checkName=false;
		boolean checkTAGBuilder=false;
		boolean checkID=false;
		boolean checkInstruments=false;
		Olders o=null;
		Builders b = null;
		ConcurrentSkipListSet<Olders> skipList = new ConcurrentSkipListSet<>();
		Stream<Olders> stream = skipList.stream();
		Path path = Paths.get(filePath);
		Scanner scanner = new Scanner(path);
		//Начинаем построчно считывать файл
		while(scanner.hasNext()) {
			String s = scanner.nextLine().trim();
			if(s.equals("<Older>")==true) {
				checkTAGOlder = true;
				continue;
			}if(checkTAGOlder == true & checkName==false) {
				if(CollectionFileScanner.getName(s, patternName)==true) {
					String[] sCheck = lastName.split("");
					for(String e : sCheck) {
						if(CollectionFileScanner.isNumeric(e)) {
							System.err.println("Имя старца не может содержать цифры! Проверье файл и исправьте ошибку");
							System.exit(0);
						}
					}
					o=new Olders(lastName);
					InsertInStream insertInStreamObject = (st,element)->{
						List<Olders> list = (List<Olders>) st.collect(Collectors.toList());
						list.add(element);
						return list.stream();
					};
					stream = insertInStreamObject.insertInStream(stream,o);
					lastName=null;
					checkName=true;
					continue;
				}
			}
			if(checkName==true & checkID ==false) {
					checkID=true;
			}
			if(checkID ==true & CollectionFileScanner.getName(s,patternID)==true) {
            	o.setId(Integer.parseInt(lastName));
			}

			if(checkTAGOlder == true & checkName == true & checkTAGBuilder == false & s.equals("<Builder>")==true) {
				checkTAGBuilder=true;
				continue;
			}
			if(checkTAGBuilder == true&checkInstruments==false) {
				checkInstruments=true;
			}

			if(s.equals("</Older>")==true) {
				checkName=false;
				checkTAGOlder=false;
				InsertInStream insertInStreamObject = (st,element)->{
					List<Olders> list = (List<Olders>) st.collect(Collectors.toList());
					list.add(element);
					return list.stream();
				};
				stream = insertInStreamObject.insertInStream(stream,o);
				o=null;
				continue;
			}	
		}
		CollectionsOlders collectionsOlders = new CollectionsOlders();
		collectionsOlders.setCollectionsOlders(stream.collect(Collectors.toCollection(()  -> new ConcurrentSkipListSet<Olders>())));
		return collectionsOlders;
		}catch(IOException e) {
			System.out.println("Произошла ошибка при чтении файла.");
			System.exit(0);
		}
		return null;
	}
	/**
	 * this method return a filename
	 * @return filename - name of file
	 */
	public static String getFileName() {
		return filePath;
	}
	/**
	 * this method set file name
	 * @param fileName - name of file
	 */
	public static void setFileName(String fileName) {
		CollectionFileScanner.filePath = fileName;
	}
	/**
	 * this method checks whether it is possible to convert a string to a number(checking whether the string is number)
	 * @param str check string
	 * @return true,false depending in the result
	 */
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    int d = Integer.parseInt(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	public int getCountCheck() {
		return countCheck;
	}
	public void setCountCheck(int countCheck) {
		this.countCheck = countCheck;
	}
	
}