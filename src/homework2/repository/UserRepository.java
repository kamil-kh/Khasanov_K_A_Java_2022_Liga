package homework2.repository;

import homework2.entity.Task;
import homework2.entity.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class UserRepository {

    //Хранит выгруженных пользователей из файла
    private HashMap<Integer, User> inMemoryUsers = new HashMap<>();
    //Хранит id всех пользователей
    private Set<Integer> idUsers = new HashSet<>();
    //Хранит id всех задач
    private Set<Integer> idTasks = new HashSet<>();
    private final String dirUsers = "csv/Users.csv";
    private final String dirTasks = "csv/Tasks.csv";

    public UserRepository() throws IOException {
        init();
    }

    //Выгружает данные из файлов
    private void init() throws IOException {
        BufferedReader reader = Files.newBufferedReader(Path.of(dirUsers), StandardCharsets.UTF_8);
        String line;
        String[] words;
        int id;
        for (line = reader.readLine(); line != null; line = reader.readLine()) {
            try {
                if (line.charAt(0) != 35 && line.substring(0, 2).compareTo("//") != 0) {
                    words = line.split("[,;]");
                    id = Integer.parseInt(words[0]);
                    inMemoryUsers.put(id, new User(id, words[1], new HashMap()));
                    idUsers.add(id);
                }
            } catch (NumberFormatException ex) {
                System.out.println(ex.getMessage());
            } catch (IndexOutOfBoundsException ex) {
                System.out.println(ex.getMessage());
            }
        }
        reader.close();
        reader = Files.newBufferedReader(Path.of(dirTasks), StandardCharsets.UTF_8);
        for (line = reader.readLine(); line != null; line = reader.readLine()) {
            try {
                if (line.charAt(0) != 35 && line.substring(0, 2).compareTo("//") != 0) {
                    words = line.split("[,;]");
                    id = Integer.parseInt(words[0]);
                    int idUser = Integer.parseInt(words[1]);
                    Task task = new Task(id, idUser, words[2], words[3], words[4]);
                    ((User)inMemoryUsers.get(idUser)).getTasks().put(id, task);
                    idTasks.add(id);
                }
            } catch (NumberFormatException ex) {
                System.out.println(ex.getMessage());
            } catch (IndexOutOfBoundsException ex) {
                System.out.println(ex.getMessage());
            } catch (NullPointerException ex) {}
        }
        reader.close();
    }

    //Удаляет задачу из памяти и файла
    public String removeTask(int keyUser, int keyTask) {
        try {
            inMemoryUsers.get(keyUser).getTasks().remove(keyTask);
            idTasks.remove(keyTask);
            rewriteFileTasks();
            return "";
        } catch (IOException ex) {
            return "\nОшибка изменения файлов";
        }
    }

    //Удаляет пользователя из памяти и файла
    public String removeUser(int keyUser){
        try {
            inMemoryUsers.remove(keyUser);
            idUsers.remove(keyUser);
            rewriteFileUsers();
            rewriteFileTasks();
            return "";
        } catch (IOException ex) {
            return "\nОшибка изменения файлов";
        }
    }

    //Добавляет задачу в память и файл
    public String addTask(int keyUser, String header, String description, String date) {
        try {
            int idTask = generateIdTask();
            idTasks.add(idTask);
            Task task = new Task(idTask, keyUser, header, description, date);
            inMemoryUsers.get(keyUser).getTasks().put(idTask, task);
            rewriteFileTasks();
            return "";
        } catch (IOException ex) {
            return "\nОшибка записи в файл Task.csv";
        }
    }

    //добавляет пользователя в файл и память
    public void addUser(String name) throws IOException {
        int idUser = generateIdUser();
        idUsers.add(idUser);
        inMemoryUsers.put(idUser, new User(idUser, name, new HashMap<Integer, Task>()));
        rewriteFileUsers();
    }

    //Изменяет задачу в памяти и файле
    public String changeTask(int keyUser, int keyTask, String description, String date, String status) {
        try {
            Task task = inMemoryUsers.get(keyUser).getTasks().get(keyTask);
            task.setDescription(description);
            task.setDate(date);
            task.setStatus(status);
            rewriteFileTasks();
            return "";
        } catch (IOException ex) {
            return "\nОшибка записи в файл Task.csv";
        }
    }

    //Удаляет всех пользователей из памяти и файла. Также удаляет все задачи, так как они не будут иметь зависимости.
    public String clearUsers() {
        try {
            inMemoryUsers.clear();
            BufferedWriter writer = Files.newBufferedWriter(Path.of(dirTasks), StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING
            );
            writer.close();
            writer = Files.newBufferedWriter(Path.of(dirUsers), StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING
            );
            writer.close();
            return "";
        } catch (IOException ex) {
            return "\nОшибка записи в файл User.csv";
        }
    }

    //Удаляет все задачи из памяти и файла
    public String clearTasks() {
        try {
            for (int keyUser : idUsers) {
                inMemoryUsers.get(keyUser).getTasks().clear();
            }
            BufferedWriter writer = Files.newBufferedWriter(Path.of(dirTasks), StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING
            );
            writer.close();
            return "";
        } catch (IOException ex) {
            return "\nОшибка записи в файл Task.csv";
        }
    }

    //Возвращает копию памяти всех пользователей
    public HashMap<Integer,User> getUsers() {
        return (HashMap<Integer,User>)inMemoryUsers.clone();
    }

    //Возвращает копию пользователя по ключу
    public User getUser(int key) {
        return inMemoryUsers.get(key).clone();
    }

    //Генерирует простой id для пользователя
    private int generateIdUser() {
        try {
            return Collections.max(idUsers) + 1;
        } catch (NoSuchElementException ex) {
            return 1;
        }
    }

    //Генерирует простой id для задачи
    private int generateIdTask() {
        try {
            return Collections.max(idTasks) + 1;
        } catch (NoSuchElementException ex) {
            return 1;
        }
    }

    //Перезаписывает файл задач
    private void rewriteFileTasks() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Path.of(dirTasks), StandardCharsets.UTF_8,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING
        );
        writer.write("//id,idUser,Header,Description,Date\n" +
                "#id должен быть уникальным\n");
        HashMap<Integer, Task> tasks = new HashMap<>();
        for (int keyUser : idUsers) {
            tasks.putAll(inMemoryUsers.get(keyUser).getTasks());
        }
        idTasks = tasks.keySet().stream().sorted((key1, key2) -> tasks.get(key1).getId() - tasks.get(key2).getId())
                .collect(Collectors.toSet());
        for (int keyTask : idTasks) {
            Task task = tasks.get(keyTask);
            writer.write(Integer.toString(task.getId()) + "," + Integer.toString(task.getUserId()) +
                    "," + task.getHeader() + "," + task.getDescription() + "," + task.getDate() + "," +
                    task.getStatus() + "\n"
            );
        }
        writer.close();
    }

    //Перезаписывает файл пользователей
    private void rewriteFileUsers() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter (
                Path.of(dirUsers), StandardCharsets.UTF_8,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING
        );
        writer.write (
                "#id,Name\n" +
                "//id должен быть уникальным\n"
        );
        idUsers = idUsers.stream().sorted((id1, id2) -> id1 - id2).collect(Collectors.toSet());
        for (int keyUser : idUsers) {
            writer.write(Integer.toString(keyUser) + "," + inMemoryUsers.get(keyUser).getName() + "\n");
        }
        writer.close();
    }

}
