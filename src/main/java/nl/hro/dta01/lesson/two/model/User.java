package nl.hro.dta01.lesson.two.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import nl.hro.dta01.lesson.two.model.enums.Gender;
import nl.hro.dta01.lesson.two.model.enums.Occupation;

@DatabaseTable(tableName = "users")
public class User {

    public static class Column {
        public static final String ID = "user_id";
        public static final String AGE = "age";
        public static final String GENDER = "gender";
        public static final String OCCUPATION = "occupation";
        public static final String ZIP = "zup_code";
    }

    @DatabaseField(columnName = Column.ID)
    private Integer id;

    @DatabaseField(columnName = Column.AGE)
    private Integer age;

    @DatabaseField(columnName = Column.GENDER)
    private Gender gender;

    @DatabaseField(columnName = Column.OCCUPATION)
    private Occupation occupation;

    @DatabaseField(columnName = Column.ZIP)
    private String zipCode;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
