# Doctor-Channeling-System

# Doctor Channeling System

Simple command-line Java application to register patients and doctors, book and cancel appointments, and manage waiting lists.

## Features
- Register patients (name, mobile, email, city, age, medical history)
- Register doctors (ID, name, specialization, consultation fee, available time slots)
- Search doctors by specialization
- Book appointments for patients with selected doctor/time slot
  - If selected slot is already booked, patient is added to the doctor's waiting queue
- Display all patients and appointments
- Cancel appointment (automatically assigns next patient in waiting queue to the freed slot)
- Sort doctors by name
- Basic validation for empty name, positive age and non-negative fee
- Simple, single-file Java implementation (DoctorChannelingSystem.java)

## Requirements
- Java 8 or later
- javac and java on your PATH

## Build and run
1. Download or clone the repository (this contains DoctorChannelingSystem.java).
2. Compile:
   javac DoctorChannelingSystem.java
3. Run:
   java DoctorChannelingSystem

## Usage
When you run the program you'll see a menu with numbered choices:
1. Initialize — clears all registered patients and doctors
2. Register patients — interactive prompts for patient details
3. Display patients — list all registered patients
4. Register doctors — interactive prompts including entering available time slots (type `done` to finish)
5. Search doctors — search by specialization
6. Make appointments — prompt for patient name and doctor ID, then show available slots and book or add to waiting list
7. Display appointments — shows booked slots per doctor
8. Cancel appointments — free a slot and auto-assign next patient from the waiting queue (if any)
9. Sort doctors by name — sorts and prints doctors
10. Exit

Example flow:
- Register a patient (choose 2)
- Register a doctor with slots (choose 4)
- Make an appointment (choose 6): enter patient name, doctor ID, choose a slot
- If the slot is already booked, they are added to the doctor's waiting list
- Cancel an appointment (choose 8): freeing a slot assigns the next waiting patient to it automatically

## Code overview
The single source file DoctorChannelingSystem.java contains:

- NotFoundException
  - Custom exception thrown when a patient or doctor is not found.
- Patient
  - Fields: name, mobile, email, city, age, medicalHistory
  - toString prints basic contact/age/city info.
- Doctor
  - Fields: id, name, specialization, List<String> timeSlots, fee
  - Holds appointmentQueue (Queue<Patient>) for waiting list and bookedSlots (Map<String, Patient>) mapping time slot -> patient
- DoctorChannelingSystem
  - Main interactive CLI, maintains in-memory lists of patients and doctors.
  - Methods:
    - menu(), initialize(), registerPatient(), displayPatients()
    - registerDoctor(), searchDoctor(), makeAppointment(), displayAppointments()
    - cancelAppointment(), sortDoctorsByName()
    - findPatient(name) and findDoctor(id) throw NotFoundException when not found

Appointment logic:
- When making an appointment, the program checks doctor.timeSlots and doctor.bookedSlots:
  - If a slot is free, it is assigned to the patient and stored in bookedSlots.
  - If the chosen slot is already booked, the patient is added to the doctor's appointmentQueue (waiting list).
- When an appointment is canceled for a slot, if the queue is non-empty the next patient is popped and assigned to the freed slot.

## Validation and error handling
- Basic validations:
  - Patient name cannot be blank.
  - Age must be a positive integer.
  - Consultation fee must be non-negative.
- findPatient and findDoctor throw NotFoundException which is caught and reported to the user.
- Most I/O is guarded with try/catch to avoid crash on unexpected input.

## Limitations / Known issues
- No persistence: all data is stored in memory and lost when the program exits.
- Time slots are plain strings (no date/time type), so no validation or ordering of slots.
- Minimal validation for mobile/email format.
- Single-threaded CLI; not safe for concurrent use.
- No user authentication or role separation (patients vs admin).
- Doctor ID uniqueness is not enforced (duplicate IDs may cause ambiguous behavior).
- No unit tests included.

## Suggested improvements
- Add persistence (save/load to JSON, CSV, or a database).
- Use java.time.LocalDateTime or structured slot objects for accurate slot handling.
- Validate mobile and email formats.
- Enforce unique doctor IDs.
- Add unit tests and input-driven integration tests.
- Provide a REST API or GUI/web frontend for multi-user access.
- Add logging and better error messages.
- Consider thread-safety if moving to a server model.

## Contributing
- Fork the repo, make changes, and open a pull request.
- Improvements that would be especially useful: persistence, slot typing, validation, unit tests, and a more robust CLI or web UI.

