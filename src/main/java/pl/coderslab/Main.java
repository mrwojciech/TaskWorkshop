package pl.coderslab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static String[][] tasks;

    public static void main(String[] args) {
        tasks = tasks();

        printMenu();
    }

    public static void printMenu() {
        boolean programRunning = true;
        while (programRunning) {

            System.out.println(ConsoleColors.BLUE + "Please select an option:" + ConsoleColors.RESET);
            String[] options = {"add", "remove", "list", "exit"};
            for (String option : options) {
                System.out.println(option);
            }

            System.out.print(ConsoleColors.GREEN);
            Scanner scanner = new Scanner(System.in);
            System.out.print(ConsoleColors.RESET);

            String option = scanner.next();
            switch (option) {
                case "add" -> add();
                case "remove" -> remove();
                case "list" -> list();
                case "exit" -> {
                    save();
                    programRunning = false;
                }
                default -> System.out.println("Invalid value");
            }
        }
    }

    private static void save() {
        StringBuilder content = new StringBuilder();
        for (String[] task : tasks) {
            content.append(String.join(", ", task)).append(System.lineSeparator());
        }
        try {
            Files.writeString(Paths.get("tasks.csv"), content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void remove() {
        System.out.println("Please select number to remove.");
        System.out.print(ConsoleColors.GREEN);
        Scanner scanner = new Scanner(System.in);
        System.out.print(ConsoleColors.RESET);

        int index = 0;
        boolean askForInput = true;

        while (askForInput) {
            String value = scanner.next();
            char[] charArray = value.toCharArray();
            boolean isDigit = true;
            for (int i = 0; i < value.length(); i++) {
                if (Character.isLetter(charArray[i])) {
                    System.out.println("Incorrect argument passed. Please give number greater or equal 0.");
                    isDigit = false;
                    break;
                }
            }
            if (isDigit) {
                index = Integer.parseInt(value);
                if (index < 0) {
                    System.out.println("Incorrect argument passed. Please give number greater or equal 0.");
                } else if (index >= tasks.length) {
                    System.out.println("Incorrect argument passed. Please give number smaller or equal " + tasks.length);
                } else {
                    askForInput = false;
                }
            }
        }

        tasks[index][0] = tasks[tasks.length - 1][0];
        tasks[index][1] = tasks[tasks.length - 1][1];
        tasks[index][2] = tasks[tasks.length - 1][2];
        tasks = Arrays.copyOf(tasks, tasks.length - 1);
        System.out.println("Value was successfully deleted");
    }

    private static void list() {
        for (int i = 0; i < tasks.length; i++) {
            System.out.println(i + " : " + tasks[i][0] + " " + tasks[i][1] + " " + tasks[i][2]);
        }
    }

    private static void add() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description: ");
        System.out.print(ConsoleColors.GREEN);
        String task = scanner.next();
        System.out.print(ConsoleColors.RESET);
        System.out.println("Please add task due date: ");
        System.out.print(ConsoleColors.GREEN);
        String date = scanner.next();
        System.out.print(ConsoleColors.RESET);
        System.out.println("Is task important: true/ false");
        System.out.print(ConsoleColors.GREEN);
        String isImportant = scanner.next();
        System.out.print(ConsoleColors.RESET);
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        int lastRecord = tasks.length - 1;
        tasks[lastRecord] = new String[3];
        tasks[lastRecord][0] = task;
        tasks[lastRecord][1] = date;
        tasks[lastRecord][2] = isImportant;
    }

    static String[][] tasks() {
        String[] line = new String[1];
        int index = 0;

        try {
            Scanner scanner = new Scanner(Paths.get("tasks.csv"));
            while (scanner.hasNext()) {
                line[index] = scanner.nextLine();
                index++;
                if (scanner.hasNext()) {
                    line = Arrays.copyOf(line, line.length + 1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[][] tasks = new String[index][3];
        for (int i = 0; i < line.length; i++) {
            tasks[i][0] = line[i].split(",")[0];
            tasks[i][1] = line[i].split(",")[1];
            tasks[i][2] = line[i].split(",")[2];
        }

        return tasks;
    }
}