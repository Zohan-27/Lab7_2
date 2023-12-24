import java.io.*;
import java.util.Scanner;

abstract class Version implements Playable, Serializable {
    private String versionNumber;

    public Version(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
    public String toString() {
        return "Version Number: " + versionNumber;
    }

    abstract void display();
}

interface Playable {
    void pause();

    void stop();

    default void play() {

    }
}

class Video extends Version implements Playable, Serializable {
    private String videoName;

    public Video(String versionNumber, String videoName) {
        super(versionNumber);
        this.videoName = videoName;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    @Override
    public String toString() {
        return super.toString() + ", Video Name: " + videoName;
    }

    @Override
    protected void display() {
        System.out.println("Version Number: " + getVersionNumber());
        System.out.println("Video Name: " + videoName);
    }

    @Override
    public void play() {
        System.out.println("Playing video: " + videoName);
    }

    @Override
    public void pause() {
        System.out.println("Pausing video: " + videoName);
    }

    @Override
    public void stop() {
        System.out.println("Stopping video: " + videoName);
    }
}

class View extends Video implements Serializable {
    private int views;

    public View(String versionNumber, String videoName, int views) {
        super(versionNumber, videoName);
        this.views = views;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return super.toString() + ", Views: " + views;
    }

    @Override
    protected void display() {
        super.display();
        System.out.println("Views: " + views);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        Version[] versions = new Version[10];
        int versionIndex = 0;

        while (!exit) {
            System.out.println("=== Меню ===");
            System.out.println("1. Создать объект класса Video");
            System.out.println("2. Вывести всю информацию");
            System.out.println("3. Записать в файл");
            System.out.println("4. Прочитать из файла");
            System.out.println("5. Выход");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Введите номер версии: ");
                    String versionNumber = scanner.next();
                    System.out.print("Введите название видео: ");
                    String videoName = scanner.next();
                    System.out.print("Введите количество просмотров: ");
                    int numViews = scanner.nextInt();

                    versions[versionIndex] = new View(versionNumber, videoName, numViews);

                    System.out.println("Объект класса Video создан");
                    versionIndex++;
                    break;
                case 2:
                    for (int i = 0; i < versionIndex; i++) {
                        versions[i].display();
                    }
                    break;
                case 3:
                    try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("objects.txt"))) {
                        for (int i = 0; i < versionIndex; i++) {
                            outputStream.writeObject(versions[i]);
                        }
                        System.out.println("Объекты успешно записаны в файл.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("objects.txt"))) {
                        System.out.println("Чтение объектов из файла:");
                        Version obj;
                        while ((obj = (Version) inputStream.readObject()) != null) {
                            System.out.println(obj);
                        }
                    } catch (EOFException e) {
                        System.out.println("Чтение завершено.");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    exit = true;
                    System.out.println("Программа завершена.");
                    break;
                default:
                    System.out.println("Недопустимый вариант. Попробуйте еще раз.");
                    break;
            }

            System.out.println(); // Дополнительная пустая строка для разделения выводов меню
        }

        scanner.close();
    }
}
