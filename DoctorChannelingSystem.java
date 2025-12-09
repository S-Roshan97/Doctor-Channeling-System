import java.util.*;

class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }
}

class Patient {
    String name, mobile, email, city, medicalHistory;
    int age;

    Patient(String name, String mobile, String email, String city, int age, String medicalHistory) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.city = city;
        this.age = age;
        this.medicalHistory = medicalHistory;
    }

    public String toString() {
        return name + " (" + age + "), " + city + ", " + mobile;
    }
}

class Doctor {
    String id, name, specialization;
    List<String> timeSlots;
    double fee;
    Queue<Patient> appointmentQueue = new LinkedList<>();
    Map<String, Patient> bookedSlots = new HashMap<>();

    Doctor(String id, String name, String specialization, List<String> timeSlots, double fee) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.timeSlots = timeSlots;
        this.fee = fee;
    }

    public String toString() {
        return id + " - " + name + " [" + specialization + "] - Fee: Rs." + fee;
    }
}

public class DoctorChannelingSystem {
    static Scanner sc = new Scanner(System.in);
    static List<Patient> patients = new ArrayList<>();
    static List<Doctor> doctors = new ArrayList<>();

    public static void main(String[] args) {
        int choice;
        do {
            menu();
            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1 -> initialize();
                case 2 -> registerPatient();
                case 3 -> displayPatients();
                case 4 -> registerDoctor();
                case 5 -> searchDoctor();
                case 6 -> makeAppointment();
                case 7 -> displayAppointments();
                case 8 -> cancelAppointment();
                case 9 -> sortDoctorsByName();
                case 10 -> System.out.println("Exiting system...");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 10);
    }

    static void menu() {
        System.out.println("\nDoctor Channeling System");
        System.out.println("1 - Initialize");
        System.out.println("2 - Register patients");
        System.out.println("3 - Display patients");
        System.out.println("4 - Register doctors");
        System.out.println("5 - Search doctors");
        System.out.println("6 - Make appointments");
        System.out.println("7 - Display appointments");
        System.out.println("8 - Cancel appointments");
        System.out.println("9 - Sort doctors by name");
        System.out.println("10 - Exit");
        System.out.print("Enter Choice [1-10]: ");
    }

    static void initialize() {
        patients.clear();
        doctors.clear();
        System.out.println("System initialized successfully.");
    }

    static void registerPatient() {
        try {
            String name;
            do {
                System.out.print("Name: ");
                name = sc.nextLine().trim();
                if (name.isEmpty()) System.out.println("Name cannot be blank. Please try again.");
            } while (name.isEmpty());

            System.out.print("Mobile: ");
            String mobile = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("City: ");
            String city = sc.nextLine();

            int age;
            while (true) {
                try {
                    System.out.print("Age: ");
                    age = Integer.parseInt(sc.nextLine());
                    if (age <= 0) {
                        System.out.println("Age must be positive.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid age entered.");
                }
            }

            System.out.print("Medical History: ");
            String history = sc.nextLine();

            patients.add(new Patient(name, mobile, email, city, age, history));
            System.out.println("Patient registered.");
        } catch (Exception e) {
            System.out.println("Unexpected error during patient registration.");
        }
    }

    static void displayPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
        } else {
            for (Patient p : patients) {
                System.out.println(p);
            }
        }
    }

    static void registerDoctor() {
        try {
            System.out.print("Doctor ID: ");
            String id = sc.nextLine();
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Specialization: ");
            String spec = sc.nextLine();

            double fee;
            while (true) {
                try {
                    System.out.print("Consultation Fee: ");
                    fee = Double.parseDouble(sc.nextLine());
                    if (fee < 0) {
                        System.out.println("Fee cannot be negative. Try again.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid fee entered.");
                }
            }

            List<String> slots = new ArrayList<>();
            System.out.println("Enter available time slots (type 'done' to finish):");
            while (true) {
                String slot = sc.nextLine();
                if (slot.equalsIgnoreCase("done")) break;
                if (!slot.trim().isEmpty()) slots.add(slot.trim());
            }

            doctors.add(new Doctor(id, name, spec, slots, fee));
            System.out.println("Doctor registered.");
        } catch (Exception e) {
            System.out.println("Unexpected error during doctor registration.");
        }
    }

    static void searchDoctor() {
        System.out.print("Enter specialization: ");
        String spec = sc.nextLine();
        boolean found = false;
        for (Doctor d : doctors) {
            if (d.specialization.equalsIgnoreCase(spec)) {
                System.out.println(d);
                found = true;
            }
        }
        if (!found) System.out.println("No doctor found with this specialization.");
    }

    static void makeAppointment() {
        try {
            System.out.print("Enter patient name: ");
            String pname = sc.nextLine();
            Patient patient = findPatient(pname);

            System.out.print("Enter Doctor ID: ");
            String docId = sc.nextLine();
            Doctor doctor = findDoctor(docId);

            System.out.println("Available slots:");
            boolean slotAvailable = false;
            for (String slot : doctor.timeSlots) {
                if (!doctor.bookedSlots.containsKey(slot)) {
                    System.out.println("- " + slot);
                    slotAvailable = true;
                }
            }

            if (!slotAvailable) {
                System.out.println("Doctor is currently unavailable. All slots full.");
                return;
            }

            System.out.print("Select time slot: ");
            String selectedSlot = sc.nextLine();

            if (doctor.bookedSlots.containsKey(selectedSlot)) {
                doctor.appointmentQueue.add(patient);
                System.out.println("Slot full. You are added to the waiting list.");
            } else {
                doctor.bookedSlots.put(selectedSlot, patient);
                System.out.println("Appointment booked. Confirmation sent to: " + patient.email);
            }
        } catch (NotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error occurred during appointment booking.");
        }
    }

    static void displayAppointments() {
        for (Doctor d : doctors) {
            System.out.println("\nAppointments for Dr. " + d.name);
            if (d.bookedSlots.isEmpty()) {
                System.out.println("No appointments booked.");
            } else {
                for (Map.Entry<String, Patient> entry : d.bookedSlots.entrySet()) {
                    System.out.println("Slot: " + entry.getKey() + " -> " + entry.getValue().name);
                }
            }
        }
    }

    static void cancelAppointment() {
        try {
            System.out.print("Enter Doctor ID: ");
            String docId = sc.nextLine();
            Doctor doctor = findDoctor(docId);

            System.out.print("Enter time slot to cancel: ");
            String slot = sc.nextLine();
            if (doctor.bookedSlots.containsKey(slot)) {
                Patient removed = doctor.bookedSlots.remove(slot);
                System.out.println("Appointment for " + removed.name + " cancelled.");

                if (!doctor.appointmentQueue.isEmpty()) {
                    Patient nextPatient = doctor.appointmentQueue.poll();
                    doctor.bookedSlots.put(slot, nextPatient);
                    System.out.println("Next patient " + nextPatient.name + " assigned to slot.");
                }
            } else {
                System.out.println("No appointment in selected slot.");
            }
        } catch (NotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void sortDoctorsByName() {
        doctors.sort(Comparator.comparing(d -> d.name.toLowerCase()));
        System.out.println("Doctors sorted by name:");
        for (Doctor d : doctors) {
            System.out.println(d);
        }
    }

    static Patient findPatient(String name) throws NotFoundException {
        for (Patient p : patients) {
            if (p.name.equalsIgnoreCase(name)) return p;
        }
        throw new NotFoundException("Patient not found: " + name);
    }

    static Doctor findDoctor(String id) throws NotFoundException {
        for (Doctor d : doctors) {
            if (d.id.equalsIgnoreCase(id)) return d;
        }
        throw new NotFoundException("Doctor not found: " + id);
    }
}
