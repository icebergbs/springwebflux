package reactor3.springwebflux.demo;

public class Merge {

	public static void main(String[] args) {
		merge();
	}

	private static void merge() {
//		Flux.merge(Flux.intervalMillis(0, 100).take(5), Flux.intervalMillis(50, 100).take(5)).toStream()
//				.forEach(System.out::println);
//
//		System.out.println("---------");
//
//		Flux.mergeSequential(Flux.intervalMillis(0, 100).take(5), Flux.intervalMillis(50, 100).take(5)).toStream()
//				.forEach(System.out::println);
	}
}
