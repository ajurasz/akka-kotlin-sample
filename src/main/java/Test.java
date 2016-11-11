import java.util.AbstractCollection;
import java.util.Iterator;

/**
 * Created by arek on 10.11.16.
 */
public class Test {
    public static void main(String[] args) {
        AbstractCollection<Integer> ab = new AbstractCollection<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }
}
