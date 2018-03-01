# RxJava_android
RxJava is a Java VM implementation of Reactive Extensions: a library for composing asynchronous and event-based programs by using observable sequences.

It extends the observer pattern to support sequences of data/events and adds operators that allow you to compose sequences together declaratively while abstracting away concerns about things like low-level threading, synchronization, thread-safety and concurrent data structures.

Version 1.x (Javadoc)

Looking for version 1.x? Jump to the 1.x branch.

Timeline plans for the 1.x line:

June 1, 2017 - feature freeze (no new operators), only bugfixes
March 31, 2018 - end of life, no further development
Version 2.x (Javadoc)

single dependency: Reactive-Streams
continued support for Java 6+ & Android 2.3+
performance gains through design changes learned through the 1.x cycle and through Reactive-Streams-Commons research project.
Java 8 lambda-friendly API
non-opinionated about source of concurrency (threads, pools, event loops, fibers, actors, etc)
async or synchronous execution
virtual time and schedulers for parameterized concurrency
Version 2.x and 1.x will live side-by-side for several years. They will have different group ids (io.reactivex.rxjava2 vs io.reactivex) and namespaces (io.reactivex vs rx).

See the differences between version 1.x and 2.x in the wiki article What's different in 2.0. Learn more about RxJava in general on the Wiki Home.

Getting started

The first step is to include RxJava 2 into your project, for example, as a Gradle compile dependency:

compile "io.reactivex.rxjava2:rxjava:2.x.y"
The second is to write the Hello World program:

package rxjava.examples;

import io.reactivex.*;

public class HelloWorld {
    public static void main(String[] args) {
        Flowable.just("Hello world").subscribe(System.out::println);
    }
}
If your platform doesn't support Java 8 lambdas (yet), you have to create an inner class of Consumer manually:

import io.reactivex.functions.Consumer;

Flowable.just("Hello world")
  .subscribe(new Consumer<String>() {
      @Override public void accept(String s) {
          System.out.println(s);
      }
  });
RxJava 2 features several base classes you can discover operators on:

io.reactivex.Flowable: 0..N flows, supporting Reactive-Streams and backpressure
io.reactivex.Observable: 0..N flows, no backpressure
io.reactivex.Single: a flow of exactly 1 item or an error
io.reactivex.Completable: a flow without items but only a completion or error signal
io.reactivex.Maybe: a flow with no items, exactly one item or an error
One of the common use cases for RxJava is to run some computation, network request on a background thread and show the results (or error) on the UI thread:

import io.reactivex.schedulers.Schedulers;

Flowable.fromCallable(() -> {
    Thread.sleep(1000); //  imitate expensive computation
    return "Done";
})
  .subscribeOn(Schedulers.io())
  .observeOn(Schedulers.single())
  .subscribe(System.out::println, Throwable::printStackTrace);

Thread.sleep(2000); // <--- wait for the flow to finish
This style of chaining methods is called a fluent API which resembles the builder pattern. However, RxJava's reactive types are immutable; each of the method calls returns a new Flowable with added behavior. To illustrate, the example can be rewritten as follows:

Flowable<String> source = Flowable.fromCallable(() -> {
    Thread.sleep(1000); //  imitate expensive computation
    return "Done";
});

Flowable<String> runBackground = source.subscribeOn(Schedulers.io());

Flowable<String> showForeground = runBackground.observeOn(Schedulers.single());

showForeground.subscribe(System.out::println, Throwable::printStackTrace);

Thread.sleep(2000);
Typically, you can move computations or blocking IO to some other thread via subscribeOn. Once the data is ready, you can make sure they get processed on the foreground or GUI thread via observeOn.

RxJava operators don't work with Threads or ExecutorServices directly but with so called Schedulers that abstract away sources of concurrency behind an uniform API. RxJava 2 features several standard schedulers accessible via Schedulers utility class. These are available on all JVM platforms but some specific platforms, such as Android, have their own typical Schedulers defined: AndroidSchedulers.mainThread(), SwingScheduler.instance() or JavaFXSchedulers.gui().

The Thread.sleep(2000); at the end is no accident. In RxJava the default Schedulers run on daemon threads, which means once the Java main thread exits, they all get stopped and background computations may never happen. Sleeping for some time in this example situations lets you see the output of the flow on the console with time to spare.

Flows in RxJava are sequential in nature split into processing stages that may run concurrently with each other:

Flowable.range(1, 10)
  .observeOn(Schedulers.computation())
  .map(v -> v * v)
  .blockingSubscribe(System.out::println);
This example flow squares the numbers from 1 to 10 on the computation Scheduler and consumes the results on the "main" thread (more precisely, the caller thread of blockingSubscribe). However, the lambda v -> v * v doesn't run in parallel for this flow; it receives the values 1 to 10 on the same computation thread one after the other.

Processing the numbers 1 to 10 in parallel is a bit more involved:

Flowable.range(1, 10)
  .flatMap(v ->
      Flowable.just(v)
        .subscribeOn(Schedulers.computation())
        .map(w -> w * w)
  )
  .blockingSubscribe(System.out::println);
Practically, paralellism in RxJava means running independent flows and merging their results back into a single flow. The operator flatMap does this by first mapping each number from 1 to 10 into its own individual Flowable, runs them and merges the computed squares.

Starting from 2.0.5, there is an experimental operator parallel() and type ParallelFlowable that helps achieve the same parallel processing pattern:

Flowable.range(1, 10)
  .parallel()
  .runOn(Schedulers.computation())
  .map(v -> v * v)
  .sequential()
  .blockingSubscribe(System.out::println);
flatMap is a powerful operator and helps in a lot of situations. For example, given a service that returns a Flowable, we'd like to call another service with values emitted by the first service:

Flowable<Inventory> inventorySource = warehouse.getInventoryAsync();

inventorySource.flatMap(inventoryItem ->
    erp.getDemandAsync(inventoryItem.getId())
    .map(demand 
        -> System.out.println("Item " + inventoryItem.getName() + " has demand " + demand));
  )
  .subscribe();
Note, however, that flatMap doesn't guarantee any order and the end result from the inner flows may end up interleaved. There are alternative operators:

concatMap that maps and runs one inner flow at a time and
concatMapEager which runs all inner flows "at once" but the output flow will be in the order those inner flows were created.
For further details, consult the wiki.

Communication

Google Group: RxJava
Twitter: @RxJava
GitHub Issues
StackOverflow: rx-java and rx-java2
Gitter.im
Versioning

Version 2.x is now considered stable and final. Version 1.x will be supported for several years along with 2.x. Enhancements and bugfixes will be synchronized between the two in a timely manner.

Minor 2.x increments (such as 2.1, 2.2, etc) will occur when non-trivial new functionality is added or significant enhancements or bug fixes occur that may have behavioral changes that may affect some edge cases (such as dependence on behavior resulting from a bug). An example of an enhancement that would classify as this is adding reactive pull backpressure support to an operator that previously did not support it. This should be backwards compatible but does behave differently.

Patch 2.x.y increments (such as 2.0.0 -> 2.0.1, 2.3.1 -> 2.3.2, etc) will occur for bug fixes and trivial functionality (like adding a method overload). New functionality marked with an @Beta or @Experimental annotation can also be added in patch releases to allow rapid exploration and iteration of unstable new functionality.

@Beta

APIs marked with the @Beta annotation at the class or method level are subject to change. They can be modified in any way, or even removed, at any time. If your code is a library itself (i.e. it is used on the CLASSPATH of users outside your own control), you should not use beta APIs, unless you repackage them (e.g. using ProGuard, shading, etc).

@Experimental

APIs marked with the @Experimental annotation at the class or method level will almost certainly change. They can be modified in any way, or even removed, at any time. You should not use or rely on them in any production code. They are purely to allow broad testing and feedback.

@Deprecated

APIs marked with the @Deprecated annotation at the class or method level will remain supported until the next major release but it is recommended to stop using them.

io.reactivex.internal.*

All code inside the io.reactivex.internal.* packages is considered private API and should not be relied upon at all. It can change at any time.

Full Documentation

Wiki
Javadoc
Binaries

Binaries and dependency information for Maven, Ivy, Gradle and others can be found at http://search.maven.org.

Example for Gradle:

compile 'io.reactivex.rxjava2:rxjava:x.y.z'
and for Maven:

<dependency>
    <groupId>io.reactivex.rxjava2</groupId>
    <artifactId>rxjava</artifactId>
    <version>x.y.z</version>
</dependency>
and for Ivy:

<dependency org="io.reactivex.rxjava2" name="rxjava" rev="x.y.z" />
Snapshots are available via https://oss.jfrog.org/libs-snapshot/io/reactivex/rxjava2/rxjava/

repositories {
    maven { url 'https://oss.jfrog.org/libs-snapshot' }
}

dependencies {
    compile 'io.reactivex.rxjava2:rxjava:2.2.0-SNAPSHOT'
}
Build

To build:

$ git clone git@github.com:ReactiveX/RxJava.git
$ cd RxJava/
$ ./gradlew build
Further details on building can be found on the Getting Started page of the wiki.