package jp.itochan.aiueokun;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainFragment extends Fragment {

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Button makeButton = (Button) view.findViewById(R.id.make_sentence);
        final EditText acronym = (EditText) view.findViewById(R.id.acronym);
        makeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSentence(acronym.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_license:
                showLicenseDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showLicenseDialog() {
        LicenseDialogFragment dialog = new LicenseDialogFragment();
        dialog.setTargetFragment(this, 0);
        dialog.show(getChildFragmentManager(), LicenseDialogFragment.TAG);
    }

    private void makeSentence(String acronym) {
        new MakeSentenceTask().execute(acronym);
    }

    private void updateResult(String result) {
        View view = getView();
        view.findViewById(R.id.aiueokun).setVisibility(View.GONE);

        TextView resultText = (TextView) view.findViewById(R.id.result);
        resultText.setText(result);
    }

    private class MakeSentenceTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... acronym) {
            OkHttpClient client = new OkHttpClient();
            char[] array = acronym[0].toCharArray();
            ArrayList<String> sentence = new ArrayList<>();
            Random random = new Random();

            for (char character : array) {
                String query = getRandomHiragana(character);

                String url = "http://www.google.com/complete/search?hl=ja&q=" + query + "&output=toolbar";
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response;
                try {
                    response = client.newCall(request).execute();
                    ArrayList<String> suggestions = new ArrayList<>();

                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(new StringReader(response.body().string()));

                    int eventType = parser.getEventType();

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if (parser.getName().equals("suggestion")) {
                                    suggestions.add(parser.getAttributeValue(null, "data"));
                                }
                                break;
                        }

                        eventType = parser.next();
                    }

                    sentence.add(suggestions.get(random.nextInt(suggestions.size())));

                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
            }

            return sentence;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            String sentence = "";
            for (String value : result) {
                sentence += value + "\n";
            }
            MainFragment.this.updateResult(sentence);
        }

        private String getRandomHiragana(char alphabet) {
            HashMap<Character, String[]> table = new LinkedHashMap<>();
            table.put('A', new String[]{"あ"});
            table.put('B', new String[]{"ば", "び", "ぶ", "べ", "ぼ", "びゃ", "びゅ", "びょ"});
            table.put('C', new String[]{"ちゃ", "ち", "ちゅ", "ちぇ", "ちょ"});
            table.put('D', new String[]{"だ", "でぃ", "でゅ"});
            table.put('E', new String[]{"え"});
            table.put('F', new String[]{"ふ", "ふぁ", "ふぃ", "ふゅ", "ふぇ", "ふぉ"});
            table.put('G', new String[]{"が", "ぎ", "ぐ", "げ", "ご"});
            table.put('H', new String[]{"は", "ひ", "ふ", "へ", "ほ"});
            table.put('I', new String[]{"い"});
            table.put('J', new String[]{"じゃ", "じ", "じゅ", "じぇ", "じょ"});
            table.put('K', new String[]{"か", "き", "く", "け", "こ"});
            table.put('L', new String[]{"ら", "り", "る", "れ", "ろ"});
            table.put('M', new String[]{"ま", "み", "む", "め", "も"});
            table.put('N', new String[]{"な", "に", "ぬ", "ね", "の", "にゃ", "にゅ", "にょ"});
            table.put('O', new String[]{"お"});
            table.put('P', new String[]{"ぱ", "ぴ", "ぷ", "ぺ", "ぽ", "ぴゃ", "ぴゅ", "ぴょ"});
            table.put('Q', new String[]{"くぁ", "くぃ", "きゅ", "きぇ", "きょ"});
            table.put('R', new String[]{"ら", "り", "る", "れ", "ろ"});
            table.put('S', new String[]{"さ", "し", "す", "せ", "そ", "しゃ", "しゅ", "しょ"});
            table.put('T', new String[]{"た", "ち", "つ", "て", "と", "ちゃ", "ちゅ", "ちょ"});
            table.put('U', new String[]{"う"});
            table.put('V', new String[]{"ヴぁ", "ヴぃ", "ヴゅ", "ヴぇ", "ヴょ"});
            table.put('W', new String[]{"わ", "うぃ", "うぇ", "うぉ"});
            table.put('X', new String[]{"えっくす"});
            table.put('Y', new String[]{"や", "ゆ", "よ"});
            table.put('Z', new String[]{"ざ", "じ", "ず", "ぜ", "ぞ"});

            Random random = new Random();
            String[] hiragana = table.get(alphabet);

            if (hiragana != null) {
                return hiragana[random.nextInt(hiragana.length)];
            } else {
                return "";
            }
        }
    }
}
