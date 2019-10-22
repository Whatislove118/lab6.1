import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@FunctionalInterface
public interface InsertInStream {
     Stream<Olders> insertInStream(Stream<Olders> st , Olders element);
}
