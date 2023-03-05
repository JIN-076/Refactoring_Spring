package toby.spring.inha.refactor.learningTst.template;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

public class CalcSumTest {

    Calculator calculator;
    String numFilePath;

    @BeforeEach
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilePath = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    @DisplayName("템플릿/콜백 패턴을 적용한 테스트")
    public void sumOfNumbersV3() throws IOException {
        Assertions.assertThat(calculator.calcSumV3(this.numFilePath)).isEqualTo(10);
    }

    @Test
    @DisplayName("템플릿/콜백 패턴을 적용한 곱셈 테스트")
    public void multiplyOfNumbers() throws IOException {
        Assertions.assertThat(calculator.calcMultiply(this.numFilePath)).isEqualTo(24);
    }

    @Test
    @DisplayName("Generic을 적용한 문자열 연결 테스트")
    public void concatenateStrings() throws IOException {
        Assertions.assertThat(calculator.concatenate(this.numFilePath)).isEqualTo("1234");
    }

    @Test
    @DisplayName("numbers.txt 내 숫자의 합 테스트")
    public void sumOfNumbers() throws IOException {
        Calculator calculator = new Calculator();

        int sum = calculator.calcSum(getClass().getResource("/numbers.txt").getPath());
        Assertions.assertThat(sum).isEqualTo(10);
    }

    @Test
    @DisplayName("예외처리를 추가한 테스트")
    public void sumOfNumbersV2() throws IOException {
        Calculator calculator = new Calculator();

        int sum = calculator.calcSumV2(getClass().getResource("/numbers.txt").getPath());
        Assertions.assertThat(sum).isEqualTo(10);
    }
}
