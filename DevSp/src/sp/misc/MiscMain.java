package sp.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MiscMain {

    public class Student implements Comparable<Student>{
        private int no;
        private String name;

        public Student(int no, String name) {
            this.no = no;
            this.name = name;
        }

        public int getNo() {
            return this.no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Student [no=" + this.no + ", name=" + this.name + "]";
        }

        @Override
        public int compareTo(Student o) {
            return getNo() - o.getNo();
        }
    }

    public void sort() {
        // list sort
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(4);
        list.add(1);
        System.out.println("org: "+list);

        Collections.sort(list);
        System.out.println("asc: "+list);

        Collections.sort(list, (a, b) -> b.compareTo(a));
        System.out.println("desc: "+list);

        List<Student> listStudent = new ArrayList<>();
        listStudent.add(new Student(2, "tom"));
        listStudent.add(new Student(1, "jane"));
        listStudent.add(new Student(3, "zoro"));
        System.out.println("org: "+listStudent);

        Collections.sort(listStudent);
        System.out.println("asc no: "+listStudent);

        listStudent.sort((final Student a, final Student b) -> b.getName().compareTo(a.getName()));
        System.out.println("desc name: "+listStudent);

        Comparator<Student> co = new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return (o2.getName().compareTo(o1.getName()));
            }
        };
        Collections.sort(listStudent, co);
        Collections.reverse(listStudent);

    }

    public void opString() {
        // zero padding string
        int length = 10;
        String str = "5";
        String padding = String.format("%1$" + length + "s", str).replace(' ', '0');
        System.out.println(padding);
    }

    public static void intToByte(byte [] buffer, int offset, int num) {
        buffer[offset + 3] = (byte)(num >> 24);
        buffer[offset + 2] = (byte)(num >> 16);
        buffer[offset + 1] = (byte)(num >> 8);
        buffer[offset + 0] = (byte)(num);
    }

    public static int byteToInt(byte [] buffer, int offset) {
        int nRet = (((buffer[offset+3] & 0xff) << 24) |
                ((buffer[offset+2] & 0xff) << 16) |
                ((buffer[offset+1] & 0xff) << 8) |
                ((buffer[offset] & 0xff)));

        return nRet;
    }

    public static void main(String[] args) {
        MiscMain m = new MiscMain();
        m.sort();
        m.opString();
    }
}
