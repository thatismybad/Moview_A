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
import java.util.HashMap;
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

    private JMenu moviesSubmenu, genresMenu, yearsMenu;

    private List<MenuItem> menuItems;
    private HashMap<String, String> genresMap;
    private HashMap<String, String> yearsMap;

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
        menuItems = new ArrayList<>();
        try {
            genresMap = FileUtils.decomposeData(FileUtils.readStringFromFile(FileUtils.TYPE_GENRES));
            yearsMap = FileUtils.decomposeData(FileUtils.readStringFromFile(FileUtils.TYPE_YEARS));
        } catch (IOException e) {
            genresMap = new HashMap<>();
            yearsMap = new HashMap<>();
        }
        JMenuBar menuBar = new JMenuBar();
        JMenu mainMenu;
        mainMenu = new JMenu("Menu");

        JMenuItem moviesItem = new JMenuItem("Seznam filmů");
        moviesItem.addActionListener(this);
        try {
            String movies = FileUtils.readStringFromFile(FileUtils.TYPE_ALL);
            menuItems.add(new MenuItem("Seznam filmů", null, movies, moviesItem));
        } catch (IOException e) {
            e.printStackTrace();
        }

        moviesSubmenu = new JMenu("Seznam filmů podle");
        genresMenu = new JMenu("žánru");
        yearsMenu = new JMenu("roku");

//        String[] genres = { "Akční", "Sci-Fi", "Horor", "Drama" };
//        String[] years = { "1977", "1985", "1990", "1993", "2000", "2005", "2017", "2020" };

        mainMenu.add(moviesItem);
        mainMenu.add(moviesSubmenu);

        moviesSubmenu.add(genresMenu);
        moviesSubmenu.add(yearsMenu);

        for(String s : genresMap.keySet()) {
            JMenuItem item = new JMenuItem(s);
            item.addActionListener(this);
            menuItems.add(new MenuItem(
                    String.format("Seznam filmů podle žánru: %s", s),
                    "genres",
                    genresMap.get(s),
                    item
                    )
            );
            genresMenu.add(item);
        }

        for(String s : yearsMap.keySet()) {
            JMenuItem item = new JMenuItem(s);
            item.addActionListener(this);
            menuItems.add(new MenuItem(
                            String.format("Seznam filmů podle roku: %s", s),
                            "years",
                            yearsMap.get(s),
                            item
                    )
            );
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
            Movie movie = MovieParser.parseMovieDetail(m.getMovieId());
            for(Genre g : movie.getGenreList()) {
                if (genresMap.get(g.getName()) == null) {
                    genresMap.put(g.getName(), m.getMovieId());
                }
                genresMap.put(g.getName(), String.format("%s;%s", genresMap.get(g.getName()), m.getMovieId()));
            }
            if (yearsMap.get(m.getYear()) == null) {
                yearsMap.put(m.getYear(), m.getMovieId());
            } else {
                yearsMap.put(m.getYear(), String.format("%s;%s", yearsMap.get(m.getYear()), m.getMovieId()));
            }

            FileUtils.saveStringToFile(m.getMovieId(), FileUtils.TYPE_ALL);
            FileUtils.saveStringToFile(FileUtils.composeData(genresMap), FileUtils.TYPE_GENRES);
            FileUtils.saveStringToFile(FileUtils.composeData(yearsMap), FileUtils.TYPE_YEARS);

            System.out.println(FileUtils.readStringFromFile(FileUtils.TYPE_ALL));
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
            setVisibility(true, lblTitle, lblYear, lblType, lblPoster);
            moviesSubmenu.remove(genresMenu);
            moviesSubmenu.remove(yearsMenu);
            try {
                HashMap<String, String> genres = FileUtils.decomposeData(FileUtils.readStringFromFile(FileUtils.TYPE_GENRES));
                for(String s : genres.keySet()) {
                    JMenuItem item = new JMenuItem(s);
                    item.addActionListener(this);
                    menuItems.add(new MenuItem(
                                    String.format("Seznam filmů podle žánru: %s", s),
                                    "genres",
                                    genresMap.get(s),
                                    item
                            )
                    );
                    genresMenu.add(item);
                }

                HashMap<String, String> years = FileUtils.decomposeData(FileUtils.readStringFromFile(FileUtils.TYPE_YEARS));
                for(String s : years.keySet()) {
                    JMenuItem item = new JMenuItem(s);
                    item.addActionListener(this);
                    menuItems.add(new MenuItem(
                                    String.format("Seznam filmů podle roku: %s", s),
                                    "year",
                                    years.get(s),
                                    item
                            )
                    );
                    yearsMenu.add(item);
                }

                moviesSubmenu.add(genresMenu);
                moviesSubmenu.add(yearsMenu);
                moviesSubmenu.updateUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
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
        JMenuItem item = (JMenuItem) e.getSource();
        for(MenuItem menuItem : menuItems) {
            if (menuItem.getItem().equals(item)) {
                new MovieListFrame(menuItem);
            }
        }
    }
}
