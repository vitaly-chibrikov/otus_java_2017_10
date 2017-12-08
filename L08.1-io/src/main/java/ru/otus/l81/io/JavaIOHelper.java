package ru.otus.l81.io;

import java.io.*;

/**
 * Created by tully.
 */
class JavaIOHelper {
    static void writeObject(String file, Object student) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(student);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Object readObject(String file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
