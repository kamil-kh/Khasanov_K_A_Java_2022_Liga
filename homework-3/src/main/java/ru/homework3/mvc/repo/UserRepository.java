package ru.homework3.mvc.repo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRepository {
    private HashMap<Integer, User> inMemoryUsers = new HashMap<>();
    //Хранит id всех пользователей
    private Set<Integer> idUsers = new HashSet<>();
    //Хранит id всех задач
    private Set<Integer> idTasks = new HashSet<>();
    private final String dirUsers = "csv/Users.csv";
    private final String dirTasks = "csv/Tasks.csv";

    public UserRepository() {
        try {
            init();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Выгружает данные из файлов
    private void init() throws IOException {
        System.out.println(dirTasks);
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
                    Task task = new Task(id, idUser, words[2], words[3], words[4], "Новая");
                    ((User)inMemoryUsers.get(idUser)).getTasks().put(id, task);
                    idTasks.add(id);
                    task.setStatus(words[5]);
                }
            } catch (NumberFormatException ex) {
                System.out.println(ex.getMessage());
            } catch (IndexOutOfBoundsException ex) {
            } catch (NullPointerException ex) {}
        }
        reader.close();
    }

    public List<User> getUsers() {
        return new ArrayList<User>(inMemoryUsers.values());
    }

    public List<Task> getTasks(int idUser) {
        return new ArrayList<Task>(inMemoryUsers.get(idUser).getTasks().values());
    }

    public Task getTask(int idUser, int idTask) {
        return inMemoryUsers.get(idUser).getTasks().get(idTask);
    }

    //добавляет пользователя в файл и память
    public boolean addUser(String name) {
        int idUser = generateIdUser();
        idUsers.add(idUser);
        inMemoryUsers.put(idUser, new User(idUser, name, new HashMap<Integer, Task>()));
        try {
            rewriteFileUsers();
        } catch (IOException ex) {
            idUsers.remove(idUser);
            inMemoryUsers.remove(idUser);
            return false;
        }
        return true;
    }

    //Удаляет пользователя из памяти и файла
    public boolean removeUser(int keyUser){
        User user = inMemoryUsers.remove(keyUser);
        idUsers.remove(keyUser);
        try {
            rewriteFileUsers();
            rewriteFileTasks();
        } catch (IOException ex) {
            inMemoryUsers.put(keyUser, user);
            idUsers.add(keyUser);
            return false;
        }
        return true;
    }

    //Удаляет всех пользователей из памяти и файла. Также удаляет все задачи, так как они не будут иметь зависимости.
    public boolean clearUsers() {
        HashMap<Integer, User> users = (HashMap<Integer,User>)inMemoryUsers.clone();
        inMemoryUsers.clear();
        try {
            BufferedWriter writer = Files.newBufferedWriter(Path.of(dirTasks),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            writer.close();
            writer = Files.newBufferedWriter(Path.of(dirUsers),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            writer.close();
            idUsers.clear();
            idTasks.clear();
        } catch (IOException ex) {
            inMemoryUsers = users;
            return false;
        }
        return true;
    }

    //Добавляет задачу в память и файл
    public boolean addTask(int keyUser, String header, String description, String date) {
        int idTask = generateIdTask();
        idTasks.add(idTask);
        Task task = new Task(idTask, keyUser, header, description, date, "Новая");
        try {
            inMemoryUsers.get(keyUser).getTasks().put(idTask, task);
            rewriteFileTasks();
        } catch (IOException ex) {
            inMemoryUsers.get(keyUser).getTasks().remove(task);
            idTasks.remove(idTask);
            return false;
        }
        return true;
    }

    //Изменяет задачу в памяти и файле
    public boolean changeTask(int keyUser, int keyTask, String description, String date, String status) {
        Task task = inMemoryUsers.get(keyUser).getTasks().get(keyTask);
        String oldDescription = task.getDescription();
        String oldDate = task.getDate();
        String oldStatus = task.getStatus();
        task.setDescription(description);
        task.setDate(date);
        task.setStatus(status);
        try {
            rewriteFileTasks();
        } catch (IOException ex) {
            task.setDescription(oldDescription);
            task.setDate(oldDate);
            task.setStatus(oldStatus);
            return false;
        }
        return true;
    }

    //Удаляет задачу из памяти и файла
    public boolean removeTask(int keyUser, int keyTask) {
        HashMap<Integer, Task> tasks = inMemoryUsers.get(keyUser).getTasks();
        Task task = tasks.remove(keyTask);
        idTasks.remove(keyTask);
        try {
            rewriteFileTasks();
        } catch (IOException ex) {
            tasks.put(keyTask, task);
            idTasks.add(keyTask);
            return false;
        }
        return true;
    }

    //Удаляет все задачи из памяти и файла
    public boolean clearTasks() {
        HashMap<Integer, User> users = (HashMap<Integer,User>)inMemoryUsers.clone();
        try {
            for (int keyUser : idUsers) {
                inMemoryUsers.get(keyUser).getTasks().clear();
            }
            BufferedWriter writer = Files.newBufferedWriter(Path.of(dirTasks),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            writer.close();
        } catch (IOException ex) {
            inMemoryUsers = users;
            return false;
        }
        return true;
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
        writer.write("//id,idUser,Header,Description,Date\n" + "#id должен быть уникальным\n");
        HashMap<Integer, Task> tasks = new HashMap<>();
        for (int keyUser : idUsers) {
            tasks.putAll(inMemoryUsers.get(keyUser).getTasks());
        }
        idTasks = tasks.keySet().stream().sorted((key1, key2) -> tasks.get(key1).getId() - tasks.get(key2).getId())
                .collect(Collectors.toSet());
        for (int keyTask : idTasks) {
            Task task = tasks.get(keyTask);
            writer.write(Integer.toString(task.getId()) + "," + Integer.toString(task.getIdUser()) +
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
        writer.write ("#id,Name\n" + "//id должен быть уникальным\n");
        idUsers = idUsers.stream().sorted((id1, id2) -> id1 - id2).collect(Collectors.toSet());
        for (int keyUser : idUsers) {
            writer.write(Integer.toString(keyUser) + "," + inMemoryUsers.get(keyUser).getName() + "\n");
        }
        writer.close();
    }
}
