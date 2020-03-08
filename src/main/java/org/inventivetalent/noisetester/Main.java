package org.inventivetalent.noisetester;

import javafx.application.Application;

import java.util.Arrays;

public class Main {

	public static void main(String... args) {
		System.out.println(Arrays.toString(args));
		Application.launch(MyApp.class,args);
	}

}
