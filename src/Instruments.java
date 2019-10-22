import java.io.Serializable;

public enum Instruments implements Serializable {
	/**
	 * Enum class which contained a constant value of the Builders instruments
	 */
	kletochnyaMassa,protoplazma,molotok,gvozdi,pila,otvertka,drel;
	@Override
	/**
	 * textual representation of the object
	 * @return name - name of the object
	 */
   public String toString() {
	   return this.name();
   }
	private static final long serialVersionUID = 1L;
}
