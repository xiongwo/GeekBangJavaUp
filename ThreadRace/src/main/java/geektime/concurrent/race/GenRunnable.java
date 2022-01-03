package geektime.concurrent.race;

import java.util.concurrent.Callable;

public interface GenRunnable extends Callable<Integer[]> {

	Integer[] gen();

}
