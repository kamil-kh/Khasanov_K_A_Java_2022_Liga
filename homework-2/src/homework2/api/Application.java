package homework2.api;

import homework2.entity.Task;
import homework2.entity.User;
import homework2.repository.UserRepository;
import homework2.validator.Validator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Application {

    private UserRepository userRepository;

    private Scanner in;

    private final String messageError = "\nВыбранный пунтк не существет.\n";

    public Application () throws IOException {
        userRepository = new UserRepository();
        in = new Scanner(System.in);
    }

    //Основное меню
    public void start() {
        boolean flagExc = true;
        while (flagExc) {
            System.out.println("Пользователи:");
            userRepository.getUsers().keySet().stream()
                    .forEach(key -> System.out.printf("%d. " + userRepository.getUser(key).getName() + "\n", key));
            System.out.println("\nМеню:");
            System.out.println("1. Вывод задач пользователя.");
            System.out.println("2. Добавить пользователя.");
            System.out.println("3. Удалить пользователя.");
            System.out.println("4. Удалить всех пользователей.");
            System.out.println("5. Удалить все задачи.");
            System.out.println("0. Выход из программы.\n");
            System.out.print("Введите номер пункта: ");
            switch (Validator.validateInt(in.next())) {
                case 1:
                    flagExc = displayTasks();
                    break;
                case 2:
                    addUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    System.out.print(userRepository.clearUsers());
                    break;
                case 5:
                    System.out.print(userRepository.clearTasks());
                    break;
                case 0:
                    flagExc = false;
                    break;
                default:
                    System.out.println(messageError);
                    break;
            }
        }
    }

    //Меню добавления пользователя
    private void addUser() {
        System.out.print("Введите имя нового пользователя: ");
        try {
            userRepository.addUser(in.next());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Меню удаления пользователя
    private void deleteUser() {
        while (true) {
            System.out.print("Введите ID пользователя (для отмены введите '0'): ");
            int key = Validator.validateInt(in.next());
            if (key == 0) {
                break;
            }
            if (userRepository.getUsers().containsKey(key)) {
                userRepository.removeUser(key);
                break;
            } else {
                    System.out.println("Пользователь с выбранным ID отсутствует.");
            }
        }
    }

    //Меню выбора пользователя для дальнейшего вывода его задач
    private boolean displayTasks(){
        while(true) {
            System.out.print("Введите номер пользователя (для отмены введите '0'): ");
            int key = Validator.validateInt(in.next());
            if (key == 0) {
                break;
            }
            if (userRepository.getUsers().containsKey(key)) {
                User user = userRepository.getUser(key);
                if (user.getTasks().isEmpty()) {
                    System.out.printf("У пользователя {%s} отсутствуют задачи.\n\n", user.getName());
                    return true;
                }
                return infoTask(key);
            } else {
                System.out.println("Выбранный пользователь не существет.");
            }
        }
        return true;
    }

    //Вывод всех задач выбранного пользователя
    private boolean infoTask(int keyUser) {
        User user = userRepository.getUser(keyUser);
        HashMap<Integer, Task> tasks = user.getTasks();
        boolean flag = true, filter = false;
        while (flag) {
            System.out.printf("\nЗадачи пользователя {ID: %d | Имя: %s}:\n", user.getId(), user.getName());
            if (filter) {
                tasks.keySet().stream().sorted(
                        (key1, key2) -> tasks.get(key1).getStatus().compareTo(tasks.get(key2).getStatus())
                ).forEach(key -> System.out.println("ID задачи: " + tasks.get(key).getId() + " | Заголовок: " +
                        tasks.get(key).getHeader() + " | Описание: " + tasks.get(key).getDescription() +
                        " | Дата: " + tasks.get(key).getDate() + " | Статус: " + tasks.get(key).getStatus())
                );
            } else {
                tasks.keySet().stream().sorted(
                        (key1, key2) -> tasks.get(key1).getId() - tasks.get(key2).getId()).
                        forEach(key -> System.out.println("ID задачи: " + tasks.get(key).getId() +
                        " | Заголовок: " + tasks.get(key).getHeader() + " | Описание: " + tasks.get(key).getDescription() +
                        " | Дата: " + tasks.get(key).getDate() + " | Статус: " + tasks.get(key).getStatus())
                );
            }
            System.out.println("\nМеню:");
            System.out.println("1. Отфильтровать задачи по 'ID'.");
            System.out.println("2. Отфильтровать задачи по 'статусу'.");
            System.out.println("3. Добавить задачу.");
            System.out.println("4. Редактировать задачу.");
            System.out.println("5. Удалить задачу.");
            System.out.println("6. Вернуться в предыдущее меню.");
            System.out.println("0. Выход из программы.\n");
            System.out.print("Введите номер пункта: ");
            switch (Validator.validateInt(in.next())) {
                case 1:
                    filter = false;
                    break;
                case 2:
                    filter = true;
                    break;
                case 3:
                    addTask(keyUser);
                    break;
                case 4:
                    updateTask(keyUser);
                    break;
                case 5:
                    deleteTask(keyUser);
                    break;
                case 6:
                    flag = false;
                    System.out.println("");
                    break;
                case 0:
                    return false;
                default:
                    System.out.println(messageError);
                    break;
            }
        }
        return true;
    }

    //Меню изменения задачи
    private void updateTask(int keyUser) {
        HashMap<Integer,Task> tasks = userRepository.getUsers().get(keyUser).getTasks();
        while(true) {
            System.out.print("Введите ID задачи (для отмены введите '0'): ");
            int key = Validator.validateInt(in.next());
            if (key == 0) {
                break;
            }
            if (tasks.containsKey(key)) {
                System.out.print("Введите новое описание задачи: ");
                String description = in.next();
                String date, status = "Новая";
                while (true) {
                    System.out.print("Введите новую дату в формате [dd.mm.yyyy]: ");
                    date = in.next();
                    if (Validator.validateDate(date)) {
                        break;
                    } else {
                        System.out.println("Неверный формат даты.");
                    }
                }
                boolean flag = true;
                while (flag) {
                    System.out.print("Установить статус ['1'->{новая} | '2'->{в работе} |" +
                            " '3'->{готово}]: ");
                    switch (Validator.validateInt(in.next())) {
                        case 1:
                            flag = false;
                            break;
                        case 2:
                            status = "В работе";
                            flag = false;
                            break;
                        case 3:
                            status = "Готово";
                            flag = false;
                            break;
                        default:
                            System.out.println(messageError);
                            break;
                    }
                }
                System.out.print(userRepository.changeTask(keyUser, key, description, date, status));
                break;
            } else {
                System.out.println("Выбранная задача не существет.");
            }
        }
    }

    //Меню добавления задачи
    private void addTask(int keyUser) {
        System.out.print("Введите название задачи: ");
        String header = in.next();
        System.out.print("Введите описание задачи: ");
        String description = in.next();
        String date;
        while (true) {
            System.out.print("Введите дату в формате [dd.mm.yyyy]: ");
            date = in.next();
            if (Validator.validateDate(date)) {
                break;
            } else {
                System.out.println("Неверный формат даты.");
            }
        }
        System.out.print(userRepository.addTask(keyUser, header, description, date));
    }

    //Меню удаления задачи
    private void deleteTask(int keyUser) {
        HashMap<Integer, Task> tasks = userRepository.getUser(keyUser).getTasks();
        while (true) {
            System.out.print("Введите ID задачи (для отмены введите '0'): ");
            int key = Validator.validateInt(in.next());
            if (key == 0) {
                break;
            }
            if (tasks.containsKey(key)) {
                System.out.print(userRepository.removeTask(keyUser, key));
                break;
            } else {
                System.out.println("Задача с выбранным ID отсутствует.");
            }
        }
    }

}
