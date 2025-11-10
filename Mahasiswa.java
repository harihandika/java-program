import java.util.Objects;
import java.util.regex.Pattern;

public class Mahasiswa {

    private static final Pattern NIM_PATTERN = Pattern.compile("\\d{6,12}");
    private static final double MIN_SCORE = 0.0;
    private static final double MAX_SCORE = 100.0;
    private static final double PASSING_SCORE = 70.0;

    private String nama;
    private String nim;
    private double nilai;

    public Mahasiswa() {
    }

    /**
     * @param words input array of words (may contain nulls; they are ignored)
     * @param k     number of top elements to return (k <= 0 -> empty list)
     * @return unmodifiable list of top-k words (size <= k)
     * @throws NullPointerException if words is null
     */
    public Mahasiswa(String nama, String nim, double nilai) {
        setData(nama, nim, nilai);
    }

    public void setData(String nama, String nim, double nilai) {
        Objects.requireNonNull(nama, "name cannot be null");
        Objects.requireNonNull(nim, "nim cannot be null");

        String trimmedNama = nama.trim();
        String trimmedNim = nim.trim();

        if (trimmedNama.isEmpty()) {
            throw new IllegalArgumentException("nama cannot be empty");
        }
        if (!NIM_PATTERN.matcher(trimmedNim).matches()) {
            throw new IllegalArgumentException("nim not valid: must 6-12 digit");
        }
        if (nilai < MIN_SCORE || nilai > MAX_SCORE) {
            throw new IllegalArgumentException(
                    String.format("the value must between %.1f dan %.1f", MIN_SCORE, MAX_SCORE));
        }

        this.nama = trimmedNama;
        this.nim = trimmedNim;
        this.nilai = nilai;
    }

    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public double getNilai() {
        return nilai;
    }

    public boolean isPassed() {
        return this.nilai > PASSING_SCORE;
    }

    public String getStatus() {
        return isPassed() ? "Passed" : "Failed";
    }

    public void printStatus() {
        System.out.printf("Name: %s (NIM: %s) - Score: %.1f - Status: %s%n",
                nama, nim, nilai, getStatus());
    }

    @Override
    public String toString() {
        return "Mahasiswa{" +
                "name='" + nama + '\'' +
                ", nim='" + nim + '\'' +
                ", score=" + nilai +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Mahasiswa that = (Mahasiswa) o;
        return Double.compare(that.nilai, nilai) == 0 &&
                Objects.equals(nama, that.nama) &&
                Objects.equals(nim, that.nim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nama, nim, nilai);
    }

    public static void main(String[] args) {
        System.out.println("===== VALID TEST CASES =====");

        Mahasiswa m1 = new Mahasiswa("Siti", "123456", 85.0);
        m1.printStatus();

        Mahasiswa m2 = new Mahasiswa();
        m2.setData("Anto", "000987", 65.5);
        m2.printStatus();

        Mahasiswa m3 = new Mahasiswa("Rina", "987654", 70.0);
        m3.printStatus();

        System.out.println("\n===== INVALID TEST CASES =====");

        try {
            Mahasiswa invalid1 = new Mahasiswa("", "123456", 90.0);
            invalid1.printStatus();
        } catch (Exception ex) {
            System.err.println("Invalid student data: " + ex.getMessage());
        }

        try {
            Mahasiswa invalid2 = new Mahasiswa("Budi", "ABC123", 80.0);
            invalid2.printStatus();
        } catch (Exception ex) {
            System.err.println("Invalid student data: " + ex.getMessage());
        }

        try {
            Mahasiswa invalid3 = new Mahasiswa("Dewi", "999999", 120.0);
            invalid3.printStatus();
        } catch (Exception ex) {
            System.err.println("Invalid student data: " + ex.getMessage());
        }
    }
}