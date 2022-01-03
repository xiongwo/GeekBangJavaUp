package geektime.concurrent.race;

import java.util.Arrays;

public class SimpleDivideCompute implements ComputeRunnable {

	SimpleShareData ssd;
	int size;
	int offset;
	public SimpleDivideCompute(SimpleShareData ssd, int size, int offset) {
		this.ssd = ssd;
		this.size = size;
		this.offset = offset;
	}
	
	@Override
	public void run() {
		go();

	}

	@Override
	public void go() {
		System.out.println("开始计算随机数: " + size + " " + offset);
//		int[] alist = new int[size];

		Object[] objects = ssd.getScore().toArray();
		Integer[] arrs = new Integer[size];
		System.arraycopy(objects, offset * size, arrs, 0, size);
		Arrays.sort(arrs);

//		for (int i = 0; i < size; i++) {
//			alist[i] = ssd.getScore().indexOf(offset * size + i);
//		}

//		int unit = SimpleShareData.BUFSIZE * size / SimpleShareData.COUNT;
//		for (int i = unit * offset; i < unit * (offset + 1); i++) {
		for (int i = 0; i < SimpleShareData.BUFSIZE * size / SimpleShareData.COUNT; i++) {
			//System.out.println("随机数: " + alist[alist.length - i - 1]);
			ssd.addExchange(arrs[arrs.length - i - 1]);
		}

		ssd.getCompSig().countDown();
		//System.out.println("计算随机数完毕: " + size + " " + SimpleShareData.BUFSIZE * size / SimpleShareData.COUNT);
	}
}
