package toby.spring.inha.refactor.learningTst.template;

import toby.spring.inha.refactor.learningTst.template.interfce.BufferedReaderCallback;
import toby.spring.inha.refactor.learningTst.template.interfce.GenericLineCallBack;
import toby.spring.inha.refactor.learningTst.template.interfce.LineCallback;

import javax.sound.sampled.Line;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public Integer calcSum(String filepath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));

        Integer sum = 0;
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            sum += Integer.valueOf(line);
        }

        bufferedReader.close();

        return sum;
    }

    public Integer calcSumV2(String filepath) throws IOException {
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(filepath));
            Integer sum = 0;
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sum += Integer.valueOf(line);
            }
            return sum;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(filepath));
            int result = callback.doSomethingWithReader(bufferedReader);
            return result;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public Integer calcSumV3(String filepath) throws IOException {
        BufferedReaderCallback callback = new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader bufferedReader) throws IOException {
                Integer sum = 0;
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sum += Integer.valueOf(line);
                }
                return sum;
            }
        };
        return fileReadTemplate(filepath, callback);
    }

    public Integer calcMultiply(String filepath) throws IOException {
        BufferedReaderCallback callback = new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader bufferedReader) throws IOException {
                Integer multiply = 1;
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    multiply *= Integer.valueOf(line);
                }
                return multiply;
            }
        };
        return fileReadTemplate(filepath, callback);
    }

    public Integer lineReadTemplate(String filepath, LineCallback callback, int initVal) throws IOException {
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(filepath));
            Integer result = initVal;
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                result = callback.doSomethingWIthLine(line, result);
            }
            return result;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public Integer calcSumV4(String filepath) throws IOException {
        LineCallback callback = new LineCallback() {
            @Override
            public Integer doSomethingWIthLine(String line, Integer value) {
                return value + Integer.valueOf(line);
            }
        };
        return lineReadTemplate(filepath, callback, 0);
    }

    public Integer calcMultiplyV2(String filepath) throws IOException {
        LineCallback callback = new LineCallback() {
            @Override
            public Integer doSomethingWIthLine(String line, Integer value) {
                return value * Integer.valueOf(line);
            }
        };
        return lineReadTemplate(filepath, callback, 1);
    }

    public <T> T lineReadTemplate(String filepath, GenericLineCallBack<T> callback, T initVal) throws IOException {
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(filepath));
            T result = initVal;
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                result = callback.doSomethingWithLine(line, result);
            }
            return result;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public String concatenate(String filepath) throws IOException {
        GenericLineCallBack<String> concatenateCallback = new GenericLineCallBack<String>() {
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value + line;
            }
        };
        return lineReadTemplate(filepath, concatenateCallback, "");
    }
}
