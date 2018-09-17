package me.jupdyke01.esbot;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class App extends ListenerAdapter {

	public static final String URL = "https://csgo-stats.com/player/";
	WebClient client;
	Random r;

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		JDA jda = new JDABuilder(AccountType.BOT).setToken(Config.token).addEventListener(new App()).buildBlocking();
		new App();
	}

	public App() {
		r = new Random();
		client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		//User user = event.getUser();
		//Guild server = event.getGuild();
		//MessageChannel channel = server.getTextChannelsByName("test", true).get(0);
		//channel.sendMessage("Polar showed up nibbas!").queue();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		User user = event.getAuthor();
		MessageChannel channel = event.getChannel();
		Guild guild = event.getGuild();
		Message message = event.getMessage();
		if (message.getContentRaw().startsWith("!")) {
			if (message.getContentRaw().equals(Config.prefix + "help")) {
				EmbedBuilder help = new EmbedBuilder();
				help.setTitle(" ");
				help.setThumbnail(Config.PICTURE_URL);
				help.setColor(new Color(0xF40C0C));
				help.addField("!help", "Displays this help embed", false);
				help.addField("!pun", "Tells you a random pun", false);
				help.addField("!botinfo", "Displays information about the bot", false);
				help.addField("!serverinfo", "Displays information about the server", false);
				help.addField("!stats (player)", "Displays cs:go stats for a steam user", false);
				channel.sendMessage(help.build()).queue();
			} else if (message.getContentRaw().equals(Config.prefix + "botinfo")) {
				EmbedBuilder botInfo = new EmbedBuilder();
				botInfo.setTitle(" ");
				botInfo.setThumbnail(Config.PICTURE_URL);
				botInfo.setColor(new Color(0xF40C0C));
				botInfo.addField("Bot Name", Config.BOTNAME, false);
				botInfo.addField("Bot Version", Config.VERSION, false);
				botInfo.addField("Bot Developer", Config.AUTHOR, false);
				channel.sendMessage(botInfo.build()).queue();
			} else if (message.getContentRaw().equals(Config.prefix + "serverinfo")) {
				EmbedBuilder serverInfo = new EmbedBuilder();
				serverInfo.setTitle(" ");
				serverInfo.setThumbnail(guild.getIconUrl());
				serverInfo.setColor(new Color(0xF40C0C));
				serverInfo.addField("Server Name", guild.getName(), false);
				serverInfo.addField("Created On", guild.getCreationTime().getMonthValue() + "/" + guild.getCreationTime().getDayOfMonth() + "/" + guild.getCreationTime().getYear(), false);
				serverInfo.addField("You Joined", guild.getMember(user).getJoinDate().getMonthValue() + "/" + guild.getMember(user).getJoinDate().getDayOfMonth() + "/" + guild.getMember(user).getJoinDate().getYear(), false);
				serverInfo.addField("Online Members/Total Members", String.valueOf(getMembersOnline(guild.getMembers())) + "/" + String.valueOf(guild.getMembers().size()), false);
				channel.sendMessage(serverInfo.build()).queue();
			} else if (message.getContentRaw().equals(Config.prefix + "pun")) {
				channel.sendMessage(Puns.getRandomPun()).queue();
			} else if (message.getContentRaw().contains(Config.prefix + "stats")) {
				ArrayList<String> stats = searchPlayerStats(message.getContentRaw().split(" ")[1]);

				EmbedBuilder serverInfo = new EmbedBuilder();
				serverInfo.setTitle(stats.get(7) + "'s stats");
				serverInfo.setThumbnail(stats.get(6));
				serverInfo.setColor(new Color(0xF40C0C));
				serverInfo.addField("Kills", stats.get(0), false);
				serverInfo.addField("Deaths", stats.get(9), false);
				serverInfo.addField("KD", stats.get(8), false);
				serverInfo.addField("Time Played", stats.get(1), false);
				serverInfo.addField("Win Rate", stats.get(2), false);
				serverInfo.addField("Accuracy", stats.get(3), false);
				serverInfo.addField("Headshot %", stats.get(4), false);
				serverInfo.addField("MVP's", stats.get(5), false);
				channel.sendMessage(serverInfo.build()).queue();
			} else if (message.getContentRaw().contains(Config.prefix + "joke")) {
				channel.sendMessage(findPun()).queue();
			}
		}
	}

	public int getMembersOnline(List<Member> members) { 
		int membersonline = 0;

		for (Member member : members) {
			if (member.getOnlineStatus().equals(OnlineStatus.ONLINE))
				membersonline++;
		}

		return membersonline;
	}

	public String findPun() {
		String searchUrl = "https://onelinefun.com/" + (r.nextInt(492) + 1) + "/";
		try {
			HtmlPage page = client.getPage(searchUrl);
			List<HtmlElement> jokes = page.getByXPath(".//div[@class='o']/p");
			if (jokes.isEmpty()) {
				System.out.println("404 Joke not found.");
			} else {
				return jokes.get(r.nextInt(jokes.size() - 1)).asText();
			}
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
		return "404 Joke not found.";
	}
	
	public ArrayList<String> searchPlayerStats(String username) {
		String searchUrl = URL + username;
		try {
			ArrayList<String> statString = new ArrayList<>();
			HtmlPage page = client.getPage(searchUrl);
			List<HtmlElement> stats = page.getByXPath(".//span[@class='main-stats-data-row-data']");
			if (stats.isEmpty()) {
				System.out.println("Error");
			} else {
				DomAttr pictureRaw = page.getFirstByXPath(".//img[@class='img-avatar']/@src");
				String picture = pictureRaw.toString().split("=")[2].split("]")[0];
				HtmlElement name = page.getFirstByXPath(".//h1[@class='steam-name']");
				HtmlElement kdr = page.getFirstByXPath(".//script[@type='text/javascript']");
				List<HtmlElement> death = page.getByXPath(".//p[@class='other-stats-data']");
				//picture = picture.split("\"")[1];
				//picture = picture.split("\"")[0];
				
				for (HtmlElement element : stats) {
					statString.add(element.asText());
				}
				statString.add(picture);
				statString.add(name.asText());
				statString.add(kdr.toString().split("return \"")[1].split("\"")[0]);
				statString.add(death.get(10).asText());
				//statString.add(death.asText());
				return statString;
			}
		} catch(FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/*public String capitalizeFirstLetter(String str) {
		String finalString = str;
		finalString = finalString.toLowerCase();
		finalString = finalString.replaceFirst(finalString.substring(0, 1), finalString.substring(0, 1).toUpperCase());
		return finalString;
	}*/
}
