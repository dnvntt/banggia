package priceboard;


/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {
		System.out.println("Hello World Java 8!");

		IntegerMath math = new IntegerMath() {
			@Override
			public int operation(int a, int b) {
				return a + b;
			}
		};
		System.out.println(math.operation(10, 20));
		
		IntegerMath math2 = (a, b) -> a + b;
		System.out.println(math2.operation(20, 20));
		
		Printer p = (s) -> System.out.println(s);
		p.print("Hello Java 8");
		new UsingPrinter().print("Hello", (s) -> System.out.println(s));
		
		Runnable r = () -> System.out.println(Thread.currentThread());
		new Thread(r).start();
		new Thread(() -> System.out.println(Thread.currentThread())).start();
		
	}
}

interface IntegerMath {
	int operation(int a, int b);
}

interface Printer {
	void print(String s);
}

class UsingPrinter {
	void print(String s, Printer p) {
		p.print(s);
	}
}