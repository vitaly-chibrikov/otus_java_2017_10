package ru.otus.user;

public class UsersDataSet {
	private long id;
	private String name0;

	public UsersDataSet(String name) {
		this.name0 = name;
	}

	public UsersDataSet(long id, String name){
		this.id = id;
		this.name0 = name;
	}
	
	public String getName0() {
		return name0;
	}
	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "UsersDataSet{" +
				"id=" + id +
				", name0='" + name0 + '\'' +
				'}';
	}
}
