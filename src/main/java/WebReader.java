import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;


public class WebReader {
    private final String url;

    WebReader(String url){
        this.url = url;
    }
    Document getDocument() throws HttpStatusException{
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (Exception e){
            throw new HttpStatusException("can't find match gallery", 404, url);
        }
        return document;
    }
    int getPages(Document document){
        int pages = Integer.parseInt(document.select("div.field-name.tag-container:nth-of-type(8) span.name").text());
        return pages;

    }
    ArrayList<String> getImgLinks(Document document){
        ArrayList<String> links = new ArrayList<String>();
        for(Element src :document.select("a.gallerythumb img[src]")){
            StringBuffer str1 = new StringBuffer(src.attr("data-src"));
            if(str1.isEmpty()){
                continue;
            } else{
                StringBuffer str2 = new StringBuffer(str1.reverse().toString().replaceFirst("[t]",""));
                String link = str2.reverse().toString().replaceFirst("t.nhentai.net", "i.nhentai.net");
                links.add(link);
            }
        }
        return links;
    }
}
