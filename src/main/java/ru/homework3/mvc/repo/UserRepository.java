package ru.homework3.mvc.repo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.model.User;

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
    private final String dirUsers;
    private final String dirTasks;

    private final char SHARP = 35; //символ '#'

    public UserRepository(@Value("${csv.usersDir}") String dirUsers, @Value("${csv.tasksDir}") String dirTasks) {
        this.dirUsers = dirUsers;
        this.dirTasks = dirTasks;
        try {
            initUsers();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void initUsers() throws IOException {
        List<String> users = Files.readAllLines(Path.of(dirUsers), StandardCharsets.UTF_8);
        for (String userLine : users) {
            try {
                if (userLine.charAt(0) != SHARP && userLine.substring(0, 2).compareTo("//") != 0) {
                    String[] words = userLine.split("[,;]");
                    Integer id = Integer.parseInt(words[0]);
                    inMemoryUsers.put(id, new User(id, words[1], new HashMap<>()));
                    idUsers.add(id);
                }
            } catch (NumberFormatException | IndexOutOfBoundsException | NullPointerException ex) {}
        }
        idUsers = idUsers.stream().sorted((id1, id2) -> id1 - id2).collect(Collectors.toSet());
        initTasks();
    }

    private void initTasks() throws IOException {
        List<String> tasksLines = Files.readAllLines(Path.of(dirTasks), StandardCharsets.UTF_8);
        for (String taskLine : tasksLines) {
            try {
                if (taskLine.charAt(0) != 35 && taskLine.substring(0, 2).compareTo("//") != 0) {
                    String[] words = taskLine.split("[,;]");
                    Integer idTask = Integer.parseInt(words[0]);
                    Integer idUser = Integer.parseInt(words[1]);
                    Task task = new Task(idTask, idUser, words[2], words[3], words[4], "Новая");
                    if (inMemoryUsers.containsKey(idUser)) {
                        HashMap<Integer, Task> tasks = inMemoryUsers.get(idUser).getTasks();
                        if (tasks != null) {
                            tasks.put(idTask, task);
                            idTasks.add(idTask);
                        }
                    }
                    task.setStatus(words[5]);
                }
            } catch (NumberFormatException | IndexOutOfBoundsException | NullPointerException ex) {}
        }
        idTasks = idTasks.stream().sorted((id1, id2) -> id1 - id2).collect(Collectors.toSet());
    }

    public List<User> getUsers() {
        return new ArrayList<>(inMemoryUsers.values());
    }

    public List<Task> getTasks(Integer idUser) {
        return new ArrayList<>(inMemoryUsers.get(idUser).getTasks().values());
    }

    public Task getTask(Integer idUser, Integer idTask) {
        return inMemoryUsers.get(idUser).getTasks().get(idTask);
    }

    //добавляет пользователя в файл и память
    public boolean addUser(User user) {
        Integer idUser = generateIdUser();
        user.setId(idUser);
        idUsers.add(idUser);
        inMemoryUsers.put(idUser, user);
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
    public boolean removeUser(Integer keyUser){
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
    public boolean addTask(Task task) {
        Integer idTask = generateIdTask();
        idTasks.add(idTask);
        task.setId(idTask);
        Integer keyUser = task.getIdUser();
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
    public boolean changeTask(Task updateTask) {
        HashMap<Integer,Task> tasks = inMemoryUsers.get(updateTask.getIdUser()).getTasks();
        Integer idTask = updateTask.getId();
        Task taskCopy = tasks.get(idTask).clone();
        tasks.replace(idTask, updateTask);
        try {
            rewriteFileTasks();
        } catch (IOException ex) {
            tasks.replace(idTask, taskCopy);
            return false;
        }
        return true;
    }

    //Удаляет задачу из памяти и файла
    public boolean removeTask(Integer keyUser, Integer keyTask) {
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
            for (Integer keyUser : idUsers) {
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
    private Integer generateIdUser() {
        try {
            return Collections.max(idUsers) + 1;
        } catch (NoSuchElementException ex) {
            return 1;
        }
    }

    //Генерирует простой id для задачи
    private Integer generateIdTask() {
        try {
            return Collections.max(idTasks) + 1;
        } catch (NoSuchElementException ex) {
            return 1;
        }
    }

    //Перезаписывает файл задач
    private void rewriteFileTasks() throws IOException {
        HashMap<Integer, Task> tasks = new HashMap<>();
        for (Integer keyUser : idUsers) {
            tasks.putAll(inMemoryUsers.get(keyUser).getTasks());
        }
        List<String> lines = new ArrayList<>(tasks.size() + 1);
        lines.add("//id,idUser,Header,Description,Date\n#id должен быть уникальным");
        for (Integer keyTask : idTasks) {
            lines.add(tasks.get(keyTask).toStringCsv());
        }
        Files.write(Path.of(dirTasks), lines, StandardCharsets.UTF_8,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    //Перезаписывает файл пользователей
    private void rewriteFileUsers() throws IOException {
        List<String> lines = new ArrayList<>(inMemoryUsers.size() + 1);
        lines.add("#id,Name\n//id должен быть уникальным");
        for (Integer keyUser : idUsers) {
            lines.add(inMemoryUsers.get(keyUser).toStringCsv());
        }
        Files.write(Path.of(dirUsers), lines, StandardCharsets.UTF_8,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING
        );
    }
}
