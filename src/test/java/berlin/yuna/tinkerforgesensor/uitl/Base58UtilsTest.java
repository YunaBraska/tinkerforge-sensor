package berlin.yuna.tinkerforgesensor.uitl;

import org.junit.jupiter.api.Test;

import static com.tinkerforge.Base58Utils.base58Decode;
import static com.tinkerforge.Base58Utils.base58Encode;
import static com.tinkerforge.Base58Utils.base58Random;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class Base58UtilsTest {

    @Test
    void random() {
        assertThat(base58Random(), is(notNullValue()));
    }

    @Test
    void encodeDecode() {
        final long id = 1234567890;
        assertThat(base58Decode(base58Encode(id)), is(id));
    }
}