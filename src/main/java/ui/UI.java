package ui;

import model.Article;
import service.NewsService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.List;

public class UI extends JFrame {
    private final NewsService newsService = new NewsService();
    private final DefaultListModel<Article> listModel = new DefaultListModel<>();
    private final JList<Article> articleList = new JList<>(listModel);

    public UI() {
        setTitle("News Viewer");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Top News Headlines", SwingConstants.CENTER);
        add(header, BorderLayout.NORTH);

        articleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articleList.setCellRenderer(new ArticleRenderer());
        add(new JScrollPane(articleList), BorderLayout.CENTER);

        articleList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Article selected = articleList.getSelectedValue();
                    if (selected != null) openInBrowser(selected.getUrl());
                }
            }
        });

        loadHeadlines();
    }

    private void loadHeadlines() {
        List<Article> articles = newsService.getTopHeadlines("top");
        listModel.clear();
        for (Article a : articles) listModel.addElement(a);
    }

    private void openInBrowser(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot open link: " + e.getMessage());
        }
    }

    static class ArticleRenderer extends JLabel implements ListCellRenderer<Article> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Article> list, Article article,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            setText("<html><b>" + article.getTitle() + "</b><br><i>" + article.getSource() + "</i></html>");
            setOpaque(true);
            setBackground(isSelected ? new Color(220, 230, 255) : Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            return this;
        }
    }
}