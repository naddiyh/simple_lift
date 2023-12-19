import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;

// Interface untuk Lift
interface Lift {
    void moveToFloor(int currentFloor, int destinationFloor, boolean allowEntry);
    void skipFloor(int floor);
    int getCurrentFloor();
}

// Implementasi kelas Lift
class SimpleLift implements Lift {

    //Menetapakan nilai maksimum lantai dan minimum
    private static final int MAX_FLOORS = 15;
    private static final int MIN_FLOOR = 1;

    private int currentFloor;

    public SimpleLift() {
        currentFloor = MIN_FLOOR;
    }

    @Override
    public void moveToFloor(int currentFloor, int destinationFloor, boolean allowEntry) {
        if (isValidFloor(currentFloor) && isValidFloor(destinationFloor)) {
            printMoveMessage(currentFloor, destinationFloor, allowEntry);

            this.currentFloor = destinationFloor;
        } else {
            System.out.println("Nomor lantai tidak valid. Silakan pilih lantai antara 1 dan 15.");
        }
    }

    @Override
    public void skipFloor(int floor) {
        if (isValidFloor(floor)) {
            System.out.println("Melewati lantai " + floor);
            this.currentFloor = floor;
        } else {
            System.out.println("Nomor lantai tidak valid. Silakan pilih lantai antara 1 dan 15.");
        }
    }

    @Override
    // Mengembalikan nilai lantai saat ini
    public int getCurrentFloor() {       
        return currentFloor;
    }
        // memeriksa apakah nomor lantai valid
    private boolean isValidFloor(int floor) {
        return floor >= MIN_FLOOR && floor <= MAX_FLOORS;
    }

    private void printMoveMessage(int currentFloor, int destinationFloor, boolean allowEntry) {
        // Menampilkan pesan naik, turun, atau sudah berada di lantai tujuan
        if (currentFloor < destinationFloor) {
            System.out.println("Naik dari lantai " + currentFloor + " menuju lantai " + destinationFloor);
        } else if (currentFloor > destinationFloor) {
            System.out.println("Turun dari lantai " + currentFloor + " menuju lantai " + destinationFloor);
        } else {
            System.out.println("Anda sudah berada di lantai tujuan.");
        }

        if (!allowEntry) {
            System.out.println("Anda telah tiba di lantai "+ destinationFloor);
        }
    }
}

// Kelas aplikasi LiftApp
public class LiftApp {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Lift lift = new SimpleLift();
            Queue<Integer> passengerQueue = new LinkedList<>();

            System.out.print("Lantai saat ini: ");
            int currentFloor = scanner.nextInt();
            System.out.print("Masukkan tujuan lantai (1-15) atau 0 untuk keluar: ");
            int destinationFloor = scanner.nextInt();

            // Loop utama
            while (true) {
                System.out.print("Apakah ada orang yang ingin masuk ke dalam lift di lantai ini? (y/n): ");
                char entryInput = scanner.next().charAt(0);
                boolean allowEntry = entryInput == 'y' || entryInput == 'Y';

                if (!allowEntry) {
                    // Jika tidak ada orang yang ingin masuk, lift langsung berpindah ke tujuan yang dimasukkan sebelumnya
                    lift.moveToFloor(currentFloor, destinationFloor, false);
                    currentFloor = destinationFloor;
                    break;
                }

                System.out.print("Masukkan tujuan lantai penumpang: ");
                int passengerDestination = scanner.nextInt();
                passengerQueue.offer(passengerDestination);

                // Loop untuk menangani penumpang dalam antrian
                while (!passengerQueue.isEmpty()) {
                    int nextDestination = passengerQueue.poll();
                    // Memindahkan lift ke lantai berikutnya sesuai dengan tujuan penumpang
                    lift.moveToFloor(currentFloor, nextDestination, true);
                    currentFloor = nextDestination;

                    // Menampilkan pesan jika masih ada penumpang dalam antrian
                    if (!passengerQueue.isEmpty()) {
                        System.out.println("Anda telah tiba di lantai " + currentFloor);
                    }

                    System.out.print("Apakah Anda ingin melanjutkan ke lantai berikutnya? (y/n): ");
                    char continueInput = scanner.next().charAt(0);

                    if (continueInput != 'y' && continueInput != 'Y') {
                        System.out.println("Terima kasih, keluar dari lift.");
                        System.exit(0);
                        break;
                    }
                }
            }
        }
    }
}
