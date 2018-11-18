package assignment4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Queries {

	public static void main(String[] args) {
		List<Person> persons = getPersonsList();
		Map<Integer, List<Friendship>> friendships = getFriendshipsMap(persons);

		List<Friendship> queryOne = runQueryOne(persons, friendships);
		System.out.println("Query One Result Size = " + queryOne.size());
		System.out.println("-------------------------------");

		List<Friendship> queryTwo = runQueryTwo(persons, friendships);
		System.out.println("Query Two Result Size = " + queryTwo.size());
		System.out.println("-------------------------------");

		List<Person> queryThree = runQueryThree(persons, friendships);
		System.out.println("Query Three Result Size = " + queryThree.size());
		System.out.println("Query Three Result Person = " + queryThree.get(0).getName());
		System.out.println("--------------------------------------------");

		List<Person> queryFour = runQueryFour(persons, friendships);
		System.out.println("Query Four Result Size = " + queryFour.size());
		System.out.println("Query Four Result Person = " + queryFour.get(0).getName());
		System.out.println("--------------------------------------------");

	}

	private static List<Person> runQueryFour(List<Person> persons, Map<Integer, List<Friendship>> friendships) {
		List<Person> queryFour = new ArrayList<Person>();
		int max = getMax(persons, friendships);
		for (Person p : persons) {
			if (friendships.containsKey(p.getId())) {
				if (friendships.get(p.getId()).size() == max)
					queryFour.add(p);
			}
		}
		return queryFour;
	}

	private static List<Person> runQueryThree(List<Person> persons, Map<Integer, List<Friendship>> friendships) {
		List<Person> queryThree = new ArrayList<Person>();
		;
		int max = getMaxSince2005(persons, friendships);

		for (Person p : persons) {
			if (p.getCountry().equals("Nebraska") && friendships.containsKey(p.getId())) {
				int friendshipSince2005Size = 0;
				for (Friendship friendship : friendships.get(p.getId())) {
					if (friendship.getYear() >= 2005)
						friendshipSince2005Size++;
				}
				if (friendshipSince2005Size == max)
					queryThree.add(p);
			}
		}
		return queryThree;
	}

	private static int getMax(List<Person> persons, Map<Integer, List<Friendship>> friendships) {
		int max = -1;
		for (Person p : persons) {
			if (friendships.containsKey(p.getId())) {
				if (friendships.get(p.getId()).size() > max)
					max = friendships.get(p.getId()).size();
			}
		}
		return max;
	}

	private static int getMaxSince2005(List<Person> persons, Map<Integer, List<Friendship>> friendships) {
		int max = -1;
		for (Person p : persons) {
			if (p.getCountry().equals("Nebraska") && friendships.containsKey(p.getId())) {
				int friendshipSince2005Size = 0;
				for (Friendship friendship : friendships.get(p.getId())) {
					if (friendship.getYear() >= 2005)
						friendshipSince2005Size++;
				}
				if (friendshipSince2005Size > max)
					max = friendshipSince2005Size;
			}
		}
		return max;
	}

	private static List<Friendship> runQueryTwo(List<Person> persons, Map<Integer, List<Friendship>> friendships) {
		List<Friendship> queryTwo = new ArrayList<Friendship>();
		for (Person p : persons) {
			if (p.getCountry().equals("Nebraska") && friendships.containsKey(p.getId())) {
				for (Friendship friend : friendships.get(p.getId()))
					if (friend.getYear() >= 2005)
						queryTwo.add(friend);
			}
		}
		return queryTwo;
	}

	private static List<Friendship> runQueryOne(List<Person> persons, Map<Integer, List<Friendship>> friendships) {
		List<Friendship> queryOne = new ArrayList<Friendship>();
		for (Person p : persons) {
			if (p.getCountry().equals("Nebraska") && friendships.containsKey(p.getId())) {
				for (Friendship friend : friendships.get(p.getId()))
					queryOne.add(friend);
			}
		}
		return queryOne;
	}

	private static Map<Integer, List<Friendship>> getFriendshipsMap(List<Person> persons) {
		Map<Integer, List<Friendship>> friendships = new HashMap<Integer, List<Friendship>>();
		String csvFile = "friends.csv";
		BufferedReader br = null;
		String line = "";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				Person friend = getFriend(Integer.parseInt(data[1]), persons);
				Friendship friendship = new Friendship(friend, Integer.parseInt(data[2]));
				if (!friendships.containsKey(Integer.parseInt(data[0])))
					friendships.put(Integer.parseInt(data[0]), new ArrayList<Friendship>());
				friendships.get(Integer.parseInt(data[0])).add(friendship);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return friendships;
	}

	private static Person getFriend(int id, List<Person> persons) {
		for (Person p : persons) {
			if (p.getId() == id)
				return p;
		}
		return null;
	}

	private static List<Person> getPersonsList() {
		List<Person> persons = new ArrayList<Person>();
		String csvFile = "persons.csv";
		BufferedReader br = null;
		String line = "";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				Person person = new Person(Integer.parseInt(data[0]), data[1], data[2]);
				persons.add(person);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return persons;
	}
}
