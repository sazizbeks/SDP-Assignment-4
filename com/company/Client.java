package com.company;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ICalculator calculator = new Calculator();
        String input = scanner.nextLine();
        try {
            System.out.println(calculator.interpret(input));
        } catch (CalculateException e) {
            e.printStackTrace();
        }
    }
}
