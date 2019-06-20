package DCP;

import java.util.function.BiFunction;
import java.util.function.Function;

public class App {

  public static <T,U,R> Function<BiFunction<T,U,R>,R> cons(T a, U b) {
    Function<BiFunction<T,U,R>,R> pair = f -> f.apply(a,b);
    return pair;
  }

  public static <T,U> T car(Function<BiFunction<T,U,T>,T> cons) {
    return cons.apply((a,b) -> a);
  }

  public static <T,U> U cdr(Function<BiFunction<T,U,U>,U> cons) {
    return cons.apply((a,b) -> b);
  }

}
