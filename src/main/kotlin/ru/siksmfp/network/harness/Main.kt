package ru.siksmfp.network.harness

fun main(args: Array<String>) {
    org.openjdk.jmh.Main.main(arrayOf("nioSmallFileBenchmark"));
//    NioBenchmark().nioSmallFileBenchmark(NioState())
}
