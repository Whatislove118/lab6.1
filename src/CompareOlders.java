import java.util.Comparator;
/**
 * @author whatislove118
 * class comparator
 */
public class CompareOlders implements Comparator<Olders> {
	/**
	 * This method com pares Olders objects by the number of their builders (Builder class objects)
	 * @return int i - result of comparing two objects
	 */
	public int compare(Olders o1,Olders o2) {
		if(o1.getId()-o2.getId()==0) {
			return 0;
		}if(o1.getId()-o2.getId()>0) {
			return 1;
		}else {
			return -1;
		}
	}

}
