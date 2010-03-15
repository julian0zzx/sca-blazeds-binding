package osteching.sca.binding.blazeds;

import java.io.Serializable;

public class Buddy implements Serializable {
    
    private static final long serialVersionUID = -8299849752949054030L;
    
    private int id;
    private String nick;
    private String gender;
    private int age;
    private String address;
    
    public Buddy() {}

    public Buddy(String nick, String sex, int age, String address) {
        this.nick = nick;
        this.gender = sex;
        this.age = age;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean flag = false;
        if (obj != null) {
            if (obj instanceof Buddy) {
                flag = this.id == ((Buddy)obj).id;
            }
        }
        return flag;
    }
    
    @Override
    public int hashCode() {
        return id + nick.hashCode() * 47;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[Buddy: ");
        sb.append("nick is ").append(nick).append(", ");
        sb.append("gender is ").append(gender).append(", ");
        sb.append("age is ").append(age).append(", ");
        sb.append("address is ").append(address).append(" ");
        sb.append("] ");
        return sb.toString(); 
    }
}
