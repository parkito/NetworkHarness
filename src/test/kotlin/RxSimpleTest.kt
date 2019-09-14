import io.reactivex.Observable
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RxSimpleTest {

    @Test
    fun returnAValue() {
        var result = ""
        val observer = Observable.just("Hello") // provides date
        observer.subscribe { s -> result = s } // Callable as subscriber
        assertTrue(result == "Hello")
    }
}