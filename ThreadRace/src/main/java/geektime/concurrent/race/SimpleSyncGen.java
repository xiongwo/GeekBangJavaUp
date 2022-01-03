package geektime.concurrent.race;

import java.util.Arrays;
import java.util.Random;

public class SimpleSyncGen implements GenRunnable {

	SimpleShareData ssd;
	int size;
	int offset;
	public SimpleSyncGen(SimpleShareData ssd, int size, int offset) {
		this.ssd = ssd;
		this.size = size;
		this.offset = offset;
	}
	
//	@Override
//	public void run() {
//		gen();
//	}

	@Override
	public Integer[] gen() {
		//System.out.println("开始产生随机数: " + size);
		Random rand = new Random(System.currentTimeMillis());
		int r;
		int i;
//    	for (i = 0; i < size; i++) {
//    		r = rand.nextInt(SimpleShareData.COUNT);
//    		synchronized (ssd.getScore()) {
//    			ssd.getScore().add(new Integer(r));
//    		}
//    	}

		// 粗化锁粒度，在此案例中，整体执行的性能跟多线程并行计算随机数的结果差不多
//		synchronized (ssd.getScore()) {
//			for (i = 0; i < size; i++) {
//				r = rand.nextInt(SimpleShareData.COUNT);
//				ssd.getScore().add(new Integer(r));
//			}
//		}

    	//System.out.println("产生随机数个数: " + i);
//    	ssd.getGenSig().countDown();

		Integer[] arrs = new Integer[size];

		for (i = 0; i < size; i++) {
    		r = rand.nextInt(SimpleShareData.COUNT);
			arrs[i] = r;
    	}

		return arrs;
	}

	@Override
	public Integer[] call() throws Exception {
		return gen();
	}
}
