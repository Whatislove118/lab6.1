import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A class whose objects are contained in the collection.
 * @author whatislove118
 * имя, название или аналогичный текстовый идентификатор; +
 * размер или аналогичный числовой параметр; +
 * характеристику, определяющую местоположение объекта на плоскости/в пространстве;+
 * время/дату рождения/создания объекта.+
 */
public class Olders implements Comparable<Olders>, Serializable {
	private static final long serialVersionUID = 1L;
	/** String name -field, which contained String value  of class Olders object name */
	private String name;
	/** int id = field, which contained integer value of id-unique number,assigned to each object class Olders */
	private  int id;
	private int countOlders=0;

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	private int userID;

	public OffsetDateTime getDateOfInitialization() {
		return dateOfInitialization;
	}

	public void setDateOfInitialization(OffsetDateTime dateOfInitialization) {
		this.dateOfInitialization = dateOfInitialization;
	}

	private OffsetDateTime dateOfInitialization = null;
	/** ArrayList arrBuilders - collection, which have object of class Builders */




	/**
	 * constructor of this class
	 * @param imya - name of object
	 */
	Olders(String imya) {
		dateOfInitialization=OffsetDateTime.now();
		//SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("E dd.MM.yyyy  'Время создания обьекта ' hh:mm:ss a" );
		this.name =imya;
	}
	/**
	 * this method that compares Olders objects by their id
	 * @return int i -depending on the comparison result
	 */
	@Override
	public int compareTo(Olders o) {
		if(this.id-o.getId()==0) {
			return 0;
		}if(this.id-o.getId()>0) {
			return 1;
		}else {
			return -1;
		}
	}


	/**
	 * this method print array of Builders objects
	 */

    /**
     * This method creates a class Builders object and adds it to the ArrayList arrBuilders
     * @param  Bname - name of the builder
     * @return noName- object of the builder
     */
	public Builders inviteBuilders(String Bname) {
		Builders noName = new Builders(Bname);
		return (noName);
	}
	/**
	 * textual representation of the object
	 */
	@Override
	public String toString() {
		return ("Старец " + this.name+ ",с номером " + this.id );
	}
	/**
	 * This method return object's name
	 * @return name - name of object
	 */
	public String getName() {
		return this.name;
	}

	public int getCountOlders() {
		return countOlders;
	}

	/**
	 * this method set name of object
	 * @param n-name of object
	 */
	public void setName(String n) {
		this.name=n;
	}
	/**
	 * this method return id of object
	 * @return id - id of object
	 */
	public int getId() {
		return this.id;
	}
	/**
	 * this method set if of object
	 * @param c - id of object
	 */
	public  void setId(int c) {
		try {
		this.id=c;
		}catch(NumberFormatException e) {
			System.err.println("id старца не может состоять из букв! Проверьте файл и исправьте ошибку");
			System.exit(0);
		}
	}
	/**
	 * this method return Olders reputation in mine in cave
	 * @return prey - reputation
	 */
	@Override
	public int hashCode() {
		return this.id;
	}
	/**
	 * method of comparing objects by their hashcode (in our case by their id)
	 * @return true,false - depends on equality
	 */
	@Override
	public boolean equals(Object c) {
		if(c.hashCode()==this.hashCode()){
			return true;
		}else {
			return false;
		}
	}
	/**
	 * method return array of Builders
	 * @return arrBuilders - array of Builders
	 */

}
