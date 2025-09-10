package com.anis.android.medref.bookviewer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anis.android.medref.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.core.MarkwonTheme;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.image.picasso.PicassoImagesPlugin;

public class BookViewerActivity extends AppCompatActivity {

    private TextView markdownView;
    private DrawerLayout drawerLayout;
    private RecyclerView headingsList;

    private Markwon markwon;
    private String markdownContent;

    private List<HeadingItem> headingItems = new ArrayList<>();
    private HeadingAdapter headingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_viewer);

        // 🔽 Setup Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 🔽 Setup DrawerLayout and Hamburger
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 🔽 UI references
        markdownView = findViewById(R.id.markdown_view);
        headingsList = findViewById(R.id.headings_list);

        // 🔽 Handle incoming Intent extras
        String fileName = getIntent().getStringExtra("fileName");
        String title = getIntent().getStringExtra("title");

        // Set toolbar title
        if (getSupportActionBar() != null && title != null) {
            getSupportActionBar().setTitle(title);
        }

        if (fileName == null) {
            fileName = "mdfiles/sample.md"; // fallback
        }

        markwon = Markwon.builder(this)
                .usePlugin(HtmlPlugin.create())
                .usePlugin(PicassoImagesPlugin.create(this))
                .usePlugin(new AbstractMarkwonPlugin() {
                    @Override
                    public void configureTheme(MarkwonTheme.Builder builder) {
                        builder.headingBreakHeight(0);
                    }
                })
                .build();



        // 🔽 Load and render Markdown
        markdownContent = loadMarkdownFromAssets(fileName);
        parseHeadings(markdownContent);
        markwon.setMarkdown(markdownView, markdownContent);

        // 🔽 Setup navigation drawer list
        headingAdapter = new HeadingAdapter(headingItems, position -> {
            int line = headingItems.get(position).lineNumber;
            scrollToLine(line);
            drawerLayout.closeDrawers();
        });

        headingsList.setLayoutManager(new LinearLayoutManager(this));
        headingsList.setAdapter(headingAdapter);
    }

    /**
     * Loads a markdown file from assets folder.
     */
    private String loadMarkdownFromAssets(String filename) {
        try {
            InputStream inputStream = getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            reader.close();
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error loading markdown file: " + filename;
        }
    }

    /**
     * Extracts headings that start with "# " (level 1) for navigation drawer.
     */
    private void parseHeadings(String content) {
        headingItems.clear();
        String[] lines = content.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.startsWith("# ") && !line.startsWith("##")) {
                String title = line.replace("#", "").trim();
                headingItems.add(new HeadingItem(title, i));
            }
        }
    }

    /**
     * Scrolls the markdown view to a specific line.
     */
    private void scrollToLine(int lineNumber) {
        markdownView.post(() -> {
            int lineHeight = markdownView.getLineHeight();
            int y = lineNumber * lineHeight;
            markdownView.scrollTo(0, y);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bookview_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Search text...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAndScroll(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: live search
                return false;
            }
        });

        return true;
    }
    private void searchAndScroll(String query) {
        if (query == null || query.isEmpty()) return;

        String content = markdownContent.toLowerCase();
        int index = content.indexOf(query.toLowerCase());

        if (index != -1) {
            // Estimate line number by counting '\n' before the match
            int lineNumber = content.substring(0, index).split("\n").length;
            scrollToLine(lineNumber);
        } else {
            Toast.makeText(this, "No match found", Toast.LENGTH_SHORT).show();
        }
    }

}