package Conversor;

import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);

                System.out.println("Bem-vindo ao Conversor de Moedas!");


                String[] currencies = {"USD", "EUR", "GBP", "JPY", "BRL", "AUD"};


                System.out.println("Selecione a moeda de origem:");
                for (int i = 0; i < currencies.length; i++) {
                    System.out.println((i + 1) + ". " + currencies[i]);
                }
                int fromCurrencyIndex = scanner.nextInt() - 1;

                System.out.println("Selecione a moeda de destino:");
                for (int i = 0; i < currencies.length; i++) {
                    System.out.println((i + 1) + ". " + currencies[i]);
                }
                int toCurrencyIndex = scanner.nextInt() - 1;

                System.out.print("Digite o valor a ser convertido: ");
                double amount = scanner.nextDouble();

                scanner.close();



                System.out.println("Valor convertido: ");
            }

        }




