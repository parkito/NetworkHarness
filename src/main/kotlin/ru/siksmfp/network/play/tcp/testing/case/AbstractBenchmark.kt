package ru.siksmfp.network.play.tcp.testing.case

import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode.AverageTime
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Warmup
import java.util.concurrent.TimeUnit.NANOSECONDS

@BenchmarkMode(AverageTime)
@OutputTimeUnit(NANOSECONDS)
@Fork(value = 2, jvmArgs = ["-Xms2G", "-Xmx2G"])
@Measurement(iterations = 5)
@Warmup(iterations = 5)
abstract class AbstractBenchmark
