package ru.siksmfp.network.harness.benchmarking.api

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.annotations.Mode.AverageTime
import java.util.concurrent.TimeUnit.NANOSECONDS

@BenchmarkMode(AverageTime)
@OutputTimeUnit(NANOSECONDS)
@Fork(value = 2, jvmArgs = ["-Xms2G", "-Xmx2G"])
@Measurement(iterations = 3)
@Warmup(iterations = 1)
abstract class AbstractBenchmark
