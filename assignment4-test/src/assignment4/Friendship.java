package assignment4;

public class Friendship {
	private Person friend;
	private int year;

	public Friendship(Person person, int year) {
		this.friend = person;
		this.year = year;
	}

	public Person getPerson() {
		return friend;
	}

	public int getYear() {
		return year;
	}

	public void setPerson(Person person) {
		this.friend = person;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
