package geektime.concurrent.race;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class SimplePolicy {
	
	final int genThreadInPool = 2; //不超过8
	final int computeThreadInPool = 2; //不超过16
	SimpleShareData ssd;
	
	public SimplePolicy() {
		ssd = new SimpleShareData();
		ssd.initGenSignals(genThreadInPool);
		ssd.initCompSignals(computeThreadInPool);
		ssd.initExchange();
	}

	public long go() throws Exception {
		ExecutorService genThreadPool = Executors.newFixedThreadPool(genThreadInPool);
		ExecutorService computeThreadPool = Executors.newFixedThreadPool(computeThreadInPool);
		
		//使用自定义方法A计算
		long startTime = System.currentTimeMillis();
		System.out.println("开始自定义A方法计算计时: " + startTime);

		List<FutureTask<Integer[]>> futureTasks = new ArrayList<FutureTask<Integer[]>>();

		for (int tp = 0; tp < genThreadInPool; tp++) {
			SimpleSyncGen simpleSyncGen = new SimpleSyncGen(ssd, SimpleShareData.COUNT / genThreadInPool, tp);
			FutureTask<Integer[]> futureTask = new FutureTask<>(simpleSyncGen);
			futureTasks.add(futureTask);
//			genThreadPool.execute(simpleSyncGen);
			genThreadPool.submit(futureTask);
		}

		for (FutureTask<Integer[]> futureTask : futureTasks) {
			ssd.getScore().addAll(Arrays.asList(futureTask.get()));
		}

//		ssd.getGenSig().await();
		
		long genTime = System.currentTimeMillis();
		System.out.println("产生随机数时长: " + (genTime - startTime));
		

		for (int tp = 0; tp < computeThreadInPool; tp++) {

			SimpleDivideCompute simpleDiv = new SimpleDivideCompute(ssd, SimpleShareData.COUNT / computeThreadInPool, tp);
			computeThreadPool.execute(simpleDiv);
		}
		ssd.getCompSig().await();

		Integer[] box = ssd.getShare().toArray(new Integer[ssd.getShare().size()]);
		Arrays.sort(box);
		for (int i = 0; i < SimpleShareData.BUFSIZE; i++) {
			ssd.getTop()[i] = box[box.length - i - 1];
		}

		long sortTime = System.currentTimeMillis();
		System.out.println("自定义A方法计算时长: " + (sortTime - genTime));

		printTop();
		long total = System.currentTimeMillis() - startTime;
		System.out.println("总时长：" + total);

		computeThreadPool.shutdown();
		genThreadPool.shutdown();
		return total;
	}
    void printTop() {
    	System.out.println("前10成绩为:");
    	for (int j = 0; j <= 10; j++) {
    		System.out.print(ssd.getTop()[j] + " ");
    	}
    	System.out.println();
    }
}
