package yila.experiments.winter;

/**
 * JFL 16/11/18
 */
public class Person {

    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Person && ((Person) o).age == this.age && ((Person)o).name.equals(this.name);
    }
}
