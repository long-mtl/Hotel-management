package canhan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.text.DecimalFormat;

class Room {
    private int roomNumber;
    private boolean isBooked;
    private String guestName;
    private LocalDate checkInDate;
    private double roomPrice;

    public Room(int roomNumber, double roomPrice) {
        this.roomNumber = roomNumber;
        this.isBooked = false;
        this.guestName = "";
        this.roomPrice = roomPrice;
    }

    public int getRoomNumber() { return roomNumber; }
    public boolean isBooked() { return isBooked; }
    public String getGuestName() { return guestName; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public double getRoomPrice() { return roomPrice; }

    public void bookRoom(String guestName, LocalDate checkInDate) {
        this.isBooked = true;
        this.guestName = guestName;
        this.checkInDate = checkInDate;
    }

    public double checkOut(LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate.isBefore(checkInDate)) {
            return 0;
        }
        long daysStayed = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        this.isBooked = false;
        this.guestName = "";
        this.checkInDate = null;
        return daysStayed * roomPrice;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " - " + (isBooked ? "Booked by: " + guestName + " | Price: " + roomPrice + " VND/day" : "Available | Price: " + roomPrice + " VND/day");
    }
}

public class HeThongKhachSanGUI extends JFrame {
    private static final int NUMBER_OF_ROOMS = 10;
    private Room[] roomList;
    private JTextArea textArea;
    private JTextField guestNameField, checkInField, checkOutField, roomNumberField;
    private DecimalFormat df;

    public HeThongKhachSanGUI() {
        roomList = new Room[NUMBER_OF_ROOMS];
        df = new DecimalFormat("###,### VND");
        for (int i = 0; i < NUMBER_OF_ROOMS; i++) {
            roomList[i] = new Room(i + 1, 500000 + (i * 100000));
        }

        setTitle("Hotel Management System");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Room ID:"));
        roomNumberField = new JTextField();
        panel.add(roomNumberField);
        panel.add(new JLabel("Guest Name:"));
        guestNameField = new JTextField();
        panel.add(guestNameField);
        panel.add(new JLabel("Check-in Date (yyyy-mm-dd):"));
        checkInField = new JTextField();
        panel.add(checkInField);
        panel.add(new JLabel("Check-out Date (yyyy-mm-dd):"));
        checkOutField = new JTextField();
        panel.add(checkOutField);

        JButton bookRoomButton = new JButton("Book Room");
        bookRoomButton.setBackground(new Color(100, 149, 237));
        bookRoomButton.setForeground(Color.WHITE);
        bookRoomButton.addActionListener(e -> bookRoom());
        panel.add(bookRoomButton);

        JButton checkOutButton = new JButton("Check Out");
        checkOutButton.setBackground(new Color(220, 20, 60));
        checkOutButton.setForeground(Color.WHITE);
        checkOutButton.addActionListener(e -> checkOut());
        panel.add(checkOutButton);

        add(panel, BorderLayout.SOUTH);

        displayRoomList();
        setVisible(true);
    }

    private void displayRoomList() {
        textArea.setText("--- ROOM LIST ---\n\n");
        for (Room room : roomList) {
            textArea.append(room.toString() + "\n");
        }
    }

    private void bookRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            if (roomNumber < 1 || roomNumber > NUMBER_OF_ROOMS || roomList[roomNumber - 1].isBooked()) {
                JOptionPane.showMessageDialog(this, "Room is invalid or already booked.");
                return;
            }
            String guestName = guestNameField.getText();
            LocalDate checkInDate = LocalDate.parse(checkInField.getText());
            roomList[roomNumber - 1].bookRoom(guestName, checkInDate);
            JOptionPane.showMessageDialog(this, "Booking successful!");
            displayRoomList();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid data.");
        }
    }

    private void checkOut() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            if (roomNumber < 1 || roomNumber > NUMBER_OF_ROOMS || !roomList[roomNumber - 1].isBooked()) {
                JOptionPane.showMessageDialog(this, "Room is invalid or not booked yet.");
                return;
            }
            LocalDate checkOutDate = LocalDate.parse(checkOutField.getText());
            double totalAmount = roomList[roomNumber - 1].checkOut(checkOutDate);
            JOptionPane.showMessageDialog(this, "Check out successful! Total amount: " + df.format(totalAmount));
            displayRoomList();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid data.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HeThongKhachSanGUI::new);
    }
}
