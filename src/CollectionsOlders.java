import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.google.gson.*;
/**
 *A class containing a collection and methods with which you can interact with the collection or obtain information about it. * @author whatislove118
 *  CollectionsOlders collectionsOlders - the collection itself
 */
public class CollectionsOlders implements Serializable {

	public CollectionsOlders(){
		this.intDate=new Date();
	}

	/**
		 * collectionsOlders - the collection itself
		 */
		private ConcurrentSkipListSet<Olders> collectionsOlders = new ConcurrentSkipListSet<Olders>();
		/**
		 * containerOfInstruments - ArrayList which contained a value of instruments
		 */
		private CopyOnWriteArrayList<Instruments> containerOfInstruments = new CopyOnWriteArrayList<Instruments>();
		/**
		 * * Date intDate  - parameter containing the collection initialization time
		 */
		private Date intDate;
		/**
		 * Method that returns the maximum (as you compare) element of the collection
		 * @return Olders o- maximum array element
		 */

		/**
		 * Method that returns a collection of Olders objects
		 * @return HashSet c - HashSet type collection (with generic type Olders)
		 */
		public ConcurrentSkipListSet<Olders> getCollectionsOlders(){
			return this.collectionsOlders;
		}
		/**
		 * Method that returns a collection of objects in the Instruments class
		 * @return ArrayList i - ArrayList type collection (with generic type Instrument)
		 */
		public CopyOnWriteArrayList<Instruments> getArrInstruments(){
			return this.containerOfInstruments;
		}
	public void setCollectionsOlders(ConcurrentSkipListSet<Olders> collectionsOlders) {
		this.collectionsOlders = collectionsOlders;
	}
		/**
		 * a method that displays information about the collection (collection type, initialization date and size)
		 */
		public String info() {
			String instrumentsString = " ";
			for(Instruments i : Instruments.values()){
				instrumentsString +=i.toString() + "\n";
			}
			return ("База данных представляет из себя набор старцев(обьектов Olders)," +"\n"+
                    "каждый из которых имеет поля :" +"\n"+
                    "id(уникальный идентификатор)" +"\n"+
                    "name(имя старца)" +"\n"+
                    "userid(уникальный идентификатор пользователя, который является его владельцем)" +"\n"+
                    "dateofinit-дата инициализация старца");
		}
		/**
		 * This method removes objects that are less than this value.
		 * @param o -method argument, which is specified via keyboard input
		 */
		public String remove_lower(Olders o) {
			try {
			CompareOlders compare = new CompareOlders();
			for(Olders p : collectionsOlders){
				if(compare.compare(p,o)==-1){
					collectionsOlders.remove(p);
					return "Обьект "+p+ " удален из коллекции";
				}
			}
				}catch(IllegalArgumentException e) {
				return "Обьект данного типа не может быть удален из коллекции";
			}
			return  "В коллекции нет обьектов, меньше заданного";
		}
		/**
		 * This method clears the collection.
		 */
		public String clear() {
			collectionsOlders.clear();
			return "Коллекция была очищена";
		}
		/**
		 * The method adds object Olders to the collection.
		 * @param o - method argument, which is specified via keyboard input
		 */
		public String  add(Olders o) {
			try {
				if(collectionsOlders.add(o)) {
				collectionsOlders.add(o);	
				return "Обьект "+o.toString() + " был добавлен в коллекцию";
				}else {
					return "Обьект уже существует в коллекции! Попробуйте еще раз";
				}
			}catch(ArrayStoreException e) {
				return "Обьект данного типа не может быть добавлен в коллекцию";
			
			}
		}
		 /**
		  * The method adds an item to the collection.
		  * @param - method argument, which is specified via keyboard input
		  */
		 public String add_if_max(Olders olders) {
			 try {
				 CompareOlders comp = new CompareOlders();
			 	Olders olderMax = Collections.max(collectionsOlders);
					 for(Olders op : collectionsOlders) {
						 if (!collectionsOlders.contains(olders)) {
							 if (comp.compare( olders, olderMax) > 0) {
								 InsertInStream insertInStream = (st, element) -> {
									 List<Olders> list = (List<Olders>) st.collect(Collectors.toList());
									 list.add(element);
									 return list.stream();
								 };

								 return "Обьект добавлен в коллекцию";
							 } else {
								 return "Данный обьект не максимальный!";
							 }
						 } else {
							return "Данный обьект уже существует в коллекции! Попробуйте еще раз";
						 }
					 }

				 }catch(ArrayStoreException e){
					 return "Обьект данного типа не может быть добавлен в коллекцию";
				 }
				 return null;
			 }


		 public String help() {
			 return ("Команды программы : \n add {Object} - данная команда добавляет в базу данных старца Olders \n" +
					 "add_if_max {Object} - данная команда добавляет в базу данных старца Olders," +
					 "если id у входящего обьекта больше, чем максимальное значение id у обьектов Olders,находящихся  в базе данных \n " +
					 "remove {Object} - команда удаляет старца из базы данных(если владельцем этого обьекта являетесь именно вы) \n" +
					 "remove_lower {Object} - команда удаляет все обьекты из базы данных, id которых меньше id входящего обьекта Olders (если владельцем этого обьекта являетесь именно вы) \n" +
					 "info - выводит основную информацию о программе \n"+
					 "clear - очистка базы данных(если владельцем этого обьекта являетесь именно вы)\n"+
					 "show - вывод содержимого базы данных\n" +
					 "import - импортирует содержимое файла клиента на сервер \n" +
					 "load - загружает содержимое файла на сервере \n"+
					 "save - сохраняет содержимое файла \n"+
					 "exit - выход из программы");
		 }
		 /**
		 * This method removes the object o of the Olders class.
		 * @param o -method argument, which is specified via keyboard input
		 */
		 public String remove(Olders o) {
			 try {
			 collectionsOlders.remove(o);
			 return  "Обьект "+o.toString()+ " удален из коллекции";
			 }catch(IllegalArgumentException e) {
				 return "Вы не можете удалить этот обьект, так как его нету в коллекции!";
			 }
			 
		 }
		 /**
		  * The method shows the contents of the collection in Json format. 
		  */
		 
		 public String show() {
		 		String result ="";
		 		Gson gson = new Gson();
		 		for(Olders o : collectionsOlders){
						result+=gson.toJson(o,Olders.class) + "\n";


				}

				return result;
		 }
		 /**
		 /**
		  * The method saves the contents of the collection in Json format to the source file.
		  * @throws IOException - this method can throws IOException
		  * @throws ParserConfigurationException - this method can throws PareConfigurationException(if requested functionality is not available in the analyzer used)
		  */
		 public String save() {
			 try {
				 File file = new File(CollectionFileScanner.getFileName());
				 String fileName = CollectionFileScanner.getFileName();
				 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				 DocumentBuilder builder = factory.newDocumentBuilder();
				 Document document = builder.newDocument();
				 Element tagCollections = document.createElement("Collections");
				 document.appendChild(tagCollections);
				 for (Olders e : collectionsOlders) {
					 if (e.getId() != 0) {
						 Element tagOlders = document.createElement("Older");
						 Element nameOlder = document.createElement("Name");
						 Element tagID = document.createElement("ID");
						 Element tagDimension = document.createElement("Dimension");
						 Text iDText = document.createTextNode(new Long(e.getId()).toString());
						 tagCollections.appendChild(tagOlders);
						 tagOlders.appendChild(nameOlder);
						 Text nameText = document.createTextNode(e.getName());
						 nameOlder.appendChild(nameText);
						 tagOlders.appendChild(tagID);
						 tagID.appendChild(iDText);
						 tagOlders.appendChild(tagDimension);


					 }
				 }
				 DOMImplementation impl = document.getImplementation();
				 DOMImplementationLS implLS = (DOMImplementationLS) impl.getFeature("LS", "3.0");
				 LSSerializer ser = implLS.createLSSerializer();
				 ser.getDomConfig().setParameter("format-pretty-print", true);
				 String wellDone = ser.writeToString(document);
				 LSOutput out = implLS.createLSOutput();
				 out.setEncoding("UTF-8");
				 try {
					 out.setByteStream(Files.newOutputStream(Paths.get(fileName)));
				 } catch (IOException e1) {
					 return "Файл не найден или не доступен";
				 }
				 try {
					 ser.write(document, out);
				 } catch (LSException l) {

				 }
				 return "Коллекция успешно была сохранена!";
			 } catch (ParserConfigurationException e) {
				 return "Ошибка парсинга файла!";
			 }
		 }

				 				
		 }