package footballleagueguıı;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//imports
public class FootballLeagueGUII {
    // the JFrame class represents the window

    private JFrame frame;
    //the JPanel contains user interface components

    private JPanel panel;
    //the JLabel displays text

    private JLabel titleLabel;
    //the JTable is used to display data

    private JTable table;
    //the DefaultTableModel holds data for the table

    private DefaultTableModel tableModel;
    //simulate button

    private JButton simulateButton;
    // submit button

    private JButton submitButton;
    // stores fixtures

    private List<Fixture> fixtures;
    // index for fixtures

    private int fixtureIndex;
    //represents the league

    private League league;

    public FootballLeagueGUII() {
        // initialize the window and set the close operation

        frame = new JFrame("Football League");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // initialize the panel and set its layout

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        // add the title label

        titleLabel = new JLabel("League Standings");
        panel.add(titleLabel, BorderLayout.NORTH);
        // initialize the table model and add columns

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Team");
        tableModel.addColumn("Points");
        tableModel.addColumn("Wins");
        tableModel.addColumn("Draws");
        tableModel.addColumn("Losses");
        tableModel.addColumn("GF");
        tableModel.addColumn("GA");
        tableModel.addColumn("GD");
        // initialize the table and set its size

        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);
        // add a scroll pane

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        // initialize the simulate button and add its listener

        simulateButton = new JButton("Simulate Matches");
        submitButton = new JButton("Submit");

        simulateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fixtures = league.getFixtures();
                fixtureIndex = 0;
                // show the match if enabled

                if (fixtures.size() > 0) {
                    showFixture();
                    simulateButton.setEnabled(false);
                }
            }
        });
        // initialize the submit button and add its listener

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // if fixtures are not null and the fixture index is less than the size of fixtures

                if (fixtures != null && fixtureIndex < fixtures.size()) {
                    Fixture fixture = fixtures.get(fixtureIndex);
                    int homeGoals = Integer.parseInt(javax.swing.JOptionPane.showInputDialog("Enter the number of goals for the home team: "));
                    int awayGoals = Integer.parseInt(javax.swing.JOptionPane.showInputDialog("Enter the number of goals for the away team: "));

                    league.simulateMatch(fixture, homeGoals, awayGoals);
                    fixtureIndex++;
                    // if the fixture index is less than the size of fixtures show the match

                    if (fixtureIndex < fixtures.size()) {
                        showFixture();
                    } else {
                        submitButton.setEnabled(false);
                        simulateButton.setEnabled(true);
                        JOptionPane.showMessageDialog(frame, "All matches have been simulated and submitted.");
                    }

                    updateTable();
                }
            }
        });
        // add the button panel.

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(simulateButton);
        buttonPanel.add(submitButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        // add the panel to the frame.

        frame.getContentPane().add(panel);
        // pack the frame and make it visible.

        frame.pack();
        frame.setVisible(true);
    }
    // initialize the league.

    public void initializeLeague() {
        // get the number of teams.

        int numTeams = Integer.parseInt(javax.swing.JOptionPane.showInputDialog("Enter the number of teams in the league: "));
        league = new League(numTeams);
        // add teams.

        for (int i = 0; i < numTeams; i++) {
            String teamName = javax.swing.JOptionPane.showInputDialog("Enter the name of team " + (i + 1) + ": ");
            Team team = new Team(teamName);
            league.addTeam(team);
        }
        // generate fixtures.

        league.generateFixtures();
        updateTable();
    }
    // show a match.

    private void showFixture() {
        Fixture fixture = fixtures.get(fixtureIndex);
        String homeTeamName = fixture.getHomeTeam().getName();
        String awayTeamName = fixture.getAwayTeam().getName();

        JOptionPane.showMessageDialog(frame, "Fixture: " + homeTeamName + " vs " + awayTeamName);
    }
    // update the table.

    private void updateTable() {
        tableModel.setRowCount(0);
        // get teams.

        List<Team> teams = league.getTeams();
        // add teams to the table.

        for (Team team : teams) {
            Object[] rowData = {
                    team.getName(),
                    team.getPoints(),
                    team.getWins(),
                    team.getDraws(),
                    team.getLosses(),
                    team.getGoalsFor(),
                    team.getGoalsAgainst(),
                    team.getGoalDifference()
            };

            tableModel.addRow(rowData);
        }
    }
    // main method

    public static void main(String[] args) {
        FootballLeagueGUII leagueGUI = new FootballLeagueGUII();
        leagueGUI.initializeLeague();
    }
}
class Team {
    // represents a team

    private String name;
    private int wins;
    private int draws;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return wins * 3 + draws;
    }

    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void incrementWins() {
        wins++;
    }

    public void incrementDraws() {
        draws++;
    }

    public void incrementLosses() {
        losses++;
    }

    public void incrementGoalsFor(int goals) {
        goalsFor += goals;
    }

    public void incrementGoalsAgainst(int goals) {
        goalsAgainst += goals;
    }
}

class Fixture {
    // represents a fixture

    private Team homeTeam;
    // home team
    private Team awayTeam;
    // away team

    public Fixture(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        // set the home team
        this.awayTeam = awayTeam;
        // set the away team
    }

    public Team getHomeTeam() {
        return homeTeam;
        // return the home team
    }

    public Team getAwayTeam() {
        return awayTeam;
        // return the away team
    }
}

class League {
    // represents a league

    private List<Team> teams;
    // stores teams
    private List<Fixture> fixtures;
    // stores fixtures
    private int numTeams;
    // stores the number of teams

    public League(int numTeams) {
        this.numTeams = numTeams;
        // set the number of teams
        teams = new ArrayList<>();
        // initialize the teams list
        fixtures = new ArrayList<>();
        // initialize the fixtures list
    }

    public void addTeam(Team team) {
        teams.add(team);
        // add a team
    }

    public List<Fixture> getFixtures() {
        return fixtures;
        // return fixtures
    }

    public void generateFixtures() {
        if (teams.size() != numTeams) {
            // if not all teams are added to the league display an error message and return
            System.out.println("Not all teams have been added to the league.");
            return;
        }

        Collections.shuffle(teams);
        // shuffle the teams

        for (int i = 0; i < numTeams; i++) {
            Team homeTeam = teams.get(i);
            Team awayTeam;

            for (int j = 0; j < numTeams - 1; j++) {
                int awayIndex = (i + j + 1) % numTeams;
                awayTeam = teams.get(awayIndex);

                Fixture fixture = new Fixture(homeTeam, awayTeam);
                fixtures.add(fixture);
            }
        }
    }

    public void simulateMatch(Fixture fixture, int homeGoals, int awayGoals) {
        Team homeTeam = fixture.getHomeTeam();
        Team awayTeam = fixture.getAwayTeam();

        homeTeam.incrementGoalsFor(homeGoals);
        homeTeam.incrementGoalsAgainst(awayGoals);
        awayTeam.incrementGoalsFor(awayGoals);
        awayTeam.incrementGoalsAgainst(homeGoals);

        if (homeGoals > awayGoals) {
            homeTeam.incrementWins();
            awayTeam.incrementLosses();
        } else if (homeGoals < awayGoals) {
            homeTeam.incrementLosses();
            awayTeam.incrementWins();
        } else {
            homeTeam.incrementDraws();
            awayTeam.incrementDraws();
        }
    }

    public List<Team> getTeams() {
        return teams;
    }
}
