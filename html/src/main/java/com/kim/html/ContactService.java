package com.kim.html;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KIM on 15/12/25.
 */
public class ContactService {
    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1, "张三", "12345678910"));
        contacts.add(new Contact(2, "里斯", "11111111111"));
        contacts.add(new Contact(3, "王五", "22222222222"));
        return contacts;
    }
}
