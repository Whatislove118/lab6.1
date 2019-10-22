import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * this class realize Builders
 * @author whatislove118
 */
public class Builders implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * String name - name of the builder
	 */
	private String name;
	/**
	 * ArrayList instrument - array of the instruments of the builder
	 */
	private CopyOnWriteArrayList<Instruments> instrument = new CopyOnWriteArrayList<Instruments>();
	public Builders(){
	}
	Builders(String name){
		this.name=name;
	}
	/**
	 * this method add instrument to the builder
	 * @param i - Instrument for the builder
	 */
	public void TakeInstruments(Instruments i) {
		instrument.add(i);
	}
	/**
	 * textual representation of the object
	 */
	@Override
	public String toString() {
		return ("Строитель " +this.name);
		
	}
	/**
	 * this method return name of the builder
	 * @return name - name of the object
	 */
	public String getName() {
		return name;
	}
	/**
	 * this method set name of the builder
	 * @param name - name of the object
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * this method return arr of the instruments of the builders
	 * @return ArrayList instrument- arr of the instruments
	 */
	public CopyOnWriteArrayList<Instruments> getArrInstruments(){
		return this.instrument;
	}
}


