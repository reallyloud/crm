package ru.mentee.power.crm;

import java.util.Arrays;

public class Exercize {
    static void main() {
        int[] test = new int[]{1,2,3,4,5,6};
        increment(test);
    }

    public static void increment(int[] source) {
            StringBuilder numbers = new StringBuilder();
        for (int j : source) {
            numbers.append(j);
        }
            int finalNumbers = Integer.parseInt(numbers.toString()) + 1;
            char[] finalNumbersCharArray = String.valueOf(finalNumbers).toCharArray();
            int[] result = new int[finalNumbersCharArray.length];
        for (int i = 0; i < finalNumbersCharArray.length; i++) {
            result[i] = Character.getNumericValue(finalNumbersCharArray[i]);
        }
        System.out.println("Старый массив: " + Arrays.toString(source));
        System.out.println("Новый массив: " + Arrays.toString(result));

    }
}
