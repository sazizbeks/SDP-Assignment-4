package com.company;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator implements ICalculator {

    @Override
    public int interpret(String inputString) throws CalculateException {
        StringBuilder input = new StringBuilder(inputString.replaceAll("\\s|[a-zA-Z]", ""));

        if (input.charAt(0) != '-' && !Character.isDigit(input.charAt(0)))
            input.deleteCharAt(0);

        if (!Character.isDigit(input.charAt(input.length() - 1)))
            input.deleteCharAt(input.length() - 1);

        if (input.charAt(0) != '-' && !Character.isDigit(input.charAt(0)) ||
                !Character.isDigit(input.charAt(input.length() - 1)))
            throw new CalculateException("Incorrect string was passed");

        return calculate(input);
    }

    private int calculate(StringBuilder input) {
        multiplication(input);
        return addOrSub(input.toString());
    }

    private void multiplication(StringBuilder input) {
        while (input.toString().contains("*")) {
            int a = 0;
            int b = 0;

            int index = input.indexOf("*");
            String left = input.substring(0, index);
            String right = input.substring(index + 1);

            int indexOfLastOperandInLeftSub = Math.max(left.lastIndexOf('+'), left.lastIndexOf('-'));
            int indexOfFirstOperandInRightSub = -1;

            Pattern leftPattern = Pattern.compile("(.{0," + (indexOfLastOperandInLeftSub == -1 ? 0 : indexOfLastOperandInLeftSub) + "})([+\\-]?)(\\d+)");
            Pattern rightPattern = Pattern.compile("(\\d+)([+\\-*]?)(.*)");

            Matcher leftMatcher = leftPattern.matcher(left);
            Matcher rightMatcher = rightPattern.matcher(right);

            if (leftMatcher.matches()) {
                a = Integer.parseInt(leftMatcher.group(3));
            }

            if (rightMatcher.matches()) {
                b = Integer.parseInt(rightMatcher.group(1));
                indexOfFirstOperandInRightSub = right.indexOf(rightMatcher.group(2)) == 0 ? -1 : right.indexOf(rightMatcher.group(2));
            }

            int c = a * b;

            input.replace(indexOfLastOperandInLeftSub == -1 ? 0 : indexOfLastOperandInLeftSub + 1,
                    indexOfFirstOperandInRightSub == -1 ? input.length() : index + indexOfFirstOperandInRightSub + 1,
                    String.valueOf(c));
        }
    }

    private int addOrSub(String input) {
        int res = 0;

        if (!input.equals("")) {

            if (input.charAt(0) == '-') {
                Pattern pattern = Pattern.compile("-(\\d+)(.*)");
                Matcher matcher = pattern.matcher(input);
                if (matcher.matches())
                    res = -1 * Integer.parseInt(matcher.group(1));

                input = input.replaceFirst("-(\\d+)", "0");
            }

            String[] numbers = input.split("[+-]");
            String[] operators = input.split("[0-9]+");

            res += Integer.parseInt(numbers[0]);

            for (int i = 1; i < numbers.length; i++) {
                if (operators[i].equals("+")) res += Integer.parseInt(numbers[i]);
                else res -= Integer.parseInt(numbers[i]);
            }
        }

        return res;
    }


}
