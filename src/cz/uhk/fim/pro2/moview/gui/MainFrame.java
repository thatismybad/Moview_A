package cz.uhk.fim.pro2.moview.gui;

import cz.uhk.fim.pro2.moview.model.*;
import cz.uhk.fim.pro2.moview.utils.DateHandler;
import cz.uhk.fim.pro2.moview.utils.FileUtils;
import cz.uhk.fim.pro2.moview.utils.ImageHandler;
import cz.uhk.fim.pro2.moview.utils.MovieParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame implements ActionListener {

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

    private JMenuItem moviesItem;

    private List<Movie> movies;

    public MainFrame() {
        initFrame();
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

    private void initUi(){
        JMenuBar menuBar = new JMenuBar();
        JMenu mainMenu, moviesSubmenu, genresMenu, yearsMenu;
        mainMenu = new JMenu("Menu");

        moviesItem = new JMenuItem("Seznam filmů");
        moviesItem.addActionListener(this);

        moviesSubmenu = new JMenu("Seznam filmů podle");
        genresMenu = new JMenu("žánru");
        yearsMenu = new JMenu("roku");

        String[] genres = { "Akční", "Sci-Fi", "Horor", "Drama" };
        String[] years = { "1977", "1985", "1990", "1993", "2000", "2005", "2017", "2020" };

        mainMenu.add(moviesItem);
        mainMenu.add(moviesSubmenu);

        moviesSubmenu.add(genresMenu);
        moviesSubmenu.add(yearsMenu);

        for(String s : genres) {
            JMenuItem item = new JMenuItem(s);
            item.addActionListener(this);
            genresMenu.add(item);
        }

        for(String s : years) {
            JMenuItem item = new JMenuItem(s);
            item.addActionListener(this);
            yearsMenu.add(item);
        }

        menuBar.add(mainMenu);

        setJMenuBar(menuBar);


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
                    setDataToUi();
                }
            }
        });

        btnAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!movies.isEmpty()) {
                    addMovie(movies.get(0));
                }
                setDataToUi();
            }
        });

        btnSkip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!movies.isEmpty()) {
                    skipMovie(movies.get(0));
                }
                setDataToUi();
            }
        });
    }

    private void addMovie(Movie m) {
        System.out.println(m);
        try {
            FileUtils.saveStringToFile(m.getMovieId());
            System.out.println(FileUtils.readStringFromFile("movies.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        movies.remove(m);
    }

    private void skipMovie(Movie m) {
        System.out.println(m);
        movies.remove(m);
    }

    private void setDataToUi() {
        if (!movies.isEmpty()) {
            Movie m = movies.get(0);
            lblTitle.setText(m.getTitle());
            lblYear.setText(m.getYear());
            lblType.setText(m.getType().getType());
            lblPoster.setIcon(new ImageIcon(m.getPoster()));
        } else {
//            lblTitle.setVisible(false);
//            lblYear.setVisible(false);
//            lblType.setVisible(false);
//            lblPoster.setVisible(false);
            setVisibility(false, lblTitle, lblYear, lblType, lblPoster);
        }
    }

    private void setVisibility(boolean shouldBeVisible, JComponent... components) {
        for(JComponent c : components) {
            c.setVisible(shouldBeVisible);
        }
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
                "1977",
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

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("On Action Performed");
        if (e.getSource() == moviesItem) {
            new MovieListFrame("Seznam filmů");
        } else {
            JMenuItem item = (JMenuItem) e.getSource();

            new MovieListFrame(item.getText());
        }
    }
}
