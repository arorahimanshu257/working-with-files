import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        // write your code here
        readFile();
        writeFile();
        readCsvFile();
        walkDirectoriesUsingStreamAPI();
        try {
            walkThroughDirectories();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void walkDirectoriesUsingStreamAPI() {
        Path path = Path.of("files");
        try {
            Stream<Path> stream = Files.walk(path);
            System.out.println("Total Files = " + stream.count());

            long directory = Files.walk(path).filter(Files::isDirectory).count();
            System.out.println("number of directories " + directory);
            long files = Files.walk(path).filter(Files::isDirectory).count();
            System.out.println("number of files " + files);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void walkThroughDirectories() throws IOException {
        Path path = Path.of("files");

        boolean directory = Files.isDirectory(path);
        System.out.println("Directory = " + directory);

        var visitor = new FileVisitor<Path>() {
            private long count = 0L;

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                count++;
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        };

        Files.walkFileTree(path, visitor);
        System.out.println("count = " + visitor.count);
    }

    private static void readCsvFile() {


        Path path = Path.of("files/username.csv");
        try (Stream<String> lines = Files.lines(path);) {

            lines.filter(line -> !line.startsWith("#"))
                    .flatMap(Main::lineToUser)
                    .forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Stream<User> lineToUser(String line) {
        try {
            String[] elements = line.split(";");

            String userName = elements[0];
            int id = Integer.parseInt(elements[1]);
            String firstName = elements[2];
            String lastName = elements[3];

            return Stream.of(new User(userName, id, firstName, lastName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }

    private static void readFile() {
        Path path = Path.of("files/someFile.txt");
        boolean exists = Files.exists(path);
        System.out.println("Files existence : " + exists);

        try (BufferedReader reader = Files.newBufferedReader(path);) {
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeFile() {
        Path path = Path.of("files/debug.log");
        // all the resources should be declared in try as parameters,
        // no flush is required
        try (BufferedWriter writer = Files.newBufferedWriter(path);
             BufferedWriter writer1 = new BufferedWriter(writer);
             PrintWriter printWriter = new PrintWriter(writer1);) {
            writer.write("Hello World !");
            printWriter.printf("\ni=%d\n", 12);
            System.out.println("file updated!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
