package me.jupdyke01.esbot;

import java.util.Random;

public enum Puns {
	pun1("How do you throw a space party? You planet."),
	pun2("How was Rome split in two? With a pair of Ceasars."),
	pun3("Nope. Unintended."),
	pun4("The shovel was a ground breaking invention, but everyone was blow away by the leaf blower."),
	pun5("A scarecrow says, \"This job isn't for everyone, but hay, it's in my jeans.\""),
	pun6("A Buddhist walks up to a hot dog stand and says \"Make me one with everything.\""),
	pun7("Did you hear about the guy who lost the left side of his body? He's alright now."),
	pun8("What do you call a girl with one leg that's shorter than the other? Ilene."),
	pun9("The broom swept the nation away."),
	pun10("I did a theatrical performance on puns. It was a play on words."),
	pun11("What does a clock do when it's hungry? It goes back for seconds."),
	pun12("What do you do with a dead chemist? You barium."),
	pun13("I bet the person who created the door knocker won a Nobel prize."),
	pun14("Towels can’t tell jokes. They have a dry sense of humor."),
	pun15("Two birds are sitting on a perch and one says “Do you smell fish?”"),
	pun16("Did you hear about the cheese factory that exploded in france? There was nothing but des brie."),
	pun17("Do you know sign language? You should learn it, it’s pretty handy."),
	pun18("What do you call a beautiful pumpkin? GOURDgeous."),
	pun19("Why did one banana spy on the other? Because she was appealing."),
	pun20("What do you call a cow with no legs? Ground beef."),
	pun21("What do you call a cow with two legs? Lean beef."),
	pun22("What do you call a cow with all of its legs? High steaks."),
	pun23("A cross eyed teacher couldn’t control his pupils."),
	pun24("After the accident, the juggler didn’t have the balls to do it."),
	pun25("I used to be afraid of hurdles, but I got over it."),
	pun26("To write with a broken pencil is pointless."),
	pun27(" read a book on anti-gravity. I couldn’t put it down."),
	pun28("I couldn’t remember how to throw a boomerang but it came back to me."),
	pun29("What did the buffalo say to his son? Bison."),
	pun30("What should you do if you’re cold? Stand in the corner. It’s 90 degrees.");
	
	private String pun;
	
	Puns(String pun) {
		this.pun = pun;
	}
	
	public static String getRandomPun() {		
		Random r = new Random();
		return Puns.values()[r.nextInt(Puns.values().length)].pun;
	}
}
