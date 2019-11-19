package cz.uhk.fim.pro2.moview.gui;

import cz.uhk.fim.pro2.moview.model.*;
import cz.uhk.fim.pro2.moview.utils.DateHandler;
import cz.uhk.fim.pro2.moview.utils.ImageHandler;
import cz.uhk.fim.pro2.moview.utils.MovieParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private JPanel rootPanel;
    private JTextField txtInput;
    private JLabel lblInputYear;
    private JLabel lblInputTitle;
    private JButton btnSearch;
    private JCheckBox checkByYear;
    private JTextField txtYear;
    private JButton btnAdd;
    private JButton btnSkip;
    private JLabel lblTitle;
    private JLabel lblYear;
    private JLabel lblType;
    private JLabel lblPoster;

    private List<Movie> movies;

    public MainFrame() {
        initFrame();
        initTestData();
        initUi();
    }

    private void initFrame() {
        setTitle("Moview");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (screenSize.width * 0.75), (int) (screenSize.height * 0.75));
        setContentPane(rootPanel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

    private void initUi() {
        checkByYear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lblInputYear.setVisible(checkByYear.isSelected());
                txtYear.setVisible(checkByYear.isSelected());
            }
        });

        btnSearch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String searchTitle = txtInput.getText().trim();
                if (!searchTitle.isEmpty()) {
                    movies = MovieParser.parseMovieSearch(searchTitle);
                }
            }
        });
    }

    private void setDataToUi() {
    }

    private void addMovieToFavorites(Movie movie) {

    }

    private void skipMovie(Movie movie) {

    }

    private void initTestData() {

        boolean notCallAgain = true;
        for (Movie m : MovieParser.parseMovieSearch("star wars")) {
            System.out.println(MovieParser.parseMovieDetail(m.getMovieId()));
            break;
        }

        List<Genre> genres = new ArrayList<>(4);
        genres.add(new Genre("Sci-Fi"));
        genres.add(new Genre("Action"));
        genres.add(new Genre("Adventure"));
        genres.add(new Genre("Fantasy"));

        List<Actor> actors = new ArrayList<>(3);
        actors.add(new Actor("Mark Hammil"));
        actors.add(new Actor("Harisson Ford"));
        actors.add(new Actor("Carrie Fisher"));

        List<Rating> ratings = new ArrayList<>(3);
        ratings.add(new Rating("Internet Movie Database", "8.6/10"));
        ratings.add(new Rating("Rotten Tomatoes", "93%"));
        ratings.add(new Rating("Metacritic", "90/100"));

        Movie m1 = new Movie(
                "asdasd",
                "Star Wars - ep. 4",
                1977,
                DateHandler.getDateFromString("25 May 1977"),
                121,
                genres,
                "George Lucas",
                "George Lucas",
                actors,
                "...",
                "USA",
                "English",
                ImageHandler.getImageFromUrl("https://images-na.ssl-images-amazon.com/images/I/81WjGytz7HL._SY445_.jpg"),
                ratings,
                MovieType.MOVIE
        );

//        System.out.println(m1);
    }
}
