package com.example.gill.todolist1;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class User {
	public String name;
	public String hometown;

	public User(String name) {
		this.name = name;
		this.hometown = DateFormat.getDateInstance().format(new Date());;
	}

	public User(String name, String Date)
	{
		this.name=name;
		this.hometown=Date;
	}

	public static ArrayList<User> getUsers() {
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User("Harry","10 Apr 2016"));
		users.add(new User("Marla","09 Mar 2015"));

		return users;
	}

	public String getName(){
		return this.name;
	}

	public String getDate(){
		return this.hometown;
	}


}
