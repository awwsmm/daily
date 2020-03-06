package daily;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import java.util.concurrent.TimeUnit;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 100, time=500, timeUnit=TimeUnit.MILLISECONDS)
@Measurement(iterations = 100, time=500, timeUnit=TimeUnit.MILLISECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class MyBenchmark {

	@State(Scope.Benchmark)
	public static class Setup {

    @Param({ "10000000", "100000000", "1000000000" })
		int N;

    // create a List of numbers 1...length
    List<Integer> list = IntStream.rangeClosed(1, N).boxed().collect(Collectors.toList());

    // always larger than the largest possible sum of two elements
    int k = Integer.MAX_VALUE;

    // shuffle the array before returning it to the user
		int[] get_given() {

      // shuffle the List
      Collections.shuffle(list);

      // convert the List to an array
      int[] given = list.stream().mapToInt(i->i).toArray();

			return given;
		}

    // return k
    int get_k() {
      return k;
    }

	}

  //----------------------------------------------------------------------------
  //
  //  TESTS
  //
  //----------------------------------------------------------------------------

	@Benchmark
  public boolean testAndrew(Setup d) {

    // setup
    int[] given = d.get_given();
    int k = d.get_k();

    // algorithm

    int len = given.length;

    for (int outer = 0; outer < (len - 1); ++outer)
      for (int inner = outer + 1; inner < len; ++inner)
        if (given[outer] + given[inner] == k)
          return true;

    return false;
  }

	@Benchmark
  public boolean testAndrei(Setup d) {

    // setup
    int[] given = d.get_given();
    int k = d.get_k();

    // algorithm

    Set<Integer> set = new TreeSet<Integer>();

    for (int number : given){
      if (set.contains(number))
        return true;
      set.add(k - number);
    }

    return false;
  }

	@Benchmark
  public boolean testJoan(Setup d) {

    // setup
    int[] given = d.get_given();
    int k = d.get_k();

    // algorithm

    Arrays.sort(given);

    int i = 0;
    int j = given.length - 1;

    while (i < given.length && j >= 0) {

      int sum = given[i] + given[j];

      if      (sum == k) return true;
      else if (sum  > k) --j;
      else               ++i;

    }

    return false;
  }

}
