package cz.uhk.fim.pro2.moview.gui;

import cz.uhk.fim.pro2.moview.model.Movie;
import cz.uhk.fim.pro2.moview.utils.FileUtils;
import cz.uhk.fim.pro2.moview.utils.MovieParser;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieListFrame extends JFrame {

    public MovieListFrame(String parameter) {
        initFrame(parameter);
        initUiAndData();
    }

    private void initFrame(String parameter) {
        setTitle(parameter);
        setSize(640, 480);
        setVisible(true);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initUiAndData() {
        JTable table = new JTable();
        MovieTableModel model = new MovieTableModel();
        List<Movie> movieList = new ArrayList<>();
        try {
            for(String s : FileUtils.readStringFromFile("movies.txt").split(";")) {
                movieList.add(MovieParser.parseMovieDetail(s));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.setMovieList(movieList);
        table.setModel(model);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table));
        add(tablePanel);
    }
}
