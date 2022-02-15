package _Manager;

import _Model.Contact;
import _Regex.Validate;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class ContactManager {
    private final ArrayList<Contact> contactList;
    private final Scanner scanner = new Scanner(System.in);
    private final Validate validate = new Validate();
    public static final String PATH_NAME = "src/_FileCSV/contact.csv";

    public ContactManager() {
        if (new File(PATH_NAME).length() == 0) {
            this.contactList = new ArrayList<>();
        } else {
            this.contactList = readFile(PATH_NAME);
        }
    }

    public ArrayList<Contact> getContactList() {
        return contactList;
    }

    public String getGender(int choice) {
        String gender = "";
        switch (choice) {
            case 1:
                gender = "Nam";
                break;
            case 2:
                gender = "Nữ";
                break;
        }
        return gender;
    }

    public void addContact() {
        System.out.println("Nhập thông tin");
        System.out.println("-----");
        String phoneNumber = enterPhoneNumber();
        System.out.println("Nhập tên nhóm");
        String group = scanner.nextLine();
        System.out.println("Nhập họ tên");
        String name = scanner.nextLine();
        System.out.println("Nhập giới tính");
        System.out.println("1. Nam");
        System.out.println("2. Nữ");
        int gender = Integer.parseInt(scanner.nextLine());
        System.out.println("Nhập địa chỉ");
        String address = scanner.nextLine();
        System.out.println("Nhập ngày sinh(dd-mm-yyyy)");
        String date = scanner.nextLine();
        LocalDate dateOfBirth = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-LL-yyyy"));
        String email = enterEmail();
        if (getGender(gender).equals("")) {
            System.out.println("Nhập sai lựa chọn! Vui lòng nhập lại!\n");
            return;
        }
        Contact contact = new Contact(phoneNumber, group, name, getGender(gender), address, dateOfBirth, email);
        contactList.add(contact);
        System.out.println("Thêm " + phoneNumber + " vào danh bạ thành công !\n");
    }

    public void updateContact(String phoneNumber) {
        Contact editContact = null;
        for (Contact contact : contactList) {
            if (contact.getPhoneNumber().equals(phoneNumber)) {
                editContact = contact;
            }
        }
        if (editContact != null) {
            int index = contactList.indexOf(editContact);
            System.out.println("Nhập tên nhóm mới");
            editContact.setGroup(scanner.nextLine());
            System.out.println("Nhập họ tên mới");
            editContact.setName(scanner.nextLine());
            System.out.println("Nhập giới tính mới");
            System.out.println("1. Male");
            System.out.println("2. Female");
            int gender = Integer.parseInt(scanner.nextLine());
            editContact.setGender(getGender(gender));
            System.out.println("Nhập địa chỉ mới");
            editContact.setAddress(scanner.nextLine());
            System.out.println("Nhập ngày sinh mới(dd-mm-yyyy)");
            String date = scanner.nextLine();
            LocalDate dateOfBirth = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-LL-yyyy"));
            editContact.setBirthday(dateOfBirth);
            editContact.setEmail(enterEmail());
            contactList.set(index,editContact);
            System.out.println("Sửa " + phoneNumber + " thành công !\n");
        } else {
            System.out.println("Không tìm được danh bạ với số điện thoại trên !\n");
        }
    }

    public void deleteContact(String phoneNumber) {
        Contact deleteContact = null;
        for (Contact contact : contactList) {
            if (contact.getPhoneNumber().equals(phoneNumber)) {
                deleteContact = contact;
            }
        }
        if (deleteContact != null) {
            System.out.println("Nhập xác nhận:");
            System.out.println("Y: Xóa");
            System.out.println("Nhập ký tự bất kì: Thoát");
            String confirm = scanner.next();
            if (confirm.equalsIgnoreCase("y")) {
                contactList.remove(deleteContact);
                System.out.println("Xóa " + phoneNumber + " thành công !\n");

            }
        } else {
            System.out.println("Không tìm thấy danh bạ với số điện thoại trên !\n");
        }
    }

    public void searchByKeyWord(String keyword) {
        ArrayList<Contact> contacts = new ArrayList<>();
        for (Contact contact : contactList) {
            if (validate.validateSearchByKeyword(keyword, contact.getPhoneNumber())) {
                contacts.add(contact);
            }
        }
        if (contacts.isEmpty()) {
            System.out.println("Không tìm thấy danh bạ với từ khóa trên !\n");
        } else {
            System.out.println("Thông tin cần tìm theo từ khóa trên: ");
            contacts.forEach(System.out::println);

        }
    }

    public void displayAll() {
        System.out.println("-----");
        System.out.printf("%-20S%-20S%-20S%-20S%-20S\n", "Số điện thoại", "Nhóm", "Họ tên", "Giới tính", "Địa chỉ");
        System.out.println("-----");
        for (Contact contact : contactList) {
            System.out.printf("%-20S%-20S%-20S%-20S%-20S\n", contact.getPhoneNumber(), contact.getGroup(), contact.getName(), contact.getGender(), contact.getAddress());
            System.out.println("-----");
        }
    }

    public String enterPhoneNumber() {
        String phoneNumber;
        while (true) {
            System.out.println("Nhập số điện thoại: ");
            String phone = scanner.nextLine();
            if (!validate.validatePhone(phone)) {
                System.out.println("Số điện thoại không hợp lệ!");
                System.out.println("-----");
            } else {
                phoneNumber = phone;
                break;
            }
        }
        return phoneNumber;
    }

    private String enterEmail() {
        String email;
        while (true) {
            System.out.println("Nhập email: ");
            String inputEmail = scanner.nextLine();
            if (!validate.validateEmail(inputEmail)) {
                System.out.println("Email không hợp lệ!");
                System.out.println("-----");
            } else {
                email = inputEmail;
                break;
            }
        }
        return email;
    }

    public void writeFile(ArrayList<Contact> contactList, String path) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
            for (Contact contact : contactList) {
                bufferedWriter.write(contact.getPhoneNumber() + "," +
                        contact.getGroup() + "," +
                        contact.getName() + "," +
                        contact.getGender() + "," +
                        contact.getAddress() + "," +
                        contact.getBirthday() + "," +
                        contact.getEmail() + "\n");
            }
            bufferedWriter.close();
            System.out.println("Ghi file thành công");
            System.out.println("-----");
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public ArrayList<Contact> readFile(String path) {
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split(",");
                contacts.add(new Contact(strings[0], strings[1], strings[2], strings[3], strings[4], LocalDate.parse(strings[5], DateTimeFormatter.ISO_LOCAL_DATE), strings[6]));
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        return contacts;
    }
}