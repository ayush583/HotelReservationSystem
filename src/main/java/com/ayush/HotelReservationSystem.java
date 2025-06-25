package com.ayush;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

public class HotelReservationSystem {

    private static SessionFactory factory;

    public static void main(String[] args) throws InterruptedException {
        factory = new Configuration().configure().buildSessionFactory();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nHotel Reservation System");
            System.out.println("1. Reserve a room");
            System.out.println("2. View Reservations");
            System.out.println("3. Get Room Number");
            System.out.println("4. Update Reservation");
            System.out.println("5. Delete Reservation");
            System.out.println("0. Exit");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    reserveRoom(sc);
                    break;
                case 2:
                    viewReservations();
                    break;
                case 3:
                    getRoomNumber(sc);
                    break;
                case 4:
                    updateReservation(sc);
                    break;
                case 5:
                    deleteReservation(sc);
                    break;
                case 0: {
                    exit();
                    factory.close();
                    sc.close();
                    return;
                }
                default:
                    System.out.println("Invalid Choice. Try again");
            }
        }
    }

    private static void reserveRoom(Scanner sc) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        sc.nextLine(); // Clear input buffer
        System.out.print("Enter guest name: ");
        String guestName = sc.nextLine();
        System.out.print("Enter room number: ");
        int roomNumber = sc.nextInt();
        System.out.print("Enter contact number: ");
        String contactNumber = sc.next();

        Reservation r = new Reservation();
        r.setGuestName(guestName);
        r.setRoomNumber(roomNumber);
        r.setContactNumber(contactNumber);

        session.persist(r);
        tx.commit();
        session.close();

        System.out.println("Reservation Successful: " + r);
    }

    private static void viewReservations() {
        Session session = factory.openSession();
        List<Reservation> list = session.createQuery("from Reservation", Reservation.class).list();
        session.close();

        System.out.println("\nReservations:");
        for (Reservation r : list) {
            System.out.println(r);
        }
    }

    private static void getRoomNumber(Scanner sc) {
        System.out.print("Enter reservation ID: ");
        int id = sc.nextInt();

        Session session = factory.openSession();
        Reservation r = session.get(Reservation.class, id);
        session.close();

        if (r != null) {
            System.out.println("Room number for reservation ID " + id + ": " + r.getRoomNumber());
        } else {
            System.out.println("Reservation not found.");
        }
    }

    private static void updateReservation(Scanner sc) {
        System.out.print("Enter reservation ID to update: ");
        int id = sc.nextInt();
        sc.nextLine(); // Clear buffer

        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Reservation r = session.get(Reservation.class, id);

        if (r != null) {
            System.out.print("Enter new guest name: ");
            r.setGuestName(sc.nextLine());
            System.out.print("Enter new room number: ");
            r.setRoomNumber(sc.nextInt());
            System.out.print("Enter new contact number: ");
            r.setContactNumber(sc.next());

            session.update(r);
            tx.commit();
            System.out.println("Updated: " + r);
        } else {
            System.out.println("Reservation not found.");
            tx.rollback();
        }

        session.close();
    }

    private static void deleteReservation(Scanner sc) {
        System.out.print("Enter reservation ID to delete: ");
        int id = sc.nextInt();

        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Reservation r = session.get(Reservation.class, id);

        if (r != null) {
            session.remove(r);
            tx.commit();
            System.out.println("Deleted successfully.");
        } else {
            System.out.println("Reservation not found.");
            tx.rollback();
        }

        session.close();
    }

    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        for (int i = 5; i > 0; i--) {
            System.out.print(".");
            Thread.sleep(1000);
        }
        System.out.println("\nThank you for using Hotel Reservation System!");
    }
}
