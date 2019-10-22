import com.google.gson.Gson;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class H {
    public static void main(String[] args) {
        CollectionFileScanner collectionFileScanner = new CollectionFileScanner();
        CollectionsOlders collectionsOlders = collectionFileScanner.readFile("/Users/whatislove118/desktop/программирование/lab6.1/lab5.xml");
        System.out.println(collectionsOlders.show());
    }
}
