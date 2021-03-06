package examples6;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Runner {

	private int count = 0;
	private Lock lock = new ReentrantLock();
	private Condition cond = lock.newCondition();

	public void increment() {
		for (int i = 0; i < 10000; i++) {
			count++;
		}
	}

	public void firstThread() throws InterruptedException {
		lock.lock();
		System.out.println("Waiting...");
		cond.await();

		System.out.println("Woken up");
		try {
			increment();
		} finally {
			lock.unlock();
		}
	}

	public void secondThread() throws InterruptedException {

		Thread.sleep(1000);
		lock.lock();

		System.out.println("Press the return key...");
		try (Scanner s = new Scanner(System.in)) {
			s.nextLine();
		}
		System.out.println("Got returned key");
		cond.signal();

		try {
			increment();
		} finally {
			lock.unlock();
		}

	}

	public void finished() {
		System.out.println("Count is " + count);
	}
}
